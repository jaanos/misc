#!/bin/bash

function iptoint {
	A=$(echo $1 | cut -d "." -f 1)
	B=$(echo $1 | cut -d "." -f 2)
	C=$(echo $1 | cut -d "." -f 3)
	D=$(echo $1 | cut -d "." -f 4)
	echo $((16777216*$A + 65536*$B + 256*$C + $D))
}

PARAMS=$@

ROUTE=1
while [ "$(echo "$1" | cut -c 1)" = "-" ]; do
	i=4
	A=$(echo "$1" | cut -c 2)
	B=$(echo "$1" | cut -c 3)
	while [ -n "$A" ]; do
		case "$A" in
			c|w|s)
				if [ -z "$B" ]; then
					shift
				fi;;
			I)
				ROUTE=0;;
			*)
				A=$B
		                B=$(echo "$1" | cut -c $i)
                		i=$(($i+1))
				continue;;
		esac
		break
	done
	shift
done
if [ $ROUTE -eq 1 -a -n "$1" ]; then
	IP=$1
	if [ -z "$(echo $IP | grep -E "^([0-9]+\.){3}[0-9]+$")" ]; then
		IP=$(resolveip -s $IP 2> /dev/null)
	fi
	if [ -n "$IP" ]; then
		IP=$(iptoint $IP)
		i=0
		for x in $(route -n | tail --lines=+3 | tr -s " " | cut -d " " -f 1,3,8); do
			case $i in
				0)
					ROUTE=$(iptoint $x);;
				1)
					MASK=$(iptoint $x)
					if [ $ROUTE -eq $(($IP & $MASK)) ]; then
						i=3
						continue
					fi;;
				3)
					DEV="-I $x"
			esac
			if [ $i -eq 3 ]; then
				break
			fi
			i=$((($i+1)%3))
		done
	fi
fi

/usr/bin/arping $DEV $PARAMS
