use std::env;

fn main() {
    match env::current_dir() {
        Ok(path) => println!("{}", path.into_os_string().into_string().unwrap()),
        Err(err) => {
            eprintln!("pwd: {}", err)
        }
    }
}
