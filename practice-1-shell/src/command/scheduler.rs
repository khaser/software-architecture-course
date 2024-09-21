use std::process::ExitCode;

use super::executable::Executable;

pub struct Scheduler {}

impl Scheduler {
    pub fn run(commands: Vec<Box<dyn Executable>>) -> Vec<ExitCode> {
        todo!()
    }
}
