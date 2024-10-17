use lexer::Lexer;
use parser::{PResult, Parser};
use token::Token;

use crate::{cu_kind::Command, env::Env};

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

fn parser_test(tokens: Vec<Token>, expected: PResult<Vec<Command>>) {
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
            Command::Cat(vec!["example.txt".to_string()]),
            Command::Wc(vec![]),
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
        Ok(vec![Command::SetEnvVar(var, value)]),
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
    let ls = String::from("ls");
    parser_test(
        vec![Token::Ident(ls.clone())],
        Ok(vec![Command::External(ls, vec![])]),
    )
}

#[test]
fn parser_no_var_expanse_test() {
    let content = String::from("some $string");
    parser_test(
        vec![
            Token::Ident(String::from("echo")),
            Token::WhiteSpace,
            Token::Literal {
                content: content.clone(),
                kind: token::LiteralKind::DoubleQuoted,
                terminated: true,
            },
        ],
        Ok(vec![Command::Echo(vec!["some ".to_string()])]),
    );
}

#[test]
fn parser_var_expanse() {
    let var = String::from("a");
    let value = String::from("test");
    let mut env = Env::new();
    env.insert(var, value.clone());
    let parser = Parser::new(&env);
    let command = parser.parse(vec![
        Token::Ident(String::from("echo")),
        Token::WhiteSpace,
        Token::Literal {
            content: "$a".to_string(),
            kind: token::LiteralKind::DoubleQuoted,
            terminated: true,
        },
    ]);
    assert_eq!(command, Ok(vec![Command::Echo(vec![value])]));
}

#[test]
fn parser_no_expanse_in_single() {
    let var = String::from("a");
    let value = String::from("test");
    let mut env = Env::new();
    env.insert(var, value.clone());
    let parser = Parser::new(&env);
    let command = parser.parse(vec![
        Token::Ident(String::from("echo")),
        Token::WhiteSpace,
        Token::Literal {
            content: "$a".to_string(),
            kind: token::LiteralKind::SingleQuoted,
            terminated: true,
        },
    ]);
    assert_eq!(command, Ok(vec![Command::Echo(vec!["$a".to_string()])]));
}

#[test]
fn parser_ident_expanse() {
    let var = String::from("a");
    let value = String::from("test");
    let mut env = Env::new();
    env.insert(var, value.clone());
    let parser = Parser::new(&env);
    let command = parser.parse(vec![
        Token::Ident(String::from("echo")),
        Token::WhiteSpace,
        Token::Ident("$a".to_string()),
    ]);
    assert_eq!(command, Ok(vec![Command::Echo(vec![value])]));
}

#[test]
fn parser_multiple_expanse() {
    let var_a = String::from("a");
    let var_b = String::from("b");
    let value_a = String::from("val_a");
    let value_b = String::from("val_b");
    let mut env = Env::new();
    env.insert(var_a, value_a.clone());
    env.insert(var_b, value_b.clone());
    let parser = Parser::new(&env);
    let command = parser.parse(vec![
        Token::Ident(String::from("echo")),
        Token::WhiteSpace,
        Token::Literal {
            content: "$a$b $a$a $b".to_string(),
            kind: token::LiteralKind::DoubleQuoted,
            terminated: true,
        },
    ]);
    assert_eq!(
        command,
        Ok(vec![Command::Echo(vec![
            "val_aval_b val_aval_a val_b".to_string()
        ])])
    );
}
