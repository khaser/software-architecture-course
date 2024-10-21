use std::{env, fs, io::Read, process};

fn wc_str(content: String) -> (usize, usize, usize) {
    (
        content.len(),
        content
            .split(&[' ', '\n'])
            .filter(|x| !x.is_empty())
            .count(),
        content.split('\n').count() - 1,
    )
}

fn main() {
    let args: Vec<String> = env::args().skip(1).collect();

    let mut exit_code = 0;

    match args.as_slice() {
        [] => {
            let mut buf = String::new();
            std::io::stdin().read_to_string(&mut buf).unwrap();
            let (lines, words, bytes) = wc_str(buf);
            println!("{} {} {}", lines, words, bytes);
        }
        [filename] => match fs::read_to_string(&filename) {
            Ok(file_content) => {
                let (lines, words, bytes) = wc_str(file_content);
                println!("{} {} {} {}", lines, words, bytes, filename);
            }
            Err(err) => {
                eprintln!("wc: {}: {}", filename, err);
                exit_code = 1;
            }
        },
        args => {
            let (mut lines_total, mut bytes_total, mut words_total) = (0usize, 0usize, 0usize);
            for filename in args {
                match fs::read_to_string(&filename) {
                    Ok(file_content) => {
                        let (lines, words, bytes) = wc_str(file_content);
                        println!("{} {} {} {}", lines, words, bytes, filename);
                        lines_total += lines;
                        bytes_total += bytes;
                        words_total += words;
                    }
                    Err(err) => {
                        eprintln!("wc: {}", err);
                        exit_code = 1;
                    }
                }
            }
            println!("{} {} {} total", lines_total, words_total, bytes_total);
        }
    }

    process::exit(exit_code)
}
