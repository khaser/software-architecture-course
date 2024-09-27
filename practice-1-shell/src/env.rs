use std::collections::HashMap;

pub struct Env {
    vars: HashMap<String, String>,
}

impl Env {
    pub fn new() -> Self {
        Env {
            vars: HashMap::new(),
        }
    }

    pub fn get(var: String) -> String {
        todo!()
    }

    pub fn set(var: String, val: String) {
        todo!()
    }
}
