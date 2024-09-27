pub type Args = Vec<String>;

pub enum CommandUnitKind {
    Cat(Args),
    Echo(Args),
    Wc(Args),
    Pwd(Args),
    Exit,
    External(String, Args), // SetEnvVar(String, String)
}
