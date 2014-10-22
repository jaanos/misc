def monome(deg, var='x'):
    """ Poskrbi za znakovno predstavitev èlena polinoma. """
    if deg == 0:
        return '1'
    if deg == 1:
        return var
    return '%s^%d' % (var, deg)

def weight(word): # Èasovna zahtevnost: O(len(word))
    """ Izraèuna težo besede. """
    w = 0
    while word > 0:
        if (word & 1) == 1:
            w += 1
        word = word >> 1
    return w

def buildWords(length, poly, max, dict): # Èasovna zahtevnost: O((length + len(poly)) 2^length)
    """ Gradi besede in se ustavi, èe pade teža pod podano mejo. """
    list = [0]
    min = length
    for i in range(0, length):
        for j in range(0, len(list)):
            w = list[j] ^ poly
            t = weight(w)
            if t < max:
                return False
            if t < min:
                min = t
            if not dict.has_key(t):
                dict[t] = 0
            dict[t] += 1
            list.append(w)
        poly = poly << 1
    return min

class CRCPolynom:
    def __init__(self, poly, deg):
        """ Konstruktor. """
        self.poly = poly
        self.deg = deg
        self.length = None
        self.weights = None
        self.minWeight = None
        self.keys = None
    
    def __str__(self):
        """ Poskrbi za znakovno predstavitev generatorskega polinoma. """
        list = []
        tmp = self.poly
        i = 0
        while tmp > 0:
            if (tmp & 1) == 1:
                list.append(monome(i))
            i += 1
            tmp /= 2
        list.reverse()
        return ' + '.join(list)

    def __cmp__(self, other):
        """ Poskrbi za primerjanje kodov. """
        for i in range(0, min(len(self.weights), len(other.weights))):
            out = other.keys[0] - self.keys[0]
            if out != 0:
                return out
            out = self.weights[self.keys[0]] - other.weights[self.keys[0]]
            if out != 0:
                return out
        return len(self.weights) - len(other.weights)

    def wordWeights(self, deg, length, max): # Èasovna zahtevnost: O((deg + len(poly)) 2^deg)
        """ Izraèuna teže kodnih besed. """
        if self.weights == None:
            self.length = length
            dict = {}
            self.minWeight = buildWords(deg, self.poly, max, dict)
            self.weights = dict
            self.keys = dict.keys()
            self.keys.sort()
        return self.minWeight

def multiple(poly, min): # Skupna èasovna zahtevnost: <= O(st 2^st)
    """ Najde polinom oblike x^t + 1, t >= min, ki je veèkratnik podanega polinoma. """
    res = poly.poly
    i = 0
    while True: # Maksimalno število obhodov: 2^st - 1
        check = False # Povpreèno število obhodov: <= O(2^st)
        if poly.deg + i >= min: # (st = poly.deg)
            for j in range(1, poly.deg):
                if (res & (1L << (i+j))) != 0:
                    check = True
                    break
            if not check:
                return CRCPolynom(res, poly.deg + i)
        i += 1
        while (res & (1L << i)) == 0:
            i += 1
        res ^= poly.poly << i

def find(n, k): # Ker v naših primerih velja k > n-k: èasovna zahtevnost O(n 2^k)
    """ Najde generatorske polinome za (skrajšane) CRC kode in najboljše izpiše v LaTeXovi tabeli. """
    print ''
    print "$[%d, %d]$-kodi" % (n, k)
    # Polinomi so predstavljeni kot bitno polje
    poly = 1L << (n - k)
    max = 0
    # Poskusimo z vsemi polinomi stopnje n-k s konstantnim èlenom 1
    for i in range(poly+1, 2*poly, 2): # Število obhodov zanke: 2^(n - k - 1)
        div = CRCPolynom(i, n-k)
        mul = multiple(div, n) # O((n-k) 2^(n-k))
        minWeight = div.wordWeights(k, mul.deg-div.deg, max) # O((k + (n-k)) 2^k)
        # Preverimo, èe je dosežena najveèja minimalna razdalja
        if minWeight != False and minWeight >= max:
            # Èe imamo boljšo minimalno razdaljo, dosedanje rezultate pozabimo
            if minWeight > max:
                max = minWeight
                best = []
            best.append(div)
    keys = list(set(reduce(lambda x,y: x+y, [[y for y in x.keys] for x in best])))
    keys.sort()
    best.sort()

    print '$$ \\begin{tabular}{|c|cccc|%s|}' % ('c'*len(keys))
    print '\\multicolumn{1}{c}{Generatorski polinom} & \\multicolumn{4}{c}{Parametri} & \\multicolumn{%d}{c}{Teže kodnih besed} \\\\' % len(keys)
    print '\cline{2-%d} \multicolumn{1}{c|}{} & $n$ & $k$ & $s$ & $d$' % (len(keys)+5),
    for y in keys:
        print '& %d' % y,
    print '\\\\ \\hline'
    
    for x in best:       
        print '$%s$ & %d & %d & %d & %d' % (x, x.deg+x.length, x.length, x.length-k, x.minWeight),
        for y in keys:
            print '& %d' % (x.weights[y] if x.weights.has_key(y) else 0),
        print '\\\\'

    print '\\hline \\end{tabular} $$'

# a) Ker sta pri stopnji 2 možna le 2 generatorska polinoma, nam funkcija ne bo našla skrajšanih CRC kodov.
find(6, 4);

# b)
find(12, 8);
find(13, 8);
find(14, 8);

# Glede na ocenjeno èasovno zahtevnost bi morala sledeèa primera trajati 2 * 2^16 ~ 130.000 krat veè kot zadnji primer.
#find(28, 24);
#find(32, 24);
