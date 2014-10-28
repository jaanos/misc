import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Zogica implements ZogicaPovrsina {
    public static void main(String[] args) {
        new ZogicaOkno();
    }
    
    int r;
    double x, y, vx, vy;
    Color c;
    
    public Zogica(double x, double y, int r, double vx, double vy, Color c) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.vx = vx;
        this.vy = vy;
        this.c = c;
    }
    
    public void move(Graphics g) {
        if (x+r >= SIRINA)
            vx = -Math.abs(vx);
        else if (x-r <= 0)
            vx = Math.abs(vx);
        else if (x+r < SIRINA-GRAVITACIJA_X-EPSILON && x-r > EPSILON-GRAVITACIJA_X)
            vx+=GRAVITACIJA_X;
        else if (Math.abs(vx) < EPSILON)
            vx = 0;
        if (y+r >= VISINA)
            vy = -Math.abs(vy);
        else if (y-r <= 0)
            vy = Math.abs(vy);
        else if (y+r < VISINA-GRAVITACIJA_Y-EPSILON && y-r > EPSILON-GRAVITACIJA_Y)
            vy+=GRAVITACIJA_Y;
        else if (Math.abs(vy) < EPSILON)
            vy = 0;
        x+=vx;
        y+=vy;
        vx*=TRENJE;
        vy*=TRENJE;
        g.setColor(c);
        g.fillOval((int)x-r,(int)y-r,2*r,2*r);
    }
    
    public boolean collide(Zogica z, boolean ze) {
        if ((x-z.x)*(x-z.x)+(y-z.y)*(y-z.y) >= (r+z.r)*(r+z.r))
            return false;
        if (ze)
            return true;
        double zgx = z.gibalnaKolicinaX();
        double zgy = z.gibalnaKolicinaY();
        double gx = gibalnaKolicinaX();
        double gy = gibalnaKolicinaY();
        z.setGibalnaKolicinaX(TRENJE*(PROZNOST*gx+(1-PROZNOST)*zgx));
        z.setGibalnaKolicinaY(TRENJE*(PROZNOST*gy+(1-PROZNOST)*zgy));
        setGibalnaKolicinaX(TRENJE*((1-PROZNOST)*gx+PROZNOST*zgx));
        setGibalnaKolicinaY(TRENJE*((1-PROZNOST)*gy+PROZNOST*zgy));
        return true;
    }
    
    public double gibalnaKolicinaX() {
        return r*r*vx;
    }
    
    public double gibalnaKolicinaY() {
        return r*r*vy;
    }
    
    public void setGibalnaKolicinaX(double gx) {
        vx = gx/(r*r);
    }
    
    public void setGibalnaKolicinaY(double gy) {
        vy = gy/(r*r);
    }
}

class ZogicaOkno extends JFrame implements ZogicaPovrsina {
    public ZogicaOkno() {
        setSize(SIRINA+6,VISINA+32);
        setResizable(false);
        setTitle("Žogica");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().add(new ZogicaPanel());
        setVisible(true);
    }
}

class ZogicaPanel extends JPanel implements ZogicaPovrsina, Runnable {
    static int b=0;
    static final Color[] barve = {Color.red, Color.black, Color.cyan, Color.pink, Color.magenta, Color.gray, Color.orange, Color.blue, Color.lightGray, Color.green};
    static final int ST_ZOG = 3;
    int frame;
    Thread animator;
    Zogica[] z = new Zogica[ST_ZOG];
    boolean[][] collide = new boolean[ST_ZOG][ST_ZOG];

    public ZogicaPanel() {
        animator = new Thread(this);
        animator.start();
        z[0] = new Zogica(440,200,15,15,-40,barve[0]);
        z[1] = new Zogica(200,350,10,30,20,barve[1]);
        z[2] = new Zogica(100,480,20,-5,60,barve[2]);
    }
    
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0,0,SIRINA,VISINA);
        for (int i=0; i < z.length; i++) {
            z[i].move(g);
            for (int j=i+1; j < z.length; j++)
                collide[i][j] = z[i].collide(z[j], collide[i][j]);
        }
        g.setColor(Color.BLACK);
        g.fillRect(SIRINA,0,getWidth()-SIRINA,getHeight());
        g.fillRect(0,VISINA,getWidth(),getHeight()-VISINA);
    }
    
    public void run() {
        // Remember the starting time
        long tm = System.currentTimeMillis();
        while (Thread.currentThread() == animator) {
            // Display the next frame of animation.
            repaint();
    
            // Delay depending on how far we are behind.
            try {
                tm += DELAY;
                Thread.sleep(Math.max(0, tm - System.currentTimeMillis()));
            } catch (InterruptedException e) {
                break;
            }
    
            // Advance the frame
            frame++;
        }
    }
}

interface ZogicaPovrsina {
    int SIRINA = 500;
    int VISINA = 500;
    double GRAVITACIJA_X = 0;
    double GRAVITACIJA_Y = 1;
    double TRENJE = 0.99;
    double PROZNOST = 0.9;
    double EPSILON = 0.3;
    int DELAY = 50;
}