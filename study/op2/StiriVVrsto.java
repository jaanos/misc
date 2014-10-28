import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StiriVVrsto {
    public static final Panel4VV panel = new Panel4VV();
    public static void main(String[] args) {
        new Okno4VV(600, 600, "Štiri v vrsto", panel);
    }
}

class Okno4VV extends JFrame {
    public Okno4VV(int x, int y, String naslov, JPanel panel) {
        setSize(x, y);
        setTitle(naslov);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().add(panel);
        show();
    }
}

class OknoIgr extends JFrame {
    public OknoIgr(Igralec igr) {
        setSize(480, 500);
        setTitle("Lastnosti igralca " + igr.getIme());
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().add(new PanelIgr(igr, this));
        show();
    }
}

class Panel4VV extends JPanel implements ActionListener {
    static final int ST = 7;
    public int[][] polja = new int[ST][ST];
    int[][] stiri = new int[4][2];
    JButton[] gumb = new JButton[ST];
    JLabel[] vr = new JLabel[ST];
    JButton nova = new JButton("Nova igra");
    JButton igr1 = new JButton("Igralec 1");
    JButton igr2 = new JButton("Igralec 2");
    JLabel label = new JLabel();
    public OknoIgr oknoigr1, oknoigr2;
    public Igralec prvi = new Igralec("A", Color.green, true);
    public Igralec drugi = new Igralec("B", Color.blue, false);
    public boolean igra = false;
    public boolean aktivna = true;
    
    public Panel4VV() {
        super();
        for (int i=0; i < ST; i++) {
            gumb[i] = new JButton((char)(i+65) + "");
            gumb[i].addActionListener(this);
            vr[i] = new JLabel((i+1) + "");
        }
        nova.addActionListener(this);
        igr1.addActionListener(this);
        igr2.addActionListener(this);
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        nova.setBounds(90, 480, 100, 30);
        label.setBounds(200, 480, 200, 30);
        igr1.setBounds(90, 520, 200, 30);
        igr2.setBounds(300, 520, 200, 30);
        igr1.setBackground(prvi.getBarva());
        igr2.setBackground(drugi.getBarva());
        add(nova);
        add(label);
        add(igr1);
        add(igr2);
        int n=0;
        for (int i=0; i < ST; i++) {
            gumb[i].setBounds(85+60*i, 10, 50, 20);
            vr[i].setBounds(40, 60+60*i, 30, 20);
            add(gumb[i]);
            add(vr[i]);
            gumb[i].setBackground((!igra ? prvi : drugi).getBarva());
            for (int j=0; j < ST; j++) {
                g.setColor(Color.black);
                g.drawRect(80+60*i, 40+60*j, 60, 60);
                if (!aktivna && n < 4 && i == stiri[n][0] && j == stiri[n][1]) {
                    g.setColor(Color.red);
                    n++;
                }
                else
                    g.setColor(Color.white);
                g.fillRect(81+60*i, 41+60*j, 59, 59);
                if (polja[i][j] == 0)
                    continue;
                g.setColor(Color.black);
                g.drawOval(90+60*i, 50+60*j, 40, 40);
                if (polja[i][j] == 1)
                    g.setColor(prvi.getBarva());
                else
                    g.setColor(drugi.getBarva());
                g.fillOval(91+60*i, 51+60*j, 39, 39);
            }
        }
    }
    
    public void actionPerformed(ActionEvent e) {
        JButton izvor = (JButton)e.getSource();
        if (izvor == nova) {
            for (int i=0; i < ST; i++)
                for (int j=0; j < ST; j++)
                    polja[i][j] = 0;
            igra = false;
            aktivna = true;
            label.setText("");
            Igralec tmp = prvi;
            OknoIgr okno = oknoigr1;
            prvi = drugi;
            oknoigr1 = oknoigr2;
            drugi = tmp;
            oknoigr2 = okno;
            prvi.setPrvi(true);
            drugi.setPrvi(false);
            if (prvi instanceof IgralecUI)
                preveri(((IgralecUI)(prvi)).igraj());
            System.out.println();
            repaint();
            return;
        }
        if (izvor == igr1) {
            try {
                oknoigr1.setVisible(true);
            } catch (NullPointerException ex) {
                oknoigr1 = new OknoIgr(prvi);
            }
            return;
        }
        if (izvor == igr2) {
            try {
                oknoigr2.setVisible(true);
            } catch (NullPointerException ex) {
                oknoigr2 = new OknoIgr(drugi);
            }
            return;
        }
        for (int i=0; i < ST; i++) {
            if (izvor == gumb[i]) {
                if (polja[i][0] != 0 || !aktivna)
                    return;
                preveri(i, (!igra ? prvi : drugi).igraj(i));
                repaint();
                return;
            }
        }
    }
    
