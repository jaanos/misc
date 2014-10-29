import java.util.*;

class Tocka {
    double x, y, z;
    Tocka prec;
    double path;
    boolean enabled = true;
    int pos;
        
    protected Tocka() {}
    
    public Tocka(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        reset();
    }
        
    public void reset() {
        path = Double.POSITIVE_INFINITY;
        prec = null;
        pos = -1;
    }
        
    public String toString() {
        return x + " " + y + " " + z;
    }
}

class DijkstraHeap {
    public static boolean diagonal = true;
    Tocka[] t;
    Tocka[][] z;
    int size;
    
    public DijkstraHeap(Tocka[][] z) {
        this(z, 1048576);
    }
    
    public DijkstraHeap(Tocka[][] z, int cap) {
        this.z = z;
        this.t = new Tocka[cap];
    }
    
    private void doubleCapacity() {
        Tocka[] n = new Tocka[2*t.length];
        for (int i=0; i < size; i++) {
            n[i] = t[i];
        }
        t = n;
    }
    
    public int size() {
        return size;
    }
    
    public int capacity() {
        return t.length;
    }
    
    public void add(Tocka p) {
        if (size == t.length) doubleCapacity(); //O(n) - ni verjetno
        p.reset();
        p.pos = size;
        //int pos = size;
        t[size++] = p;
        /*while (pos > 0) {
            if (t[(pos-1)/2].path > p.path) {
                t[pos] = t[(pos-1)/2];
                t[pos].pos = pos;
                pos = (pos-1)/2;
                t[pos] = p;
                p.pos = pos;
            } else return;
        }*/
    }
    
    public Tocka pop() {
        if (size == 0) return null;
        Tocka p = t[0];
        p.pos = -1;
        t[0] = t[--size];
        if (size == 0) return p;
        int pos = 0;
        Tocka q = t[0];
        q.pos = pos;
        while (2*pos+1 < size) {
            if (2*pos+2 == size && t[2*pos+1].path < q.path) {
                t[pos] = t[2*pos+1];
                t[pos].pos = pos;
                pos = 2*pos+1;
                t[pos] = q;
                q.pos = pos;
                break;
            } else if (t[2*pos+1].path < q.path && t[2*pos+1].path <= t[2*pos+2].path) {
                t[pos] = t[2*pos+1];
                t[pos].pos = pos;
                pos = 2*pos+1;
                t[pos] = q;
                q.pos = pos;
            } else if (t[2*pos+2].path < q.path) {
                t[pos] = t[2*pos+2];
                t[pos].pos = pos;
                pos = 2*pos+2;
                t[pos] = q;
                q.pos = pos;
            } else break;
        }
        return p;
    }
    
    public boolean set(Tocka p, double path) {
        if (p.pos < 0 || path >= p.path) return false;
        if (p != t[p.pos]) throw new IllegalArgumentException();
        p.path = path;
        int pos = p.pos;
        while (pos > 0) {
            if (t[(pos-1)/2].path > p.path) {
                t[pos] = t[(pos-1)/2];
                t[pos].pos = pos;
                pos = (pos-1)/2;
                t[pos] = p;
                p.pos = pos;
            } else break;
        }
        return true;
    }
    
