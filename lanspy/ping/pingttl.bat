@echo off
set p1=%2
set p2=%3
if not defined p1 set p1=1
if not defined p2 set p2=254
for /L %%x in (%p1%,1,%p2%) do (
   @ping.exe -n 1 %1.%%x | find "TTL"
)
set p1=
set p2=