use std::process::ExitCode;

use super::env::Env;

pub trait Executable {
    fn execute(&self, env: Env) -> ExitCode;
}