    public Tocka[] dijkstra(Tocka p, Tocka q, int[] t) {
        p.path = 0;
        p.prec = null;
        Tocka r = p;
        int i, j;
        boolean[] b = new boolean[4];
        t[0] = 0;
        do {    //n
            if (r.path == Double.POSITIVE_INFINITY) return null;
            t[0]++;
            if (r == q) break;
            i = (int)(r.x/IPPanel.stepX-IPPanel.offsetX);
            j = (int)(r.y/IPPanel.stepY-IPPanel.offsetY);
            if ((b[0] = (i > 0)) && z[i-1][j] != null && z[i-1][j].enabled) {
                if (set(z[i-1][j], r.path + Math.sqrt((r.x-z[i-1][j].x)*(r.x-z[i-1][j].x) + (r.y-z[i-1][j].y)*(r.y-z[i-1][j].y) + (r.z-z[i-1][j].z)*(r.z-z[i-1][j].z)))) { //n
                    z[i-1][j].prec = r;
                }
            }
            if ((b[1] = (j > 0)) && z[i][j-1] != null && z[i][j-1].enabled) {
                if (set(z[i][j-1], r.path + Math.sqrt((r.x-z[i][j-1].x)*(r.x-z[i][j-1].x) + (r.y-z[i][j-1].y)*(r.y-z[i][j-1].y) + (r.z-z[i][j-1].z)*(r.z-z[i][j-1].z)))) {
                    z[i][j-1].prec = r;
                }
            }
            if ((b[2] = (i+1 < z.length)) && z[i+1][j] != null && z[i+1][j].enabled) {
                if (set(z[i+1][j], r.path + Math.sqrt((r.x-z[i+1][j].x)*(r.x-z[i+1][j].x) + (r.y-z[i+1][j].y)*(r.y-z[i+1][j].y) + (r.z-z[i+1][j].z)*(r.z-z[i+1][j].z)))) {
                    z[i+1][j].prec = r;
                }
            }
            if ((b[3] = (j+1 < z[i].length)) && z[i][j+1] != null && z[i][j+1].enabled) {
                if (set(z[i][j+1], r.path + Math.sqrt((r.x-z[i][j+1].x)*(r.x-z[i][j+1].x) + (r.y-z[i][j+1].y)*(r.y-z[i][j+1].y) + (r.z-z[i][j+1].z)*(r.z-z[i][j+1].z)))) {
                    z[i][j+1].prec = r;
                }
            }
            if (diagonal) {
                if (b[0] && b[1] && z[i-1][j-1] != null && z[i-1][j-1].enabled) {
                    if (set(z[i-1][j-1], r.path + Math.sqrt((r.x-z[i-1][j-1].x)*(r.x-z[i-1][j-1].x) + (r.y-z[i-1][j-1].y)*(r.y-z[i-1][j-1].y) + (r.z-z[i-1][j-1].z)*(r.z-z[i-1][j-1].z)))) {
                        z[i-1][j-1].prec = r;
                    }
                }
                if (b[0] && b[3] && z[i-1][j+1] != null && z[i-1][j+1].enabled) {
                    if (set(z[i-1][j+1], r.path + Math.sqrt((r.x-z[i-1][j+1].x)*(r.x-z[i-1][j+1].x) + (r.y-z[i-1][j+1].y)*(r.y-z[i-1][j+1].y) + (r.z-z[i-1][j+1].z)*(r.z-z[i-1][j+1].z)))) {
                        z[i-1][j+1].prec = r;
                    }
                }
                if (b[2] && b[1] && z[i+1][j-1] != null && z[i+1][j-1].enabled) {
                    if (set(z[i+1][j-1], r.path + Math.sqrt((r.x-z[i+1][j-1].x)*(r.x-z[i+1][j-1].x) + (r.y-z[i+1][j-1].y)*(r.y-z[i+1][j-1].y) + (r.z-z[i+1][j-1].z)*(r.z-z[i+1][j-1].z)))) {
                        z[i+1][j-1].prec = r;
                    }
                }
                if (b[2] && b[3] && z[i+1][j+1] != null && z[i+1][j+1].enabled) {
                    if (set(z[i+1][j+1], r.path + Math.sqrt((r.x-z[i+1][j+1].x)*(r.x-z[i+1][j+1].x) + (r.y-z[i+1][j+1].y)*(r.y-z[i+1][j+1].y) + (r.z-z[i+1][j+1].z)*(r.z-z[i+1][j+1].z)))) {
                        z[i+1][j+1].prec = r;
                    }
                }
            }
        } while ((r = pop()) != null); //log n
        if (r == null) return null;
        int n = 1;
        while (r != p) {    //n
            n++;
            r = r.prec;
        }
        Tocka[] d = new Tocka[n];
        r = q;
        while (n-- > 0) {   //n
            d[n] = r;
            r = r.prec;
        }
        return d;
    }
}