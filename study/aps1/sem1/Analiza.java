import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class Analiza {
    public static void main(String[] args) {
        JFrame o = new JFrame();
        o.setTitle("Analiza besedil");
        o.setSize(500,500);
        o.setDefaultCloseOperation(o.EXIT_ON_CLOSE);
        o.getContentPane().add(new AnalizaPanel());
        o.setVisible(true);
    }
}

class AnalizaPanel extends JPanel implements ActionListener {
    private JFileChooser fc = new JFileChooser();
    private JScrollPane scroll = new JScrollPane();
    private JTextPane text = new JTextPane();
        
    private Color black = new Color(0,0,0);
    private LineBorder border = new LineBorder(black);
        
    private JButton stat1 = new JButton("Statistika besedila");
    private JButton stat2 = new JButton("Statistika besedila");
    private JButton statU = new JButton("Statistika unije");
    private JButton statI = new JButton("Statistika preseka");
    private JButton stat1o = new JButton("Statistika razlike");
    private JButton stat2o = new JButton("Statistika razlike");
    private JButton open1 = new JButton("Naloži datoteko . . .");
    private JButton open2 = new JButton("Naloži datoteko . . .");
    private JLabel file1 = new JLabel();
    private JLabel file2 = new JLabel();
    private JLabel num = new JLabel("Število izpisanih besed:");
    private JTextField words = new JTextField("30");
    private JCheckBox sort = new JCheckBox("Sortiraj znake po pogostosti");
    private JCheckBox nonletters = new JCheckBox("Upoštevaj tudi ločila in ostale nečrkovne znake");
    private JButton clear = new JButton("Pobriši");
        
    private TreeNumberedSet<Character, Integer> tns1c;
    private TreeNumberedSet<Character, Integer> tns2c;
    private TreeNumberedSet<String, Integer> tns1s;
    private TreeNumberedSet<String, Integer> tns2s;
    StringComparator sComp = new StringComparator(false);
    CharComparator cComp = new CharComparator(false);
    IntCounter counter = new IntCounter();
    
