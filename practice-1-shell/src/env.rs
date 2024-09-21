use std::collections::HashMap;

pub struct Env {
    vars: HashMap<String, String>,
}

impl Env {
    pub fn get(var: String) -> String {
        todo!()
    }

    pub fn set(var: String, val: String) {
        todo!()
    }

    pub fn new(&self) -> Self {
        Env {
            vars: HashMap::new(),
        }
    }
}
