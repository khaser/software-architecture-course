use super::scheduler::Scheduler;
use crate::cu_kind::*;

#[test]
fn scheduler_exit_test() {
    let mut sched = Scheduler::new();
    sched.run(vec![Command(CommandUnitKind::Exit, vec![])]);
    assert_eq!(sched.should_terminate, true);
}

#[test]
fn scheduler_echo_test() {
    let mut sched = Scheduler::new();
    sched.run(vec![Command(CommandUnitKind::Echo, vec![])]);
    assert_eq!(sched.should_terminate, false);
}
