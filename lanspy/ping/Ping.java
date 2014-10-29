import java.io.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class Ping {
    
    public static Host[] h;
    static final boolean NTF_JOIN = true;
    static final boolean NTF_NEW = true;
    
    public static String[][] tokenize(Datoteka dat, char c) {
        String[][] vrstice = new String[dat.lines()][];
        for (int i=0; i < dat.lines(); i++) {
            if (dat.vrstica(i).trim().length() == 0) {
                vrstice[i] = new String[0];
                continue;
            }
            int v=1;
            for (int j=0; j < dat.vrstica(i).trim().length(); j++)
                if (dat.vrstica(i).trim().charAt(j) == c && dat.vrstica(i).trim().charAt(j+1) != c)
                    v++;
            vrstice[i] = new String[v];
            StringBuffer sb = new StringBuffer();
            v = 0;
            for (int j=0; j < dat.vrstica(i).trim().length(); j++) {
                if (dat.vrstica(i).trim().charAt(j) == c) {
                    if (j+1 < dat.vrstica(i).trim().length() && dat.vrstica(i).trim().charAt(j+1) == c)
                        continue;
                    vrstice[i][v] = sb.toString();
                    v++;
                    sb.setLength(0);
                } else
                    sb.append(dat.vrstica(i).trim().charAt(j));
            }
            vrstice[i][v] = sb.toString();
        }
        return vrstice;
    }
    
    public static void main(String[] args) throws IOException {
        Datoteka hosts = new Datoteka("hosts.dat");
        String[][] vrstice = tokenize(hosts, ',');
        h = new Host[vrstice.length - (vrstice[vrstice.length-1].length == 0 ? 1 : 0)];

        for (int i=0; i < h.length; i++)
            h[i] = new Host(vrstice[i][0], vrstice[i][1], vrstice[i][8], vrstice[i][2], vrstice[i][9].equals("1"), vrstice[i][3], vrstice[i][4], vrstice[i][5], vrstice[i][6].equals("1"), vrstice[i][7].equals("1"));
        
        if (args.length > 0 && args[0].equals("-c"))
            check();
        else
            window();
    }

    public static void check() {
        boolean[] up = new boolean[h.length];
        Datoteka ping = new Datoteka("Ping.dat");
        String lastUp = ping.vrstica(0).replace('Ÿ', 'è') + ' ' + ping.vrstica(1);
        Datoteka arp = new Datoteka("Arp.dat");
        Host[] h2 = new Host[arp.lines()-4];
        String[][] vrstice = tokenize(arp, ' ');
        int n=h.length;
        String myip = vrstice[1][1];
        boolean amin = false;
        for (int i=0; i < h.length; i++) {
            if (!h[i].getIP().equals(myip)) {
                up[i] = h[i].getUp();
                h[i].setUp(false);
            } else {
                up[i] = true;
                h[i].setUp(true);
                h[i].getNetData();
                amin = true;
            }
        }
        for (int i=0; i < h2.length; i++) {
            h2[i] = new Host(vrstice[i+3][0], vrstice[i+3][1], lastUp, vrstice[i+3][0], vrstice[i+3][2].equals("dynamic"));
            if (h2[i].getUp()) {
                n++;
                for (int j=0; j < h.length; j++) {
                    if (h2[i].equals(h[j])) {
                        h[j].setLastUp(h2[i].getLastUp());
                        h[j].getNetData();
                        if (NTF_JOIN && !up[j])
                            notifyJoin(h[j]);
                        h[j].setUp(true);
                        h2[i].setUp(false);
                        n--;
                        break;
                    }
                }
            }
        }
        Host[] hNew = new Host[n + (amin ? 0 : 1)];
        for (int i=0; i < h.length; i++)
            hNew[i] = h[i];
        n=h.length;
        for (int i=0; i < h2.length; i++) {
            if (h2[i].getUp()) {
                hNew[n] = h2[i];
                h2[i].getNetData();
                n++;
                if (NTF_NEW)
                    notifyNew(h2[i]);
                else if (NTF_JOIN)
                    notifyJoin(h2[i]);
            }
        }
        if (!amin) {
            hNew[n] = new Host(myip, " ", "trenutno", "jaz", true);
        }
        h = hNew;
        try {
            FileWriter out = new FileWriter(new File("hosts.dat"));
            for (int i=0; i < Ping.h.length; i++)
                out.write(Ping.h[i] + "\r\n");
            out.close();
        } catch (IOException e) {}
    }
    
    public static void notifyJoin(Host host) {
        NotifyOkno o = new NotifyOkno("Prikljuèil se je", "IP: " + host.getIP() + "\nMAC: " + host.getMAC() + "\nIme: " + host.getIme() + "\nIme v mreži: " + host.getNetName() + "\nSkupina: " + host.getDomena() + "\nUporabnik: " + host.getUser());
    }
    
    public static void notifyNew(Host host) {
        NotifyOkno o = new NotifyOkno("Nov raèunalnik", "IP: " + host.getIP() + "\nMAC: " + host.getMAC() + "\nIme v mreži: " + host.getNetName() + "\nSkupina: " + host.getDomena() + "\nUporabnik: " + host.getUser());
    }
    
    public static void window() {
        PingOkno o = new PingOkno(800,300,"Omrežje");
    }
}

