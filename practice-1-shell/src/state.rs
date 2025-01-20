pub struct AppState {
    pub pwd: String,
}

impl AppState {
    pub fn new(pwd: String) -> Self {
        Self { pwd }
    }
}
