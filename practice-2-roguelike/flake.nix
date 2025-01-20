{
  description = "Kotlin simple application flake";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/336eda0d07dc5e2be1f923990ad9fdb6bc8e28e3";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs = { self, nixpkgs, flake-utils }:
    flake-utils.lib.eachDefaultSystem ( system:
    let
      pkgs = import nixpkgs { inherit system; };
      core-pkgs = with pkgs; [
          gradle # building
      ];
    in {
      devShells = rec {
        minimal = pkgs.mkShell {
          name = "kotlin";
          buildInputs = core-pkgs;
          shellHook = pkgs.lib.readFile ./scripts/shell_hook.sh;
        };
        default = minimal.overrideAttrs (finalAttrs: previousAttrs: {
          buildInputs = previousAttrs.buildInputs ++ [ pkgs.jetbrains.idea-community ];
        });
      };
    });
}
