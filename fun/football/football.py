win = 3
draw = 1
lose = 0

def standings(matches, tms=None):
    if tms == None:
        tms = matches.keys()
    st = []
    for x in tms:
        r = [0, 0, 0, x]
        for y in tms:
            if x == y:
                continue
            for z in matches[x][y]:
                if z != None:
                    if z[0] > z[1]:
                        r[0] += win
                    elif z[0] == z[1]:
                        r[0] += draw
                    elif z[0] < z[1]:
                        r[0] += lose
                    r[1] += z[0] - z[1]
                    r[2] += z[0]
        st.append(r)
    st.sort(reverse=True)
    for k in range(3):
        p = st[0][k]
        if p != st[-1][k]:
            t = 0
            for i in range(len(st)):
                if (i+1 == len(st) or st[i+1][k] != p):
                    if i > t:
                        d = dict([(r[3], r) for r in st[t:i+1]])
                        c = standings(matches, d.keys())
                        for j, r in enumerate(c):
                            st[t+j] = d[r[3]]
                    if i+1 < len(st):
                        t = i+1
                        p = st[t][k]
            break
    return st
    
def loop(matches, lm):
    mtc = dict([(x, dict([(y, matches[x][y][:]) for y in matches[x]])) for x in matches])
    sts = []
    for r in lm:
        r[1], r[2] = 0, 0
    for i in range(3**len(lm)):
        for x in lm:
            mtc[x[0]][x[3]][0] = [x[1], x[2]]
            mtc[x[3]][x[0]][1] = [x[2], x[1]]
        sts.append(([l[:] for l in lm], standings(mtc)))
        j = 0
        while j < len(lm):
            if lm[j][1] == lm[j][2]:
                lm[j][1] = 1
                break
            elif lm[j][1] == 1:
                lm[j][1:3] = [0, 1]
                break
            else:
                lm[j][2] = 0
                j += 1
    return sts