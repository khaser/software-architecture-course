mod command;
mod cu_kind;
mod env;
mod syntax;

use command::scheduler::Scheduler;
use env::Env;
use std::io;
use syntax::lexer::Lexer;
use syntax::parser::Parser;

fn main() {
    let env = Env::new();

    loop {
        print!("rush ~ ");
        let mut cmd = String::new();
        if io::stdin().read_line(&mut cmd).is_err() {
            break;
        }

        // TODO: put cmd into lexer
        let lexer = Lexer::new(&cmd);
        let mut parser = Parser::new(&env);
        let mut sched = Scheduler::new();
        sched.run(parser.parse(lexer.tokenize()));
        if sched.should_terminate {
            print!("Bye!");
            break;
        }
    }
}