    public void preveri(int[] p) {
        preveri(p[0], p[1]);
    }
    
    public void preveri(int a, int b) {
        int plus = a;
        int minus = a;
        try {
            do {
                plus++;
            } while (polja[plus][b] == polja[a][b]);
        } catch (IndexOutOfBoundsException e) {}
        try {
            do {
                minus--;
            } while (polja[minus][b] == polja[a][b]);
        } catch (IndexOutOfBoundsException e) {}
        if (plus-minus > 4) {
            aktivna = false;
            for (int i=0; i < stiri.length; i++) {
                stiri[i][0] = minus+i+1;
                stiri[i][1] = b;
            }
        }
        
        plus = b;
        minus = b;
        try {
            do {
                plus++;
            } while (polja[a][plus] == polja[a][b]);
        } catch (IndexOutOfBoundsException e) {}
        try {
            do {
                minus--;
            } while (polja[a][minus] == polja[a][b]);
        } catch (IndexOutOfBoundsException e) {}
        if (plus-minus > 4) {
            aktivna = false;
            for (int i=0; i < stiri.length; i++) {
                stiri[i][0] = a;
                stiri[i][1] = minus+i+1;
            }
        }
        
        plus = 0;
        minus = 0;
        try {
            do {
                plus++;
            } while (polja[a+plus][b+plus] == polja[a][b]);
        } catch (IndexOutOfBoundsException e) {}
        try {
            do {
                minus++;
            } while (polja[a-minus][b-minus] == polja[a][b]);
        } catch (IndexOutOfBoundsException e) {}
        if (plus+minus > 4) {
            aktivna = false;
            for (int i=0; i < stiri.length; i++) {
                stiri[i][0] = a-minus+i+1;
                stiri[i][1] = b-minus+i+1;
            }
        }
        
        plus = 0;
        minus = 0;
        try {
            do {
                plus++;
            } while (polja[a+plus][b-plus] == polja[a][b]);
        } catch (IndexOutOfBoundsException e) {}
        try {
            do {
                minus++;
            } while (polja[a-minus][b+minus] == polja[a][b]);
        } catch (IndexOutOfBoundsException e) {}
        if (plus+minus > 4) {
            aktivna = false;
            for (int i=0; i < stiri.length; i++) {
                stiri[i][0] = a-minus+i+1;
                stiri[i][1] = b+minus-i-1;
            }
        }
        
        if (!aktivna)
            label.setText("Zmagal je igralec " + (!igra ? prvi : drugi).getIme());
        else {
            for (int i=0; i < ST; i++)
                if (polja[i][0] == 0) {
                    igra = !igra;
                    if ((!igra ? prvi : drugi) instanceof IgralecUI)
                        preveri(((IgralecUI)(!igra ? prvi : drugi)).igraj());
                    return;
                }
            aktivna = false;
            label.setText("Vsa polja zapolnjena!");
            for (int i=0; i < stiri.length; i++) {
                stiri[i][0] = -1;
                stiri[i][1] = -1;
            }
        }
    }
}

class PanelIgr extends JPanel implements ActionListener {
    Igralec igr;
    OknoIgr up;
    JLabel textime = new JLabel("Ime:");
    JLabel textbarva = new JLabel("Barva:");
    JCheckBox comp = new JCheckBox("Raèunalniško voden igralec");
    JTextField ime = new JTextField();
    JButton ok = new JButton("OK");
    JButton cancel = new JButton("Preklièi");
    JColorChooser barva = new JColorChooser();
    
