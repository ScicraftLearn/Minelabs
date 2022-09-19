#!/bin/bash
sudo apt-get install -y ssh
sudo ssh-copy-id -i ${{ secrets.PUBLIC_MINELABS_KEY_ED }} minelabs@minelabs.be
sudo ssh-copy-id -i ${{ secrets.PUBLIC_MINELABS_KEY_RSA }} minelabs@minelabs.be

sudo echo "stop stop" >> restart
sudo sftp -P 2233 -i ${{ secrets.PRIVATE_MINELABS_KEY}} << EOF
cd config
put restart
bye
EOF

sudo mod_file=$(ls | grep -E "^(minelabs-)([0-9]+)(.)([0-9]+)(.)([0-9]+)(.jar)")
sudo sleep 3
sudo echo "start start" >> restart

sudo sftp -P 2233 -i ${{ secrets.PRIVATE_MINELABS_KEY}} << EOF
cd minecraft-data
rm minelabs*
put $mod_file
cd ../config
put restart
bye
EOF

sudo echo "Upload complete"