#!/bin/bash
sudo apt-get install -y ssh
ssh-copy-id -i ${{ secrets.PUBLIC_MINELABS_KEY_ED }} minelabs@minelabs.be
ssh-copy-id -i ${{ secrets.PUBLIC_MINELABS_KEY_RSA }} minelabs@minelabs.be

echo "stop stop" >> restart
sftp -P 2233 -i ${{ secrets.PRIVATE_MINELABS_KEY}} << EOF
cd config
put restart
bye
EOF

mod_file=$(ls | grep -E "^(minelabs-)([0-9]+)(.)([0-9]+)(.)([0-9]+)(.jar)")
sleep 3
echo "start start" >> restart

sftp -P 2233 -i ${{ secrets.PRIVATE_MINELABS_KEY}} << EOF
cd minecraft-data
rm minelabs*
put $mod_file
cd ../config
put restart
bye
EOF

echo "Upload complete. "