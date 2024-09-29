mod command;
mod cu_kind;
mod env;
mod syntax;

use command::scheduler::{RealFsDriver, Scheduler};
use env::Env;
use std::io;
use std::io::Write;
use syntax::lexer::Lexer;
use syntax::parser::Parser;

fn main() {
    let mut env = Env::new();

    loop {
        print!("rush ~ ");
        io::stdout().flush().unwrap();
        let mut cmd = String::new();
        if io::stdin().read_line(&mut cmd).is_err() {
            break;
        }

        let lexer = Lexer::new(&cmd);
        let parser = Parser::new(&env);
        let commands = match parser.parse(lexer.tokenize()) {
            Ok(c) => c,
            Err(parse_error) => {
                eprintln!("{}", parse_error);
                continue;
            }
        };

        let mut sched = Scheduler::new(&mut env, RealFsDriver {});
        if let Err(e) = &sched.run(commands) {
            eprintln!("{}", &e);
        }
        if sched.should_terminate {
            println!("Bye!");
            break;
        }
    }
}
