import java.io.*;
import java.util.*;

public class Branje {
    static TreeNumberedSet<Character, Integer> tns1c;
    static TreeNumberedSet<Character, Integer> tns2c;
    static TreeNumberedSet<String, Integer> tns1s;
    static TreeNumberedSet<String, Integer> tns2s;
    
    static int le1;
    static int le2;
    static int be1;
    static int be2;
    
    static {
        try {
            FileReader le = new FileReader("LittleEndian.txt");
            le1 = le.read();
            le2 = le.read();
            le.close();
            FileReader be = new FileReader("BigEndian.txt");
            be1 = be.read();
            be2 = be.read();
            be.close();
        } catch (IOException e) {
            System.err.println("Napaka: " + e.getMessage());
            System.err.println("Uporabljajo se privzete vrednosti!");
            le1 = 255;
            le2 = 254;
            be1 = 254;
            be2 = 255;
        }
    }
    
    //O(n log n)
    public static void main(String[] args) {
        try {
            long start = System.currentTimeMillis();
            FileWriter out = new FileWriter("out.txt");
            out.write(le1);
            out.write(le2);
            StringComparator sComp = new StringComparator(false);
            CharComparator cComp = new CharComparator(false);
            IntCounter counter = new IntCounter();
            tns1c = new TreeNumberedSet<Character, Integer>(cComp, counter);
            tns2c = new TreeNumberedSet<Character, Integer>(cComp, counter);
            tns1s = new TreeNumberedSet<String, Integer>(sComp, counter);
            tns2s = new TreeNumberedSet<String, Integer>(sComp, counter);
            TreeNumberedSet<Character, Integer> tnsC;
            TreeNumberedSet<String, Integer> tnsS;
            long t;
            if (args.length >= 1) {
                writeln("\r\nBranje " + args[0], out, true);
                writeln("Porabljen čas: " + readFile(args[0], tns1c, tns1s, true) + " ms", out, true);  //O(n log n)
                writeln("Skupaj črk: " + tns1c.count(), out, true);
                writeln("Skupaj besed: " + tns1s.count(), out, true);
                writeln("Različnih besed: " + tns1s.size(), out, true);
                
                charStats(tns1c, out);
                wordStats(tns1s, 30, out);
            }
            if (args.length >= 2) {
                writeln("\r\nBranje " + args[1], out, true);
                writeln("Porabljen čas: " + readFile(args[1], tns2c, tns2s, true) + " ms", out, true);  //O(n log n)
                writeln("Skupaj črk: " + tns2c.count(), out, true);
                writeln("Skupaj besed: " + tns2s.count(), out, true);
                writeln("Različnih besed: " + tns2s.size(), out, true);
                
                charStats(tns2c, out);
                wordStats(tns2s, 30, out);
                
                t = System.currentTimeMillis();
                tnsC = tns2c.copy();    //O(n log n)
                tnsC.addAll(tns1c);     //O(m log n)
                tnsS = tns2s.copy();    //O(n log n)
                tnsS.addAll(tns1s);     //O(m log n)
                
                writeln("\r\nUnija obeh besedil", out, true);
                writeln("Porabljen čas: " + (System.currentTimeMillis() - t) + " ms", out, true);
                writeln("Skupaj črk: " + tnsC.count(), out, true);
                writeln("Skupaj besed: " + tnsS.count(), out, true);
                writeln("Različnih besed: " + tnsS.size(), out, true);
                
                charStats(tnsC, out);
                wordStats(tnsS, 40, out);
                
                t = System.currentTimeMillis();
                tnsS = tns1s.copy();    //O(n log n)
                tnsS.intersection(tns2s);  //O(n log m)
                
                writeln("\r\nPresek obeh besedil", out, true);
                writeln("Porabljen čas: " + (System.currentTimeMillis() - t) + " ms", out, true);
                writeln("Skupaj besed: " + tnsS.count(), out, true);
                writeln("Različnih besed: " + tnsS.size(), out, true);
                
                wordStats(tnsS, 30, out);
                
                t = System.currentTimeMillis();
                tnsS = tns1s.copy();        //O(n log n)
                tnsS.removeAllAll(tns2s);   //O(m log n)
                
                writeln("\r\nBesede, ki se pojavljajo samo v " + args[0], out, true);
                writeln("Porabljen čas: " + (System.currentTimeMillis() - t) + " ms", out, true);
                writeln("Skupaj besed: " + tnsS.count(), out, true);
                writeln("Različnih besed: " + tnsS.size(), out, true);
                
                wordStats(tnsS, 20, out);
                
                t = System.currentTimeMillis();
                tnsS = tns2s.copy();        //O(n log n)
                tnsS.removeAllAll(tns1s);   //O(m log n)
                
                writeln("\r\nBesede, ki se pojavljajo samo v " + args[1], out, true);
                writeln("Porabljen čas: " + (System.currentTimeMillis() - t) + " ms", out, true);
                writeln("Skupaj besed: " + tnsS.count(), out, true);
                writeln("Različnih besed: " + tnsS.size(), out, true);
                
                wordStats(tnsS, 20, out);
            }
            writeln("\nSkupni čas: " + (System.currentTimeMillis() - start) + " ms", out, true);
            out.close();
        } catch (IOException e) {
            System.err.println("Napaka pri pisanju v datoteko: " + e.getMessage());
        }
    }
    
