pub enum LiteralKind {
    SingleQuoted,
    DoubleQuoted,
}
pub enum Token {
    Pipe,
    Ident(String),
    Literal {
        content: String,
        kind: LiteralKind,
        terminated: bool,
    },
    WhiteSpace,
    Uknown,
}
