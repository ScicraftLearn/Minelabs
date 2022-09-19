#!/bin/bash
echo "stop stop" > restart
rm -f id_rsa id_ed id_rsa.pub id_ed.pub

export SSH_AUTH_SOCK=~/.ssh/ssh-agent.sock
ssh-add -l 2>/dev/null >/dev/null
[ $? -ge 2 ] && ssh-agent -a "$SSH_AUTH_SOCK" >/dev/null
ssh-add - <<< "${$PRIVATE_MINELABS_KEY}"
sftp -o "StrictHostKeyChecking no" -P 2233 minelabs@minelabs.be<< EOF
cd config
put restart
get restart.log
bye
EOF
cat restart.log