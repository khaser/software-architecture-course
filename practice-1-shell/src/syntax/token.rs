enum Token {
    Pipe,
    Ident(String),
    Literal { need_exp: bool },
}
