#!/usr/bin/python

import random

t = [
    {'RSA': 'AF', 'BRA': 'SA', 'GER': 'EU', 'ITA': 'EU', 'ESP': 'EU', 'ENG': 'EU', 'FRA': 'EU', 'ARG': 'SA'},
    {'POR': 'EU', 'NED': 'EU', 'SUI': 'EU', 'SLV': 'EU', 'GRE': 'EU', 'DEN': 'EU', 'SRB': 'EU', 'SVK': 'EU'},
    {'URU': 'SA', 'CHI': 'SA', 'PAR': 'SA', 'GHA': 'AF', 'CMR': 'AF', 'CIV': 'AF', 'NGA': 'AF', 'ALG': 'AF'},
    {'KOR': 'AS', 'JPN': 'AS', 'AUS': 'AS', 'PRK': 'AS', 'MEX': 'NA', 'USA': 'NA', 'HON': 'NA', 'NZL': 'OC'}
]

sk = [{} for i in range(8)]
    
sk[0]['RSA'] = t[0]['RSA']
del t[0]['RSA']

free = range(1, 8)
euaf = 0
for i in range(4):
    lot = t[i].keys()
    random.shuffle(lot)
    for x in lot:
        for j in free:
            if t[i][x] == 'EU' or (t[i][x] not in sk[j].values() and
                    (i != 2 or t[i][x] != 'AF' or sk[j].values().count('EU') < 2 or euaf < 3)):
                print '%s -> %c' % (x, chr(j+65))
                if i == 2 and t[i][x] == 'AF' and sk[j].values().count('EU') == 2:
                    euaf += 1
                sk[j][x] = t[i][x]
                del t[i][x]
                free.remove(j)
                break
    free = range(8)
print
    
for j, s in enumerate(sk):
    print "%c: %s" % (65+j, ', '.join(s.keys()))
