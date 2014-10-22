#!/bin/sh

PW=`openssl rsautl -in key.rsa -inkey private.key -decrypt`
if [ $? -ne 0 ]; then
	exit
fi
if [ -z "$1" ]; then
	echo "$PW" | openssl aes-256-cbc -d -in passwds.aes -pass stdin | more
else
	echo "$PW" | openssl aes-256-cbc -d -in passwds.aes -pass stdin | grep "$*"
fi
echo -ne "==========================\nPress Enter to continue..."
read X
clear
