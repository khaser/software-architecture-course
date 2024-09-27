use lexer::Lexer;
use parser::Parser;
use token::Token;

use crate::{command::CommandUnit, cu_kind::CommandUnitKind, env::Env};

use super::*;

#[test]
fn lexer_smoke_test() {
    let lexer = Lexer::new("cat 'example.txt' | wc");
    let tokens = lexer.tokenize();
    assert_eq!(
        vec![
            Token::String(String::from("cat")),
            Token::WhiteSpace,
            Token::Literal {
                content: String::from("example.txt"),
                kind: token::LiteralKind::SingleQuoted,
                terminated: true
            },
            Token::WhiteSpace,
            Token::Pipe,
            Token::WhiteSpace,
            Token::String(String::from("wc"))
        ],
        tokens
    );
}

#[test]
fn lexer_symbols_in_qoutes_test() {
    let lexer = Lexer::new("echo '\" |'");
    let tokens = lexer.tokenize();
    assert_eq!(
        vec![
            Token::String(String::from("echo")),
            Token::WhiteSpace,
            Token::Literal {
                content: String::from("\" |"),
                kind: token::LiteralKind::SingleQuoted,
                terminated: true
            }
        ],
        tokens
    )
}

#[test]
fn lexer_unterminated_string_test() {
    let lexer = Lexer::new("echo \"some string");
    let tokens = lexer.tokenize();
    assert_eq!(
        vec![
            Token::String(String::from("echo")),
            Token::WhiteSpace,
            Token::Literal {
                content: String::from("some string"),
                kind: token::LiteralKind::DoubleQuoted,
                terminated: false
            }
        ],
        tokens
    )
}

fn parser_test(tokens: Vec<Token>, expected: Result<Vec<CommandUnitKind>, String>) {
    let env = Env::new();
    let parser = Parser::new(&env);
    let commands = parser.parse(tokens);
    assert_eq!(commands, expected);
}

#[test]
fn parser_smoke_test() {
    parser_test( vec![
        Token::String(String::from("cat")),
        Token::WhiteSpace,
        Token::Literal {
            content: String::from("example.txt"),
            kind: token::LiteralKind::SingleQuoted,
            terminated: true,
        },
        Token::WhiteSpace,
        Token::Pipe,
        Token::WhiteSpace,
        Token::String(String::from("wc")),
    ], Ok(vec![
        CommandUnitKind::Cat(vec!["example.txt".to_string()]),
        CommandUnitKind::Wc(vec![])
    ]))
}

fn parser_unterminated_string_test() {
    parser_test(vec![
        Token::String(String::from("echo")),
        Token::WhiteSpace,
        Token::Literal {
            content: String::from("some string"),
            kind: token::LiteralKind::DoubleQuoted,
            terminated: false
        }
    ],
    Err("Unterminated literal some string".to_string()));
}