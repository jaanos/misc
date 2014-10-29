import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Briskola {
    public static void main(String[] args) {
        new BriskolaOkno();
    }
}

class BriskolaOkno extends JFrame {
    public BriskolaOkno() {
        setTitle("Briškola");
        setSize(600,600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setIconImage(Toolkit.getDefaultToolkit().getImage("icon.gif"));
        getContentPane().add(new BriskolaPanel());
        show();
    }
}

class BriskolaPanel extends JPanel implements MouseListener, ActionListener {
    static final int ST_IGR = 4;
    static final int SIZE_X = 63;
    static final int SIZE_Y = 140;
    static final String barve = "bcds";
    static final String stSStirkami = "2456789031";
    static final String stBrezStirk = "256789031";
    static final int[] vrSStirkami = {0, 0, 0, 0, 0, 2, 3, 4, 10, 11};
    static final int[] vrBrezStirk = {0, 0, 0, 0, 2, 3, 4, 10, 11};
    static String st = stSStirkami;
    static int[] vr = vrSStirkami;
    Karta[] karta;
    KupKart kup = new KupKart();
    KupKart[] pobrano = new KupKart[2];
    Karta[][] roka = new Karta[ST_IGR][3];
    Karta[] menjava = new Karta[3];
    Karta[] dol = new Karta[ST_IGR];
    Karta briskola;
    boolean igram;
    int prvi = -1;
    int zacne;
    int stKart = 3;
    int[] igral = new int[ST_IGR];
    
    ImageIcon back = new ImageIcon("back.gif");
    JLabel lBriskola = new JLabel();
    JLabel lBack = new JLabel(back);
    JLabel lBack2 = new JLabel(back);
    JLabel[] lRoka = new JLabel[3];
    JLabel[] lDol = new JLabel[ST_IGR];
    JLabel tvoji = new JLabel();
    JLabel njihovi = new JLabel();
    JButton nova = new JButton("Nova igra");
    Timer t;
    
    public BriskolaPanel() {
        String ime;
        karta = new Karta[barve.length()*st.length()];
        for (int i=0; i < lRoka.length; i++) {
            lRoka[i] = new JLabel();
            lRoka[i].addMouseListener(this);
        }
        for (int i=0; i < lDol.length; i++)
            lDol[i] = new JLabel();
        for (int i=0; i < barve.length(); i++)
            for (int j=0; j < st.length(); j++) {
                ime = barve.charAt(i) + "" + st.charAt(j);
                karta[i*st.length()+j] = new Karta(ime, new ImageIcon(ime + ".gif"));
            }
        lBriskola.setVerticalAlignment(SwingConstants.BOTTOM);
        nova.addActionListener(this);
        nova();
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.WHITE);
        for (int i=0; i < lRoka.length; i++)
            if (roka[0][i] != null) {
                lRoka[i].setIcon(roka[0][i].getSlika());
                lRoka[i].setBounds(150 + (SIZE_X+27)*i,400,SIZE_X,SIZE_Y);
                add(lRoka[i]);
            } else
                lRoka[i].setIcon(null);
        lDol[0].setBounds(240,250,SIZE_X,SIZE_Y);
        lDol[1].setBounds(330,175,SIZE_X,SIZE_Y);
        lDol[2].setBounds(240,100,SIZE_X,SIZE_Y);
        lDol[3].setBounds(150,175,SIZE_X,SIZE_Y);
        for (int i=0; i < lDol.length; i++)
            if (dol[i] != null) {
                lDol[i].setIcon(dol[i].getSlika());
                add(lDol[i]);
            } else
                lDol[i].setIcon(null);
        if (kup.size() > 0) {
            lBriskola.setIcon(briskola.getSlika());
            lBack.setIcon(back);
            lBack2.setIcon(back);
            lBriskola.setBounds(450,SIZE_X+10,SIZE_X,3*SIZE_Y/5);
            lBack.setBounds(450-(SIZE_Y-SIZE_X)/2,10,SIZE_Y,SIZE_X);
            lBack2.setBounds(448-(SIZE_Y-SIZE_X)/2,8,SIZE_Y,SIZE_X);
            add(lBriskola);
            if (kup.size > ST_IGR)
                add(lBack2);
            else
                remove(lBack2);
            add(lBack);
        } else {
            //lBriskola.setIcon(null);
            //lBack.setIcon(null);
            remove(lBriskola);
            remove(lBack);
        }
        tvoji.setBounds(450, 100, 150, 30);
        njihovi.setBounds(450, 130, 150, 30);
        nova.setBounds(450,400,120,30);
        add(tvoji);
        add(njihovi);
        add(nova);
    }
    
