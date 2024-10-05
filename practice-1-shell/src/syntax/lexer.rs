use std::str::Chars;

use crate::env::Env;

use super::token::{LiteralKind, Token};

type Pos = usize;

const EOF_CHAR: char = '\0';
const DOLLAR_TOKEN: char = '$';
pub const EQ_TOKEN: &str = "=";

pub struct Lexer<'a> {
    input: &'a str,
    len_remaining: usize,
    chars: Chars<'a>,
}

impl<'a> Lexer<'a> {
    pub fn new(input: &'a str) -> Self {
        Lexer {
            input,
            len_remaining: input.len(),
            chars: input.chars(),
        }
    }

    pub fn tokenize(self) -> Vec<Token> {
        let mut lex = self;
        let iter = std::iter::from_fn(move || lex.get_next_token());
        iter.collect()
    }

    pub fn expanse(content: String, env: &Env) -> String {
        let mut new_content: String = String::new();
        let mut iter = content.chars();
        while let Some(c) = iter.next() {
            if c == DOLLAR_TOKEN {
                let mut var_name: String = String::from("");
                while let Some(nc) = iter.clone().next() {
                    if Lexer::is_correct_ident(nc) {
                        var_name.push(nc);
                        iter.next();
                    } else {
                        break;
                    }
                }
                new_content.push_str(env.get(var_name.as_str()).unwrap_or(&"".to_string()));
            } else {
                new_content.push(c);
            }
        }

        new_content
    }
}

impl Lexer<'_> {
    fn first(&self) -> char {
        self.chars.clone().next().unwrap_or(EOF_CHAR)
    }

    fn quoted_string(&mut self, lit_kind: LiteralKind) -> Token {
        let quote_symbol = match lit_kind {
            LiteralKind::DoubleQuoted => '\"',
            LiteralKind::SingleQuoted => '\'',
        };
        let pos = self.pos_within_token();
        while let Some(c) = self.advance() {
            match c {
                s if s == quote_symbol => {
                    return Token::Literal {
                        content: self.substr_from_to(pos, self.pos_within_token() - 1),
                        kind: lit_kind,
                        terminated: true,
                    };
                }
                '\\' if self.first() == '\\' || self.first() == quote_symbol => {
                    self.advance();
                }
                _ => (),
            }
        }
        return Token::Literal {
            content: self.substr_from(pos),
            kind: lit_kind,
            terminated: false,
        };
    }

    #[inline]
    fn substr_from_to(&self, from: usize, to: usize) -> String {
        self.input[from..to].to_string()
    }

    #[inline]
    fn substr_from(&self, from: usize) -> String {
        self.substr_from_to(from, self.pos_within_token())
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

    fn is_correct_ident(c: char) -> bool {
        match c {
            'a'..='z' | 'A'..='Z' | '_' | '0'..='9' | '/' | '-' | '.' => true,
            _ => false,
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

    fn pos_within_token(&self) -> Pos {
        self.len_remaining - self.chars.as_str().len()
    }

    fn get_next_token(&mut self) -> Option<Token> {
        let c = match self.advance() {
            Some(c) => c,
            None => return None,
        };
        let token = match c {
            '\'' => self.quoted_string(LiteralKind::SingleQuoted),
            '\"' => self.quoted_string(LiteralKind::DoubleQuoted),
            '=' => Token::Eq,
            '|' => Token::Pipe,
            c if Self::is_whitespace(c) => {
                self.eat_while(Self::is_whitespace);
                Token::WhiteSpace
            }
            c if Self::is_correct_ident(c) => {
                let pos = self.pos_within_token() - 1;
                self.eat_while(Self::is_correct_ident);
                Token::Ident(self.substr_from(pos))
            }
            _ => Token::Unknown,
        };
        Some(token)
    }

    fn advance(&mut self) -> Option<char> {
        self.chars.next()
    }
}
