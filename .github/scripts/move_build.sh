#!/bin/bash
mod_file=$(ls build/libs| grep -E "^(minelabs-)([0-9]+)(.)([0-9]+)(.)([0-9]+)(.jar)")
. <(grep mod_version gradle.properties)
. <(grep archives_base_name gradle.properties)
. <(grep loader_version gradle.properties)
. <(grep minecraft_version gradle.properties)
echo "$archives_base_name-$mod_version.jar --- $mod_file"
mkdir output
cd output
cp "../build/libss/$mod_file" . || (echo "::error::Can't move the mod")
cd ..
echo "Moved mod to output.."