use std::{env, fs, io::Read, process::ExitCode};

fn main() -> ExitCode {
    let args: Vec<String> = env::args().skip(1).collect();
    match args.as_slice() {
        [] => {
            let mut buf = String::new();
            let _ = std::io::stdin().read_to_string(&mut buf);
            print!("{}", buf);
            ExitCode::from(0)
        }
        args => {
            for filename in args {
                match fs::read_to_string(filename) {
                    Ok(file_content) => {
                        print!("{}", file_content);
                    }
                    Err(err) => {
                        eprintln!("cat: {}: {}", filename, err);
                        return ExitCode::from(1);
                    }
                }
            }
            ExitCode::from(0)
        }
    }
}
