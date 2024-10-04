use super::scheduler::*;
use crate::cu_kind::*;
use crate::env::Env;
use std::io::{Error, ErrorKind, Result};

use std::collections::HashMap;

#[derive(Clone)]
pub struct MockFsDriver {
    cur_dir: &'static str,
    dir_content: HashMap<&'static str, &'static str>,
    output: String,
}

impl SchedulerDriver for MockFsDriver {
    fn read_to_string(&self, filename: &String) -> Result<String> {
        match self.dir_content.get(filename.as_str()) {
            Some(file_content) => Ok(file_content.to_string()),
            None => Err(Error::new(ErrorKind::NotFound, "")),
        }
    }

    fn current_dir(&self) -> Result<String> {
        Ok(self.cur_dir.to_string())
    }

    fn write_fmt(&mut self, fmt: std::fmt::Arguments<'_>) {
        let _ = std::fmt::write(&mut self.output, fmt);
    }
}

fn new_default_mock() -> MockFsDriver {
    MockFsDriver {
        cur_dir: "/vroot",
        dir_content: HashMap::from([
            ("vfile1", "vcontent1"),
            ("vfile2", "vcontent2"),
            ("singleline_with_3_words", "aba caba daba"),
        ]),
        output: String::new(),
    }
}

#[test]
fn scheduler_exit_test() {
    let mut env = Env::new();
    let mut mock = new_default_mock();
    let mut sched = Scheduler::new(&mut env, &mut mock);
    assert_eq!(
        sched
            .run(vec![Command(CommandUnitKind::Exit, vec![])])
            .unwrap(),
        ExitCode::Success,
    );
    assert_eq!(sched.should_terminate, true);
}

#[test]
fn scheduler_echo_test() {
    let mut env = Env::new();
    let mut mock = new_default_mock();
    let mut sched = Scheduler::new(&mut env, &mut mock);
    assert_eq!(
        sched
            .run(vec![Command(
                CommandUnitKind::Echo,
                vec!["hello".to_string(), "from echo!".to_string()]
            )])
            .unwrap(),
        ExitCode::Success,
    );
    assert_eq!(sched.should_terminate, false);
    assert_eq!(mock.output, "hello from echo!\n");
}

#[test]
fn scheduler_cat_test() {
    let mut env = Env::new();
    let mut mock = new_default_mock();
    let mut sched = Scheduler::new(&mut env, &mut mock);
    assert_eq!(
        sched
            .run(vec![Command(
                CommandUnitKind::Cat,
                vec!["vfile1".to_string(), "vfile2".to_string()]
            )])
            .unwrap(),
        ExitCode::Success,
    );
    assert_eq!(sched.should_terminate, false);
    assert_eq!(mock.output, "vcontent1\nvcontent2\n");
}

#[test]
fn scheduler_wc_test() {
    let mut env = Env::new();
    let mut mock = new_default_mock();
    let mut sched = Scheduler::new(&mut env, &mut mock);
    assert_eq!(
        sched
            .run(vec![Command(
                CommandUnitKind::Wc,
                vec!["singleline_with_3_words".to_string()]
            )])
            .unwrap(),
        ExitCode::Success,
    );
    assert_eq!(sched.should_terminate, false);
    assert_eq!(mock.output, "1 3 13\n");
}

// No unit tests presented for external command calls
// TODO[akhorokhorin]: implement integration tests for externals
