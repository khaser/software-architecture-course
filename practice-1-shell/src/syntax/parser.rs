use std::{fmt, slice::Split, vec::IntoIter};

use crate::{cu_kind::Command, env::Env};

use super::{
    lexer::{Lexer, EQ_TOKEN},
    token::{LiteralKind, Token},
};

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

impl<'a, 'b> Parser<'a> {
    pub fn new(env: &'a Env) -> Self {
        Parser { env }
    }

    fn parse_commands(
        &mut self,
        tokens: &'b [Token],
    ) -> Split<'b, Token, impl FnMut(&'_ Token) -> bool> {
        tokens.split(|token| matches!(token, Token::Pipe))
    }

    fn expanse_string(&self, kind: LiteralKind, content: String) -> String {
        match kind {
            super::token::LiteralKind::SingleQuoted => content,
            super::token::LiteralKind::DoubleQuoted => {
                Lexer::expanse(content, self.env)
            }
        }
    }

    fn token_to_string(&self, token: &Token) -> PResult<String> {
        match token {
            Token::Ident(str) => Ok(self.expanse_string(LiteralKind::DoubleQuoted, str.clone())),
            Token::Literal {
                content,
                kind,
                terminated,
            } => {
                if !terminated {
                    Err(ParserError::UnterminatedLiteral(content.clone()))
                } else {
                    let str = self.expanse_string(*kind, content.clone());
                    Ok(str)
                }
            }
            Token::Pipe | Token::WhiteSpace => panic!("There cannot be such token"),
            Token::Unknown => Err(ParserError::UnexpectedToken(format!(
                "{:?}",
                Token::Unknown
            ))),
            Token::Eq => Ok(EQ_TOKEN.to_string()),
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
                    match self.token_to_string(token) {
                        Ok(token) => command_tokens.push(token),
                        Err(err) => return Err(err),
                    }
                }
                Ok(command_tokens)
            })
            .collect()
    }

    fn collect_args(iter: IntoIter<String>) -> Vec<String> {
        iter.collect()
    }

    fn parse_set_env(var_name: String, mut iter: IntoIter<String>) -> PResult<Command> {
        if let Some(value) = iter.next() {
            Ok(Command::SetEnvVar(var_name, value.to_string()))
        } else {
            Err(ParserError::SetEnvValueExpected)
        }
    }

    fn parse_command(command_tokens: Vec<String>) -> PResult<Command> {
        let mut iter = command_tokens.into_iter();
        let command = iter.next();
        let mut iter_eq = iter.clone();
        let arg = iter_eq.next();
        if let Some(arg) = arg {
            if arg == EQ_TOKEN {
                return Parser::parse_set_env(command.unwrap(), iter_eq);
            }
        }
        if let Some(command) = command {
            let args = Parser::collect_args(iter);
            Ok(match command.as_str() {
                "echo" => Command::Echo(args),
                "wc" => Command::Wc(args),
                "cat" => Command::Cat(args),
                "pwd" => Command::Pwd,
                "exit" => Command::Exit,
                _ => Command::External(command, args),
            })
        } else {
            Err(ParserError::ZeroCommandArgs)
        }
    }

    pub fn parse(self, tokens: Vec<Token>) -> PResult<Vec<Command>> {
        let mut parser = self;
        let splitted = parser.parse_commands(&tokens);
        let expansioned = parser.expanse(splitted)?;
        expansioned.into_iter().map(Parser::parse_command).collect()
    }
}
