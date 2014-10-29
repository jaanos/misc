import java.io.*;
import java.util.*;

public class Branje {
    public static void read(File f, Tocka[][] t, double[] mm) {
        try {
            double x, y, z;
            boolean first = true;
            BufferedReader in = new BufferedReader(new FileReader(f));
            String str;
            String[] xyz;
            while ((str = in.readLine()) != null) {
                xyz = str.split(" ");
                if (xyz.length < 3) continue;
                try {
                    x = Double.parseDouble(xyz[0]);
                    y = Double.parseDouble(xyz[1]);
                    z = Double.parseDouble(xyz[xyz.length-1]);
                } catch (NumberFormatException e) {
                    continue;
                }
                try {
                    if (t[(int)(x/IPPanel.stepX-IPPanel.offsetX)][(int)(y/IPPanel.stepY-IPPanel.offsetY)] != null) continue;
                    if (first) {
                        mm[0] = z;
                        mm[1] = z;
                        first = false;
                    } else {
                        if (z < mm[0]) mm[0] = z;
                        if (z > mm[1]) mm[1] = z;
                    }
                    t[(int)(x/IPPanel.stepX-IPPanel.offsetX)][(int)(y/IPPanel.stepY-IPPanel.offsetY)] = new Tocka(x, y, z);
                } catch (ArrayIndexOutOfBoundsException e) {}
            }
        } catch (IOException e) {
            System.err.println("Napaka: " + e);
        }
    }
}