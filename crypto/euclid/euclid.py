import math
import random

def log2(x):
    l = -1
    while x != 0:
        x >>= 1
        l += 1
    return l

def EuclidStep(a, b):
    la, lb = log2(a), log2(b)
    l = la - lb
    m = l
    while a >= b:
        if a & (1 << la):
            a ^= b << l
        l -= 1
        la -= 1
    return (b, a, m)
    
def EuclidStat(n = 1000, m = 1024):
    f = [0]*m
    a, b = 0, 0
    procenti = [i*n/100 for i in range(100)]
    for i in xrange(n):
        if i in procenti:
            print "%d%%" % procenti.index(i)
        while a == 0:
            a = random.randrange(1 << m)
        while b == 0:
            b = random.randrange(1 << m)
        if a < b:
            a, b = b, a
        while b != 0:
            a, b, l = EuclidStep(a, b)
            f[l] += 1
        a, b = 0, 0
    return f
