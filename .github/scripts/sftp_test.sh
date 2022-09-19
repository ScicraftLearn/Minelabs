#!/bin/bash
echo "stop stop" > restart
rm -f id_rsa id_ed id_rsa.pub id_ed.pub
echo $PRIVATE_MINELABS_KEY > id_rsa_win
tr -d '\015' <id_rsa_win >id_rsa
echo $PRIVATE_MINELABS_KEY "test"
cat id_rsa
cat id_rsa_win
echo "test"
chmod 600 id_rsa
#!echo $PRIVATE_MINELABS_KEY_PUB > "id_rsa.pub"
sftp -o "StrictHostKeyChecking no" -P 2233 -i ./id_rsa minelabs@minelabs.be<< EOF
cd config
put restart
get restart.log
bye
EOF

cat restart.log