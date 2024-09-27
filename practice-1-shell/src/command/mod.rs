pub mod scheduler;

use std::process::Command;

use crate::cu_kind::CommandUnitKind;

#[cfg(test)]
mod tests;

pub struct CommandUnit {
    process: Command,
    kind: CommandUnitKind,
}
