import java.io.*;

public class Datoteka {
    File input;
    FileReader in;
    StringBuffer sb;
    String[] vrstica;
    
    Datoteka(String file) {
        try {
            input = new File(file);
            in = new FileReader(input);
            sb = new StringBuffer();
            int c, v=1;
        
            while ((c = in.read()) != -1) {
                sb.append((char)c);
                if (c == 10)
                    v++;
            }
            
            vrstica = new String[v];
            c=0; v=0;
            for (int i=0; i < vrstica.length; i++) {
                try {
                    vrstica[i] = sb.substring(c, sb.indexOf("\n", c+1) - (sb.charAt(sb.indexOf("\n", c+1)-1) == '\r' ? 1 : 0));
                    c = sb.indexOf("\n", c+1)+1;
                } catch (IndexOutOfBoundsException e) {
                    vrstica[i] = sb.substring(c);
                }
            }
            
            in.close();
        }
        catch (Exception e) {
            vrstica = new String[0];
        }
    }
    
    public String toString() {
        return sb.toString();
    }
    
    public String vrstica(int i) {
        return vrstica[i];
    }
    
    public int lines() {
        return vrstica.length;
    }
    
    public int length() {
        return sb.length();
    }
}