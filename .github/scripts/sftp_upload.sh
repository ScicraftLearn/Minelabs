#!/bin/bash -e

mkdir mods
cd mods
echo "stop stop" > restart
echo $RANDOM > random_key
sftp -o "StrictHostKeyChecking no" -P 2233 minelabs@minelabs.be<< EOF
cd config
put restart
put random_key
cd ../minecraft-data/mods
get *
bye
EOF
rm restart
rm -f $(ls | grep -E "^(minelabs-)([0-9]+)(.)([0-9]+)(.)([0-9]+)(.jar)")
mv random_key random_key_local
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
get random_key
bye
EOF

echo "from the server logs at Minelabs:"
if ! cat restart.log ; then
  echo "::error::Can't download files from the SSH-server."
  exit -1
fi

if ! test $(( $(cat random_key) == -2 * $(cat random_key_local) )) ; then
  if test $(( $(cat random_key) == $(cat random_key_local) )) ; then
    echo "::error::The restart script is not running on the server."
    exit -1
  fi
  if test $(( $(cat random_key) == -1 * $(cat random_key_local) )) ; then
    echo "::warning::The server stopped, but didn't start again."
    exit -1
  fi
  if test $(( $(cat random_key) == 2 * $(cat random_key_local) )) ; then
    echo "::warning::The server wasn't running, but it started."
  else
    echo "::error::Something went wrong; maybe another upload at the same time?!"
    exit -1
  fi
fi

echo "Upload complete. "