use lexer::Lexer;
use token::Token;

use super::*;

#[test]
fn lexer_smoke_test() {
    let lexer = Lexer::new("echo 'example.txt' | wc");
    let tokens = lexer.tokenize();
    assert_eq!(
        vec![
            Token::Ident(String::from("echo")),
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
