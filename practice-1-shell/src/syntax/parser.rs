use std::{fmt, slice::Split, vec::IntoIter};

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
    SetEnvValueExpected,
}

impl fmt::Display for ParserError {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        match self {
            ParserError::UnterminatedLiteral(lit) => write!(f, "Unterminated literal: {}", lit),
            ParserError::UnexpectedToken(tok) => write!(f, "Unexpected token: {}", tok),
            ParserError::ZeroCommandArgs => write!(f, "Zero command args"),
            ParserError::SetEnvValueExpected => write!(f, "You cannot set empty value to var"),
        }
    }
}

pub type PResult<T> = Result<T, ParserError>;

enum ParserToken {
    String(String),
    Eq,
}

impl ParserToken {
    pub fn to_string(self) -> String {
        match self {
            ParserToken::String(str) => str,
            ParserToken::Eq => "=".to_string(),
        }
    }

    pub fn to_command(self) -> PResult<String> {
        match self {
            ParserToken::String(str) => Ok(str),
            ParserToken::Eq => Err(ParserError::UnexpectedToken(format!("{:?}", Token::Eq))),
        }
    }
}

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

    fn token_to_string(token: &Token) -> PResult<ParserToken> {
        match token {
            Token::Ident(str) => Ok(ParserToken::String(str.clone())),
            Token::Literal {
                content,
                kind,
                terminated,
            } => {
                if !terminated {
                    Err(ParserError::UnterminatedLiteral(content.clone()))
                } else {
                    let str = Parser::expanse_string(*kind, content.clone());
                    Ok(ParserToken::String(str))
                }
            }
            Token::Pipe | Token::WhiteSpace => panic!("There cannot be such token"),
            Token::Unknown => Err(ParserError::UnexpectedToken(format!(
                "{:?}",
                Token::Unknown
            ))),
            Token::Eq => Ok(ParserToken::Eq),
            // t @ _ => ,
        }
    }

    fn expanse(
        &mut self,
        tokens: Split<'b, Token, impl FnMut(&'_ Token) -> bool>,
    ) -> PResult<Vec<Vec<ParserToken>>> {
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

    fn collect_args(iter: IntoIter<ParserToken>) -> Vec<String> {
        iter.map(ParserToken::to_string).collect()
    }

    fn parse_external(
        mut iter: IntoIter<ParserToken>,
        first_arg: String,
    ) -> PResult<CommandUnitKind> {
        Ok(match iter.next() {
            Some(ParserToken::Eq) => {
                if let Some(value) = iter.next() {
                    CommandUnitKind::SetEnvVar(first_arg, value.to_string())
                } else {
                    return Err(ParserError::SetEnvValueExpected);
                }
            }
            Some(ParserToken::String(str)) => {
                let mut v = Parser::collect_args(iter);
                let mut c = vec![str];
                c.append(&mut v);
                CommandUnitKind::External(first_arg, c)
            }
            None => CommandUnitKind::External(first_arg, vec![]),
        })
    }

    fn parse_command(command_tokens: Vec<ParserToken>) -> PResult<CommandUnitKind> {
        let mut iter = command_tokens.into_iter();
        let command = iter.next();
        match command {
            Some(command) => {
                let first_arg = command.to_command()?;
                let kind = match first_arg.as_str() {
                    "echo" => CommandUnitKind::Echo(Parser::collect_args(iter)),
                    "wc" => CommandUnitKind::Wc(Parser::collect_args(iter)),
                    "pwd" => CommandUnitKind::Pwd,
                    "cat" => CommandUnitKind::Cat(Parser::collect_args(iter)),
                    "exit" => CommandUnitKind::Exit,
                    _ => Parser::parse_external(iter, first_arg)?,
                };
                Ok(kind)
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
