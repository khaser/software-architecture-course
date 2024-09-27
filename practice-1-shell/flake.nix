{
  description = "Rust simple application flake";

  inputs = {
    nixpkgs.follows = "khaser/nixpkgs";
    flake-utils.url = "github:numtide/flake-utils";
    khaser.url = "git+ssh://git@slanbox/~git/nixos-config?ref=master";
  };

  outputs = { self, nixpkgs, flake-utils, khaser }:
    flake-utils.lib.eachDefaultSystem ( system:
    let
      pkgs = import nixpkgs { inherit system; };
      rust-configured-vim = (khaser.lib.vim.override {
        extraPlugins = with pkgs.vimPlugins; [
          rust-vim
          tagbar
          syntastic
          LanguageClient-neovim
        ];
        extraRC = ''
          let g:rustfmt_autosave = 1

          let g:LanguageClient_serverCommands = {
           \ 'rust': ['rust-analyzer']
           \ }
          nnoremap <F5> :call LanguageClient_contextMenu()<CR>
          nnoremap <silent> gh :call LanguageClient_textDocument_hover()<CR>
          nnoremap <silent> gd :call LanguageClient_textDocument_definition()<CR>
          nnoremap <silent> gr :call LanguageClient_textDocument_references()<CR>
          nnoremap <silent> gs :call LanguageClient_textDocument_documentSymbol()<CR>
          nnoremap <silent> <F2> :call LanguageClient_textDocument_rename()<CR>
          nnoremap <silent> gf :call LanguageClient_textDocument_formatting()<CR>
        '';
      });
    in {
      devShells = rec {

        minimal = pkgs.mkShell {
          name = "rust";
          buildInputs = with pkgs; [
            rustc
            cargo
            rustfmt
            clippy
            rust-analyzer
          ];
          RUST_SRC_PATH = "${pkgs.rustPlatform.rustLibSrc}";
        };

        khaser = pkgs.mkShell {
          name = "rust-vim";
          buildInputs = minimal.buildInputs ++ [rust-configured-vim];
          RUST_SRC_PATH = "${pkgs.rustPlatform.rustLibSrc}";
        };

        default = minimal;
      };
    });
}

