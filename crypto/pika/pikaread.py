#!/usr/bin/python
import sys
from smartcard.scard import *

def tohex(l, delim='', reverse=False):
    if reverse:
        l = l[:]
        l.reverse()
    return delim.join(['%02X' % x for x in l])
        
def tostr(r):
    return ''.join(['.' if x < 0x20 or x > 0x7e else chr(x) for x in r])
        
def manuf(r, i):
    ma = tohex(r[:-2], reverse=True)
    if ma == 'FFFFFFAA':
        return 'Sample card'
    else:
        return 'Manufacturer area: %s' % ma
        
def sn(r, i):
    return 'S/N %d: %s' % (i, tohex(r[:-2]))
    
def mode(r, i):
    m = r[3] >> 6
    if m == 1:
        return 'Issuer mode'
    elif m == 2:
        return 'User mode'
    else:
        return 'Invalid mode %s - card blocked' % ('00' if m == 0 else '11')
        
def aca(r, i):
    sec = ['update forbidden', 'read protected']
    reg = ['UA2', 'Bal2', 'UA1', 'Bal1']
    j = 0
    out = []
    ac = r[3]
    while ac > 0:
        if (ac&1) == 1:
            out.append('%s %s' % (reg[j>>1], sec[j&1]))
        ac >>= 1
        j += 1
    return ', '.join(out)
    
def csc(r, i):
    return 'Card secret code %d' % ((i-6)*(i-54)/100)
    
def cscrc(r, i):
    return 'CSC %d errors: %d' % ((i-7)*(i-55)/100, sum([(r[3]>>k)&1 for k in range(4, 8)]))
        
def ctc(r, i):
    if len(r) <= 2:
        return ''
    val = int(tohex(r[:-2], reverse=True), 16) & ~0x10000000
    return 'Balance %d transaction count: %d%s' % (i/32+1, val, '' if (i&1) == 0 else ' (backup)')
        
def awf(r, i):
    return '%s %d anti-withdrawal flag' % ('CTC' if (i&1) == 0 else 'Balance', i/32+1)
    
def balance(r, i):
    if len(r) <= 2:
        return ''
    val = int(tohex(r[:-2], reverse=True), 16) << ((i&2)*16)
    return 'Balance %d: %d%s' % (i/32+1, val, '' if (i&1) == 0 else ' (backup)')

def lastuse(r, i):
    if len(r) < 6:
        return ''
    return 'Last used: %s' % tohex(r[1:4], '.')
    
def birth(r, i):
    if len(r) < 6:
        return ''
    return 'Birth date: %s.%s' % (tohex(r[:2], '.'), tohex(r[2:4]))
    
def pike(r, i):
    return 'Number of points: %d%s' % (int(tohex(r[:2])), '' if (i&1) == 0 else ' (backup)')
        
interp = {0x00: manuf, 0x01: sn, 0x02: sn, 0x03: sn, 0x04: mode, 0x05: aca,
    0x06: csc, 0x07: cscrc, 0x08: ctc, 0x09: ctc, 0x0a: awf, 0x0b: awf,
    0x0c: balance, 0x0d: balance, 0x0e: balance, 0x0f: balance, 0x10: lastuse,
    0x20: ctc, 0x21: ctc, 0x22: awf, 0x23: awf, 0x24: balance, 0x25: balance,
    0x26: balance, 0x27: balance, 0x2f: birth, 0x38: csc, 0x39: cscrc,
    0x3a: csc, 0x3b: cscrc, 0x3c: pike, 0x3d: pike}

hresult, hcontext = SCardEstablishContext(SCARD_SCOPE_USER)
if hresult!=0:
    raise error, 'Failed to establish context : ' + SCardGetErrorMessage(hresult)
        
hresult, readers = SCardListReaders(hcontext, [])
if hresult!=0:
    raise error, 'Failed to list readers: ' + SCardGetErrorMessage(hresult)
print 'PCSC Readers:', readers

if len(readers) == 0:
    raise error, 'No smart card readers'
    
if len(sys.argv) > 1:
    ri = int(sys.argv[1])
else:
    if len(readers) > 1:
        print 'Using first reader.'
    ri = 0
    
hresult, hcard, dwActiveProtocol = SCardConnect(hcontext, readers[ri], SCARD_SHARE_SHARED, SCARD_PROTOCOL_T0)
if hresult!=0:
    raise error, 'Unable to connect: ' + SCardGetErrorMessage(hresult)
    
for i in range(0x40):
    hresult, response = SCardTransmit(hcard, SCARD_PCI_T0, [0x80, 0xBE, 0x00, i, 0x04])
    if hresult!=0:
        raise error, 'Failed to transmit: ' + SCardGetErrorMessage(hresult)
    print '0x%02X\t%5s\t%11s\t%s' % (i, tohex(response[-2:], ' '), tohex(response[:-2], ' '), interp[i](response, i) if interp.has_key(i) else tostr(response[:-2]))