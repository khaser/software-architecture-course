pub type Args = Vec<String>;

#[derive(Debug, PartialEq, Eq)]
pub enum Command {
    Cat(Args),
    Echo(Args),
    Wc(Args),
    Pwd,
    Exit,
    External(String, Args),
    SetEnvVar(String, String),
}
