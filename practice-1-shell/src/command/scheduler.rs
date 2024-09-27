use std::env::current_dir;
use std::fs::read_to_string;
use std::io::Result;
use std::process::Command;

use crate::cu_kind::Args;
use crate::cu_kind::CommandUnitKind;

pub struct Scheduler {
    pub should_terminate: bool,
}

pub enum ExitCode {
    Success,
    Failure,
}

impl Scheduler {
    pub fn new() -> Self {
        Scheduler {
            should_terminate: false,
        }
    }

    pub fn run(&mut self, commands: Vec<CommandUnitKind>) -> Result<Vec<ExitCode>> {
        Ok(vec![match commands.into_iter().next().unwrap() {
            CommandUnitKind::Cat(args) => self.cat(&args)?,
            CommandUnitKind::Echo(args) => self.echo(&args)?,
            CommandUnitKind::Wc(args) => self.wc(&args)?,
            CommandUnitKind::Pwd => self.pwd()?,
            CommandUnitKind::Exit => self.exit()?,
            CommandUnitKind::External(name, args) => self.run_external(name, args)?,
        }])
    }

    fn exit(&mut self) -> Result<ExitCode> {
        self.should_terminate = true;
        Ok(ExitCode::Success)
    }

    fn pwd(&self) -> Result<ExitCode> {
        println!("{}", current_dir()?.display());
        Ok(ExitCode::Success)
    }

    fn echo(&self, args: &Args) -> Result<ExitCode> {
        println!("{}", args.join(" "));
        Ok(ExitCode::Success)
    }

    fn cat(&self, args: &Args) -> Result<ExitCode> {
        for filename in args {
            println!("{}", read_to_string(filename)?);
        }
        Ok(ExitCode::Success)
    }

    fn wc(&self, args: &Args) -> Result<ExitCode> {
        let mut lines = 0usize;
        let mut bytes = 0usize;
        let mut words = 0usize;
        for filename in args {
            let file_content = read_to_string(filename)?;
            bytes += file_content.len();
            words += file_content
                .split(&[' ', '\n'])
                .into_iter()
                .filter(|x| !x.is_empty())
                .count();
            lines += file_content.split('\n').count() - 1;
        }
        println!("{} {} {}", lines, words, bytes);
        Ok(ExitCode::Success)
    }

    fn run_external(&mut self, name: String, args: Args) -> Result<ExitCode> {
        let mut cmd = Command::new(name);
        cmd.args(args).status().map(|s| {
            if s.success() {
                ExitCode::Success
            } else {
                ExitCode::Failure
            }
        })
    }
}
