use super::scheduler::*;
use crate::cu_kind::*;
use crate::env::Env;

#[test]
fn scheduler_exit_test() {
    let mut env = Env::new();
    let mut sched = Scheduler::new(&mut env, RealFsDriver {});
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
    let mut sched = Scheduler::new(&mut env, RealFsDriver {});
    assert_eq!(
        sched
            .run(vec![Command(
                CommandUnitKind::Echo,
                vec!["hello".to_string(), "echo".to_string()]
            )])
            .unwrap(),
        ExitCode::Success,
    );
    assert_eq!(sched.should_terminate, false);
}

// No unit tests presented for external command calls
// TODO[akhorokhorin]: implement integration tests
