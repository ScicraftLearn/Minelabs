#!/bin/bash
echo "stop stop" > restart
echo "test1"
echo $PRIVATE_MINELABS_KEY | ssh -i /dev/stdin -o "StrictHostKeyChecking no" -P 2233 minelabs@minelabs.be <<< \
 $"cd config \n put restart \n get restart.log \n bye"
cat restart.log