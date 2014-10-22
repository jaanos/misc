import socket

addr = '192.168.1.218'
email = 'satan@evil.com'
nick = 'Satan'
iro = "IRO 1 1 1 %s %s\r\n" % (email, nick)
ans = "ANS 1 OK\r\n"
buffer = ""

def recv(c, buffer):
    ls = buffer.split('\r\n')
    while len(ls[0]) == len(buffer):
        buffer += c.recv(100)
        ls = buffer.split('\r\n')
    comm = ls[0].split()
    buffer = buffer[len(ls[0])+2:]
    if len(comm) > 1 and comm[0] == "CAL":
        c.send("217 %s\r\n" % comm[1])
    if len(comm) > 3 and comm[0] == "MSG":
        if comm[2] == "A":
            c.send("ACK %s\r\n" % comm[1])
        l = int(comm[3])
        if l > len(buffer):
            buffer += c.recv(l - len(buffer))
        print buffer[:l]
        buffer = buffer[l:]
    return buffer
    

def msg(c, m):
    out = "MIME-Version: 1.0\r\nContent-Type: text/plain; charset=UTF-8\r\n\r\n" + m
    c.send("MSG %s %s %d\r\n%s" % (email, nick, len(out), out))

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((addr, 1863))
s.listen(5)

c = s.accept()[0]
c.send(iro)
c.send(ans)
