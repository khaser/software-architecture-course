use std::fmt::Write;
use std::io;
use std::io::{Result, Write as IoWrite};
use std::process;

use crate::cu_kind;
use crate::env::Env;

pub struct Scheduler<'a, T: SchedulerDriver> {
    pub should_terminate: bool,
    env: &'a mut Env,
    fs_driver: &'a mut T,
}

#[derive(Debug, PartialEq)]
pub enum ExitCode {
    Success,
    Failure,
}

// Created for the possibility of unit-testing
pub trait SchedulerDriver {
    fn read_to_string(&self, filename: &str) -> Result<String>;
    fn current_dir(&self) -> Result<String>;
}

pub struct RealFsDriver;

impl SchedulerDriver for RealFsDriver {
    fn read_to_string(&self, filename: &str) -> Result<String> {
        std::fs::read_to_string(filename)
    }

    fn current_dir(&self) -> Result<String> {
        Ok(std::env::current_dir()?.display().to_string())
    }
}

impl Write for RealFsDriver {
    fn write_str(&mut self, s: &str) -> std::fmt::Result {
        io::stdout().write_all(s.as_bytes()).unwrap();
        Ok(())
    }
}

struct ExecError;

impl ExecError {
    fn to<Err>(_: Err) -> ExecError {
        ExecError {}
    }
}

type ExecResult = std::result::Result<Option<String>, ExecError>;

impl<'a, T> Scheduler<'a, T>
where
    T: SchedulerDriver,
    T: Write,
{
    pub fn new(env: &'a mut Env, fs_driver: &'a mut T) -> Self {
        Scheduler {
            should_terminate: false,
            env,
            fs_driver,
        }
    }

    pub fn run(&mut self, commands: Vec<cu_kind::Command>) -> Vec<ExitCode> {
        let mut iobuf: Option<String> = None;
        let exit_codes = commands
            .into_iter()
            .scan(&mut iobuf, |stdin, cmd| {
                let exec_res = match cmd {
                    cu_kind::Command(cu_kind::CommandUnitKind::Cat, args) => self.cat(&args, stdin),
                    cu_kind::Command(cu_kind::CommandUnitKind::Echo, args) => self.echo(&args),
                    cu_kind::Command(cu_kind::CommandUnitKind::Wc, args) => self.wc(&args, stdin),
                    cu_kind::Command(cu_kind::CommandUnitKind::Pwd, _) => self.pwd(),
                    cu_kind::Command(cu_kind::CommandUnitKind::Exit, _) => self.exit(),
                    cu_kind::Command(cu_kind::CommandUnitKind::External, args) => {
                        self.external(&args, stdin)
                    }
                    cu_kind::Command(cu_kind::CommandUnitKind::SetEnvVar, v) => self.set_env_var(v),
                };

                Some(match exec_res {
                    Ok(new_stdin) => {
                        **stdin = new_stdin;
                        ExitCode::Success
                    }
                    Err(_) => ExitCode::Failure,
                })
            })
            .collect();

        if let Some(output) = iobuf {
            self.fs_driver.write_str(&output).unwrap();
        };

        exit_codes
    }

    fn set_env_var(&mut self, args: cu_kind::Args) -> ExecResult {
        let mut iter = args.into_iter();
        let var = iter.next().unwrap();
        let val = iter.next().unwrap();
        self.env.insert(var, val);
        Ok(None)
    }

    fn external(&mut self, args: &cu_kind::Args, stdin: &Option<String>) -> ExecResult {
        let mut iter = args.iter();
        let name = iter.next().expect("external program name not provided");
        let mut cmd = process::Command::new(name);
        cmd.args(iter)
            .envs(self.env as &Env)
            .stdout(process::Stdio::piped());
        let mut child = cmd
            .stdin(match stdin {
                Some(_) => process::Stdio::piped(),
                None => process::Stdio::inherit(),
            })
            .spawn()
            .expect("failed to execute child");
        if let Some(input) = stdin {
            child
                .stdin
                .take()
                .unwrap()
                .write_all(input.as_bytes())
                .unwrap();
        }
        let output = child.wait_with_output().unwrap();
        let out_buffer = String::from_utf8(output.stdout).unwrap();
        if output.status.success() {
            Ok(Some(out_buffer))
        } else {
            Err(ExecError)
        }
    }

    fn exit(&mut self) -> ExecResult {
        self.should_terminate = true;
        Ok(None)
    }

    fn pwd(&mut self) -> ExecResult {
        match std::env::current_dir() {
            Ok(path) => {
                let mut str = String::new();
                writeln!(&mut str, "{}", path.display()).map_err(ExecError::to)?;
                Ok(Some(str))
            }
            Err(_) => Err(ExecError {}),
        }
    }

    fn echo(&mut self, args: &cu_kind::Args) -> ExecResult {
        let mut str = String::new();
        writeln!(&mut str, "{}", args.join(" ")).map_err(ExecError::to)?;
        Ok(Some(str))
    }

    fn cat(&mut self, args: &cu_kind::Args, stdin: &Option<String>) -> ExecResult {
        let mut out = String::new();
        if let Some(input) = stdin {
            writeln!(&mut out, "{}", input).map_err(ExecError::to)?;
        }
        for filename in args {
            match self.fs_driver.read_to_string(filename) {
                Ok(file_content) => {
                    writeln!(&mut out, "{}", file_content).map_err(ExecError::to)?;
                }
                Err(_) => return Err(ExecError {}),
            }
        }
        Ok(Some(out))
    }

    fn wc(&mut self, args: &cu_kind::Args, stdin: &Option<String>) -> ExecResult {
        let mut lines = 0usize;
        let mut bytes = 0usize;
        let mut words = 0usize;

        if let Some(input) = stdin {
            bytes += input.len();
            words += input.split(&[' ', '\n']).filter(|x| !x.is_empty()).count();
            lines += input.split('\n').count();
        }

        for filename in args {
            match self.fs_driver.read_to_string(filename) {
                Ok(file_content) => {
                    bytes += file_content.len();
                    words += file_content
                        .split(&[' ', '\n'])
                        .filter(|x| !x.is_empty())
                        .count();
                    lines += file_content.split('\n').count();
                }
                Err(_) => return Err(ExecError {}),
            }
        }
        let mut out = String::new();
        writeln!(&mut out, "{} {} {}", lines, words, bytes).map_err(ExecError::to)?;
        Ok(Some(out))
    }
}
