mod command;
mod cu_kind;
mod env;
mod syntax;

use command::scheduler::Scheduler;
use env::Env;
use std::io;
use std::io::Write;
use std::result::Result;
use syntax::lexer::Lexer;
use syntax::parser::Parser;

fn main() {
    let env = Env::new();

    loop {
        print!("rush ~ ");
        io::stdout().flush().unwrap();
        let mut cmd = String::new();
        if io::stdin().read_line(&mut cmd).is_err() {
            break;
        }

        let lexer = Lexer::new(&cmd);
        let parser = Parser::new(&env);
        let mut sched = Scheduler::new();
        let commands = match parser.parse(lexer.tokenize()) {
            Ok(c) => c,
            Err(parse_error) => {
                eprintln!("{}", parse_error);
                continue;
            }
        };
        if let Err(e) = &sched.run(commands) {
            eprintln!("{}", &e);
        }
        if sched.should_terminate {
            println!("Bye!");
            break;
        }
    }
}
