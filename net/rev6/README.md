# rev6

A short `awk` script for reversing IPv6 addresses into the form used by DNS PTR records..

Written in July 2011.

## Usage

`echo 2001:db8:1:ffff::1 | awk -f rev6.awk --source '{print rev6($1)}'`