    public AnalizaPanel() {
        stat1.addActionListener(this);
        statU.addActionListener(this);
        stat2.addActionListener(this);
        stat1o.addActionListener(this);
        statI.addActionListener(this);
        stat2o.addActionListener(this);
        open1.addActionListener(this);
        open2.addActionListener(this);
        clear.addActionListener(this);
        nonletters.addActionListener(this);
        stat1.setEnabled(false);
        statU.setEnabled(false);
        stat2.setEnabled(false);
        stat1o.setEnabled(false);
        statI.setEnabled(false);
        stat2o.setEnabled(false);
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        stat1.setBounds(5, getHeight() - 95, getWidth()/3 - 5, 20);
        statU.setBounds(getWidth()/3, getHeight() - 95, getWidth()/3, 20);
        stat2.setBounds(2*getWidth()/3, getHeight() - 95, getWidth()/3 - 5, 20);
        stat1o.setBounds(5, getHeight() - 70, getWidth()/3 - 5, 20);
        statI.setBounds(getWidth()/3, getHeight() - 70, getWidth()/3, 20);
        stat2o.setBounds(2*getWidth()/3, getHeight() - 70, getWidth()/3 - 5, 20);
        open1.setBounds(5, getHeight() - 45, getWidth()/2 - 5, 20);
        open2.setBounds(getWidth()/2, getHeight() - 45, getWidth()/2 - 5, 20);
        scroll.setBounds(5, 5, getWidth() - 10, getHeight() - 150);
        scroll.setViewportView(text);
        text.setBorder(border);
        text.setEditable(false);
        text.setAutoscrolls(false);
        file1.setBounds(5, getHeight() - 25, getWidth()/2 - 5, 20);
        file1.setHorizontalAlignment(file1.CENTER);
        file2.setBounds(getWidth()/2, getHeight() - 25, getWidth()/2 - 5, 20);
        file2.setHorizontalAlignment(file2.CENTER);
        clear.setBounds(getWidth() - 80, getHeight() - 140, 75, 20);
        num.setBounds(5, getHeight() - 140, 140, 20);
        words.setBounds(145, getHeight() - 140, 30, 20);
        words.setHorizontalAlignment(words.RIGHT);
        sort.setBounds(getWidth()/2 - 45, getHeight() - 140, 184, 20);
        nonletters.setBounds(getWidth()/2 - 45, getHeight() - 120, 290, 20);
        add(scroll);
        add(stat1);
        add(statU);
        add(stat2);
        add(stat1o);
        add(statI);
        add(stat2o);
        add(open1);
        add(open2);
        add(file1);
        add(file2);
        add(clear);
        add(num);
        add(words);
        add(sort);
        add(nonletters);
    }
    
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        long t;
        int w;
        try {
            w = Integer.parseInt(words.getText());
        } catch (NumberFormatException x) {
            w = 30;
        }
        if (o == open1) {
            if (fc.showOpenDialog(this) == fc.APPROVE_OPTION) {
                tns1c = new TreeNumberedSet<Character, Integer>(cComp, counter);
                tns1s = new TreeNumberedSet<String, Integer>(sComp, counter);
                File file = fc.getSelectedFile();
                file1.setText(file.getName());
                t = Branje.readFile(file, tns1c, tns1s, nonletters.isSelected());
                println("Branje datoteke " + file1.getText());
                println("Porabljeni čas: " + t + " ms\n");
                stat1.setEnabled(true);
                if (tns2s != null) {
                    statI.setEnabled(true);
                    stat1o.setEnabled(true);
                    stat2o.setEnabled(true);
                    if (tns2c != null) {
                        statU.setEnabled(true);
                    }
                }
            }
        } else if (o == open2) {
            if (fc.showOpenDialog(this) == fc.APPROVE_OPTION) {
                tns2c = new TreeNumberedSet<Character, Integer>(cComp, counter);
                tns2s = new TreeNumberedSet<String, Integer>(sComp, counter);
                File file = fc.getSelectedFile();
                file2.setText(file.getName());
                t = Branje.readFile(file, tns2c, tns2s, nonletters.isSelected());
                println("Branje datoteke " + file2.getText());
                println("Porabljeni čas: " + t + " ms\n");
                stat2.setEnabled(true);
                if (tns1s != null) {
                    statI.setEnabled(true);
                    stat1o.setEnabled(true);
                    stat2o.setEnabled(true);
                    if (tns1c != null) {
                        statU.setEnabled(true);
                    }
                }
            }
        } else if (o == stat1) {
            if (tns1s == null || tns1c == null) {
                println("Napaka: datoteka ni naložena.\n");
                return;
            }
            
            println("Statistika za " + file1.getText());
            println("Skupaj črk: " + tns1c.count());
            println("Skupaj besed: " + tns1s.count());
            println("Različnih besed: " + tns1s.size());
            
            charStats(tns1c, sort.isSelected());
            wordStats(tns1s, w);
            println();
        } else if (o == stat2) {
            if (tns2s == null || tns2c == null) {
                println("Napaka: datoteka ni naložena.\n");
                return;
            }
            
            println("Statistika za " + file2.getText());
            println("Skupaj črk: " + tns2c.count());
            println("Skupaj besed: " + tns2s.count());
            println("Različnih besed: " + tns2s.size());
            
            charStats(tns2c, sort.isSelected());
            wordStats(tns2s, w);
            println();
        } else if (o == statU) {
            if (tns1s == null || tns1c == null || tns2s == null || tns2c == null) {
                println("Napaka: naloženi morata biti obe datoteki.\n");
                return;
            }
            
            t = System.currentTimeMillis();
            TreeNumberedSet<Character,Integer> tnsC = tns2c.copy();    //O(n log n)
            tnsC.addAll(tns1c);                                     //O(m n) - majhna m, n!
            TreeNumberedSet<String,Integer> tnsS = tns2s.copy();    //O(n log n)
            tnsS.addAll(tns1s);
            
            println("Unija besedil");
            println("Porabljeni čas: " + (System.currentTimeMillis() - t) + " ms\n");
            
            println("Statistika unije besedil");
            println("Skupaj črk: " + tnsC.count());
            println("Skupaj besed: " + tnsS.count());
            println("Različnih besed: " + tnsS.size());
            
            charStats(tnsC, sort.isSelected());
            wordStats(tnsS, w);
            println();
        } else if (o == stat1o) {
            if (tns1s == null || tns2s == null) {
                println("Napaka: naloženi morata biti obe datoteki.\n");
                return;
            }
            
            t = System.currentTimeMillis();
            TreeNumberedSet<String,Integer> tnsS = tns1s.copy();        //O(n log n)
            tnsS.removeAllAll(tns2s);                                   //O(m log n)
            
            println("Razlika besedil");
            println("Porabljeni čas: " + (System.currentTimeMillis() - t) + " ms\n");
            
            println("Besede, ki se pojavljajo samo v " + file1.getText());
            println("Skupaj besed: " + tnsS.count());
            println("Različnih besed: " + tnsS.size());
            
            wordStats(tnsS, w);
            println();
        } else if (o == stat2o) {
            if (tns1s == null || tns2s == null) {
                println("Napaka: naloženi morata biti obe datoteki.\n");
                return;
            }
            
            t = System.currentTimeMillis();
            TreeNumberedSet<String,Integer> tnsS = tns2s.copy();        //O(n log n)
            tnsS.removeAllAll(tns1s);                                   //O(m log n)
            
            println("Razlika besedil");
            println("Porabljeni čas: " + (System.currentTimeMillis() - t) + " ms\n");
            
            println("Besede, ki se pojavljajo samo v " + file2.getText());
            println("Skupaj besed: " + tnsS.count());
            println("Različnih besed: " + tnsS.size());
            
            wordStats(tnsS, w);
            println();
        } else if (o == statI) {
            if (tns1s == null || tns2s == null) {
                println("Napaka: naloženi morata biti obe datoteki.\n");
                return;
            }
            
            t = System.currentTimeMillis();
            TreeNumberedSet<String,Integer> tnsS = tns1s.copy();    //O(n log n)
            tnsS.intersection(tns2s);                           //O(n log m)
            
            println("Presek besedil");
            println("Porabljeni čas: " + (System.currentTimeMillis() - t) + " ms\n");
            
            println("Statistika preseka besedil");
            println("Skupaj besed: " + tnsS.count());
            println("Različnih besed: " + tnsS.size());
            
            wordStats(tnsS, w);
            println();
        } else if (o == nonletters) {
            if (tns1s != null || tns1c != null || tns2s != null || tns2c != null) {
                println("Ločila in ostali nečrkovni znaki se" + (nonletters.isSelected() ? "" : " ne") + " bodo upoštevali ob naslednjem nalaganju datotek.\n");
            }
        } else if (o == clear) {
            text.setText("");
        }
        repaint();
    }
    
    private void print(String str) {
        text.setText(text.getText() + str);
    }
    
    private void println() {
        print("\n");
    }
    
    private void println(String str) {
        print(str + "\n");
    }
    
    private void charStats(TreeNumberedSet<Character, Integer> tns, boolean sort) {
        println("\nStatistika znakov:");
        NumberedSetIterator<Character, Integer> it = (sort ? tns.numberedSetIteratorByValue(false) : tns.numberedSetIterator());
        int val, chars = tns.count();
        char c;
        while (it.hasNext()) {
            c = it.next();
            println(" " + uniCode(c) + "\t" + (val = it.value()) + "\t(" + Math.round(10000.0*val/chars)/100.0 + " %)");
        }
    }
    
    private static String uniCode(char c) {
        if (c == '\u0119') return "\u00EA";
        if (c == '\u0155') return "\u00E0";
        if (c >= 32) return c + "";
        StringBuilder sb = new StringBuilder(Integer.toString(c,16));
        while (sb.length() < 4) {
            sb.insert(0,'0');
        }
        sb.insert(0,"U+");
        return sb.toString();
    }
    
    //O(1) (limit!)
    private void wordStats(TreeNumberedSet<String, Integer> tns, int limit) {
        println("\nStatistika besed:");
        NumberedSetIterator<String, Integer> it = tns.numberedSetIteratorByValue(false);
        int val, i = 0, words = tns.count();
        while (it.hasNext() && i++ < limit) {
            println(" " + it.next() + "\t" + (val = it.value()) + "\t(" + Math.round(10000.0*val/words)/100.0 + " %)");
        }
    }
}