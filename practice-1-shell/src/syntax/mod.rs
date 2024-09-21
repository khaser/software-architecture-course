pub mod lexer;
pub mod parser;
mod token;

#[cfg(test)]
mod tests;

pub enum CommandUnitType {
    Env { variable: String, value: String },
    Exec { cmd: String },
}