class Host {
    String ip, mac, lastUp, ime, netName, domena, user;
    boolean up, share, msg;
    
    public Host(String ip, String mac, String lastUp, String ime, boolean up) {
        this.ip = ip;
        this.mac = mac;
        this.lastUp = lastUp;
        this.ime = ime;
        this.up = up;
    }
    
    public Host(String ip, String mac, String lastUp, String ime, boolean up, String netName, String domena, String user, boolean share, boolean msg) {
        this(ip, mac, lastUp, ime, up);
        this.netName = netName;
        this.domena = domena;
        this.user = user;
        this.share = share;
        this.msg = msg;
    }
    
    public String getIP() {
        return ip;
    }
    
    public String getMAC() {
        return mac;
    }
    
    public String getLastUp() {
        return lastUp;
    }
    
    public String getIme() {
        return ime;
    }
    
    public boolean getUp() {
        return up;
    }
    
    public String getNetName() {
        return netName;
    }
    
    public String getDomena() {
        return domena;
    }
    
    public String getUser() {
        return user;
    }
    
    public boolean getShare() {
        return share;
    }
    
    public boolean getMsg() {
        return msg;
    }
    
    public void setIP(String ip) {
        this.ip = ip;
    }
    
    public void setMAC(String mac) {
        this.mac = mac;
    }
    
    public void setLastUp(String lastUp) {
        this.lastUp = lastUp;
    }
    
    public void setIme(String ime) {
        this.ime = ime;
    }
    
    public void setUp(boolean up) {
        this.up = up;
    }
    
    public void setNetName(String netName) {
        this.netName = netName;
    }
    
    public void setDomena(String domena) {
        this.domena = domena;
    }
    
    public void setUser(String user) {
        this.user = user;
    }
    
    public void setShare(boolean share) {
        this.share = share;
    }
    
    public void setMsg(boolean msg) {
        this.msg = msg;
    }
    
    public void getNetData() {
        UDP comp = new UDP(ip);
        try {
            netName = comp.getNetName();
            domena = comp.getDomainName();
            user = comp.getUser();
            share = comp.isFileServer();
            msg = comp.hasMessenger();
        } catch (NullPointerException e) {}
        if (netName == null)
            netName = "n/a";
        if (domena == null)
            domena = "n/a";
        if (user == null)
            user = "n/a";
    }
    
    public boolean equals(Host h) {
        return ip.equals(h.ip) && mac.equals(h.mac);
    }
    
    public Object[] getRow() {
        return new Object[] {ip, mac, ime, netName, domena, user, new JCheckBox("", share), new JCheckBox("", msg), lastUp, new JCheckBox("", up)};
    }
    
