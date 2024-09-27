use std::env::current_dir;
use std::fs::read_to_string;
use std::process::ExitCode;

use crate::cu_kind::Args;
use crate::cu_kind::CommandUnitKind;

pub struct Scheduler {
    pub should_terminate: bool,
}

impl Scheduler {
    pub fn new() -> Self {
        Scheduler {
            should_terminate: false,
        }
    }

    pub fn run(&mut self, commands: Vec<CommandUnitKind>) -> Vec<ExitCode> {
        vec![match commands.into_iter().next().unwrap() {
            CommandUnitKind::Cat(args) => self.cat(&args),
            CommandUnitKind::Echo(args) => self.echo(&args),
            CommandUnitKind::Wc(args) => self.wc(&args),
            CommandUnitKind::Pwd => self.pwd(),
            CommandUnitKind::Exit => self.exit(),
            CommandUnitKind::External(name, args) => unimplemented!(),
        }]
    }

    fn exit(&mut self) -> ExitCode {
        self.should_terminate = true;
        ExitCode::SUCCESS
    }

    fn pwd(&self) -> ExitCode {
        match current_dir() {
            Ok(path) => {
                println!("{}", path.display());
                ExitCode::SUCCESS
            }
            Err(err) => {
                eprintln!("{}", err);
                ExitCode::FAILURE
            }
        }
    }

    fn echo(&self, args: &Args) -> ExitCode {
        println!("{}", args.join(" "));
        ExitCode::SUCCESS
    }

    fn cat(&self, args: &Args) -> ExitCode {
        for filename in args {
            match read_to_string(filename) {
                Ok(file_content) => {
                    println!("{}", file_content);
                }
                Err(err) => {
                    eprintln!("{}", err);
                    return ExitCode::FAILURE;
                }
            }
        }
        ExitCode::SUCCESS
    }

    fn wc(&self, args: &Args) -> ExitCode {
        let mut lines = 0usize;
        let mut bytes = 0usize;
        let mut words = 0usize;
        for filename in args {
            match read_to_string(filename) {
                Ok(file_content) => {
                    bytes += file_content.len();
                    words += file_content.split(&[' ', '\n']).count() - 1;
                    lines += file_content.split('\n').count() - 1;
                }
                Err(err) => {
                    eprintln!("{}", err);
                    return ExitCode::FAILURE;
                }
            }
        }
        println!("{} {} {}", lines, words, bytes);
        ExitCode::SUCCESS
    }
}
