#!/bin/bash
sudo apt-get install -y ssh
echo $PUBLIC_MINELABS_KEY_ED >> id_ed.pub
echo $PUBLIC_MINELABS_KEY_RSA >> id_rsa.pub

#! ssh-copy-id -i -f id_ed.pub minelabs@minelabs.be
#! ssh-copy-id -i -f id_rsa.pub minelabs@minelabs.be

echo "stop stop" >> restart

echo $PRIVATE_MINELABS_KEY >> id_rsa
echo $PRIVATE_MINELABS_KEY_PUB >> id_rsa.pub
sftp -o "StrictHostKeyChecking no" -P 2233 -i id_rsa minelabs@minelabs.be<< EOF
cd config
put restart
bye
EOF

mod_file=$(ls output| grep -E "^(minelabs-)([0-9]+)(.)([0-9]+)(.)([0-9]+)(.jar)")
sleep 3
echo "start start" >> restart

sftp -o "StrictHostKeyChecking no" -P 2233 -i id_rsa  minelabs@minelabs.be<< EOF
cd minecraft-data
rm minelabs*
put output/$mod_file
cd ../config
put restart
bye
EOF

echo "Upload complete. "