    //O(n) (n je majhen) - praktično O(1)
    private static void charStats(TreeNumberedSet<Character, Integer> tns, FileWriter out) {
        writeln("\r\nStatistika znakov:", out, false);
        NumberedSetIterator<Character, Integer> it = tns.numberedSetIterator();
        int val, chars = tns.count();
        char c;
        while (it.hasNext()) {
            c = it.next();
            writeln(uniCode(c) + "\t" + (val = it.value()) + "\t(" + Math.round(10000.0*val/chars)/100.0 + " %)", out, false);
        }
    }
    
    private static String uniCode(char c) {
        if (c >= 32) return c + "";
        StringBuilder sb = new StringBuilder(Integer.toString(c,16));
        while (sb.length() < 4) {
            sb.insert(0,'0');
        }
        sb.insert(0,"U+");
        return sb.toString();
    }
    
    //O(1) (limit!)
    private static void wordStats(TreeNumberedSet<String, Integer> tns, int limit, FileWriter out) {
        writeln("\r\nStatistika besed:", out, false);
        NumberedSetIterator<String, Integer> it = tns.numberedSetIteratorByValue(false);
        int val, i = 0, words = tns.count();
        while (it.hasNext() && i++ < limit) {
            writeln(it.next() + "\t" + (val = it.value()) + "\t(" + Math.round(10000.0*val/words)/100.0 + " %)", out, false);
        }
    }
    
    private static void writeln(String str, FileWriter out, boolean scr) {
        try {
            int n;
            for (int i=0; i < str.length(); i++) {
                n = str.charAt(i);
                if (n == 281 || n == 341) {
                    out.write(n);
                    out.write(0);
                } else {
                    out.write(n%256);
                    out.write(n/256);
                }
            }
            out.write("\r\0\n\0");
        } catch (IOException e) {
            System.err.println("Napaka pri pisanju v datoteko: " + e.getMessage());
        }
        if (scr) {
            System.out.println(str.replace('č','c').replace('Č','C').replace('š','s').replace('Š','S').replace('ž','z').replace('Ž','Z'));
        }
    }
    
    //O(n log n)
    public static long readFile(String file, TreeNumberedSet<Character, Integer> tnsc, TreeNumberedSet<String, Integer> tnss, boolean nonletters) {
        return readFile(new File(file), tnsc, tnss, nonletters);
    }
    
    //O(n log n)
    public static long readFile(File file, TreeNumberedSet<Character, Integer> tnsc, TreeNumberedSet<String, Integer> tnss, boolean nonletters) {
        long t = System.currentTimeMillis();
        try {
            StringBuilder sb = new StringBuilder();
            FileReader in = new FileReader(file);
            char c;
            int m, n;
            m = in.read();
            n = in.read();
            //System.out.println(m+" "+n);
            boolean bigendian = (m == be1 && n == be2);
            if (!bigendian && (m != le1 || n != le2)) {
                if (m > -1) {
                    c = (char)m;
                    c = Character.toLowerCase(c);
                    if (c != '\r' && c != '\n' && (nonletters || Character.isLetter(c))) {
                        tnsc.add(c);                                        //O(log n)
                    }
                    if (Character.isLetter(c)) {
                        sb.append(c);
                    } else if (sb.length() > 0) {
                        tnss.add(sb.toString());                            //O(log n)
                        sb.setLength(0);
                    }
                    
                    if (n > -1) {
                        c = (char)n;
                        c = Character.toLowerCase(c);
                        if (c != '\r' && c != '\n' && (nonletters || Character.isLetter(c))) {
                            tnsc.add(c);                                        //O(log n)
                        }
                        if (Character.isLetter(c)) {
                            sb.append(c);
                        } else if (sb.length() > 0) {
                            tnss.add(sb.toString());                            //O(log n)
                            sb.setLength(0);
                        }
                        
                        while ((m = in.read()) > -1) {  //O(n log n)
                            c = (char)m;
                            c = Character.toLowerCase(c);
                            if (c != '\r' && c != '\n' && (nonletters || Character.isLetter(c))) {
                                tnsc.add(c);                                        //O(log n)
                            }
                            if (Character.isLetter(c)) {
                                sb.append(c);
                            } else if (sb.length() > 0) {
                                tnss.add(sb.toString());                            //O(log n)
                                sb.setLength(0);
                            }
                        }
                    }
                }
            } else {
                while ((m = in.read()) > -1 && (n = in.read()) > -1) {  //O(n log n)
                    c = (char)(bigendian ? n+256*m : m+256*n);
                    c = Character.toLowerCase(c);
                    if (c != '\r' && c != '\n' && (nonletters || Character.isLetter(c))) {
                        tnsc.add(c);                                        //O(log n)
                    }
                    if (Character.isLetter(c)) {
                        sb.append(c);
                    } else if (sb.length() > 0) {
                        tnss.add(sb.toString());                            //O(log n)
                        sb.setLength(0);
                    }
                }
            }
            if (sb.length() > 0) {
                tnss.add(sb.toString().toLowerCase());                  //O(log n)
            }
        } catch (FileNotFoundException e) {
            System.err.println("Ne najdem datoteke: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Napaka pri branju datoteke: " + e.getMessage());
        }
        return System.currentTimeMillis() - t;
    }
}