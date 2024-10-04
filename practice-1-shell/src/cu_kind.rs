pub type Args = Vec<String>;

#[derive(Debug, PartialEq, Eq)]
pub enum CommandUnitKind {
    Cat,
    Echo,
    Wc,
    Pwd,
    Exit,
    External,
    SetEnvVar,
}

#[derive(Debug, PartialEq, Eq)]
pub struct Command(pub CommandUnitKind, pub Args);
