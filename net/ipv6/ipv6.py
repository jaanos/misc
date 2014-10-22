import re

def _parse6(addr):
    cnt = addr.count(':')
    m = re.findall(r'::+', addr)
    if cnt <= 7 and len(m) == 1:
        addr = addr.replace(m[0], ':' + '0:'*(6 - cnt + len(m[0])))
        cnt = 7
    if cnt == 7:
        return [0 if x == '' else int(x, 16) for x in addr.split(':')]
    return None

def _parse4(addr, out):
    if out != None:
        ip4 = [int(x) for x in addr.split('.')]
        if [x < 256 for x in ip4] == [True]*4:
            out[6] = 256*ip4[0] + ip4[1]
            out[7] = 256*ip4[2] + ip4[3]
            return out
    return None

def parse(s):
    addr, delim, cidr = s.partition('/')
    out = None
    if re.match(r'^\[.*\]$', addr):
        addr = addr[1:-1]
    if re.match(r'^(?:[0-9A-F]{0,4}:){2,7}[0-9A-F]{0,4}$', addr, re.IGNORECASE):
        out = _parse6(addr)
    elif re.match(r'^(?:[0-9A-F]{0,4}:){2,6}(?:[0-9]{1,3}\.){3}[0-9]{1,3}$', addr, re.IGNORECASE):
        ri = addr.rindex(':')
        out = _parse4(addr[ri+1:], _parse6(addr[:ri] + ':0:0'))
    elif re.match(r'^(?:[0-9]{1,3}\.){3}[0-9]{1,3}$', addr):
        out = _parse4(addr, [0]*8)
    if out != None:
        if delim != '':
            return out, int(cidr)
        else:
            return out
    raise 'Error: invalid IPv6 address "%s"' % s
    
def fullForm(addr, uppercase=False):
    if type(addr) == str:
        addr = parse(addr)
    if len(addr) == 2:
        return '%s/%d' % (fullForm(addr[0]), addr[1])
    return ':'.join([('%04X' if uppercase else '%04x') % x for x in addr])
        
def expandedForm(addr, uppercase=False):
    if type(addr) == str:
        addr = parse(addr)
    if len(addr) == 2:
        return '%s/%d' % (expandedForm(addr[0]), addr[1])
    return ':'.join([('%X' if uppercase else '%x') % x for x in addr])
        
def compactForm(addr, uppercase=False):
    if type(addr) == str:
        addr = parse(addr)
    ln = len(addr)
    if ln == 2:
        return '%s/%d' % (compactForm(addr[0]), addr[1])
    out = ':'.join([('%X' if uppercase else '%x') % x for x in addr])
    s = ''
    ls = 0
    for x in re.findall(r'(?:^|:)0(?::0)+', out):
        lx = len(x)
        if lx - (1 if x[0] == ':' else 0) > ls:
            s = x
            ls = lx
    if ls == 15:
        return '::'
    if s != '':
        if out[:ls] == s:
            if ls == 11:
                return '::%d.%d.%d.%d' % (addr[6] >> 8, addr[6] % 256, addr[7] >> 8, addr[7] % 256)
            return ':' + out[ls:]
        if out[-ls:] == s:
            return out[:-ls] + '::'
        idx = out.index(s)
        return out[:idx] + ':' + out[idx+ls:]
    return out