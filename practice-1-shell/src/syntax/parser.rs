use std::{fmt, slice::Split};

use crate::{cu_kind::CommandUnitKind, env::Env};

use super::token::{LiteralKind, Token};

pub struct Parser<'a> {
    env: &'a Env,
}

#[derive(Debug, PartialEq, Eq)]
pub enum ParserError {
    UnterminatedLiteral(String),
    UnexpectedToken(String),
    ZeroCommandArgs,
}

impl fmt::Display for ParserError {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        match self {
            ParserError::UnterminatedLiteral(lit) => write!(f, "Unterminated literal: {}", lit),
            ParserError::UnexpectedToken(tok) => write!(f, "Unexpected token: {}", tok),
            ParserError::ZeroCommandArgs => write!(f, "Zero command args"),
        }
    }
}

pub type PResult<T> = Result<T, ParserError>;

impl<'a, 'b> Parser<'a> {
    pub fn new(env: &'a Env) -> Self {
        Parser { env: env }
    }

    fn parse_commands(
        &mut self,
        tokens: &'b Vec<Token>,
    ) -> Split<'b, Token, impl FnMut(&'_ Token) -> bool> {
        tokens.split(|token| if let Token::Pipe = token { true } else { false })
    }

    fn expanse_string(kind: LiteralKind, content: String) -> String {
        match kind {
            super::token::LiteralKind::SingleQuoted => content,
            super::token::LiteralKind::DoubleQuoted => {
                content // TODO("Changed later")
            }
        }
    }

    fn token_to_string(token: &Token) -> PResult<String> {
        match token {
            Token::String(str) => Ok(str.clone()),
            Token::Literal {
                content,
                kind,
                terminated,
            } => {
                if !terminated {
                    Err(ParserError::UnterminatedLiteral(content.clone()))
                } else {
                    let str = Parser::expanse_string(*kind, content.clone());
                    Ok(str)
                }
            }
            t @ _ => Err(ParserError::UnexpectedToken(format!("{:?}", t))),
        }
    }

    fn expanse(
        &mut self,
        tokens: Split<'b, Token, impl FnMut(&'_ Token) -> bool>,
    ) -> PResult<Vec<Vec<String>>> {
        tokens
            .map(|tokens| {
                let mut command_tokens = Vec::new();
                for token in tokens {
                    if let Token::WhiteSpace = token {
                        continue;
                    }
                    match Parser::token_to_string(token) {
                        Ok(token) => command_tokens.push(token),
                        Err(err) => return Err(err),
                    }
                }
                Ok(command_tokens)
            })
            .collect()
    }

    fn parse_command(command_tokens: Vec<String>) -> PResult<CommandUnitKind> {
        let mut iter = command_tokens.into_iter();
        let command = iter.next();
        match command {
            Some(command) => {
                let args = iter.collect();
                Ok(match command.as_str() {
                    "echo" => CommandUnitKind::Echo(args),
                    "wc" => CommandUnitKind::Wc(args),
                    "pwd" => CommandUnitKind::Pwd,
                    "cat" => CommandUnitKind::Cat(args),
                    "exit" => CommandUnitKind::Exit,
                    _ => CommandUnitKind::External(command, args),
                })
            }
            None => return Err(ParserError::ZeroCommandArgs),
        }
    }

    pub fn parse(self, tokens: Vec<Token>) -> PResult<Vec<CommandUnitKind>> {
        let mut parser = self;
        let splitted = parser.parse_commands(&tokens);
        let expansioned = parser.expanse(splitted)?;
        expansioned.into_iter().map(Parser::parse_command).collect()
    }
}
