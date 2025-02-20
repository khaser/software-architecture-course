mod command;
mod cu_kind;
mod env;
mod syntax;

use command::scheduler::{ExitCode, Scheduler};
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
                eprintln!("Error: {}", parse_error);
                continue;
            }
        };

        let mut sched = Scheduler::new(&mut env);
        let exit_codes = sched.run(commands);
        if exit_codes.into_iter().any(|x| x == ExitCode::Failure) {
            eprintln!("Error: Execution failed");
        }
        if sched.should_terminate {
            println!("Bye!");
            break;
        }
    }
}
