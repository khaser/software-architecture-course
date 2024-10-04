use std::io::{stdout, Result, Write};
use std::vec::IntoIter;

use crate::cu_kind::Args;
use crate::cu_kind::Command;
use crate::cu_kind::CommandUnitKind;

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
    fn read_to_string(&self, filename: &String) -> Result<String>;
    fn current_dir(&self) -> Result<String>;
    fn write_fmt(&mut self, fmt: std::fmt::Arguments<'_>);
}

pub struct RealFsDriver;

impl SchedulerDriver for RealFsDriver {
    fn read_to_string(&self, filename: &String) -> Result<String> {
        std::fs::read_to_string(filename)
    }

    fn current_dir(&self) -> Result<String> {
        Ok(std::env::current_dir()?.display().to_string())
    }

    fn write_fmt(&mut self, fmt: std::fmt::Arguments<'_>) {
        stdout().write_fmt(fmt).unwrap()
    }
}

impl<'a, T> Scheduler<'a, T>
where
    T: SchedulerDriver,
{
    pub fn new(env: &'a mut Env, fs_driver: &'a mut T) -> Self {
        Scheduler {
            should_terminate: false,
            env,
            fs_driver,
        }
    }

    pub fn run(&mut self, commands: Vec<Command>) -> Result<ExitCode> {
        Ok(match commands.into_iter().next().unwrap() {
            Command(CommandUnitKind::Cat, args) => self.cat(&args)?,
            Command(CommandUnitKind::Echo, args) => self.echo(&args)?,
            Command(CommandUnitKind::Wc, args) => self.wc(&args)?,
            Command(CommandUnitKind::Pwd, _) => self.pwd()?,
            Command(CommandUnitKind::Exit, _) => self.exit()?,
            Command(CommandUnitKind::External, args) => {
                let mut iter = args.into_iter();
                self.run_external(iter.next().unwrap(), iter)?
            }
            Command(CommandUnitKind::SetEnvVar, v) => {
                let mut iter = v.into_iter();
                let var = iter.next().unwrap();
                let val = iter.next().unwrap();
                self.env.insert(var, val);
                ExitCode::Success
            }
        })
    }

    fn exit(&mut self) -> Result<ExitCode> {
        self.should_terminate = true;
        Ok(ExitCode::Success)
    }

    fn pwd(&mut self) -> Result<ExitCode> {
        write!(
            &mut self.fs_driver,
            "{}\n",
            std::env::current_dir()?.display()
        );
        Ok(ExitCode::Success)
    }

    fn echo(&mut self, args: &Args) -> Result<ExitCode> {
        write!(&mut self.fs_driver, "{}\n", args.join(" "));
        Ok(ExitCode::Success)
    }

    fn cat(&mut self, args: &Args) -> Result<ExitCode> {
        for filename in args {
            let file_content = self.fs_driver.read_to_string(filename)?;
            write!(&mut self.fs_driver, "{}\n", file_content);
        }
        Ok(ExitCode::Success)
    }

    fn wc(&mut self, args: &Args) -> Result<ExitCode> {
        let mut lines = 0usize;
        let mut bytes = 0usize;
        let mut words = 0usize;
        for filename in args {
            let file_content = self.fs_driver.read_to_string(filename)?;
            bytes += file_content.len();
            words += file_content
                .split(&[' ', '\n'])
                .into_iter()
                .filter(|x| !x.is_empty())
                .count();
            lines += file_content.split('\n').count();
        }
        write!(&mut self.fs_driver, "{} {} {}\n", lines, words, bytes);
        Ok(ExitCode::Success)
    }

    fn run_external(&mut self, name: String, args: IntoIter<String>) -> Result<ExitCode> {
        // TODO[akhorokhorin]: wrap command execution execution into SchedulerDriver bubble???
        let mut cmd = std::process::Command::new(name);
        cmd.args(args).envs(self.env as &Env).status().map(|s| {
            if s.success() {
                ExitCode::Success
            } else {
                ExitCode::Failure
            }
        })
    }
}
