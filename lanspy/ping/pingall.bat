@echo off
echo . >> ping.txt
echo IP SCAN >> ping.txt
date /t > ping.dat
time /t >> ping.dat
for /L %%x in (1,1,127) do (
   @ping.exe -w 100 -n 1 192.168.1.%%x | find "Reply" >> ping.dat
   if NOT ERRORLEVEL 1 echo 192.168.1.%%x
)
type ping.dat >> ping.txt
arp -a > arp.dat
c:\windows\system32\javaw.exe Ping -c
for /L %%x in (1,128,255) do (
   @ping.exe -w 100 -n 1 192.168.1.%%x | find "Reply" >> ping.dat
   if NOT ERRORLEVEL 1 echo 192.168.1.%%x
)
type ping.dat >> ping.txt
arp -a > arp.dat
javaw Ping -c