pub mod lexer;
pub mod parser;
mod token;

pub enum CommandUnitType {
    Env { variable: String, value: String },
    Exec { cmd: String },
}
