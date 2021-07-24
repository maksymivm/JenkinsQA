#!/bin/bash
# wait-for-jenkins.sh

set -e

seconds=220
while ! curl -sSL "http://localhost:8080/" 2>&1 \
             | grep "<title>Sign in [Jenkins]</title>" >/dev/null; do
    echo 'Waiting for the Jenkins'
    sleep 10
    seconds=$(($seconds - 10))

    if [ $seconds -le 0 ]; then
        echo "Time out"
        exit 1
    fi
done

echo "Jenkins is up"