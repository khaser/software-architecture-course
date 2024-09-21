use std::process::ExitCode;

pub trait Executable {
    fn execute(&self) -> ExitCode;
}
