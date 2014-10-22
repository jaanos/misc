import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
* Razred za napad z golim kriptogramom na Vigenerjevo šifro.
* Za besedilo se prièakuje, da je v anglešèini.
*
* @author Janoš Vidali, 63040303
*/
public class Vigenere extends Crypto {
	private Vigenere() {}
	
	/**
	* Main metoda programa.
	*
	* Iz datoteke, podane v parametru, prebere kriptogram,
	* ki ga poskuša razbiti.
	*
	* @param args		argumenti: ime datoteke.
	*/
	public static void main(String[] args) throws IOException {
		setAlphabet(ENGLISH);
		readFile(args[0]);
		boolean ok = false;
		//Poskušamo za bloke velikosti 3 - 15
		for (Integer i : kasiski(3,4,5,6,7,8,9,10,11,12,13,14,15)) {
			//Na dobljenih vrednostih preveri indeks sovpadanja
			if (ok = coincidenceIndex(i)) break;
		}
		if (!ok) {
			System.out.println("Desifriranje ni uspelo!");
		}
	}
	
	/**
	* Zašifrira sporoèilo.
	*
	* @param key		kljuè.
	* @return			zašifrirano sporoèilo.
	*/
	public static String encrypt(String key) {
		StringBuilder sb = new StringBuilder(text.toLowerCase());
		for (int i=0; i < text.length(); i++) {
			sb.setCharAt(i,add(sb.charAt(i),key.charAt(i%key.length())));
		}
		return sb.toString().toUpperCase();
	}
	
	/**
	* Odšifrira sporoèilo.
	*
	* @param key		kljuè.
	* @return			odšifrirano sporoèilo.
	*/
	public static String decrypt(String key) {
		StringBuilder sb = new StringBuilder(text.toLowerCase());
		for (int i=0; i < text.length(); i++) {
			sb.setCharAt(i,sub(sb.charAt(i),key.charAt(i%key.length())));
		}
		return sb.toString();
	}
	
	/**
	* Izvede test Kasiskega.
	*
	* Najde ponovljene nize treh znakov v kriptogramu ter za podane
	* velikosti blokov preveri, koliko razdalj med ponovljenimi nizi
	* je deljivih z njimi. Vrne urejen seznam velikosti blokov od
	* najboljšega (takega, da je bilo najveè razdalj deljivih z njim)
	* do najslabšega.
	*
	* @param test		seznam velikosti blokov za preverjanje.
	* @return			urejen seznam velikosti blokov
	*/
	public static Collection<Integer> kasiski(int ... test) {
		//Tabela ponovljenih nizov z mesti njihovih pojavitev.
		Hashtable<String,LinkedList<Integer>> h =
			new Hashtable<String,LinkedList<Integer>>();
		//Tabela ponovljenih nizov, urejenih po številu pojavitev.
		TreeMap<LinkedList<Integer>,String> tm =
			new TreeMap<LinkedList<Integer>,String>(
				new Comparator<LinkedList<Integer>>() {
					/**
					* Primerja seznama pojavitev niza.
					*
					* Poskrbi, da so v tabeli prej nizi, ki se veèkrat
					* pojavijo oziroma se prej pojavijo v kriptogramu.
					*
					* @param l1		prvi seznam.
					* @param l2		drugi seznam.
					* @return		-1, èe je prvi seznam pred drugim,
					*				0, èe sta seznama ekvivalentna,
					*				1, èe je drugi seznam pred prvim.
					*/
					public int compare(LinkedList<Integer> l1, LinkedList<Integer> l2) {
						int diff = l2.size() - l1.size();
						if (diff == 0 && l1.size() > 0) {
							diff = l1.get(0) - l2.get(0);
						}
						return diff;
					}
				}
			);
		//Tabela velikosti blokov, urejena od najboljšega do najslabšega.
		TreeMap<int[],Integer> out =
			new TreeMap<int[],Integer>(
				new Comparator<int[]>() {
					/**
					* Primerja število deljivih razdalj med nizi za
					* velikosti blokov.
					*
					* Poskrbi, da so v tabeli prej velikosti blokov, za
					* katere se je veè razdalj deljivih oziroma so te
					* velikosti veèje.
					*
					* @param a1		prvi par število deljivih razdalj,
					*				velikost bloka.
					* @param a1		drugi par število deljivih razdalj,
					*				velikost bloka.
					* @return		-1, èe je prva velikost pred drugo,
					*				0, èe sta velikosti ekvivalentni,
					*				1, èe je druga velikost pred drugo.
					*/
					public int compare(int[] a1, int[] a2) {
						int diff = a2[0] - a1[0];
						if (diff == 0) {
							diff = a2[1] - a1[1];
						}
						return diff;
					}
				}
			);
		LinkedList<Integer> l;
		String sub;
		//Dodajamo nize 3 zaporednih znakov v tabelo.
		for (int i=0; i < text.length()-2; i++) {
			sub = text.substring(i,i+3);
			l = h.get(sub);
			//Èe niz še ne obstaja, ustvari seznam pojavitev.
			if (l == null) {
				l = new LinkedList<Integer>();
				h.put(sub, l);
			}
			l.add(i);
		}
		//Uredi seznam po številu pojavitev.
		for (Map.Entry<String,LinkedList<Integer>> e : h.entrySet()) {
			if (e.getValue().size() > 1) {
				tm.put(e.getValue(), e.getKey());
			}
		}
		
		//Najdemo število deljivih razdalj.
		int[] t = new int[test.length];
		int[] f = new int[test.length];
		for (Map.Entry<LinkedList<Integer>,String> e : tm.entrySet()) {
			System.out.print(e.getValue()+":");
			Integer j = null;
			for (Integer i : e.getKey()) {
				if (j != null) {
					System.out.print(" (" + (i-j) + ")");
					for (int k=0; k < test.length; k++) {
						//Gledamo samo razdalje med zaporednimi ponovitvami
						//nizov - izkaže se za zadostno.
						if ((i-j)%test[k] == 0) t[k]++; else f[k]++;
					}
				}
				System.out.print(" "+i);
				j = i;
			}
			System.out.println();
		}

		//Uredimo po številu deljivih razdalj.
		for (int k=0; k < test.length; k++) {
			System.out.println(t[k] + " razlik deljivih s stevilom " + test[k] + ", " + f[k] + " ni deljivih");
			out.put(new int[] {t[k], test[k]}, test[k]);
		}
		
		return out.values();
	}
	
