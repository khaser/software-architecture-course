mod env;
pub mod executable;

use std::process::{Command, ExitCode};

use executable::Executable;


pub struct Exit {
    process: Command,
}

impl Executable for Exit {
    fn execute(&self, env: env::Env) -> ExitCode {
        todo!()
    }
}
pub struct Cat {
    process: Command,
}

impl Executable for Cat {
    fn execute(&self, env: env::Env) -> ExitCode {
        todo!()
    }
}

pub struct Echo {
    process: Command,
}

impl Executable for Echo {
    fn execute(&self, env: env::Env) -> ExitCode {
        todo!()
    }
}

pub struct Wc {
    process: Command,
}

impl Executable for Wc {
    fn execute(&self, env: env::Env) -> ExitCode {
        todo!()
    }
}

pub struct Pwd {
    process: Command,
}

impl Executable for Pwd {
    fn execute(&self, env: env::Env) -> ExitCode {
        todo!()
    }
}

pub struct SetEnvVar {
    process: Command,
}

impl Executable for SetEnvVar {
    fn execute(&self, env: env::Env) -> ExitCode {
        todo!()
    }
}

pub struct External {
    process: Command,
}

impl Executable for External {
    fn execute(&self, env: env::Env) -> ExitCode {
        todo!()
    }
}
