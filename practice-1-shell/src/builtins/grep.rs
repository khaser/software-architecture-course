use std::{
    fs,
    io::{self, Error, ErrorKind},
    process::ExitCode,
};

use clap::Parser;
use regex::Regex;

/// grep - print lines that match patterns
#[derive(Parser)]
#[command(about)]
struct Args {
    /// Ignore case distinctions in patterns and data
    #[arg(short, long = "ignore-case", default_value_t = false)]
    ignore_case: bool,
    /// Match only whole words
    #[arg(short, long = "word-regexp", default_value_t = false)]
    word_regexp: bool,
    /// Print AFTER_CONTEXT lines if trailing context
    #[arg(short = 'A', long = "after-context", default_value_t = 0)]
    after_context: usize,
    /// Pattern to search
    pattern: String,
    /// Source file names for search
    file_name: Vec<String>,
}

fn error_main(args: Args) -> io::Result<()> {
    let filenames = &args.file_name;
    let ignore_case = if args.ignore_case { "i" } else { "" };
    let whole_words = if args.word_regexp { "\\b" } else { "" };
    let regex = format!(
        "(?{}:{}{}{})",
        ignore_case, whole_words, &args.pattern, whole_words
    );

    let re = Regex::new(regex.as_str()).map_err(|err| Error::new(ErrorKind::InvalidInput, err))?;
    for filename in filenames {
        let content = match fs::read_to_string(filename) {
            Ok(content) => content,
            Err(err) => {
                eprintln!("grep: {}: {}", filename, err);
                return Err(err);
            }
        };
        let lines = content.split('\n').collect::<Vec<_>>();
        let lines_count = lines.len();
        let intervals = (0..lines_count)
            .filter(|line_number| re.is_match(lines[*line_number]))
            .map(|index| (index, index + 1 + args.after_context));
        let merged_intervals =
            intervals.fold(Vec::<(usize, usize)>::new(), |mut vec, (start, end)| {
                match vec.last_mut() {
                    Some((_, last_end)) if *last_end >= start => {
                        *last_end = std::cmp::min(end, lines_count);
                    }
                    _ => vec.push((start, end)),
                };
                vec
            });
        for (left, right) in merged_intervals {
            if filenames.len() == 1 {
                println!("{}", lines[left..right].join("\n"))
            } else {
                println!("{}: \t{}", filename, lines[left..right].join("\n"))
            }
        }
    }
    Ok(())
}

fn main() -> ExitCode {
    match error_main(Args::parse()) {
        Ok(()) => ExitCode::from(0),
        Err(_) => ExitCode::from(1),
    }
}