	/**
	* Izraèuna indekse sovpadanja ter testira hipotezo o velikosti bloka.
	*
	* @param test		velikost bloka.
	* @return			true, èe je hipoteza potrjena; false sicer.
	*/
	public static boolean coincidenceIndex(int test) {
		System.out.println("Izbrana vrednost: " + test);
		
		//Spodnja dopustna meja za indeks sovpadanja.
		final double LOW_BOUND = 0.06;
		boolean solved = true;
		double[][] p = new double[test][alphabet.length()];
		double[] index = new double[test];
		String txt = text.toLowerCase();
		int len;
		//Štejemo pojavitve posameznega znaka v vsakem podnizu.
		for (int i=0; i < txt.length(); i++) {
			p[i%test][back.get(txt.charAt(i))]++;
		}
		//Raèunamo koincidenène indekse.
		for (int i=0; i < test; i++) {
			//Dolžina podniza.
			len = (text.length()-i-1+test)/test;
			if (len == 0) break;
			for (int j=0; j < alphabet.length(); j++) {
				//Najprej izraèunamo vsoto kvadratov.
				index[i] += p[i][j] * p[i][j];
				//Števila pojavitev veè ne rabimo,
				//nadomestimo z relativno frekvenco.
				p[i][j] /= len;
			}
			//Delimo s kvadratom dolžine in dobimo indeks sovpadanja.
			index[i] /= len * len;
			System.out.println("I(y_" + i + ") = " + index[i] + (index[i] < LOW_BOUND ? " < " : " >= ") + LOW_BOUND);
			//Èe je indeks prenizek na enem podnizu, zavrnemo hipotezo.
			if (index[i] < LOW_BOUND) solved = false;
		}
		
		if (solved) {
			//Èe je hipoteza sprejeta:
			//poišèemo tak odmik, za katerega je vsota produktov frekvenc
			//èrk s prièakovanimi frekvencami najveèja.
			int[] offset = new int[test];
			double sum, max;
			for (int i=0; i < test; i++) {
				max = 0;
				for (int j=0; j < alphabet.length(); j++) {
					sum = 0;
					for (int k=0; k < alphabet.length(); k++) {
						sum += freq[k]*p[i][(j+k)%alphabet.length()];
					}
					if (sum > max) {
						offset[i] = j;
						max = sum;
					}
				}
			}
			System.out.println("Preizkus za velikost bloka " + test + " uspel!");
			
			//Pokažemo grafièni vmesnik.
			new VigenerePanel(p, offset);
		} else {
			System.out.println("Preizkus za velikost bloka " + test + " ni uspel.");
		}
		
		return solved;
	}
}

/**
* Grafièni vmesnik za dešifriranje Vigenerjeva šifre.
*
* @author Janoš Vidali, 63040303
*/
class VigenerePanel extends JPanel implements ActionListener {
	/**
	* Relativne frekvence èrk po podnizih.
	*/
	double[][] p;
	
	/**
	* Odmiki za posamezne podnize.
	*/
	int[] offset;
	
	/**
	* Barve stolpcev.
	*/
	static final Color[] COLORS = {Color.MAGENTA, Color.ORANGE, Color.CYAN,
		Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE, Color.PINK,
		Color.BLACK, Color.LIGHT_GRAY, Color.DARK_GRAY, Color.GRAY};
	
