units = [50000, 20000, 10000, 5000, 2000, 1000, 500, 200, 100, 50, 20, 10, 5, 2, 1]
sols = {}

def find(x):
    if x >= units[0]:
        return 0
    if x < units[-1]:
        return -1
        
    l = 0
    r = len(units)-1
    while True:
        i = (l+r)/2
        if units[i-1] <= x:
            r = i-1
        elif units[i] > x:
            l = i+1
        else:
            return i

def printsol(sol):
    it = sol.items()
    it.sort(reverse=True)
    print ', '.join(['%d(%d)' % x for x in it if x[1] > 0])

def split(n, m=None, sol=None, prn=False):
    if prn and sol == None:
        sol = dict([(x, 0) for x in units])
        
    if n < 0:
        return 0
    if n == 0:
        if prn:
            printsol(sol)
        return 1
    
    if m == 1:
        if prn:
            sol[1] += n
            printsol(sol)
            sol[1] -= n
        return 1
        
    if m == None or m > n:
        m = n
    
    j = find(m)
    if not prn:
        m = units[j]
        if sols.has_key(n) and sols[n].has_key(m):
            return sols[n][m]
    
    out = 0
    if j == -1:
        return 0
    for i in units[j:]:
        if prn:
            sol[i] += 1
        out += split(n-i, i, sol, prn)
        if prn:
            sol[i] -= 1
        
    if not sols.has_key(n):
        sols[n] = {}
    sols[n][m] = out
    return out
    