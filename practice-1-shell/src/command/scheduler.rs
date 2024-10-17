use std::ffi::OsString;
use std::io::Error as IoError;
use std::process::{self, Stdio};
use std::slice::Iter;

use crate::cu_kind::{self, Command::*};
use crate::env::Env;

pub struct Scheduler<'a> {
    pub should_terminate: bool,
    env: &'a mut Env,
}

#[derive(Debug, PartialEq)]
pub enum ExitCode {
    Success,
    Failure,
}

struct ExecError;

impl From<IoError> for ExecError {
    fn from(_err: IoError) -> Self {
        ExecError {}
    }
}

type ExecResult = std::result::Result<(), ExecError>;

impl<'a> Scheduler<'a> {
    pub fn new(env: &'a mut Env) -> Self {
        Scheduler {
            should_terminate: false,
            env,
        }
    }

    pub fn run(&mut self, commands: Vec<cu_kind::Command>) -> Vec<ExitCode> {
        let mut stdio_state = Stdio::inherit();
        let exit_codes = commands
            .into_iter()
            .scan(&mut stdio_state, |stdin, cmd| {
                let exec_res = match cmd {
                    Exit => {
                        self.should_terminate = true;
                        **stdin = process::Stdio::null();
                        Ok(())
                    }
                    SetEnvVar(var, val) => {
                        self.env.insert(var, val);
                        **stdin = process::Stdio::null();
                        Ok(())
                    }
                    External(exec_path, args) => {
                        self.external(exec_path.into(), args.iter(), stdin)
                    }
                    Cat(args) => {
                        self.external(self.get_builtin_absolute_path("cat"), args.iter(), stdin)
                    }
                    Echo(args) => {
                        self.external(self.get_builtin_absolute_path("echo"), args.iter(), stdin)
                    }
                    Wc(args) => {
                        self.external(self.get_builtin_absolute_path("wc"), args.iter(), stdin)
                    }
                    Pwd => self.external(self.get_builtin_absolute_path("pwd"), [].iter(), stdin),
                };

                Some(match exec_res {
                    Ok(()) => ExitCode::Success,
                    Err(_) => ExitCode::Failure,
                })
            })
            .collect();

        let mut cmd = process::Command::new(self.get_builtin_absolute_path("cat"));
        let mut child = cmd
            .stdin(stdio_state)
            .stdout(Stdio::inherit())
            .spawn()
            .unwrap();
        let _ = child.wait();

        exit_codes
    }

    fn get_builtin_absolute_path(&self, executable: &str) -> OsString {
        let exe_path = std::env::current_exe().expect("failed to determine crate install path");
        exe_path.parent().unwrap().join(executable).into()
    }

    fn external(
        &self,
        executable: OsString,
        args: Iter<String>,
        stdin: &mut &mut Stdio,
    ) -> ExecResult {
        let mut cmd = process::Command::new(&executable);
        unsafe {
            cmd.args(args)
                .envs(self.env as &Env)
                .stdin(std::ptr::read(*stdin as *mut Stdio))
                .stdout(Stdio::piped());
            match cmd.spawn() {
                Ok(child) => {
                    **stdin = child.stdout.unwrap().into();
                    Ok(())
                }
                Err(err) => {
                    **stdin = Stdio::null();
                    eprintln!(
                        "failed to execute {}: {}",
                        executable.into_string().unwrap(),
                        err
                    );
                    Err(err.into())
                }
            }
        }
    }
}
