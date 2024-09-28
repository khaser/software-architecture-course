use lexer::Lexer;
use parser::{PResult, Parser};
use token::Token;

use crate::{cu_kind::CommandUnitKind, env::Env};

use super::*;

#[test]
fn lexer_smoke_test() {
    let lexer = Lexer::new("cat 'example.txt' | wc");
    let tokens = lexer.tokenize();
    assert_eq!(
        vec![
            Token::Ident(String::from("cat")),
            Token::WhiteSpace,
            Token::Literal {
                content: String::from("example.txt"),
                kind: token::LiteralKind::SingleQuoted,
                terminated: true
            },
            Token::WhiteSpace,
            Token::Pipe,
            Token::WhiteSpace,
            Token::Ident(String::from("wc"))
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
            Token::Ident(String::from("echo")),
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
            Token::Ident(String::from("echo")),
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

fn parser_test(tokens: Vec<Token>, expected: PResult<Vec<CommandUnitKind>>) {
    let env = Env::new();
    let parser = Parser::new(&env);
    let commands = parser.parse(tokens);
    assert_eq!(commands, expected);
}

#[test]
fn parser_smoke_test() {
    parser_test(
        vec![
            Token::Ident(String::from("cat")),
            Token::WhiteSpace,
            Token::Literal {
                content: String::from("example.txt"),
                kind: token::LiteralKind::SingleQuoted,
                terminated: true,
            },
            Token::WhiteSpace,
            Token::Pipe,
            Token::WhiteSpace,
            Token::Ident(String::from("wc")),
        ],
        Ok(vec![
            CommandUnitKind::Cat(vec!["example.txt".to_string()]),
            CommandUnitKind::Wc(vec![]),
        ]),
    )
}

#[test]
fn parser_set_env_test() {
    let var = String::from("a");
    let value = String::from("test");
    parser_test(
        vec![
            Token::Ident(var.clone()),
            Token::Eq,
            Token::Literal {
                content: value.clone(),
                kind: token::LiteralKind::SingleQuoted,
                terminated: true,
            },
        ],
        Ok(vec![CommandUnitKind::SetEnvVar(var, value)]),
    )
}

#[test]
fn parser_unterminated_string_test() {
    let content = String::from("some string");
    parser_test(
        vec![
            Token::Ident(String::from("echo")),
            Token::WhiteSpace,
            Token::Literal {
                content: content.clone(),
                kind: token::LiteralKind::DoubleQuoted,
                terminated: false,
            },
        ],
        Err(parser::ParserError::UnterminatedLiteral(content)),
    );
}

#[test]
fn parser_zero_command_test() {
    let content = String::from("some string");
    parser_test(
        vec![
            Token::WhiteSpace,
            Token::Pipe,
            Token::Literal {
                content: content.clone(),
                kind: token::LiteralKind::DoubleQuoted,
                terminated: true,
            },
        ],
        Err(parser::ParserError::ZeroCommandArgs),
    );
}

#[test]
fn parser_external_command_test() {
    parser_test(
        vec![Token::Ident(String::from("ls"))],
        Ok(vec![CommandUnitKind::External("ls".to_string(), vec![])]),
    )
}
