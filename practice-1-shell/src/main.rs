mod command;
mod cu_kind;
mod env;
mod syntax;

use command::scheduler::Scheduler;
use env::Env;
use std::io;
use std::io::Write;
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
        let mut parser = Parser::new(&env);
        let mut sched = Scheduler::new();
        let commands = parser.parse(lexer.tokenize());
        sched.run(commands.unwrap()); //TODO(kirill-mitkin): remove unwrap
        if sched.should_terminate {
            println!("Bye!");
            break;
        }
    }
}
