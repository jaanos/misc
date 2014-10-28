import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LPP {
    static Proga[] pr;
    static File outputFile;
    static FileWriter out;
    static LPPOkno o = null;
    static LPPPanel panel = new LPPPanel();
    
    public static String[][] tokenize(Datoteka proge) {
        String[][] vrstice = new String[proge.lines()][];
        for (int i=0; i < proge.lines(); i++) {
            if (proge.vrstica(i).length() == 0 || proge.vrstica(i).trim().startsWith("//")) {
                vrstice[i] = new String[0];
                continue;
            }
            int v=1;
            for (int j=0; j < proge.vrstica(i).length(); j++)
                if (proge.vrstica(i).charAt(j) == ',')
                    v++;
            vrstice[i] = new String[v];
            StringBuffer sb = new StringBuffer();
            v = 0;
            for (int j=0; j < proge.vrstica(i).length(); j++) {
                if (proge.vrstica(i).charAt(j) == ',') {
                    vrstice[i][v] = sb.toString();
                    v++;
                    sb.setLength(0);
                } else
                    sb.append(proge.vrstica(i).charAt(j));
            }
            vrstice[i][v] = sb.toString();
        }
        return vrstice;
    }
    
    public static void main(String[] args) throws IOException {
        Datoteka proge = new Datoteka("LPP.txt");
        String[][] vrstice = tokenize(proge);
        
        int v=-1;
        for (int i=0; i < vrstice.length; i++) {
            switch (vrstice[i].length) {
                case 0:
                    continue;
                case 1:
                    if (v < 0)
                        pr = new Proga[Integer.parseInt(vrstice[i][0])];
                    else
                        pr[v].dodaj(vrstice[i][0]);
                    break;
                case 2:
                    pr[v].dodaj(vrstice[i][0], Integer.parseInt(vrstice[i][1]));
                    break;
                default:
                    v++;
                    pr[v] = new Proga(vrstice[i][0], vrstice[i][1], vrstice[i][2], Integer.parseInt(vrstice[i][3]));
            }
        }
        
        String zacetek;
        String konec;
        
        if (args.length == 1) {
            najdiProge(args[0]);
            return;
        }
        else if (args.length > 1) {
            zacetek = args[0];
            konec = args[1];
            try {
                outputFile = new File(args[2]);
                out = new FileWriter(outputFile);
            }
            catch (IndexOutOfBoundsException e) {}
            najdiPot(zacetek, konec);
        } else {
            //System.out.print("Vnesi zacetno postajo: ");
            //zacetek = BranjePodatkov.preberiString();
            //System.out.print("Vnesi koncno postajo: ");
            //konec = BranjePodatkov.preberiString();
            //System.out.println();
            o = new LPPOkno(400,500,"LPP",panel);
        }
        
        //najdiPot(zacetek, konec);
        
        if (out != null)
            out.close();
    }
    
    public static void najdiProge(String postaja) {
        boolean uspeh = false;
        for (int i=0; i < pr.length; i++)
            if (pr[i].najdi(postaja) > -1) {
                uspeh = true;
                String smer = (pr[i].getSmer(pr[i].najdi(postaja)) == 0 ? " <> " : (pr[i].getSmer(pr[i].najdi(postaja)) == 1 ? " > " : " < "));
                if (pr[i].najdi(postaja) == 0 || pr[i].najdi(postaja) == pr[i].getDolzina()-1)
                    izpisiln(pr[i] + "\r\n");
                else
                    izpisiln("Proga " + pr[i].getSt() + ": " + pr[i].getPostaja(0) + smer + postaja + smer + pr[i].getPostaja(pr[i].getDolzina()-1) + "\r\n");
            }
            
        if (!uspeh)
            izpisiln("Ne najdem postaje! Preveri, ce je njeno ime pravilno napisano.");
    }
    
    public static void najdiPot(String zacetek, String konec) {
        int i=2;
        while (!najdiPot(zacetek, konec, i)) {
            i++;
            if (i > 4) {
                izpisiln("Avtobusna povezava ne obstaja! Preveri, ce sta imeni postaj pravilno napisani.");
                return;
            }
        }
    }
    
    private static boolean najdiPot(String zacetek, String konec, Proga proga) {
        if (proga.najdi(zacetek) == -1 || proga.najdi(konec) == -1)
            return false;
        if (obstajaPot(zacetek, konec, proga)) {
            if (proga.najdi(zacetek) > proga.najdi(konec)) {
                izpisi("Proga " + proga.getSt() + " : " + zacetek);
                for (int j=proga.najdi(zacetek); j > proga.najdi(konec); j--)
                    if (proga.getSmer(j-1) < 1)
                        izpisi(" - " + proga.getPostaja(j-1));
            } else {
                izpisi("Proga " + proga.getSt() + " : " + zacetek);
                for (int j=proga.najdi(zacetek); j < proga.najdi(konec); j++)
                    if (proga.getSmer(j+1) > -1)
                        izpisi(" - " + proga.getPostaja(j+1));
            }
        }
        else {
            int i = (proga.najdi(zacetek) > proga.najdi(konec) ? 1 : -1);
            String nazaj = zacetek;
            String naprej = konec;
            if (proga.getSmer(proga.najdi(zacetek)) == i) {
                do {
                    nazaj = proga.getPostaja(proga.najdi(nazaj) + i);
                } while (proga.getSmer(proga.najdi(nazaj)) == i);
            }
            if (proga.getSmer(proga.najdi(konec)) == i) {
                do {
                    naprej = proga.getPostaja(proga.najdi(naprej) - i);
                } while (proga.getSmer(proga.najdi(naprej)) == i);
            }
            najdiPot(zacetek, nazaj, naprej, konec, proga);
        }
        return true;
    }
    
    private static void najdiPot(String zacetek, String nazaj, String naprej, String konec, Proga proga) {
        if (!zacetek.equals(nazaj)) {
            najdiPot(zacetek, nazaj, proga);
            if (proga.najdi(nazaj) == 0) {
                for (int j=proga.najdi(nazaj); j < proga.najdi(naprej); j++)
                    if (proga.getSmer(j+1) > -1)
                        izpisi(" - " + proga.getPostaja(j+1));
            } else if (proga.najdi(nazaj) == proga.getDolzina()-1) {
                for (int j=proga.najdi(nazaj); j > proga.najdi(naprej); j--)
                    if (proga.getSmer(j-1) < 1)
                        izpisi(" - " + proga.getPostaja(j-1));
            } else {
                izpisiln();
                najdiPot(nazaj, naprej, proga);
            }
        } else
            najdiPot(nazaj, naprej, proga);
        
        if (konec.equals(naprej))
            return;
            
        if (proga.najdi(naprej) == 0) {
            for (int j=proga.najdi(naprej); j < proga.najdi(konec); j++)
                if (proga.getSmer(j+1) > -1)
                    izpisi(" - " + proga.getPostaja(j+1));
        } else if (proga.najdi(naprej) == proga.getDolzina()-1) {
            for (int j=proga.najdi(naprej); j > proga.najdi(konec); j--)
                if (proga.getSmer(j-1) < 1)
                    izpisi(" - " + proga.getPostaja(j-1));
        } else {
            izpisiln();
            najdiPot(naprej, konec, proga);
        }
    }
    
    private static boolean obstajaPot(String zacetek, String konec, Proga proga) {
        if (proga.najdi(zacetek) > proga.najdi(konec))
            return (proga.getSmer(proga.najdi(zacetek)) < 1 && proga.getSmer(proga.najdi(konec)) < 1);
        else
            return (proga.getSmer(proga.najdi(zacetek)) > -1 && proga.getSmer(proga.najdi(konec)) > -1);
    }
    
    public static boolean najdiPot(String zacetek, String konec, int n) {
        if (zacetek.equals(konec))
            return false;
        boolean uspeh = false;
        //if (n > 1)
        //    najdiPot(zacetek, konec, n-1);
        switch (n) {
            case 1:
                for (int i=0; i < pr.length; i++)
                    if (najdiPot(zacetek, konec, pr[i])) {
                        izpisiln("\r\n");
                        uspeh = true;
                    }
                break;
            case 2:
                uspeh = najdiPot(zacetek, konec, 1);
                for (int i=0; i < pr.length; i++) {
                    for (int j=0; j < pr.length; j++) {
                        String[] sk = skupnaPostaja(pr[i], pr[j]);
                        if (i == j || sk.length == 0 || pr[i].najdi(zacetek) == -1 || pr[j].najdi(konec) == -1)
                            continue;
                        for (int k=0; k < sk.length; k++)
                            if (najdiPot(zacetek, sk[k], konec, pr[i], pr[j])) {
                                izpisiln("\r\n");
                                uspeh = true;
                            }
                    }            
                }
                break;
            default:
                Proga[] p = new Proga[n];
                String[] postaja = new String[n+1];
                postaja[0] = zacetek;
                postaja[n] = konec;
                for (int i=0; i < pr.length; i++) {
                    if (pr[i].najdi(zacetek) == -1)
                        continue;
                    p[0] = pr[i];
                    uspeh = najdiPot(p, postaja, 1) || uspeh;
                }
        }
        return uspeh;
    }
    
    private static boolean najdiPot(Proga[] p, String[] postaja, int n) {
        boolean uspeh = false;
        if (n == p.length) {
            try {
                int k = (p[n-2].najdi(postaja[n-2]) - p[n-2].najdi(postaja[n-1]) > 0 ? -1 : 1);
                int l = (p[n-1].najdi(postaja[n]) - p[n-1].najdi(postaja[n-1]) > 0 ? 1 : -1);
                if (p[n-2].getPostaja(p[n-2].najdi(postaja[n-1])-k).equals((p[n-1].getPostaja(p[n-1].najdi(postaja[n-1])+l))) && p[n-2].getSmer(p[n-2].najdi(postaja[n-1])-k) != -k && p[n-1].getSmer(p[n-1].najdi(postaja[n-1])+l) != -l)
                    return false;
            }
            catch(IndexOutOfBoundsException e) {}
            if (p[n-1].najdi(postaja[n]) > -1) {
                for (int i=0; i < n; i++) {
                    najdiPot(postaja[i], postaja[i+1], p[i]);
                    izpisiln();
                }
                izpisiln();
                return true;
            }
            return false;
        }
        
        for (int i=0; i < pr.length; i++) {
            p[n] = pr[i];
            String[] sk = skupnaPostaja(p[n-1], p[n]);
            if (p[n-1] == p[n] || sk.length == 0)
                continue;
            for (int j=0; j < sk.length; j++) {
                //if (!obstajaPot(postaja[n-1], sk[j], p[n-1]))
                //    continue;
                postaja[n] = sk[j];
                if (n > 1) {
                    try {
                        int k = (p[n-2].najdi(postaja[n-2]) - p[n-2].najdi(postaja[n-1]) > 0 ? -1 : 1);
                        int l = (p[n-1].najdi(postaja[n]) - p[n-1].najdi(postaja[n-1]) > 0 ? 1 : -1);
                        if (p[n-2].getPostaja(p[n-2].najdi(postaja[n-1])-k).equals((p[n-1].getPostaja(p[n-1].najdi(postaja[n-1])+l))) && p[n-2].getSmer(p[n-2].najdi(postaja[n-1])-k) != -k && p[n-1].getSmer(p[n-1].najdi(postaja[n-1])+l) != -l)
                            continue;
                    }
                    catch(IndexOutOfBoundsException e) {}
                }
                uspeh = najdiPot(p, postaja, n+1) || uspeh;
            }
        }
        return uspeh;
    }
        
    private static boolean najdiPot(String zacetek, String prestop, String konec, Proga p1, Proga p2) {
        try {
            int i = (p1.najdi(zacetek) - p1.najdi(prestop) > 0 ? -1 : 1);
            int j = (p2.najdi(konec) - p2.najdi(prestop) > 0 ? 1 : -1);
            if (p1.getPostaja(p1.najdi(prestop)-i).equals((p2.getPostaja(p2.najdi(prestop)+j))) && p1.getSmer(p1.najdi(prestop)-i) != -i && p2.getSmer(p2.najdi(prestop)+j) != -j)
                    return false;
        }
        catch(IndexOutOfBoundsException e) {}
        if (!zacetek.equals(prestop) && !prestop.equals(konec)) {
            najdiPot(zacetek, prestop, p1);
            izpisiln();
            najdiPot(prestop, konec, p2);
            return true;
        }
        return false;
    }
    
    private static String[] skupnaPostaja(Proga p1, Proga p2) {
        String[] postaja = new String[Math.min(p1.getDolzina(), p2.getDolzina())];
        if (p1 == p2) {
            for (int i=0; i < postaja.length; i++)
                postaja[i] = p1.getPostaja(i);
            return postaja;
        }
        int n=0;
        for (int i=0; i < p1.getDolzina(); i++)
            for (int j=0; j < p2.getDolzina(); j++)
                if (p1.getPostaja(i).equals(p2.getPostaja(j))) {
                    postaja[n] = p1.getPostaja(i);
                    n++;
                }
        String[] postaje = new String[n];
        for (int i=0; i < n; i++)
            postaje[i] = postaja[i];
        return postaje;
    }
    
    public static void izpisi(String s) {
        if (o != null) {
            panel.getText().setText(panel.getText().getText() + s);
            return;
        }
        System.out.print(s);
        if (out != null)
            try {
                out.write(s);
            } catch (IOException e) {}
    }
    
    public static void izpisiln(String s) {
        if (o != null) {
            panel.getText().setText(panel.getText().getText() + s + "\n");
            return;
        }
        System.out.println(s);
        if (out != null) {
            try {
                out.write(s);
                out.write("\r\n");
            } catch (IOException e) {}
        }
    }
    
    public static void izpisiln() {
        if (o != null) {
            panel.getText().setText(panel.getText().getText() + "\n");
            return;
        }
        System.out.println();
        if (out != null)
            try {
                out.write("\r\n");
            } catch (IOException e) {}
    }
}

