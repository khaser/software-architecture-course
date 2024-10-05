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
    out_buffer: String,
}

#[derive(Debug, PartialEq)]
pub enum ExitCode {
    Success,
    Failure,
}

// Created for the possibility of unit-testing
pub trait SchedulerDriver {
    fn read_to_string(&self, filename: &String) -> Result<String>;
    fn current_dir(&self) -> Result<String>;
}

pub struct RealFsDriver;

impl SchedulerDriver for RealFsDriver {
    fn read_to_string(&self, filename: &String) -> Result<String> {
        std::fs::read_to_string(filename)
    }

    fn current_dir(&self) -> Result<String> {
        Ok(std::env::current_dir()?.display().to_string())
    }
}

impl Write for RealFsDriver {
    fn write_str(&mut self, s: &str) -> std::fmt::Result {
        Ok(io::stdout().write_all(s.as_bytes()).unwrap())
    }
}

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
            out_buffer: String::new(),
        }
    }

    pub fn run(&mut self, commands: Vec<cu_kind::Command>) -> Vec<ExitCode> {
        let mut iobuf: Option<String> = None;
        let exit_codes = commands
            .into_iter()
            .scan(&mut iobuf, |stdin, cmd| {
                let exit_code = Some(match cmd {
                    cu_kind::Command(cu_kind::CommandUnitKind::Cat, args) => self.cat(&args, stdin),
                    cu_kind::Command(cu_kind::CommandUnitKind::Echo, args) => self.echo(&args),
                    cu_kind::Command(cu_kind::CommandUnitKind::Wc, args) => self.wc(&args, stdin),
                    cu_kind::Command(cu_kind::CommandUnitKind::Pwd, _) => self.pwd(),
                    cu_kind::Command(cu_kind::CommandUnitKind::Exit, _) => self.exit(),
                    cu_kind::Command(cu_kind::CommandUnitKind::External, args) => {
                        let mut iter = args.into_iter();
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
                        self.out_buffer = String::from_utf8(output.stdout).unwrap();
                        if output.status.success() {
                            ExitCode::Success
                        } else {
                            ExitCode::Failure
                        }
                    }
                    cu_kind::Command(cu_kind::CommandUnitKind::SetEnvVar, v) => {
                        let mut iter = v.into_iter();
                        let var = iter.next().unwrap();
                        let val = iter.next().unwrap();
                        self.env.insert(var, val);
                        ExitCode::Success
                    }
                });

                **stdin = Some(self.out_buffer.drain(..).collect());
                exit_code
            })
            .collect();

        if let Some(output) = iobuf {
            self.fs_driver.write_str(&output);
        };

        exit_codes
    }

    fn exit(&mut self) -> ExitCode {
        self.should_terminate = true;
        ExitCode::Success
    }

    fn pwd(&mut self) -> ExitCode {
        match std::env::current_dir() {
            Ok(path) => {
                write!(&mut self.out_buffer, "{}\n", path.display());
                ExitCode::Success
            }
            Err(_) => ExitCode::Failure,
        }
    }

    fn echo(&mut self, args: &cu_kind::Args) -> ExitCode {
        write!(&mut self.out_buffer, "{}\n", args.join(" "));
        ExitCode::Success
    }

    fn cat(&mut self, args: &cu_kind::Args, stdin: &Option<String>) -> ExitCode {
        if let Some(input) = stdin {
            write!(&mut self.out_buffer, "{}\n", input);
        }
        for filename in args {
            match self.fs_driver.read_to_string(filename) {
                Ok(file_content) => {
                    write!(&mut self.out_buffer, "{}\n", file_content);
                }
                Err(_) => return ExitCode::Failure,
            }
        }
        ExitCode::Success
    }

    fn wc(&mut self, args: &cu_kind::Args, stdin: &Option<String>) -> ExitCode {
        let mut lines = 0usize;
        let mut bytes = 0usize;
        let mut words = 0usize;

        if let Some(input) = stdin {
            bytes += input.len();
            words += input
                .split(&[' ', '\n'])
                .into_iter()
                .filter(|x| !x.is_empty())
                .count();
            lines += input.split('\n').count();
        }

        for filename in args {
            match self.fs_driver.read_to_string(filename) {
                Ok(file_content) => {
                    bytes += file_content.len();
                    words += file_content
                        .split(&[' ', '\n'])
                        .into_iter()
                        .filter(|x| !x.is_empty())
                        .count();
                    lines += file_content.split('\n').count();
                }
                Err(_) => return ExitCode::Failure,
            }
        }
        write!(&mut self.out_buffer, "{} {} {}\n", lines, words, bytes);
        ExitCode::Success
    }
}