    public PanelIgr(Igralec igr, OknoIgr up) {
        super();
        this.igr = igr;
        this.up = up;
        ime.setText(igr.getIme());
        barva.setColor(igr.getBarva());
        ok.addActionListener(this);
        cancel.addActionListener(this);
        add(barva);
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        textime.setBounds(10, 10, 90, 30);
        ime.setBounds(100, 10, 90, 30);
        textbarva.setBounds(10, 90, 90, 30);
        barva.setBounds(10, 120, 450, 450);
        comp.setBounds(10, 50, 200, 30);
        ok.setBounds(250, 10, 100, 30);
        cancel.setBounds(250, 50, 100, 30);
        add(textime);
        add(ime);
        add(textbarva);
        add(comp);
        add(ok);
        add(cancel);
        add(barva);
    }
        
    public void actionPerformed(ActionEvent e) {
        JButton izvor = (JButton)e.getSource();
        if (izvor == ok) {
            if (igr instanceof IgralecUI && !comp.isSelected()) {
                igr = new Igralec(ime.getText(), barva.getColor(), igr.jePrvi());
                if (igr.jePrvi())
                    StiriVVrsto.panel.prvi = igr;
                else
                    StiriVVrsto.panel.drugi = igr;
            } else if (!(igr instanceof IgralecUI) && comp.isSelected()) {
                igr = new IgralecUI(ime.getText(), barva.getColor(), igr.jePrvi());
                if (igr.jePrvi()) {
                    StiriVVrsto.panel.prvi = igr;
                    if (!StiriVVrsto.panel.igra && StiriVVrsto.panel.aktivna) {
                        StiriVVrsto.panel.preveri(((IgralecUI)(igr)).igraj());
                        StiriVVrsto.panel.repaint();
                    }
                } else {
                    StiriVVrsto.panel.drugi = igr;
                    if (StiriVVrsto.panel.igra && StiriVVrsto.panel.aktivna) {
                        StiriVVrsto.panel.preveri(((IgralecUI)(igr)).igraj());
                        StiriVVrsto.panel.repaint();
                    }
                }
            } else {
                igr.setIme(ime.getText());
                igr.setBarva(barva.getColor());
            }
        }
        up.dispose();
    }
}

class Igralec {
    String ime;
    Color barva;
    boolean prvi;
    
    public Igralec(String ime, Color barva) {
        this.ime = ime;
        this.barva = barva;
    }
    
    public Igralec(String ime, Color barva, boolean prvi) {
        this.ime = ime;
        this.barva = barva;
        this.prvi = prvi;
    }
    
    public void setIme(String ime) {
        this.ime = ime;
    }
    
    public void setBarva(Color barva) {
        this.barva = barva;
    }
    
    public void setPrvi(boolean prvi) {
        this.prvi = prvi;
    }
    
    public String getIme() {
        return ime;
    }
    
    public Color getBarva() {
        return barva;
    }
    
    public boolean jePrvi() {
        return prvi;
    }
    
    public int igraj(int n) {
        int i=StiriVVrsto.panel.ST-1;
        while (StiriVVrsto.panel.polja[n][i] != 0)
            i--;
        StiriVVrsto.panel.polja[n][i] = (prvi ? 1 : -1);
        System.out.println(ime + ": " + (char)(n+65) + (i+1));
        return i;
    }
}

class IgralecUI extends Igralec {
    int[] stolpec = new int[StiriVVrsto.panel.ST];
    
    public IgralecUI(String ime, Color barva) {
        super(ime, barva);
    }
    
    public IgralecUI(String ime, Color barva, boolean prvi) {
        super(ime, barva, prvi);
    }
    
