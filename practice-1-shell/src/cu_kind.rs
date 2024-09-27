pub type Args = Vec<String>;

pub enum CommandUnitKind {
    Cat(Args),
    Echo(Args),
    Wc(Args),
    Pwd(Args),
    // SetEnvVar(String, String)
    // External(Args)
}