    public String toString() {
        String ip = this.ip;
        if (ip == null)
            ip = "0.0.0.0";
        String mac = this.mac;
        if (mac == null)
            mac = "00:00:00:00:00:00";
        String ime = this.ime;
        if (ime == null)
            ime = (ip.equals("") ? "n/a" : ip);
        String netName = this.netName;
        if (netName == null)
            netName = "n/a";
        String domena = this.domena;
        if (domena == null)
            domena = "n/a";
        String user = this.user;
        if (user == null)
            user = "n/a";
        String lastUp = this.lastUp;
        if (lastUp == null)
            lastUp = "n/a";
        return (ip.equals("") ? "0.0.0.0" : ip) + ',' + (mac.equals("") ? "00:00:00:00:00:00" : mac) + ',' + (ime.equals("") ? "n/a" : ime) + ',' + (netName.equals("") ? "n/a" : netName) + ',' + (domena.equals("") ? "n/a" : domena) + ',' + (user.equals("") ? "n/a" : user) + ',' + (share ? 1 : 0) + ',' + (msg ? 1 : 0) + ',' + (lastUp.equals("") ? "n/a" : lastUp) + ',' + (up ? 1 : 0);
    }
}

class PingOkno extends JFrame {
	PingOkno(int a,int b, String c)
	{
		setSize(a,b);
		setTitle(c);
		Container vsebina=getContentPane();
		vsebina.add(new PingPanel());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
		show();
        setResizable(false);
	}
}

class NotifyOkno extends JFrame {
    static int y=0;
    static final int SIRINA=200;
    static final int VISINA=200;
    
	NotifyOkno(String c, String d)
	{
		setSize(SIRINA,VISINA);
        setLocation(Toolkit.getDefaultToolkit().getScreenSize().width-SIRINA,y);
        y+=VISINA;
		setTitle(c);
		Container vsebina=getContentPane();
		vsebina.add(new NotifyPanel(d));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
		show();
        setResizable(false);
        Timer t = new Timer(5000, new ActionListener() {
            public void actionPerformed(ActionEvent d) {
                dispose();
            }
        });
        t.start();
	}
}

class PingPanel extends JPanel implements ActionListener {
    private JScrollPane scroll = new JScrollPane();
    private JTable tabela = new JTable() {
        public TableCellRenderer getCellRenderer(int row, int column) {
            TableColumn tableColumn = getColumnModel().getColumn(column);
            TableCellRenderer renderer = tableColumn.getCellRenderer();
            if (renderer == null) {
                Class c = getColumnClass(column);
                if( c.equals(Object.class) )
                {
                    Object o = getValueAt(row,column);
                    if( o != null )
                        c = getValueAt(row,column).getClass();
                }
                renderer = getDefaultRenderer(c);
            }
            return renderer;
        }
        
        public TableCellEditor getCellEditor(int row, int column) {
            TableColumn tableColumn = getColumnModel().getColumn(column);
            TableCellEditor editor = tableColumn.getCellEditor();
            if (editor == null) {
                Class c = getColumnClass(column);
                if( c.equals(Object.class) )
                {
                    Object o = getValueAt(row,column);
                    if( o != null )
                        c = getValueAt(row,column).getClass();
                }
                editor = getDefaultEditor(c);
            }
            return editor;
        }
    };
    private JButton dodaj = new JButton("Dodaj vnos");
    private JButton odstrani = new JButton("Izbriši vnos");
    private JButton shrani = new JButton("Shrani vnose");
    private JButton gor = new JButton("Gor");
    private JButton dol = new JButton("Dol");
    private DefaultTableModel vsebina = new DefaultTableModel(
        new String[0][10], new String[] {
            "IP", "MAC", "Ime", "Ime v mreži", "Skupina", "Uporabnik", "Deljenje datotek", "Takojšnje sporoèanje", "Nazadnje prijavljen", "Trenutno"
        }
    );
    private DefaultTableColumnModel col = new DefaultTableColumnModel() {
        public void moveColumn(int columnIndex, int newIndex) {}
    };
        
