HEIGHT = 6
WIDTH = 6
GOAL_X = (HEIGHT-1)//2
GOAL_Y = WIDTH-1

pos0 = [
    ['',   '',   'H1', 'H1', '',   ''  ],
    ['',   '',   'L',  'V1', '',   ''  ],
    ['M',  'M',  'V2', 'V1', '',   ''  ],
    ['',   '',   'V2', 'L',  '',   ''  ],
    ['',   '',   'H2', 'H2', '',   ''  ],
    ['',   '',   'L',  'L',  '',   ''  ]
]

pos1 = [
    ['V1', 'V2', '',   'V3', 'V4', 'V5'],
    ['V1', 'V2', '',   'V3', 'V4', 'V5'],
    ['V1', 'M',  'M',  'V3', '',   ''  ],
    ['',   '',   'V6', 'H1', 'H1', 'H1'],
    ['',   '',   'V6', '',   '',   'V7'],
    ['',   '',   'H2', 'H2', 'H2', 'V7']
]

pos2 = [
    ['V1', '',   '',   '',   '',   ''  ],
    ['V1', '',   'H1', 'H1', 'H2', 'H2'],
    ['M',  'M',  'V2', '',   '',   'V3'],
    ['H3', 'H3', 'V2', 'V4', 'V5', 'V3'],
    ['H4', 'H4', 'H4', 'V4', 'V5', 'V6'],
    ['H5', 'H5', '',   '',   '',   'V6']
]

pos3 = [
    ['',   'V1', '',   'H1', 'H1', 'V2'],
    ['',   'V1', 'H2', 'H2', 'V3', 'V2'],
    ['',   'V1', 'M',  'M',  'V3', 'V4'],
    ['V5', 'V6', '',   'V7', 'V8', 'V4'],
    ['V5', 'V6', '',   'V7', 'V8', 'V4'],
    ['V5', 'H3', 'H3', 'H3', 'H4', 'H4']
]

pos4 = [
    ['V1', 'V2', 'H1', 'H1', 'H1', ''  ],
    ['V1', 'V2', '',   '',   'V3', ''  ],
    ['V1', 'M',  'M',  '',   'V3', 'V4'],
    ['H2', 'H2', 'H2', '',   'V3', 'V4'],
    ['',   '',   'V5', '',   'H3', 'H3'],
    ['',   '',   'V5', '',   '',   ''  ]
]

pos5 = [
    ['',   '',   'V4', 'H1', 'H1', 'H1'],
    ['V1', '',   'V4', 'V5', '',   'V7'],
    ['V1', 'M',  'M',  'V5', 'V6', 'V7'],
    ['V2', 'V3', 'H2', 'H2', 'V6', 'V8'],
    ['V2', 'V3', 'H3', 'H3', 'V6', 'V8'],
    ['',   '',   '',   '',   '',   ''  ]
]

pos6 = [
    ['V1', 'H1', 'H1', 'H1', 'V5', 'V6'],
    ['V1', '',   '',   'V3', 'V5', 'V6'],
    ['M',  'M',  'V2', 'V3', '',   'V6'],
    ['H2', 'H2', 'V2', 'V4', '',   ''  ],
    ['',   '',   '',   'V4', 'H3', 'H3'],
    ['H4', 'H4', 'H4', '',   '',   ''  ]
]

pos7 = [
    ['V1', 'V2', 'H1', 'H1', 'H1', 'V6'],
    ['V1', 'V2', '',   'V4', 'V5', 'V6'],
    ['V1', 'M',  'M',  'V4', 'V5', ''  ],
    ['H2', 'H2', 'V3', '',   'V5', ''  ],
    ['',   '',   'V3', 'H3', 'H3', ''  ],
    ['',   'H4', 'H4', 'H5', 'H5', ''  ]
]

def printPosition(pos):
    for r in pos:
        for x in r:
            print '%2s' % x,
        print

def copyPosition(pos):
    return [x[:] for x in pos]

def findMoves(pos, forbidden=None):
    fx = ['']
    if forbidden != None:
        fi, fj = forbidden
        fx.append(pos[fi][fj])
    for i in range(HEIGHT):
        for j in range(WIDTH):
            if pos[i][j] in fx:
                continue
            if pos[i][j] == 'M' or pos[i][j][0] == 'H':
                if j == 0 or pos[i][j-1] != pos[i][j]:
                    b, f = [], []
                    size = 1
                    for k in range(j-1, -1, -1):
                        if pos[i][k] != '':
                            break
                        f.append(k)
                    b.reverse()
                    for k in range(j+1, WIDTH):
                        if pos[i][k] == pos[i][j]:
                            size += 1
                            continue
                        if pos[i][k] != '':
                            break
                        b.append(k-size+1)
                    f.reverse()
                    for x in f+b:
                        yield (pos[i][j], [(i, k) for k in range(j, j+size)], [(i, k) for k in range(x, x+size)])
            elif pos[i][j][0] == 'V':
                if i == 0 or pos[i-1][j] != pos[i][j]:
                    b, f = [], []
                    size = 1
                    for k in range(i-1, -1, -1):
                        if pos[k][j] != '':
                            break
                        f.append(k)
                    b.reverse()
                    for k in range(i+1, HEIGHT):
                        if pos[k][j] == pos[i][j]:
                            size += 1
                            continue
                        if pos[k][j] != '':
                            break
                        b.append(k-size+1)
                    f.reverse()
                    for x in f+b:
                        yield (pos[i][j], [(k, j) for k in range(i, i+size)], [(k, j) for k in range(x, x+size)])
                        
def applyMove(pos, move):
    newpos = copyPosition(pos)
    for i, j in move[1]:
        newpos[i][j] = ''
    for i, j in move[2]:
        newpos[i][j] = move[0]
    return newpos

def generatePositions(pos, seen, forbidden, max=0):
    moves = []
    for move in findMoves(pos, forbidden):
        newpos = applyMove(pos, move)
        spos = str(newpos)
        if spos in seen and seen[spos] >= max:
            continue
        seen[spos] = max
        yield (move, newpos)

def BFS(pos):
    seen = {str(pos): 0}
    queue = [(pos, [])]
    forbidden = None
    while len(queue) > 0:
        pos, moves = queue[0]
        if len(moves) > 0:
            forbidden = moves[-1][2][0]
        del queue[0]
        for m, p in generatePositions(pos, seen, forbidden):
            newmoves = moves + [m]
            if p[GOAL_X][GOAL_Y] == 'M':
                return newmoves
            queue.append((p, newmoves))
    return None
    
def DFS(pos, max=50, moves=[], seen=None):
    if seen == None:
        seen = {str(pos): max}
    if max <= 0:
        return None
    forbidden = None
    if len(moves) > 0:
        forbidden = moves[-1][2][0]
    for m, p in generatePositions(pos, seen, forbidden, max-1):
        newmoves = moves + [m]
        if p[GOAL_X][GOAL_Y] == 'M':
            return newmoves
        newmoves = DFS(p, max-1, newmoves, seen)
        if newmoves != None:
            return newmoves
    return None

def stepByStep(pos, sol):
    printPosition(pos)
    for m in sol:
        yield m
        pos = applyMove(pos, m)
        printPosition(pos)
    if pos[GOAL_X][GOAL_Y] == 'M':
        print 'Done!'
    yield None
