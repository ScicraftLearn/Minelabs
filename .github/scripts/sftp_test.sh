#!/bin/bash
echo "stop stop" > restart
rm -f id_rsa id_ed id_rsa.pub id_ed.pub
echo $PRIVATE_MINELABS_KEY > id_rsa
cat id_rsa
chmod 600 id_rsa
sftp -o "StrictHostKeyChecking no" -P 2233 -i ./id_rsa minelabs@minelabs.be<< EOF
cd config
put restart
get restart.log
bye
EOF

cat restart.log