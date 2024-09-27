use crate::{cu_kind::CommandUnitKind, env::Env};

use super::token::Token;

pub struct Parser<'a> {
    env: &'a Env,
}

impl<'a> Parser<'a> {
    pub fn new(env: &'a Env) -> Self {
        Parser { env: env }
    }

    fn parse_commands(&'a mut self, tokens: &'a Vec<Token>) -> Vec<&[Token]> {
        tokens
            .split(|token| if let Token::Pipe = token { true } else { false })
            .collect()
    }

    fn expanse(&'a mut self, tokens: Vec<&'a [Token]>) -> Vec<&[Token]> {
        tokens
    }

    pub fn parse(&'a mut self, tokens: Vec<Token>) -> Vec<CommandUnitKind> {
        unimplemented!("TODO")
    }
}
