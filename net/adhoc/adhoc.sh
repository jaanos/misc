#!/bin/ash

ifconfig wlan0 down
iwconfig wlan0 mode ad-hoc
iwconfig wlan0 channel 3
iwconfig wlan0 enc off
iwconfig wlan0 essid internetz
ifconfig wlan0 192.168.1.1 netmask 255.255.255.0
echo 1 > /proc/sys/net/ipv4/ip_forward
iwconfig wlan0 essid internetz
ifconfig wlan0 up
