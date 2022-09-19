#!/bin/bash
echo "stop stop" > restart
rm -f id_rsa id_ed id_rsa.pub id_ed.pub
echo "test1"
echo $PRIVATE_MINELABS_KEY | ssh -i /dev/stdin -o "StrictHostKeyChecking no" -P 2233 minelabs@minelabs.be<< EOF
cd config
put restart
get restart.log
bye
EOF
cat restart.log