use std::{env, process::ExitCode};

fn main() -> ExitCode {
    match env::current_dir() {
        Ok(path) => {
            println!("{}", path.into_os_string().into_string().unwrap());
            ExitCode::from(0)
        }
        Err(err) => {
            eprintln!("pwd: {}", err);
            ExitCode::from(1)
        }
    }
}