    PingPanel() {
        tabela.setColumnModel(col);
        tabela.setModel(vsebina);
        for (int i=0; i < Ping.h.length; i++)
            vsebina.addRow(Ping.h[i].getRow());
        col.getColumn(0).setPreferredWidth(85);
        col.getColumn(1).setPreferredWidth(110);
        col.getColumn(2).setPreferredWidth(60);
        col.getColumn(3).setPreferredWidth(90);
        col.getColumn(4).setPreferredWidth(90);
        col.getColumn(5).setPreferredWidth(70);
        col.getColumn(6).setPreferredWidth(20);
        col.getColumn(7).setPreferredWidth(20);
        col.getColumn(8).setPreferredWidth(135);
        col.getColumn(9).setPreferredWidth(20);
        dodaj.addActionListener(this);
        odstrani.addActionListener(this);
        shrani.addActionListener(this);
        gor.addActionListener(this);
        dol.addActionListener(this);
        tabela.setDefaultRenderer( JComponent.class, new JComponentCellRenderer() );
        tabela.setDefaultEditor( JComponent.class, new JComponentCellEditor() );
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        scroll.setBounds(10,10,700,200);
        dodaj.setBounds(10,220,150,30);
        odstrani.setBounds(170,220,150,30);
        shrani.setBounds(330,220,150,30);
        gor.setBounds(720,70,60,30);
        dol.setBounds(720,110,60,30);
        scroll.setViewportView(tabela);
        add(scroll);
        add(dodaj);
        add(odstrani);
        add(shrani);
        add(gor);
        add(dol);
	}
    public void actionPerformed(ActionEvent d) {
        Object izvor=d.getSource();
        if (izvor == dodaj)
            vsebina.addRow(new Object[] {"0.0.0.0", "00-00-00-00-00-00", "", "", "", "n/a", new JCheckBox("", false), new JCheckBox("", false), "", new JCheckBox("", false)});
        if (izvor == odstrani) {
            try {
                int x = tabela.getSelectedRow();
                int n = tabela.getSelectedRowCount();
                for (int i=0; i < n; i++)
                    vsebina.removeRow(x);
            } catch (IndexOutOfBoundsException e) {}
        }
        if (izvor == shrani) {
            try {
                FileWriter out = new FileWriter(new File("hosts.dat"));
                Ping.h = new Host[vsebina.getRowCount()];
                for (int i=0; i < Ping.h.length; i++) {
                    Ping.h[i] = new Host((String)vsebina.getValueAt(i, 0), (String)vsebina.getValueAt(i, 1), (String)vsebina.getValueAt(i, 8), (String)vsebina.getValueAt(i, 2), ((JCheckBox)vsebina.getValueAt(i, 9)).isSelected(), (String)vsebina.getValueAt(i, 3), (String)vsebina.getValueAt(i, 4), (String)vsebina.getValueAt(i, 5), ((JCheckBox)vsebina.getValueAt(i, 6)).isSelected(), ((JCheckBox)vsebina.getValueAt(i, 7)).isSelected());
                    out.write(Ping.h[i] + "\r\n");
                }
                out.close();
            } catch (IOException e) {}
        }
        if (izvor == gor) {
            try {
                int x = tabela.getSelectedRow();
                int n = tabela.getSelectedRowCount();
                vsebina.moveRow(x-1, x-1, x+n-1);
                tabela.setRowSelectionInterval(x-1, x+n-2);
            } catch (IndexOutOfBoundsException e) {}
        }
        if (izvor == dol) {
            try {
                int x = tabela.getSelectedRow();
                int n = tabela.getSelectedRowCount();
                vsebina.moveRow(x+n, x+n, x);
                tabela.setRowSelectionInterval(x+1, x+n);
            } catch (IndexOutOfBoundsException e) {}
        }
    }
}
    
class NotifyPanel extends JPanel implements ActionListener {
    private JTextPane text = new JTextPane();
    private JButton gumb = new JButton("Glavno okno");
    private boolean perf = false;
    
    public NotifyPanel(String txt) {
        super();
        text.setText(txt);
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        text.setBounds(10,10,175,100);
        text.setEditable(false);
        gumb.setBounds(40, 120, 120, 30);
        add(text);
        add(gumb);
        gumb.addActionListener(this);
	}
    public void actionPerformed(ActionEvent d) {
        if (perf)
            return;
        perf = true;
        Object izvor=d.getSource();
        Ping.window();
    }
}

class JComponentCellRenderer implements TableCellRenderer
{
    public Component getTableCellRendererComponent(JTable table, Object value,
		boolean isSelected, boolean hasFocus, int row, int column) {
        return (JComponent)value;
    }
}