class Proga {
    final String st;
    final String ime;
    String[] postaja;
    int[] smer;
    int n;
    
    public Proga(String st, String zacetek, String konec, int dolzina) {
        this.st = st;
        ime = zacetek + " - " + konec;
        postaja = new String[dolzina];
        smer = new int[dolzina];
        postaja[0] = zacetek;
        postaja[dolzina-1] = konec;
        n = 1;
    }
    
    public void dodaj(String p) {
        dodaj(p, 0);
    }
    
    public void dodaj(String p, int sm) {
        if (postaja.length-n <= 1)
            return;
        postaja[n] = p;
        smer[n] = sm;
        n++;
    }
    
    public int najdi(String p) {
        for (int i=0; i < postaja.length; i++) {
            if (postaja[i].equals(p))
                return i;
        }
        return -1;
    }
    
    public int najdi(String p, int sm) {
        for (int i=0; i < postaja.length; i++) {
            if (postaja[i].equals(p) && smer[i]==sm)
                return i;
        }
        return -1;
    }
        
    public String getPostaja(int n) {
        return postaja[n];
    }
    
    public int getSmer(int n) {
        return smer[n];
    }
    
    public String getSt() {
        return st;
    }
    
    public int getDolzina() {
        return postaja.length;
    }
    
    public String toString() {
        return "Proga " + st + ": " + ime;
    }
}