    public int[] igraj() {
        //1 - takojsnja zmaga
        //2 - za preprecit poraz
        //3 - dvojna prisila
        //4 - prisili nasprotnika
        //5 - ne smem dat sem
        //6 - nihèe ne sme dat sem
        
        int plus, plus2, minus, minus2, b, x=(prvi ? 1 : -1);
        for (int a=0; a < StiriVVrsto.panel.ST; a++)
            if (stolpec[a] < 6)
                stolpec[a] = 0;

        for (int j=4; j > 0; j--) {
            System.out.println(j + " ");
            if (j > 1) {
                for (int i = (j==4 ? 5 : 2); i > 0; i--) {
                    if (i != 3)
                        x = -x;
                    for (int a=0; a < StiriVVrsto.panel.ST; a++) {
                        for (b=StiriVVrsto.panel.ST-1; b >= 0; b--)
                            if (StiriVVrsto.panel.polja[a][b] == 0)
                                break;
                        if (i > 2)
                            b--;
                        if (i == 3)
                            b--;
                        if (b < 0)
                            continue;
                        
                        plus = 0;
                        minus = 0;
                        try {
                            do {
                                plus++;
                            } while (StiriVVrsto.panel.polja[a+plus][b+plus] == x);
                        } catch (IndexOutOfBoundsException e) {}
                        try {
                            do {
                                minus++;
                            } while (StiriVVrsto.panel.polja[a-minus][b-minus] == x);
                        } catch (IndexOutOfBoundsException e) {}
                        if (j < 4) {
                            plus2 = 0;
                            minus2 = 0;
                            try {
                                do {
                                    plus2++;
                                } while (StiriVVrsto.panel.polja[a+plus2][b+plus2] != -x);
                            } catch (IndexOutOfBoundsException e) {}
                            try {
                                do {
                                    minus2++;
                                } while (StiriVVrsto.panel.polja[a-minus2][b-minus2] != -x);
                            } catch (IndexOutOfBoundsException e) {}
                            if (plus2+minus2 <= 4) {
                                plus = 0;
                                minus = 0;
                            }
                        } else if (i == 1 && (stolpec[a] == 0 || stolpec[a] == 3 || stolpec[a] == 4)) {
                            if (plus+minus == 4) {
                                try {
                                    if (StiriVVrsto.panel.polja[a+plus][b+plus] == 0 && (b+plus+1 == StiriVVrsto.panel.ST || StiriVVrsto.panel.polja[a+plus][b+plus+1] != 0))
                                        if (stolpec[a] == 4 || stolpec[a] == 3)
                                            stolpec[a] = 3;
                                        else
                                            stolpec[a] = 4;
                                } catch (IndexOutOfBoundsException e) {}
                                try {
                                    if (StiriVVrsto.panel.polja[a-minus][b-minus] == 0 && StiriVVrsto.panel.polja[a-minus][b-minus+1] != 0)
                                        if (stolpec[a] == 4 || stolpec[a] == 3)
                                            stolpec[a] = 3;
                                        else if (stolpec[a] == 2 || stolpec[a] == 1)
                                            stolpec[a] = 4;
                                } catch (IndexOutOfBoundsException e) {}
                            }
                            if (plus+minus == 3) {
                                try {
                                    if (StiriVVrsto.panel.polja[a+plus][b+plus] == 0 && (b+plus+1 == StiriVVrsto.panel.ST || StiriVVrsto.panel.polja[a+plus][b+plus+1] != 0) && StiriVVrsto.panel.polja[a+plus+1][b+plus+1] == x)
                                        if (stolpec[a] == 4 || stolpec[a] == 3)
                                            stolpec[a] = 3;
                                        else
                                            stolpec[a] = 4;
                                } catch (IndexOutOfBoundsException e) {}
                                try {
                                    if (StiriVVrsto.panel.polja[a-minus][b-minus] == 0 && StiriVVrsto.panel.polja[a-minus][b-minus+1] != 0 && StiriVVrsto.panel.polja[a-minus-1][b-minus-1] == x)
                                        if (stolpec[a] == 4 || stolpec[a] == 3)
                                            stolpec[a] = 3;
                                        else
                                            stolpec[a] = 4;
                                } catch (IndexOutOfBoundsException e) {}
                            }
                            if (plus+minus == 2) {
                                try {
                                    if (StiriVVrsto.panel.polja[a+1][b+1] == 0 && (b+2 == StiriVVrsto.panel.ST || StiriVVrsto.panel.polja[a+1][b+2] != 0) && StiriVVrsto.panel.polja[a+2][b+2] == x && StiriVVrsto.panel.polja[a+3][b+3] == x)
                                        if (stolpec[a] == 4 || stolpec[a] == 3)
                                            stolpec[a] = 3;
                                        else
                                            stolpec[a] = 4;
                                } catch (IndexOutOfBoundsException e) {}
                                try {
                                    if (StiriVVrsto.panel.polja[a-1][b-1] == 0 && StiriVVrsto.panel.polja[a-1][b] != 0 && StiriVVrsto.panel.polja[a-2][b-2] == x && StiriVVrsto.panel.polja[a-3][b-3] == x)
                                        if (stolpec[a] == 4 || stolpec[a] == 3)
                                            stolpec[a] = 3;
                                        else
                                            stolpec[a] = 4;
                                } catch (IndexOutOfBoundsException e) {}
                            }
                        }
                        if (plus+minus > j) {
                            System.out.println(x + "++" + j + " " + plus + " " + minus + " " + i + " " + a + " " + b);
                            if ((stolpec[a] == 5 || stolpec[a] == 6) && i == 4)
                                stolpec[a] = 6;
                            else if (stolpec[a] == 4 && i == 3)
                                stolpec[a] = 3;
                            else if (stolpec[a] == 1 && i == 2 && j < 4)
                                stolpec[a] = 2;
                            else if ((stolpec[a] == 0 || (stolpec[a] > i && j==4)) && i != 3)
                                stolpec[a] = i;
                        }
                        
                        plus = 0;
                        minus = 0;
                        try {
                            do {
                                plus++;
                            } while (StiriVVrsto.panel.polja[a+plus][b-plus] == x);
                        } catch (IndexOutOfBoundsException e) {}
                        try {
                            do {
                                minus++;
                            } while (StiriVVrsto.panel.polja[a-minus][b+minus] == x);
                        } catch (IndexOutOfBoundsException e) {}
                        if (j < 4) {
                            plus2 = 0;
                            minus2 = 0;
                            try {
                                do {
                                    plus2++;
                                } while (StiriVVrsto.panel.polja[a+plus2][b-plus2] != -x);
                            } catch (IndexOutOfBoundsException e) {}
                            try {
                                do {
                                    minus2++;
                                } while (StiriVVrsto.panel.polja[a-minus2][b+minus2] != -x);
                            } catch (IndexOutOfBoundsException e) {}
                            if (plus2+minus2 <= 4) {
                                plus = 0;
                                minus = 0;
                            }
                        } else if (i == 1 && (stolpec[a] == 0 || stolpec[a] == 3 || stolpec[a] == 4)) {
                            if (plus+minus == 4) {
                                try {
                                    if (StiriVVrsto.panel.polja[a+plus][b-plus] == 0 && StiriVVrsto.panel.polja[a+plus][b-plus+1] != 0)
                                        if (stolpec[a] == 4 || stolpec[a] == 3)
                                            stolpec[a] = 3;
                                        else
                                            stolpec[a] = 4;
                                } catch (IndexOutOfBoundsException e) {}
                                try {
                                    if (StiriVVrsto.panel.polja[a-minus][b+minus] == 0 && (b+minus+1 == StiriVVrsto.panel.ST || StiriVVrsto.panel.polja[a-minus][b+minus+1] != 0))
                                        if (stolpec[a] == 4 || stolpec[a] == 3)
                                            stolpec[a] = 3;
                                        else
                                            stolpec[a] = 4;
                                } catch (IndexOutOfBoundsException e) {}
                            }
                            if (plus+minus == 3) {
                                try {
                                    if (StiriVVrsto.panel.polja[a+plus][b-plus] == 0 && StiriVVrsto.panel.polja[a+plus][b-plus+1] != 0 && StiriVVrsto.panel.polja[a+plus+1][b-plus+1] == x)
                                        if (stolpec[a] == 4 || stolpec[a] == 3)
                                            stolpec[a] = 3;
                                        else
                                            stolpec[a] = 4;
                                } catch (IndexOutOfBoundsException e) {}
                                try {
                                    if (StiriVVrsto.panel.polja[a-minus][b+minus] == 0 && (b+minus+1 == StiriVVrsto.panel.ST || StiriVVrsto.panel.polja[a-minus][b+minus+1] != 0) && StiriVVrsto.panel.polja[a-minus-1][b+minus-1] == x)
                                        if (stolpec[a] == 4 || stolpec[a] == 3)
                                            stolpec[a] = 3;
                                        else
                                            stolpec[a] = 4;
                                } catch (IndexOutOfBoundsException e) {}
                            }
                            if (plus+minus == 2) {
                                try {
                                    if (StiriVVrsto.panel.polja[a+1][b-1] == 0 && StiriVVrsto.panel.polja[a+1][b] != 0 && StiriVVrsto.panel.polja[a+2][b-2] == x && StiriVVrsto.panel.polja[a+3][b-3] == x)
                                        if (stolpec[a] == 4 || stolpec[a] == 3)
                                            stolpec[a] = 3;
                                        else
                                            stolpec[a] = 4;
                                } catch (IndexOutOfBoundsException e) {}
                                try {
                                    if (StiriVVrsto.panel.polja[a-1][b+1] == 0 && (b+2 == StiriVVrsto.panel.ST || StiriVVrsto.panel.polja[a-1][b+2] != 0) && StiriVVrsto.panel.polja[a-2][b+2] == x && StiriVVrsto.panel.polja[a-3][b+3] == x)
                                        if (stolpec[a] == 4 || stolpec[a] == 3)
                                            stolpec[a] = 3;
                                        else
                                            stolpec[a] = 4;
                                } catch (IndexOutOfBoundsException e) {}
                            }
                        }
                        if (plus+minus > j) {
                            System.out.println(x + "+-" + j + " " + plus + " " + minus + " " + i + " " + a + " " + b);
                            if ((stolpec[a] == 5 || stolpec[a] == 6) && i == 4)
                                stolpec[a] = 6;
                            else if (stolpec[a] == 4 && i == 3)
                                stolpec[a] = 3;
                            else if (stolpec[a] == 1 && i == 2 && j < 4)
                                stolpec[a] = 2;
                            else if ((stolpec[a] == 0 || (stolpec[a] > i && j==4)) && i != 3)
                                stolpec[a] = i;
                        }
                        
                        plus = b;
                        minus = b;
                        try {
                            do {
                                plus++;
                            } while (StiriVVrsto.panel.polja[a][plus] == x);
                        } catch (IndexOutOfBoundsException e) {}
                        try {
                            do {
                                minus--;
                            } while (StiriVVrsto.panel.polja[a][minus] == x);
                        } catch (IndexOutOfBoundsException e) {}
                        if (j < 4) {
                            plus2 = b;
                            minus2 = b;
                            try {
                                do {
                                    plus2++;
                                } while (StiriVVrsto.panel.polja[a][plus2] != -x);
                            } catch (IndexOutOfBoundsException e) {}
                            try {
                                do {
                                    minus2--;
                                } while (StiriVVrsto.panel.polja[a][minus2] != -x);
                            } catch (IndexOutOfBoundsException e) {}
                            if (plus2-minus2 <= 4) {
                                plus = 0;
                                minus = 0;
                            }
                        }
                        if (plus-minus > j) {
                            System.out.println(x + "+" + j + " " + plus + " " + minus + " " + i + " " + a + " " + b);
                            if ((stolpec[a] == 5 || stolpec[a] == 6) && i == 4)
                                stolpec[a] = 6;
                            else if (stolpec[a] == 4 && i == 3)
                                stolpec[a] = 3;
                            else if (stolpec[a] == 1 && i == 2 && j < 4)
                                stolpec[a] = 2;
                            else if ((stolpec[a] == 0 || (stolpec[a] > i && j==4)) && i != 3)
                                stolpec[a] = i;
                        }
                        
                        plus = a;
                        minus = a;
                        try {
                            do {
                                plus++;
                            } while (StiriVVrsto.panel.polja[plus][b] == x);
                        } catch (IndexOutOfBoundsException e) {}
                        try {
                            do {
                                minus--;
                            } while (StiriVVrsto.panel.polja[minus][b] == x);
                        } catch (IndexOutOfBoundsException e) {}
                        if (j < 4) {
                            plus2 = a;
                            minus2 = a;
                            try {
                                do {
                                    plus2++;
                                } while (StiriVVrsto.panel.polja[plus2][b] != -x);
                            } catch (IndexOutOfBoundsException e) {}
                            try {
                                do {
                                    minus2--;
                                } while (StiriVVrsto.panel.polja[minus2][b] != -x);
                            } catch (IndexOutOfBoundsException e) {}
                            if (plus2-minus2 <= 4) {
                                plus = 0;
                                minus = 0;
                            }
                    } else if (i == 1 && (stolpec[a] == 0 || stolpec[a] == 3 || stolpec[a] == 4)) {
                            if (plus-minus == 4) {
                                try {
                                    if (StiriVVrsto.panel.polja[plus][b] == 0 && (b+1 == StiriVVrsto.panel.ST || StiriVVrsto.panel.polja[plus][b+1] != 0))
                                        if (stolpec[a] == 4 || stolpec[a] == 3)
                                            stolpec[a] = 3;
                                        else
                                            stolpec[a] = 4;
                                } catch (IndexOutOfBoundsException e) {}
                                try {
                                    if (StiriVVrsto.panel.polja[minus][b] == 0 && (b+1 == StiriVVrsto.panel.ST || StiriVVrsto.panel.polja[minus][b+1] != 0))
                                        if (stolpec[a] == 4 || stolpec[a] == 3)
                                            stolpec[a] = 3;
                                        else
                                            stolpec[a] = 4;
                                } catch (IndexOutOfBoundsException e) {}
                            }
                            if (plus-minus == 3) {
                                try {
                                    if (StiriVVrsto.panel.polja[plus][b] == 0 && (b+1 == StiriVVrsto.panel.ST || StiriVVrsto.panel.polja[plus][b+1] != 0) && StiriVVrsto.panel.polja[plus+1][b] == x)
                                        if (stolpec[a] == 4 || stolpec[a] == 3)
                                            stolpec[a] = 3;
                                        else
                                            stolpec[a] = 4;
                                } catch (IndexOutOfBoundsException e) {}
                                try {
                                    if (StiriVVrsto.panel.polja[minus][b] == 0 && (b+1 == StiriVVrsto.panel.ST || StiriVVrsto.panel.polja[minus][b+1] != 0) && StiriVVrsto.panel.polja[minus-1][b] == x)
                                        if (stolpec[a] == 4 || stolpec[a] == 3)
                                            stolpec[a] = 3;
                                        else
                                            stolpec[a] = 4;
                                } catch (IndexOutOfBoundsException e) {}
                            }
                            if (plus-minus == 2) {
                                try {
                                    if (StiriVVrsto.panel.polja[plus][b] == 0 && (b+1 == StiriVVrsto.panel.ST || StiriVVrsto.panel.polja[plus][b+1] != 0) && StiriVVrsto.panel.polja[plus+1][b] == x && StiriVVrsto.panel.polja[plus+2][b] == x)
                                        if (stolpec[a] == 4 || stolpec[a] == 3)
                                            stolpec[a] = 3;
                                        else
                                            stolpec[a] = 4;
                                } catch (IndexOutOfBoundsException e) {}
                                try {
                                    if (StiriVVrsto.panel.polja[minus][b] == 0 && (b+1 == StiriVVrsto.panel.ST || StiriVVrsto.panel.polja[minus][b+1] != 0) && StiriVVrsto.panel.polja[minus-1][b] == x && StiriVVrsto.panel.polja[minus-2][b] == x)
                                        if (stolpec[a] == 4 || stolpec[a] == 3)
                                            stolpec[a] = 3;
                                        else
                                            stolpec[a] = 4;
                                } catch (IndexOutOfBoundsException e) {}
                            }
                        }
                        if (plus-minus > j) {
                            System.out.println(x + "-" + j + " " + plus + " " + minus + " " + i + " " + a + " " + b);
                            if ((stolpec[a] == 5 || stolpec[a] == 6) && i == 4)
                                stolpec[a] = 6;
                            else if (stolpec[a] == 4 && i == 3)
                                stolpec[a] = 3;
                            else if (stolpec[a] == 1 && i == 2 && j < 4)
                                stolpec[a] = 2;
                            else if ((stolpec[a] == 0 || (stolpec[a] > i && j==4)) && i != 3)
                                stolpec[a] = i;
                        }
                    }
                }
            }
            
            StringBuffer sb = new StringBuffer();
            for (int i=1; i <= (j == 1 ? 1 : 4); i++) {
                for (int a=0; a < StiriVVrsto.panel.ST; a++) {
                    if (stolpec[a] == i || (j == 1 && stolpec[a] < 5 && StiriVVrsto.panel.polja[a][0] == 0))
                        sb.append(a);
                }
                if (sb.length() == 0)
                    continue;
                
                x = sb.charAt((int)(Math.random()*sb.length()))-48;
                System.out.println(sb + " " + stolpec[x] + " " + j + " " + x);
                return new int[] {x, igraj(x)};
            }
        }
        
        do {
            x = (int)(Math.random()*StiriVVrsto.panel.ST);
        } while (StiriVVrsto.panel.polja[x][0] != 0);
        return new int[] {x, igraj(x)};
    }
}