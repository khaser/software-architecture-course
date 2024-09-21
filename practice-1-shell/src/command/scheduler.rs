use std::process::ExitCode;

use super::command::executable::Executable;

pub struct Scheduler {

}

impl Scheduler {
    pub fn run(commands: Vec<Box<dyn Executable>>) -> Vec<ExitCode> {
        todo!()
    }
}