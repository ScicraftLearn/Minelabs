#!/bin/bash

echo "stop stop" > restart
sftp -o "StrictHostKeyChecking no" -P 2233 minelabs@minelabs.be<< EOF
cd config
put restart
bye
EOF

mod_file=$(ls output| grep -E "^(minelabs-)([0-9]+)(.)([0-9]+)(.)([0-9]+)(.jar)")
sleep 3
echo "start start" > restart

sftp -o "StrictHostKeyChecking no" -P 2233  minelabs@minelabs.be<< EOF
cd minecraft-data
rm minelabs*
put output/$mod_file
cd ../config
put restart
bye
EOF

sleep 3
sftp -o "StrictHostKeyChecking no" -P 2233  minelabs@minelabs.be<< EOF
cd config
put restart
get restart.log
bye
EOF

echo "from the server logs at Minelabs:"
cat restart.log
echo "Upload complete. "