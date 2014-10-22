#!/bin/sh

echo -e "Content-Type: text/html; charset=utf-8\r\n\r"

KNOWNFILE=/root/known_neighs
IFACES="br-lan eth0.1"
WLIFACES="wl0"

if [ "$QUERY_STRING" = "discover" ]; then
	echo "Please wait...<pre>"
	ifconfig | grep Bcast | sed -r "s/.*Bcast:([^ ]*).*/\1/" | xargs -n 1 ping -c 1 -W 1
	echo $IFACES | xargs -n 1 ping6 -c 2 -W 1 ff02::1 -I
	sleep 3
	echo "</pre><meta http-equiv='refresh' content='0;hosts.sh'>"
	exit
elif [ "`echo "$QUERY_STRING" | cut -c 1-6`" == "update" ]; then
	DATA="`echo "$QUERY_STRING" | sed -r "s/^update&mac=([0-9A-F:]+)&name=([^&/' ]+)&ipv4=([0-9.*]+)&ipv6=([0-9a-f:*]+)&iipv6=([a-z0-9.*-]+)$/\1 \2 \3 \4 \5/"`"
	if [ "$DATA" != "$QUERY_STRING" ]; then
		MAC=`echo "$DATA" | cut -d " " -f 1`
		if grep -E "^$MAC" $KNOWNFILE > /dev/null; then
			sed -i -r -e "s/^$MAC.*/$DATA/" $KNOWNFILE
		else
			echo "$DATA" >> $KNOWNFILE
		fi
	fi
	echo "<meta http-equiv='refresh' content='0;hosts.sh'>"
	exit
elif [ "`echo "$QUERY_STRING" | cut -c 1-5`" == "ping6" ]; then
	IFIP="`echo "$QUERY_STRING" | sed -r "s/^ping6&ip=([0-9a-f.:]+)&if=([a-z0-9.-]+)$/\2 \1/"`"
	if [ "$IFIP" != "$QUERY_STRING" ]; then
		echo "<pre>$ ping6 -c 2 -I $IFIP"
		ping6 -c 2 -I $IFIP
		echo "</pre>"
	fi
elif [ "`echo "$QUERY_STRING" | cut -c 1-4`" == "ping" ]; then
	IP="`echo "$QUERY_STRING" | sed -r "s/^ping&ip=([0-9.]+)$/\1/"`"
	if [ "$IP" != "$QUERY_STRING" ]; then
		echo "<pre>$ ping -c 2 $IP"
		ping -c 2 $IP
		echo "</pre>"
	fi
fi

HOSTNAME="`cat /etc/config/system | grep hostname | cut -d "'" -f 4`"
echo "<html><head><title>Hosts - $HOSTNAME</title>"
cat << EOF
<script src="/sorttable.js"></script>
<style>
body {
	font-family: Verdana, Arial, sans-serif;
}
td {
	padding-left: 5px;
}
.row1 {
	background: #f5f5f5;
}
.grey {
	color: #7F7F7F;
}
.black {
	color: #000000;
}
</style>
EOF
echo "</head>"

(
	(
		cat /tmp/dhcp.leases | awk '{ print toupper($2) " " $3 " *  " $4 " " $1 }'
		ip neigh | grep lladdr | awk '{ print toupper($5) " " $1 " " $3 " * *" }'
		ifconfig | grep Ethernet | cut -d " " -f 1 | xargs -n 1 ifconfig | grep net | awk -v mainhost="$HOSTNAME" '
		BEGIN {
			mainmac = "*";
		}
		{
			if ($1 == "inet") {
				split($2, addr, ":");
				print mac " " addr[2] " " iface " " host " *";
			} else if ($1 == "inet6") {
				split($3, addr, "/");
				print mac " " addr[1] " " iface " " host " *";
			} else {
				host = "*";
				iface = $1;
				mac = $5;
				if (mainmac == "*") {
					mainmac = mac;
				} else if (mainmac == mac) {
					host = mainhost;
				}
			}
		}'
		for w in $WLIFACES; do
			for x in `wlc ifname $w assoclist`; do
				if [ "$x" != "$w" ]; then
					echo $x | awk -v w=$w '{ print toupper($1) " * * * * " w }'
				fi
			done
		done
	) | sort
	echo "*"
) | awk -v date="`date`" -v now=`date +%s` -v knownfile=$KNOWNFILE '
BEGIN {
	mac = "*";
	name = "*";
	cl = 0;
	while ((getline < knownfile) > 0) {
		cache[$1,"name"] = $2;
		cache[$1,"ipv4"] = $3;
		cache[$1,"ipv6"] = $4;
		cache[$1,"iipv6"] = $5;
		cache[$1,"valid"] = 1;
	}
	print "<body><table width=\"100%\"><tr><td><h2>Hosts</h2></td><td align=\"right\">" date "</td></tr></table><table width=\"100%\" class=\"sortable\">"
	print "<thead><tr><th align=\"left\" valign=\"top\"><b>Hostname</b></th>"
	print "<th align=\"left\" valign=\"top\" class=\"sorttable_nosort\"><b>IPv4</b></th><th align=\"right\" valign=\"top\"><b>if</b></th>"
	print "<th align=\"left\" valign=\"top\" class=\"sorttable_nosort\"><b>IPv6</b></th><th align=\"right\" valign=\"top\"><b>if</b></th>"
	print "<th align=\"right\" valign=\"top\"><b>Leasetime</b></th><th align=\"right\" valign=\"top\"><b>wl</b></th><th align=\"right\" valign=\"top\"><b>MAC</b></th><th class=\"sorttable_nosort\">&nbsp;</th></tr></thead>";
	print "<tbody>";
}
{
	if ($1 != mac) {
		if (mac != "*") {
			cl = 1 - cl;
			
			print "<tr class=\"row" cl "\">"
			if (name != "*" || cache[mac,"name"] == "*" || cache[mac,"name"] == "") {
				if (name == cache[mac,"name"])
					st = "";
				else
					st = " <a href=\"hosts.sh?update&mac=" mac "&name=" name "&ipv4=" cache[mac,"ipv4"] "&ipv6=" cache[mac,"ipv6"] "&iipv6=" cache[mac,"iipv6"] "\" class=\"black\">*</a>";
				print "<td valign=\"top\">" name st "</td>"
				lname = name;
			} else {
				print "<td valign=\"top\" class=\"grey\">" cache[mac,"name"] "</td>"
				lname = cache[mac,"name"];
			}
			if (lname == "")
				lname = "*";
			print "<td valign=\"top\">";
			if (dhcpip != "*" && !(dhcpip in ips))
				ipv4["DHCP",0] = dhcpip;
			i = 0;
			j = 0;
			for (i in ipv4) {
				split(i, j, SUBSEP);
				if (ipv4[i] == cache[mac,"ipv4"]) {
					st = "";
					j = 1;
				} else
					st = " <a href=\"hosts.sh?update&mac=" mac "&name=" cache[mac,"name"] "&ipv4=" ipv4[i] "&ipv6=" cache[mac,"ipv6"] "&iipv6=" cache[mac,"iipv6"] "\" class=\"black\">*</a>";
				print "<a href=\"hosts.sh?ping&ip=" ipv4[i] "\" class=\"black\">" ipv4[i] "</a>" st "<br />";
				lipv4 = ipv4[i];
			}
			if (j == 0 && cache[mac,"ipv4"] != "*" && cache[mac,"ipv4"] != "") {
				print "<span class=\"grey\"> <a href=\"hosts.sh?ping&ip=" cache[mac,"ipv4"] "\" class=\"grey\">" cache[mac,"ipv4"] "</a></span>";
				lipv4 = cache[mac,"ipv4"];
			}
			if (lipv4 == "")
				lipv4 = "*";
			print "</td><td align=\"right\" valign=\"top\">";
			for (i in ipv4) {
				split(i, j, SUBSEP);
				print j[1] "<br />";
			}
			print "</td><td valign=\"top\">"
			lipv6 = "*";
			iipv6 = "*";
			j = 0;
			for (i in ipv6) {
				split(i, j, SUBSEP);
				if (ipv6[i] == cache[mac,"ipv6"] && j[1] == cache[mac,"iipv6"]) {
					st = "";
					j = 1;
				} else if (substr(ipv6[i], 1, 6) != "fe80::")
					st = " <a href=\"hosts.sh?update&mac=" mac "&name=" cache[mac,"name"] "&ipv4=" cache[mac,"ipv4"] "&ipv6=" ipv6[i] "&iipv6=" j[1] "\" class=\"black\">*</a>";
				else
					st = "";
				print "<a href=\"hosts.sh?ping6&ip=" ipv6[i] "&if=" j[1] "\" class=\"black\">" ipv6[i] "</a>" st "<br />";
				if (substr(ipv6[i], 1, 6) != "fe80::") {
					lipv6 = ipv6[i];
					iipv6 = j[1];
				}
			}
			if (j == 0 && cache[mac,"ipv6"] != "*" && cache[mac,"ipv6"] != "") {
				print "<a href=\"hosts.sh?ping6&ip=" cache[mac,"ipv6"] "&if=" cache[mac,"iipv6"] "\" class=\"grey\">" cache[mac,"ipv6"] "</a><br />";
				lipv6 = cache[mac,"ipv6"];	
				iipv6 = cache[mac,"iipv6"];	
			}
			print "</td><td align=\"right\" valign=\"top\">"
			for (i in ipv6) {
				split(i, j, SUBSEP);
				print j[1] "<br />";
			}
			if (j == 0 && cache[mac,"ipv6"] != "*" && cache[mac,"ipv6"] != "") {
				print "<span class=\"grey\">" cache[mac,"iipv6"] "</span>";
			}
			if (lease != "*" && lease != 0) {
				s = lease - now;
				m = int(s / 60);
				s = s % 60;
				h = int(m / 60);
				m = m % 60;
				d = int(h / 24);
				h = h % 24;
				ltime = "";
				if (d != 0)
					ltime = ltime " " d " d";
				if (h != 0)
					ltime = ltime " " h " h";
				if (m != 0)
					ltime = ltime " " m " min";
				if (s != 0)
					ltime = ltime " " s " s";
			} else
				ltime = "";
			print "</td><td align=\"right\" valign=\"top\" sorttable_customkey=\"" (lease-now) "\">" ltime "</td><td align=\"right\" valign=\"top\">" wl "</td><td align=\"right\" valign=\"top\">" mac "</td>"
			print "<td align=\"right\" valign=\"top\"><a href=\"hosts.sh?update&mac=" mac "&name=" lname "&ipv4=" lipv4 "&ipv6=" lipv6 "&iipv6=" iipv6 "\" class=\"black\">"
			if (mac SUBSEP "valid" in cache)
				print "*";
			else
				print "+";
			print "</a></td></tr>"
			print "";
		}
		
		mac = $1;
		wl = "";
		name = "*"
		dhcpip = "*"
		lease = "*"
		lname = "*"
		lipv4 = "*"
		lipv6 = "*"
		iipv6 = "*"
		for (i in ips)
			delete ips[i];
		for (i in ipv4)
			delete ipv4[i];
		for (i in ipv6)
			delete ipv6[i];
		for (i in ifs)
			delete ifs[i];	
	}
	if ($4 != "*")
		name = $4;
	if ($5 != "*") {
		dhcpip = $2;
		lease = $5
	}
	if ($3 != "*") {
		ifs[$3] = ifs[$3] + 1
		if ($2 ~ /:/)
			ipv6[$3,ifs[$3]] = $2;
		else {
			ipv4[$3,ifs[$3]] = $2;
			ips[$2] = $2;
		}
		
	}
	if ($6 != "") {
		wl = $6;
	}
}
END {
	print "</tbody></table></body>"
}
'
echo "<a href='hosts.sh?discover'>Discover hosts</a><p><pre>" 
logread | grep NAK
echo "</pre></p></html>"
