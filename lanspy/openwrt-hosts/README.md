# openwrt-hosts

A LAN monitoring web interface for OpenWRT. It gets info about MAC, IPv4 and IPv6 addresses from the local interfacs, DHCP leases file, the ARP/ND caches, and the list of associated wireless clients.

It should work on any system with a web server capable of running shell scripts, provided that the required binaries are available. In particular, the `ip` command from the `ip` OpenWRT package (also found in the `iproute` package on various Linux distros) should be available.

Tested on OpenWRT 10.03 Backfire running on Asus WL500G Premium v1 (2.6 kernel) and v2 (2.4 kernel). Note that listing associated wireless clients is only supported on the 2.4 kernel, as the version of OpenWRT with the 2.6 kernel does not include the `wlc` binary.

openwrt-hosts uses the [`sorttable.js`](http://www.kryogenix.org/code/browser/sorttable/) JavaScript library by Stuart Langridge.

Originally written in 2011, with minor improvements added in the course of the next few years.

## Installation

Copy the contents of this folder to `/www/` on OpenWRT. Then edit the `cgi-bin/hosts.sh` file. The following three lines near the start of the file can be customized:

    KNOWNFILE=/root/known_neighs
    IFACES="br-lan eth0.1"
    WLIFACES="wl0"

The `KNOWNFILE` variable contains the name of the file containing the precollected information about hosts. It is a text file, with each line looking like

    00:MA:ca:dd:re:ss hostname IPv4.add.re.ss IPv6::add:ress IPv6-if

Any missing data is replaced by `*`.

The `IFACES` variable is a space-separated list of all interfaces to which a ping will be sent to the IPv6 multicast address `ff02::1` during the discover phase.

The `WLIFACES` variable is a space-separated list of all wireless interfaces for which the list of associated clients will be fetched.

After that, you can access the web interface via `http://router.ip/cgi-bin/hosts.sh`.
