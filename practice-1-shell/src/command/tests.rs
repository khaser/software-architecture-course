use super::scheduler::ExitCode::*;
use super::scheduler::*;

use crate::cu_kind::Command::*;
use crate::env::Env;

#[test]
fn scheduler_exit_test() {
    let mut env = Env::new();
    let mut sched = Scheduler::new(&mut env);
    assert_eq!(sched.run(vec![Exit]), vec![Success],);
    assert_eq!(sched.should_terminate, true);
}

// No unit tests presented for external command calls
// TODO[akhorokhorin]: implement integration tests for externals
