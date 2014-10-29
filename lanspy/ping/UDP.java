import java.net.*;

public class UDP {
    byte[] tip, gr;
    String[] ime;
    static int id = 32768;
    
    public UDP(String ip) {
        try {
            id++;
            InetAddress addr = InetAddress.getByName(ip);
            String s = (char)((id/256)%128) + "" + (char)(id%256) +
                "\1\0\0\1" + 
                "\0\0\0\0\0\0 CKAAAAAAA" + 
                "AAAAAAAAAAAAAAAA" + 
                "AAAAAAA\0\0!\0\1";
            byte[] b = s.getBytes();
            byte[] data = new byte[256];
            DatagramPacket dp = new DatagramPacket(b, b.length, addr, 137);
            DatagramPacket rcv = new DatagramPacket(data, 256);
            DatagramSocket ds = new DatagramSocket();
            ds.setSoTimeout(50);
            ds.send(dp);
            ds.receive(rcv);
            int n = data[56];
            String str = new String(data, 57, data.length-57);
            tip = new byte[n];
            gr = new byte[n];
            ime = new String[n];
            //System.out.println("Naslov: " + rcv.getAddress());
            //System.out.println("Vrata: " + rcv.getPort());
            for (int i=0; i < n; i++) {
                /*String tip;
                switch (data[72 + 18*i]) {
                    case '\0':
                        if (data[73 + 18*i] < 0)
                            tip = "Domena";
                        else
                            tip = "Delovna postaja";
                        break;
                    case '\1':
                        if (data[73 + 18*i] < 0)
                            tip = "Glavni brskalnik";
                        else
                            tip = "Messenger";
                        break;
                    case '\3':
                        tip = "Messenger";
                        break;
                    case '\6':
                        tip = "RAS streznik";
                        break;
                    case '\u001B':
                        tip = "Glavni brskalnik domene";
                        break;
                    case '\u001C':
                        tip = "Nadzornik domene";
                        break;
                    case '\u001D':
                        tip = "Glavni brskalnik";
                        break;
                    case '\u001E':
                        tip = "Izvolitev glavnega brskalnika";
                        break;
                    case '\u001F':
                        tip = "NetDDE";
                        break;
                    case ' ':
                        tip = "Datotecni streznik";
                        break;
                    case '!':
                        tip = "RAS odjemalec";
                        break;
                    case '"':
                        tip = "MSMail Connector";
                        break;
                    case '#':
                        tip = "Exchange Store";
                        break;
                    case '$':
                        tip = "Exchange Directory";
                        break;
                    case '0':
                        tip = "Modemski streznik";
                        break;
                    case '1':
                        tip = "Modemski odejmalec";
                        break;
                    default:
                        tip = "Ostalo";
                } */
                tip[i] = data[72 + 18*i];
                gr[i] = data[73 + 18*i];
                ime[i] = str.substring(18*i, 18*i + 15).trim();
            }
        } catch (Exception e) {}
    }
    
    public String getNetName() {
        return decode(najdi((byte)0, false, 1));
    }
    
    public String getDomainName() {
        return decode(najdi((byte)0, true, 1));
    }
    
    public String getUser() {
        String user;
        if (najdi((byte)3, false, 1) != null && najdi((byte)3, false, 1).equals(getNetName()))
            user = najdi((byte)3, false, 2);
        else
            user = najdi((byte)3, false, 1);
        if (user == null)
            return null;
        StringBuffer sb = new StringBuffer(decode(user));
        for (int i=3; najdi((byte)3, false, i) != null; i++)
            if (!najdi((byte)3, false, i).equals(getNetName()))
                sb.append("; " + decode(najdi((byte)3, false, i)));
        return sb.toString();
    }
    
    public boolean isFileServer() {
        return najdi((byte)32, false, 1) != null;
    }
    
    public boolean hasMessenger() {
        return najdi((byte)3, false, 1) != null;
    }
    
    public String najdi(byte b, boolean g, int n) {
        int k=0;
        for (int i=0; i < ime.length; i++) {
            if (tip[i] == b && (gr[i] < 0) == g) {
                k++;
                if (k == n)
                    return ime[i];
            }
        }
        return null;
    }
    
    public String decode(String str) {
        //return str.replace('¬', 'È').replace('æ', 'Š').replace('¦', 'Ž').replace('', 'Æ').replace('Ñ', 'Ð');
	return str;
    }
}

class TestUDP {
    public static void main(String[] args) {
        UDP comp;
        try {
            comp = new UDP(args[0]);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Navedi IP!");
            return;
        }
        
        try {
            System.out.println("Ime: " + comp.getNetName());
            System.out.println("Domena: " + comp.getDomainName());
            System.out.println("Uporabnik: " + (comp.getUser() == null ? "N/A" : comp.getUser()));
            System.out.println("Deljenje datotek: " + (comp.isFileServer() ? "ja" : "ne"));
            System.out.println("Neposredno sporocanje: " + (comp.hasMessenger() ? "ja" : "ne"));
        } catch (NullPointerException e) {
            //e.printStackTrace();
            System.out.println("Ni odgovora z naslova " + args[0]);
        }
    }
}
