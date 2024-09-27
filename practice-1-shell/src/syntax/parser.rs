use std::slice::Split;

use crate::{cu_kind::CommandUnitKind, env::Env};

use super::token::{LiteralKind, Token};

pub struct Parser<'a> {
    env: &'a Env,
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

    fn expanse(
        &mut self,
        tokens: Split<'b, Token, impl FnMut(&'_ Token) -> bool>,
    ) -> Result<Vec<Vec<String>>, String> {
        tokens
            .map(|tokens| {
                tokens
                    .iter()
                    .map(|token| match token {
                        Token::String(str) => Ok(str.clone()),
                        t @ Token::Literal {
                            content,
                            kind,
                            terminated,
                        } => {
                            if !terminated {
                                Err("Unterminated literal".to_string())
                            } else {
                                let str = Parser::expanse_string(*kind, content.clone());
                                Ok(str)
                            }
                        }
                        Token::WhiteSpace => todo!(),
                        t @ _ => Err(format!("Unexpected token: {:?}", t)),
                    })
                    .collect()
            })
            .collect()
    }

    pub fn parse(&mut self, tokens: Vec<Token>) -> Result<Vec<CommandUnitKind>, String> {
        let splitted = self.parse_commands(&tokens);
        let expansioned = self.expanse(splitted)?;
        expansioned
            .into_iter()
            .map(|command_tokens| {
                let mut iter = command_tokens.into_iter();
                let command = iter.next();
                match command {
                    Some(command) => {
                        let args = iter.collect();
                        Ok(match command.as_str() {
                            "echo" => CommandUnitKind::Echo(args),
                            "wc" => CommandUnitKind::Wc(args),
                            "pwd" => CommandUnitKind::Pwd(args),
                            "cat" => CommandUnitKind::Cat(args),
                            "exit" => CommandUnitKind::Exit,
                            _ => CommandUnitKind::External(command, args),
                        })
                    }
                    None => return Err("No commands".to_string()),
                }
            })
            .collect()
    }
}
