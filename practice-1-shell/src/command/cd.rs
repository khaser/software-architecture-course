use std::path::{Path, PathBuf};

use crate::state::AppState;

use super::scheduler::ExitCode;

fn cd(current_dir: String, cd_argument: &str) -> PathBuf {
    let current_path = Path::new(&current_dir);
    let new_path = current_path.join(cd_argument);

    new_path.canonicalize().unwrap_or(new_path)
}

/* Could be implemented as a specific trait, but just a function is enough for this task. */
pub fn run_cd(state: &mut AppState, args: &[String]) -> ExitCode {
    if args.len() != 1 {
        return ExitCode::Failure;
    }

    let pwd = cd(state.pwd.clone(), &args[0]);
    if std::env::set_current_dir(pwd.clone()).is_err() {
        ExitCode::Failure
    } else {
        state.pwd = pwd.to_str().unwrap().to_owned();
        ExitCode::Success
    }
}