class LPPOkno extends JFrame {
	LPPOkno(int a,int b, String c, JPanel panel)
	{
		setSize(a,b);
		setTitle(c);
		Container vsebina=getContentPane();
		vsebina.add(panel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
		show();
	}
}

class LPPPanel extends JPanel implements ActionListener {
    private JTextField textz=new JTextField();
    private JTextField textk=new JTextField();
    private JButton najdiproge=new JButton("Najdi proge");
    private JButton najdipot=new JButton("Najdi pot");
    private JLabel labelz=new JLabel("Zaèetna postaja:");
    private JLabel labelk=new JLabel("Konèna postaja:");
    private JScrollPane scroll=new JScrollPane();
    private JTextPane text=new JTextPane();
    private String zadnjiz = "";
    private String zadnjik = "";
    private boolean proge = false;
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        najdiproge.setBounds(280,10,100,30);
        najdiproge.setBackground(Color.green);
        textz.setBounds(120,10,150,30);
        textk.setBounds(120,50,150,30);
        labelz.setBounds(10,10,100,30);
        labelk.setBounds(10,50,100,30);
        najdipot.setBackground(Color.green);
        najdipot.setBounds(280,50,100,30);
        scroll.setBounds(10,100,370,350);
        scroll.setViewportView(text);
        text.setEditable(false);
        text.setAutoscrolls(false);
        text.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        //setLayout(new java.awt.BorderLayout(0, 50));
        //text.setMaximumSize(new java.awt.Dimension(370, 350));
        //text.setText("Vpiši zaèetno in konèno postajo!");
        add(textz);
        add(textk);
        add(najdiproge);
        add(najdipot);
        add(labelz);
        add(labelk);
        add(scroll);
        najdiproge.addActionListener(this);
        najdipot.addActionListener(this);
	}
    public void actionPerformed(ActionEvent d) {
        Object izvor=d.getSource();
        if (izvor==najdiproge) {
            if ((proge && textz.getText().equals(zadnjiz)) || textz.getText().equals(""))
                return;
            proge = true;
            zadnjiz = textz.getText();
            text.setText("");
            LPP.najdiProge(zadnjiz);
        }
        if (izvor==najdipot) {
            if ((!proge && textz.getText().equals(zadnjiz) && textk.getText().equals(zadnjik)) || textz.getText().equals("") || textk.getText().equals(""))
                return;
            proge = false;
            zadnjiz = textz.getText();
            zadnjik = textk.getText();
            text.setText("");
            LPP.najdiPot(zadnjiz, zadnjik);
        }
    }
    
    public JTextPane getText() {
        return text;
    }
}