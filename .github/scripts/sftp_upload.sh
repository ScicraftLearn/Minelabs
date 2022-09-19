#!/bin/bash
sudo apt-get install -y ssh
ssh-copy-id -i $PUBLIC_MINELABS_KEY_ED minelabs@minelabs.be
ssh-copy-id -i $PUBLIC_MINELABS_KEY_RSA minelabs@minelabs.be

echo "stop stop" >> restart
sftp -P 2233 -i $PRIVATE_MINELABS_KEY << EOF
cd config
put restart
bye
EOF

mod_file=$(ls output| grep -E "^(minelabs-)([0-9]+)(.)([0-9]+)(.)([0-9]+)(.jar)")
sleep 3
echo "start start" >> restart

sftp -P 2233 -i $PRIVATE_MINELABS_KEY << EOF
cd minecraft-data
rm minelabs*
put output/$mod_file
cd ../config
put restart
bye
EOF

echo "Upload complete. "