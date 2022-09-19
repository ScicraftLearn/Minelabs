#!/bin/bash
mod_file=$(ls | grep -E "^(minelabs-)([0-9]+)(.)([0-9]+)(.)([0-9]+)(.jar)")
source <(grep mod_version gradle.properties)
source <(grep archives_base_name gradle.properties)
source <(grep loader_version gradle.properties)
source <(grep minecraft_version gradle.properties)
echo ${archives_base_name}-${mod_version}.jar "---" $(mod_file)
mkdir output
cd output
cp ../build/libs/${mod_file) .
cd .. \
echo "Moved mod to output"