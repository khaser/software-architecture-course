use std::str::Chars;

use super::token::{LiteralKind, Token};

type Pos = usize;

const EOF_CHAR: char = '\0';

pub struct Lexer<'a> {
    input: &'a str,
    chars: Chars<'a>,
}

impl<'a> Lexer<'a> {
    pub fn new(input: &'a str) -> Self {
        Lexer {
            input,
            chars: input.chars(),
        }
    }

    pub fn tokenize(&self) -> Vec<Token> {}
}

impl Lexer<'_> {

    fn first(&self) -> char {
        self.chars.clone().next().unwrap_or(EOF_CHAR)
    }

    fn quoted_string(&mut self, lit_kind: LiteralKind) -> Token {
        let mut content = String::new();
        let quote_symbol = match lit_kind {
            LiteralKind::DoubleQuoted => '\"',
            LiteralKind::SingleQuoted => '\''
        };
        while let Some(c) = self.advance() {
            match c {
                s if s == quote_symbol => {
                    return Token::Literal { content, kind: lit_kind, terminated: true };
                }
                '\\' if self.first() == '\\' || self.first() == quote_symbol => {
                    content.push(self.advance().unwrap());
                }
                _ => ()
            }
        }
        return Token::Literal { content, kind: lit_kind, terminated: false };
    }

    fn identifier(&mut self) -> Token {
        todo!()
        // self.eat_while(Self::is_id_continue)
    }

    fn is_whitespace(c: char) -> bool {
        matches!(
            c,
            '\u{0009}'   // \t
            | '\u{000A}' // \n
            | '\u{000D}' // \r
            | '\u{0020}' // space
        )
    }

    fn is_id_start(c: char) -> bool {
        match c {
            'a'..'z' | 'A'..'Z' | '_' => true,
            _ => false
        }
    }

    fn is_id_continue(c: char) -> bool {
        match c {
            'a'..'z' | 'A'..'Z' | '_' | '0'..'9' => true,
            _ => false
        }
    }

    fn is_eof(&self) -> bool {
        self.chars.as_str().is_empty()
    }

    fn eat_while(&mut self, mut predicate: impl FnMut(char) -> bool) {
        while predicate(self.first()) && !self.is_eof() {
            self.advance();
        }
    }


    fn get_next_token(&mut self) -> Option<Token> {
        let c = match self.advance() {
            Some(c) => c,
            None => return None
        };
        let token = match c {
            '\'' => {
                self.quoted_string(LiteralKind::SingleQuoted)
            }
            '\"' => {
                self.quoted_string(LiteralKind::DoubleQuoted)
            }
            '|' => {
                Token::Pipe
            }
            c if Self::is_whitespace(c) => {
                self.eat_while(Self::is_whitespace);
                Token::WhiteSpace
            }
            c if Self::is_id_start(c) => {
                self.identifier()
            }
        }
    }

    fn advance(&mut self) -> Option<char> {
        self.chars.next()
    }
}
