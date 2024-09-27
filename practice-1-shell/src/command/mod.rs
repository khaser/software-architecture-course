pub mod scheduler;

use std::process::{Command, ExitCode};

use crate::cu_kind::CommandUnitKind;

pub struct CommandUnit {
    process: Command,
    kind: CommandUnitKind,
}
