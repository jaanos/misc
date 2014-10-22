# msnhijack.py

A simple MSN switchboard server.

If one can send a properly spoofed packet to a Live Messenger user, the client can be forced to connect to an arbitrary switchboard server, which is responsible for forwarding messages between users in a conversation. By directing a user to our switchboard server, one may impersonate another user, or simply send unsolicited messages.

Spoofing the appropriate packet is the hard part (not included here). It has once been done as a prank on a LAN (the MSN notification server session had to be intercepted to properly craft the spoofed packet), and then this server was used for the conversation. The contents of the spoofed packet are in the file `msn.rng`. Since Live Messenger has been discontinued, this will likely remain the only time such an attack has been performed.

Written in February 2011.
