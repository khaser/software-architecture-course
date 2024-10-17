use std::{env, fs, io::Read, process};

fn main() {
    let args: Vec<String> = env::args().skip(1).collect();
    match args.as_slice() {
        [] => {
            let mut buf = String::new();
            let _ = std::io::stdin().read_to_string(&mut buf);
            print!("{}", buf);
        }
        args => {
            for filename in args {
                match fs::read_to_string(filename) {
                    Ok(file_content) => {
                        print!("{}", file_content);
                    }
                    Err(err) => {
                        eprintln!("cat: {}: {}", filename, err);
                        process::exit(1)
                    }
                }
            }
        }
    }
}
