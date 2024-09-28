use std::env::current_dir;
use std::fs::read_to_string;
use std::io::Result;
use std::vec::IntoIter;

use crate::cu_kind::Args;
use crate::cu_kind::Command;
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

    pub fn run(&mut self, commands: Vec<Command>) -> Result<Vec<ExitCode>> {
        Ok(vec![match commands.into_iter().next().unwrap() {
            Command(CommandUnitKind::Cat, args) => self.cat(&args)?,
            Command(CommandUnitKind::Echo, args) => self.echo(&args)?,
            Command(CommandUnitKind::Wc, args) => self.wc(&args)?,
            Command(CommandUnitKind::Pwd, _) => self.pwd()?,
            Command(CommandUnitKind::Exit, _) => self.exit()?,
            Command(CommandUnitKind::External, args) => {
                let mut iter = args.into_iter();
                self.run_external(iter.next().unwrap(), iter)?
            },
            Command(CommandUnitKind::SetEnvVar, _) => todo!(),
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

    fn run_external(&mut self, name: String, args: IntoIter<String>) -> Result<ExitCode> {
        let mut cmd = std::process::Command::new(name);
        cmd.args(args).status().map(|s| {
            if s.success() {
                ExitCode::Success
            } else {
                ExitCode::Failure
            }
        })
    }
}
