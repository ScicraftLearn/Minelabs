#!/bin/bash
sudo mod_file=$(ls | grep -E "^(minelabs-)([0-9]+)(.)([0-9]+)(.)([0-9]+)(.jar)")
sudo source <(grep mod_version gradle.properties)
sudo source <(grep archives_base_name gradle.properties)
sudo source <(grep loader_version gradle.properties)
sudo source <(grep minecraft_version gradle.properties)
sudo echo ${archives_base_name}-${mod_version}.jar "---" $(mod_file)
sudo mkdir output
sudo cd output
sudo cp ../build/libs/${mod_file) .
sudo cd ..
sudo echo "Moved mod to output."