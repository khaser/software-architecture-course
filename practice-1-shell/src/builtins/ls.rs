use std::env;
use std::fs;
use std::io;
use std::path::Path;

fn list_directory_contents(path: &Path) -> io::Result<()> {
    if path.is_dir() {
        let entries = fs::read_dir(path)?;
        for entry in entries {
            let entry = entry?;
            let file_name = entry.file_name();
            println!("{}", file_name.to_string_lossy());
        }
    } else {
        println!("{} is not a directory", path.display());
    }
    Ok(())
}

fn main() {
    let args: Vec<_> = env::args().collect();

    let target_dir = match args.len() {
        1 => env::current_dir().expect("Failed to get current directory"),
        2 => Path::new(&args[1]).to_path_buf(),
        _ => {
            eprintln!("Error: Too many arguments");
            std::process::exit(1);
        }
    };

    // List the contents of the target directory
    if let Err(e) = list_directory_contents(&target_dir) {
        eprintln!("Error: {}", e);
    }
}
