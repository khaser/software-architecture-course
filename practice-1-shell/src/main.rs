mod command;
mod env;
mod syntax;

use command::scheduler::Scheduler;
use env::Env;
use std::io;
use syntax::lexer::Lexer;
use syntax::parser::Parser;

fn main() {
    let env = Env::new;

    loop {
        print!("r-shell ~ ");
        let mut cmd = String::new();
        if io::stdin().read_line(&mut cmd).is_err() {
            break;
        }

        // TODO: put cmd into lexer
        // let lexer = Lexer {};
        let parser = Parser {};
        let sched = Scheduler {};
    }
    println!("Hello, world!");
}
