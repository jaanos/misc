#!/bin/sh

openssl rsautl -in key.rsa -inkey private.key -decrypt | openssl aes-256-cbc -d -in passwds.aes -out passwds -pass stdin
MOD=`stat -t passwds`
if [ $? -ne 0 ]; then
	exit
fi
vim passwds
if [ "`stat -t passwds`" != "$MOD" ]; then
	mv passwds.aes passwds.old
	mv key.rsa key.old
	PW=`openssl rand 64 2> /dev/null`
	echo $PW | openssl aes-256-cbc -e -in passwds -out passwds.aes -pass stdin
	echo $PW | openssl rsautl -inkey public.key -certin -encrypt -out key.rsa
fi
rm passwds
