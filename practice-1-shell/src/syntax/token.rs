#[derive(Clone, Copy, PartialEq, Debug)]
pub enum LiteralKind {
    SingleQuoted,
    DoubleQuoted,
}

#[derive(PartialEq, Debug)]
pub enum Token {
    Pipe,
    Eq,
    Ident(String),
    Literal {
        content: String,
        kind: LiteralKind,
        terminated: bool,
    },
    WhiteSpace,
    Unknown,
}