	/**
	* Širina stolpcev.
	*/
	static final int WIDTH = 5;
	
	/**
	* Oznake za abecedo.
	*/
	JLabel[] alpha;
		
	/**
	* Oznake za zamaknjeno abecedo podnizov.
	*/
	JLabel[][] vig;
		
	/**
	* Gumbi za poveèanje odmika.
	*/
	JButton[] plus;
		
	/**
	* Gumbi za zmanjšanje odmika.
	*/
	JButton[] minus;
	
	/**
	* Podroèje z dešifriranim tekstom.
	*/
	JTextArea area = new JTextArea();
	
	/**
	* Konstruktor. Odpre okno in se naseli vanj.
	*
	* @param p			relativne frekvence èrk po podnizih.
	* @param offset		odmiki za posamezne podnize.
	*/
	public VigenerePanel(double[][] p, int[] offset) {
		this.p = p;
		this.offset = offset;
		minus = new JButton[p.length];
		plus = new JButton[p.length];
		alpha = new JLabel[Vigenere.freq.length];
		vig = new JLabel[p.length][Vigenere.freq.length];
		for (int i=0; i < Vigenere.freq.length; i++) {
			alpha[i] = new JLabel(Vigenere.getLetterAt(i)+"");
			alpha[i].setHorizontalAlignment(JLabel.CENTER);
			for (int j=0; j < p.length; j++) {
				vig[j][i] = new JLabel(alpha[i].getText());
				vig[j][i].setHorizontalAlignment(JLabel.CENTER);
			}
		}
		for (int j=0; j < p.length; j++) {
			minus[j] = new JButton("<");
			plus[j] = new JButton(">");
			minus[j].addActionListener(this);
			plus[j].addActionListener(this);
		}
		area.setEditable(false);
		area.setLineWrap(true);
		decipher();
		JFrame o = new JFrame("Vigenere");
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int w = 220+p.length*Vigenere.freq.length*WIDTH;
		int h = 500+p.length*20;
		o.setBounds((int)(dim.getWidth()-w)/2,(int)(dim.getHeight()-h)/2,w,h);
		o.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		o.getContentPane().add(this);
		o.setVisible(true);
	}
	
	/**
	* Nariše grafièni vmesnik.
	*
	* @param g			risalna površina.
	*/
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int height, idx;
		JLabel label;
		for (int i=0; i < Vigenere.freq.length; i++) {
			g.setColor(COLORS[p.length]);
			g.fillRect(56+i*(p.length*WIDTH+4),200-(height=(int)(Vigenere.freq[i]*1000)),p.length*WIDTH,height);
			label = alpha[i];
			label.setBounds(64+i*(p.length*WIDTH+4),220,p.length*WIDTH/2,20);
			add(label);
			for (int j=0; j < p.length; j++) {
				idx = (i+offset[j])%Vigenere.freq.length;
				g.setColor(COLORS[j%COLORS.length]);
				g.fillRect(56+i*(p.length*WIDTH+4)+j*WIDTH,200-(height=(int)(p[j][idx]*1000)),WIDTH,height);
				label = vig[j][idx];
				label.setBounds(64+i*(p.length*WIDTH+4),240+j*20,p.length*WIDTH/2,20);
				add(label);
			}
		}
		g.setColor(Color.GREEN);
		g.fillRect(64,240,p.length*WIDTH/2,20*p.length);
		for (int j=0; j < p.length; j++) {
			minus[j].setBounds(5,240+j*20,50,20);
			plus[j].setBounds(50+Vigenere.freq.length*(p.length*WIDTH+4),240+j*20,50,20);
			add(minus[j]);
			add(plus[j]);
		}
		
		area.setBounds(60,250+p.length*20,Vigenere.freq.length*(p.length*WIDTH+4)-10,200);
		add(area);
	}
	
	/**
	* Dogodek ob kliku na gumb.
	*
	* Poveèa ali zmanjša odmik na enem od podnizov.
	*
	* @param e		dogodek.
	*/
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		for (int i=0; i < p.length; i++) {
			if (src == plus[i]) {
				offset[i] = (offset[i]+1)%Vigenere.freq.length;
				System.out.println(offset[i]);
			} else if (src == minus[i]) {
				offset[i] = (offset[i]+Vigenere.freq.length-1)%Vigenere.freq.length;
			} else continue;
			break;
		}
		decipher();
		repaint();
	}
	
	/**
	* Dešifrira sporoèilo in ga izpiše v tekstovno podroèje.
	*/
	public void decipher() {
		StringBuilder sb = new StringBuilder(p.length);
		for (int i=0; i < p.length; i++) {
			sb.append(Vigenere.getLetterAt((offset[i])%Vigenere.freq.length));
		}
		area.setText(Vigenere.decrypt(sb.toString()));
	}
}