    private void nova() {
        try {
            t.stop();
        } catch (NullPointerException e) {}
        tvoji.setText("");
        njihovi.setText("");
        pobrano[0] = new KupKart();
        pobrano[1] = new KupKart();
        for (int i=0; i < dol.length; i++)
            dol[i] = null;
        prvi++;
        prvi %= ST_IGR;
        zacne = prvi;
        stKart = 3;
        deli();
        igraj(zacne);
        repaint();
    }
    
    private void deli() {
        Karta tmp;
        kup = new KupKart();
        while (kup.size() < karta.length) {
            tmp = karta[(int)(Math.random()*karta.length)];
            if (kup.search(tmp) == -1)
                kup.push(tmp);
        }
        for (int i=0; i < roka.length; i++)
            for (int j=0; j < roka[i].length; j++)
                roka[(zacne+i)%ST_IGR][j] = kup.popCard();
        briskola = kup.bottom();
    }
    
    public void actionPerformed(ActionEvent e) {
        nova();
    }
        
    public void mouseReleased(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
        
    public void mouseClicked(MouseEvent e) {
        if (!igram)
            return;
        JLabel izvor = (JLabel)e.getSource();
        for (int i=0; i < roka[0].length; i++)
            if (izvor == lRoka[i]) {
                igraj(0,i);
                igram = false;
                break;
            }
    }
    
    private void igraj(final int n, int i) {
        dol[n] = roka[n][i];
        roka[n][i] = null;
        igral[n] = i;
        try {
            t.stop();
        } catch (NullPointerException e) {}
        if ((n+1)%ST_IGR != zacne) {
            int time = 2000;
            if ((n+1)%ST_IGR == 0)
                time = 100;
            t = new Timer(time, new ActionListener() {
                public void actionPerformed(ActionEvent d) {
                    igraj((n+1)%ST_IGR);
                }
            });
        } else
            t = new Timer(2000, new ActionListener() {
                public void actionPerformed(ActionEvent d) {
                    preveri();
                }
            });
        
        t.setRepeats(false);
        t.start();
        repaint();
    }
    
    private void igraj(int n) {
        if (n == 0) {
            igram = true;
            return;
        }
        
        int[] pri = new int[stKart];
        char br = briskola.getBarva();
        char prvaBarva = br;
        if (n != zacne)
            prvaBarva = dol[zacne].getBarva();
        int m = -1;
        /*for (int i=0; i < 3; i++)
            try {
                System.out.print(roka[n][i].getIme() + " ");
            } catch (NullPointerException e) {}
            System.out.println();*/

        int punti=0;
        for (int i=0; i < ST_IGR; i++)
            if (dol[i] != null)
                punti += vr[st.indexOf(dol[i].getSt())];
        //System.out.println(punti);
        Karta briskolaDol = null;
        Karta pobereDol = null;
        for (int i=0; i < ST_IGR; i++)
            try {
                if (dol[i].getBarva() == br && (briskolaDol == null || st.indexOf(briskolaDol.getSt()) < st.indexOf(dol[i].getSt()))) {
                    briskolaDol = dol[i];
                    break;
                }
            } catch (NullPointerException e) {}
        for (int i=0; i < ST_IGR; i++)
            try {
                if (dol[i].getBarva() == prvaBarva && (pobereDol == null || st.indexOf(pobereDol.getSt()) < st.indexOf(dol[i].getSt()))) {
                    pobereDol = dol[i];
                    break;
                }
            } catch (NullPointerException e) {}
        boolean zadnja = kup.size() == ST_IGR && punti <= vr[st.indexOf(briskola.getSt())];
        
        boolean nePoberejo = n != zacne;
        try {
            String vecje = vIgri(briskolaDol == null ? pobereDol.getBarva() : br);
            for (int i=vecje.indexOf(briskolaDol == null ? pobereDol.getSt() : briskolaDol.getSt())+1; i < vecje.length(); i++) {
                Karta vecja = karta[barve.indexOf(briskolaDol == null ? pobereDol.getBarva() : br)*st.length() + st.indexOf(vecje.charAt(i))];
                if (!vRoki(n, vecja) && (kup.size() > 0 || !vRoki((n+2)%ST_IGR, vecja))) {
                    nePoberejo = false;
                    break;
                }
            }
        } catch (NullPointerException e) {}
                
        for (int i=0; i < 3; i++) {
            if (roka[n][i] == null)
                continue;
            m++;
            for (int j=0; j <= m; j++) {
                if (j == m) {
                    pri[m] = i;
                    break;
                }
                boolean vstavi = false;
        
                if ((n+1)%ST_IGR == zacne) {
                    if (pobere() == (n+2)%ST_IGR) {
                        if (roka[n][i].getBarva() != br && (roka[n][pri[j]].getBarva() == br || vr[st.indexOf(roka[n][i].getSt())] > vr[st.indexOf(roka[n][pri[j]].getSt())] || (vr[st.indexOf(roka[n][i].getSt())]+vr[st.indexOf(roka[n][pri[j]].getSt())] == 0 && st.indexOf(roka[n][i].getSt()) < st.indexOf(roka[n][pri[j]].getSt())))) {
                            vstavi = true;
                        } else if (roka[n][i].getBarva() == br) {
                            String vigri = vIgri(br);
                            if (vr[st.indexOf(roka[n][i].getSt())] == 0 && roka[n][pri[j]].getBarva() == br && st.indexOf(roka[n][i].getSt()) < st.indexOf(roka[n][pri[j]].getSt())) {
                                vstavi = true;
                            } else if (vr[st.indexOf(roka[n][i].getSt())] > vr[st.indexOf(roka[n][pri[j]].getSt())]) {
                                switch (vigri.indexOf(roka[n][i].getSt()) - vigri.indexOf(roka[n][pri[j]].getSt())) {
                                    case 1:
                                        vstavi = true;
                                        break;
                                    case 2:
                                        Karta vmes = karta[barve.indexOf(br)*st.length() + st.indexOf(vigri.charAt(vigri.indexOf(roka[n][i].getSt())-1))];
                                        vstavi = vRoki(n, vmes) || (kup.size() == 0 && vRoki((n+2)%ST_IGR, vmes));
                                        break;
                                    case 3:
                                        Karta vmes1 = karta[barve.indexOf(br)*st.length() + st.indexOf(vigri.charAt(vigri.indexOf(roka[n][i].getSt())-1))];
                                        Karta vmes2 = karta[barve.indexOf(br)*st.length() + st.indexOf(vigri.charAt(vigri.indexOf(roka[n][i].getSt())-2))];
                                        vstavi = kup.size() == 0 && ((vRoki(n, vmes1) && vRoki((n+2)%ST_IGR, vmes2)) || (vRoki(n, vmes2) && vRoki((n+2)%ST_IGR, vmes1)));
                                        break;
                                }
                            }
                        }
                    } else {
                        if (!zadnja && briskolaDol == null && roka[n][i].getBarva() == prvaBarva && st.indexOf(roka[n][i].getSt()) > st.indexOf(pobereDol.getSt())) {
                            if (roka[n][pri[j]].getBarva() != prvaBarva || st.indexOf(roka[n][i].getSt()) < st.indexOf(roka[n][pri[j]].getSt())) {
                                vstavi = true;
                            }
                        } else if (!zadnja && punti >= 5) {
                            if (roka[n][i].getBarva() == br && (punti >= 10 || st.indexOf(roka[n][i].getSt()) < st.length()-3) && (briskolaDol != null ? st.indexOf(roka[n][i].getSt()) > st.indexOf(briskolaDol.getSt()) : roka[n][pri[j]].getBarva() != prvaBarva || st.indexOf(roka[n][pri[j]].getSt()) < st.indexOf(pobereDol.getSt()))) {
                                String vigri = vIgri(br);
                                if (roka[n][pri[j]].getBarva() != br || (vr[st.indexOf(roka[n][i].getSt())]+vr[st.indexOf(roka[n][pri[j]].getSt())] == 0 && st.indexOf(roka[n][i].getSt()) < st.indexOf(roka[n][pri[j]].getSt()))) {
                                    vstavi = true;
                                } else if (vr[st.indexOf(roka[n][i].getSt())] > vr[st.indexOf(roka[n][pri[j]].getSt())]) {
                                    switch (vigri.indexOf(roka[n][i].getSt()) - vigri.indexOf(roka[n][pri[j]].getSt())) {
                                        case 1:
                                            vstavi = true;
                                            break;
                                        case 2:
                                            Karta vmes = karta[barve.indexOf(br)*st.length() + st.indexOf(vigri.charAt(vigri.indexOf(roka[n][i].getSt())-1))];
                                            vstavi = vRoki(n, vmes) || (kup.size() == 0 && vRoki((n+2)%ST_IGR, vmes));
                                            break;
                                        case 3:
                                            Karta vmes1 = karta[barve.indexOf(br)*st.length() + st.indexOf(vigri.charAt(vigri.indexOf(roka[n][i].getSt())-1))];
                                            Karta vmes2 = karta[barve.indexOf(br)*st.length() + st.indexOf(vigri.charAt(vigri.indexOf(roka[n][i].getSt())-2))];
                                            vstavi = kup.size() == 0 && ((vRoki(n, vmes1) && vRoki((n+2)%ST_IGR, vmes2)) || (vRoki(n, vmes2) && vRoki((n+2)%ST_IGR, vmes1)));
                                            break;
                                    }
                                }
                            } else if (roka[n][i].getBarva() == br ? briskolaDol != null && roka[n][pri[j]].getBarva() == br && st.indexOf(roka[n][pri[j]].getSt()) < st.indexOf(briskolaDol.getSt()) && st.indexOf(roka[n][i].getSt()) < st.indexOf(roka[n][pri[j]].getSt()) : (punti < 10 && st.indexOf(roka[n][pri[j]].getSt()) >= st.length()-3) || (briskolaDol == null ? roka[n][pri[j]].getBarva() != br && st.indexOf(roka[n][pri[j]].getSt()) < st.indexOf(pobereDol.getSt()) && st.indexOf(roka[n][i].getSt()) < st.indexOf(roka[n][pri[j]].getSt()) : (roka[n][pri[j]].getBarva() == br ? st.indexOf(roka[n][pri[j]].getSt()) < st.indexOf(briskolaDol.getSt()) : st.indexOf(roka[n][i].getSt()) < st.indexOf(roka[n][pri[j]].getSt())))) {
                                vstavi = true;
                            }
                        } else {
                            if (roka[n][i].getBarva() != br && ((roka[n][pri[j]].getBarva() == br && (vr[st.indexOf(roka[n][i].getSt())] < 10 || vr[st.indexOf(roka[n][pri[j]].getSt())] >= 10)) || st.indexOf(roka[n][i].getSt()) < st.indexOf(roka[n][pri[j]].getSt()))) {
                                vstavi = true;
                            } else if (roka[n][i].getBarva() == br) {
                                String vigri = vIgri(br);
                                if ((vr[st.indexOf(roka[n][i].getSt())] == 0 && roka[n][pri[j]].getBarva() == br && st.indexOf(roka[n][i].getSt()) < st.indexOf(roka[n][pri[j]].getSt())) || (vr[st.indexOf(roka[n][i].getSt())] < 10 && vr[st.indexOf(roka[n][pri[j]].getSt())] >= 10)) {
                                    vstavi = true;
                                } else if (vr[st.indexOf(roka[n][i].getSt())] > vr[st.indexOf(roka[n][pri[j]].getSt())]) {
                                    switch (vigri.indexOf(roka[n][i].getSt()) - vigri.indexOf(roka[n][pri[j]].getSt())) {
                                        case 1:
                                            vstavi = true;
                                            break;
                                        case 2:
                                            Karta vmes = karta[barve.indexOf(br)*st.length() + st.indexOf(vigri.charAt(vigri.indexOf(roka[n][i].getSt())-1))];
                                            vstavi = vRoki(n, vmes) || (kup.size() == 0 && vRoki((n+2)%ST_IGR, vmes));
                                            break;
                                        case 3:
                                            Karta vmes1 = karta[barve.indexOf(br)*st.length() + st.indexOf(vigri.charAt(vigri.indexOf(roka[n][i].getSt())-1))];
                                            Karta vmes2 = karta[barve.indexOf(br)*st.length() + st.indexOf(vigri.charAt(vigri.indexOf(roka[n][i].getSt())-2))];
                                            vstavi = kup.size() == 0 && ((vRoki(n, vmes1) && vRoki((n+2)%ST_IGR, vmes2)) || (vRoki(n, vmes2) && vRoki((n+2)%ST_IGR, vmes1)));
                                            break;
                                    }
                                }
                            }
                        }
                    }
                } else if ((n+2)%ST_IGR == zacne) {
                    if (pobere() == (n+3)%ST_IGR) {
                        if (!zadnja && briskolaDol == null && roka[n][i].getBarva() == prvaBarva && st.indexOf(roka[n][i].getSt()) > st.indexOf(pobereDol.getSt())) {
                            if (roka[n][pri[j]].getBarva() != prvaBarva || st.indexOf(roka[n][i].getSt()) < st.indexOf(roka[n][pri[j]].getSt())) {
                                vstavi = true;
                            }
                        } else if (!zadnja && punti >= 5) {
                            if (roka[n][i].getBarva() == br && (punti >= 10 || st.indexOf(roka[n][i].getSt()) < st.length()-5) && (briskolaDol != null ? st.indexOf(roka[n][i].getSt()) > st.indexOf(briskolaDol.getSt()) : roka[n][pri[j]].getBarva() != prvaBarva || st.indexOf(roka[n][pri[j]].getSt()) < st.indexOf(pobereDol.getSt()))) {
                                String vigri = vIgri(br);
                                if (roka[n][pri[j]].getBarva() != br || (vr[st.indexOf(roka[n][i].getSt())]+vr[st.indexOf(roka[n][pri[j]].getSt())] == 0 && st.indexOf(roka[n][i].getSt()) < st.indexOf(roka[n][pri[j]].getSt()))) {
                                    vstavi = true;
                                } else if (vr[st.indexOf(roka[n][i].getSt())] > vr[st.indexOf(roka[n][pri[j]].getSt())]) {
                                    switch (vigri.indexOf(roka[n][i].getSt()) - vigri.indexOf(roka[n][pri[j]].getSt())) {
                                        case 1:
                                            vstavi = true;
                                            break;
                                        case 2:
                                            Karta vmes = karta[barve.indexOf(br)*st.length() + st.indexOf(vigri.charAt(vigri.indexOf(roka[n][i].getSt())-1))];
                                            vstavi = vRoki(n, vmes) || (kup.size() == 0 && vRoki((n+2)%ST_IGR, vmes));
                                            break;
                                        case 3:
                                            Karta vmes1 = karta[barve.indexOf(br)*st.length() + st.indexOf(vigri.charAt(vigri.indexOf(roka[n][i].getSt())-1))];
                                            Karta vmes2 = karta[barve.indexOf(br)*st.length() + st.indexOf(vigri.charAt(vigri.indexOf(roka[n][i].getSt())-2))];
                                            vstavi = kup.size() == 0 && ((vRoki(n, vmes1) && vRoki((n+2)%ST_IGR, vmes2)) || (vRoki(n, vmes2) && vRoki((n+2)%ST_IGR, vmes1)));
                                            break;
                                    }
                                }
                            } else if (roka[n][i].getBarva() == br ? briskolaDol != null && roka[n][pri[j]].getBarva() == br && st.indexOf(roka[n][pri[j]].getSt()) < st.indexOf(briskolaDol.getSt()) && st.indexOf(roka[n][i].getSt()) < st.indexOf(roka[n][pri[j]].getSt()) : (punti < 10 && st.indexOf(roka[n][pri[j]].getSt()) >= st.length()-5) || (briskolaDol == null ? roka[n][pri[j]].getBarva() != br && st.indexOf(roka[n][pri[j]].getSt()) < st.indexOf(pobereDol.getSt()) && st.indexOf(roka[n][i].getSt()) < st.indexOf(roka[n][pri[j]].getSt()) : (roka[n][pri[j]].getBarva() == br ? st.indexOf(roka[n][pri[j]].getSt()) < st.indexOf(briskolaDol.getSt()) : st.indexOf(roka[n][i].getSt()) < st.indexOf(roka[n][pri[j]].getSt())))) {
                                vstavi = true;
                            }
                        } else {
                            if (roka[n][i].getBarva() != br && ((roka[n][pri[j]].getBarva() == br && (vr[st.indexOf(roka[n][i].getSt())] < 10 || vr[st.indexOf(roka[n][pri[j]].getSt())] >= 10)) || st.indexOf(roka[n][i].getSt()) < st.indexOf(roka[n][pri[j]].getSt()))) {
                                vstavi = true;
                            } else if (roka[n][i].getBarva() == br) {
                                String vigri = vIgri(br);
                                if ((vr[st.indexOf(roka[n][i].getSt())] == 0 && roka[n][pri[j]].getBarva() == br && st.indexOf(roka[n][i].getSt()) < st.indexOf(roka[n][pri[j]].getSt())) || (vr[st.indexOf(roka[n][i].getSt())] < 10 && vr[st.indexOf(roka[n][pri[j]].getSt())] >= 10)) {
                                    vstavi = true;
                                } else if (vr[st.indexOf(roka[n][i].getSt())] > vr[st.indexOf(roka[n][pri[j]].getSt())]) {
                                    switch (vigri.indexOf(roka[n][i].getSt()) - vigri.indexOf(roka[n][pri[j]].getSt())) {
                                        case 1:
                                            vstavi = true;
                                            break;
                                        case 2:
                                            Karta vmes = karta[barve.indexOf(br)*st.length() + st.indexOf(vigri.charAt(vigri.indexOf(roka[n][i].getSt())-1))];
                                            vstavi = vRoki(n, vmes) || (kup.size() == 0 && vRoki((n+2)%ST_IGR, vmes));
                                            break;
                                        case 3:
                                            Karta vmes1 = karta[barve.indexOf(br)*st.length() + st.indexOf(vigri.charAt(vigri.indexOf(roka[n][i].getSt())-1))];
                                            Karta vmes2 = karta[barve.indexOf(br)*st.length() + st.indexOf(vigri.charAt(vigri.indexOf(roka[n][i].getSt())-2))];
                                            vstavi = kup.size() == 0 && ((vRoki(n, vmes1) && vRoki((n+2)%ST_IGR, vmes2)) || (vRoki(n, vmes2) && vRoki((n+2)%ST_IGR, vmes1)));
                                            break;
                                    }
                                }
                            }
                        }
                    } else if (nePoberejo) {
                        if (roka[n][i].getBarva() != br && (roka[n][pri[j]].getBarva() == br || vr[st.indexOf(roka[n][i].getSt())] > vr[st.indexOf(roka[n][pri[j]].getSt())] || (vr[st.indexOf(roka[n][i].getSt())]+vr[st.indexOf(roka[n][pri[j]].getSt())] == 0 && st.indexOf(roka[n][i].getSt()) < st.indexOf(roka[n][pri[j]].getSt())))) {
                            vstavi = true;
                        } else if (roka[n][i].getBarva() == br) {
                            String vigri = vIgri(br);
                            if (vr[st.indexOf(roka[n][i].getSt())] == 0 && roka[n][pri[j]].getBarva() == br && st.indexOf(roka[n][i].getSt()) < st.indexOf(roka[n][pri[j]].getSt())) {
                                vstavi = true;
                            } else if (vr[st.indexOf(roka[n][i].getSt())] > vr[st.indexOf(roka[n][pri[j]].getSt())]) {
                                switch (vigri.indexOf(roka[n][i].getSt()) - vigri.indexOf(roka[n][pri[j]].getSt())) {
                                    case 1:
                                        vstavi = true;
                                        break;
                                    case 2:
                                        Karta vmes = karta[barve.indexOf(br)*st.length() + st.indexOf(vigri.charAt(vigri.indexOf(roka[n][i].getSt())-1))];
                                        vstavi = vRoki(n, vmes) || (kup.size() == 0 && vRoki((n+2)%ST_IGR, vmes));
                                        break;
                                    case 3:
                                        Karta vmes1 = karta[barve.indexOf(br)*st.length() + st.indexOf(vigri.charAt(vigri.indexOf(roka[n][i].getSt())-1))];
                                        Karta vmes2 = karta[barve.indexOf(br)*st.length() + st.indexOf(vigri.charAt(vigri.indexOf(roka[n][i].getSt())-2))];
                                        vstavi = kup.size() == 0 && ((vRoki(n, vmes1) && vRoki((n+2)%ST_IGR, vmes2)) || (vRoki(n, vmes2) && vRoki((n+2)%ST_IGR, vmes1)));
                                        break;
                                }
                            }
                        }
                    } else if (punti >= 10) {
                        if (briskolaDol == null) {
                            if (roka[n][i].getBarva() == br && st.indexOf(roka[n][i].getSt()) < st.length()-2) {
                                String vigri = vIgri(br);
                                if (roka[n][pri[j]].getBarva() != br || (vr[st.indexOf(roka[n][i].getSt())]+vr[st.indexOf(roka[n][pri[j]].getSt())] == 0 && st.indexOf(roka[n][i].getSt()) > st.indexOf(roka[n][pri[j]].getSt()))) {
                                    vstavi = true;
                                } else if (vr[st.indexOf(roka[n][i].getSt())] > vr[st.indexOf(roka[n][pri[j]].getSt())]) {
                                    switch (vigri.indexOf(roka[n][i].getSt()) - vigri.indexOf(roka[n][pri[j]].getSt())) {
                                        case 1:
                                            vstavi = true;
                                            break;
                                        case 2:
                                            Karta vmes = karta[barve.indexOf(br)*st.length() + st.indexOf(vigri.charAt(vigri.indexOf(roka[n][i].getSt())-1))];
                                            vstavi = vRoki(n, vmes) || (kup.size() == 0 && vRoki((n+2)%ST_IGR, vmes));
                                            break;
                                        case 3:
                                            Karta vmes1 = karta[barve.indexOf(br)*st.length() + st.indexOf(vigri.charAt(vigri.indexOf(roka[n][i].getSt())-1))];
                                            Karta vmes2 = karta[barve.indexOf(br)*st.length() + st.indexOf(vigri.charAt(vigri.indexOf(roka[n][i].getSt())-2))];
                                            vstavi = kup.size() == 0 && ((vRoki(n, vmes1) && vRoki((n+2)%ST_IGR, vmes2)) || (vRoki(n, vmes2) && vRoki((n+2)%ST_IGR, vmes1)));
                                            break;
                                    }
                                }
                            } else if (roka[n][pri[j]].getBarva() != br && (st.indexOf(roka[n][i].getSt()) < st.length()-2 ? (roka[n][i].getBarva() == pobereDol.getBarva() ? roka[n][pri[j]].getBarva() != pobereDol.getBarva() || st.indexOf(roka[n][i].getSt()) > st.indexOf(roka[n][pri[j]].getSt()) : roka[n][pri[j]].getBarva() != pobereDol.getBarva() && (vr[st.indexOf(roka[n][i].getSt())] + vr[st.indexOf(roka[n][pri[j]].getSt())] == 0 ? st.indexOf(roka[n][i].getSt()) < st.indexOf(roka[n][pri[j]].getSt()) : st.indexOf(roka[n][i].getSt()) > st.indexOf(roka[n][pri[j]].getSt()))) : st.indexOf(roka[n][pri[j]].getSt()) >= st.length()-2 && (st.indexOf(roka[n][i].getSt()) < st.indexOf(roka[n][pri[j]].getSt()) || (roka[n][i].getBarva() == pobereDol.getBarva() && st.indexOf(roka[n][i].getSt()) == st.indexOf(roka[n][pri[j]].getSt()))))) {
                                vstavi = true;
                            }
                        } else {
                            
                        }
                    } else {
                        
                    }
                }
                if (vstavi) {
                    for (int k=m; k > j; k--)
                        pri[k] = pri[k-1];
                    pri[j] = i;
                    break;
                }
            }
        }
        
        if ((n+1)%ST_IGR == zacne || (n+2)%ST_IGR == zacne) {
            /*for (int i=0; i < stKart; i++)
                System.out.print(roka[n][pri[i]].getIme() + " ");
            System.out.println();*/
            igraj(n, pri[0]);
            return;
        }
        
        int i;
        do {
            i = (int)(3*Math.random());
        } while (roka[n][i] == null);
        igraj(n, i);
    }
    
    private String vIgri(char b) {
        StringBuffer sb = new StringBuffer();
        for (int i=0; i < st.length(); i++) {
            if (pobrano[0].search(karta[barve.indexOf(b)*st.length() + i])*pobrano[1].search(karta[barve.indexOf(b)*st.length() + i]) < 0 || (stKart == 3 && briskola.equals(karta[barve.indexOf(b)*st.length() + i])))
                sb.append(st.charAt(i));
        }
        return sb.toString();
    }
    
    private boolean vRoki(int n, Karta k) {
        for (int i=0; i < 3; i++)
            try {
                if (roka[n][i].equals(k))
                    return true;
            } catch (NullPointerException e) {}
        return false;
    }
    
    private int pobere() {
        int prime = zacne;
        for (int i=1; i < ST_IGR; i++)
            try {
                if (dol[prime].getBarva() == dol[(zacne+i)%ST_IGR].getBarva()) {
                    if (st.indexOf(dol[prime].getSt()) < st.indexOf(dol[(zacne+i)%ST_IGR].getSt()))
                        prime = (zacne+i)%ST_IGR;
                } else if (dol[(zacne+i)%ST_IGR].getBarva() == briskola.getBarva())
                    prime = (zacne+i)%ST_IGR;
            } catch (NullPointerException e) {}
        return prime;
    }
    
    private void preveri() {
        int prime = pobere();
        for (int i=0; i < dol.length; i++) {
            pobrano[prime%2].push(dol[i]);
            dol[i] = null;
        }
        if (kup.size() > 0) {
            for (int i=0; i < ST_IGR; i++)
                roka[(prime+i)%ST_IGR][igral[(prime+i)%ST_IGR]] = kup.popCard();
            if (kup.size() == 0) {
                remove(lBriskola);
                remove(lBack);
            }
        } else {
            stKart--;
        }

        if (stKart == 0)
            prestej();
        else if (kup.size() == 0 && stKart == 3) {
            for (int i=0; i < menjava.length; i++) {
                menjava[i] = roka[0][i];
                roka[0][i] = roka[2][i];
            }
            zacne = prime;
            final int go = prime;
            try {
                t.stop();
            } catch (NullPointerException e) {}
            t = new Timer(5000, new ActionListener() {
                public void actionPerformed(ActionEvent d) {
                    for (int i=0; i < menjava.length; i++)
                        roka[0][i] = menjava[i];
                    tvoji.setText("");
                    repaint();
                    igraj(go);
                }
            });
            t.setRepeats(false);
            t.start();
            tvoji.setText("Tvoja ekipa: " + prestej(0));
        } else {
            zacne = prime;
            igraj(prime);
        }
        repaint();
    }
    
    private void prestej() {
        tvoji.setText("Tvoja ekipa: " + prestej(0));
        njihovi.setText("Nasprotna ekipa: " + prestej(1));
    }
    
    private int prestej(int n) {
        Karta[] kupcek = pobrano[n].toCardArray();
        int tocke = 0;
        for (int i=0; i < kupcek.length; i++)
            tocke += vr[st.indexOf(kupcek[i].getSt())];
        return tocke;
    }
}

class Karta {
    String ime;
    ImageIcon slika;
    
    public Karta(String ime, ImageIcon slika) {
        this.ime = ime;
        this.slika = slika;
    }
    
    public String getIme() {
        return ime;
    }
    
    public ImageIcon getSlika() {
        return slika;
    }
    
    public char getBarva() {
        return ime.charAt(0);
    }
    
    public char getSt() {
        return ime.charAt(1);
    }
    
    public boolean equals(Karta k) {
        return ime == k.ime;
    }
}