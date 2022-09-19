#!/bin/bash
echo "stop stop" > restart
echo "test1"
sftp "StrictHostKeyChecking no" -P 2233 minelabs@minelabs.be<< EOF
cd config
put restart
get restart.log
bye
EOF
cat restart.log