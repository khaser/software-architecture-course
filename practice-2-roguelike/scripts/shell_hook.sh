#!/usr/bin/env bash

PROJ_ROOT=$PWD
GLOBAL_ROOT=$PWD/..

cat > $GLOBAL_ROOT/.git/hooks/pre-commit <<HOOK
#!/usr/bin/env bash
######## RENDER-UML HOOK START ########
make -C uml render
git add uml

# check there is no embedded uml
if grep -qi 'startuml' *.md ; then
    echo embedded plantuml found in md files
    exit 1
fi
######## RENDER-UML HOOK END ########
HOOK

gradle addKtlintFormatGitPreCommitHook
# force using gradle from flake instead of ./gradlew
sed -i -e 's/.\/gradlew/gradle/' $GLOBAL_ROOT/.git/hooks/pre-commit

chmod u+x $GLOBAL_ROOT/.git/hooks/pre-commit