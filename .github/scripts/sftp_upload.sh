#!/bin/bash

mkdir mods
cd mods
echo "stop stop" > restart

sftp -o "StrictHostKeyChecking no" -P 2233 minelabs@minelabs.be<< EOF
cd config
put restart
cd ../minecraft-data/mods
get *
bye
EOF
rm restart
rm $(ls | grep -E "^(minelabs-)([0-9]+)(.)([0-9]+)(.)([0-9]+)(.jar)")
cd ..


mod_file=$(ls output| grep -E "^(minelabs-)([0-9]+)(.)([0-9]+)(.)([0-9]+)(.jar)")
echo $mod_file
. <(grep archives_base_name gradle.properties)
. <(grep loader_version gradle.properties)
. <(grep minecraft_version gradle.properties)
echo "MC_TYPE=FABRIC
MC_VERSION=$minecraft_version
MC_FABRIC_LOADER_VERSION=$loader_version" > minecraft.env
cat minecraft.env
sleep 3
echo "start start" > restart

cd mods
cp "../output/$mod_file" .

sftp -o "StrictHostKeyChecking no" -P 2233  minelabs@minelabs.be<< EOF
cd minecraft-data/mods
rm *
put *
cd ../../config
put ../restart
put ../minecraft.env
bye
EOF

sleep 3
sftp -o "StrictHostKeyChecking no" -P 2233  minelabs@minelabs.be<< EOF
cd config
get restart.log
bye
EOF

echo "from the server logs at Minelabs:"
cat restart.log
echo "Upload complete. "