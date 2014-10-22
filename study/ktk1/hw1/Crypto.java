import java.io.*;
import java.util.*;

/**
* Nadrazred s splošnimi metodami za kriptoanalizo.
*
* @author Janoš Vidali, 63040303
*/
public class Crypto {
	/** Konstruktor. */
	protected Crypto() {}
	/**
	* Abecede.
	*/
	static final String[] ALPHABETS = {"abcèdefghijklmnoprsštuvzž",
										"abcdefghijklmnopqrstuvwxyz"};
		
	/**
	* Porazdelitve èrk.
	*/
	static final double[][] FREQS = {
		{ //Slovenska
			0.10466, //a
			0.01939, //b
			0.00662, //c
			0.01483, //è
			0.03390, //d
			0.10707, //e
			0.00110, //f
			0.01638, //g
			0.01047, //h
			0.09042, //i
			0.04675, //j
			0.03704, //k
			0.05266, //l
			0.03305, //m
			0.06328, //n
			0.09084, //o
			0.03374, //p
			0.05010, //r
			0.05053, //s
			0.00996, //š
			0.04329, //t
			0.01879, //u
			0.03764, //v
			0.02103, //z
			0.00646  //ž
		}, { //Angleška
			0.08167, //a
			0.01492, //b
			0.02782, //c
			0.04253, //d
			0.12702, //e
			0.02228, //f
			0.02015, //g
			0.06094, //h
			0.06966, //i
			0.00153, //j
			0.00772, //k
			0.04025, //l
			0.02406, //m
			0.06749, //n
			0.07507, //o
			0.01929, //p
			0.00095, //q
			0.05987, //r
			0.06327, //s
			0.09056, //t
			0.02758, //u
			0.00978, //v
			0.02360, //w
			0.00150, //x
			0.01974, //y
			0.00074  //z
		}
	};
	
	/**
	* Slovenski jezik.
	*/
	static final int SLOVENE = 0;
	
	/**
	* Angleški jezik.
	*/
	static final int ENGLISH = 1;
	
	/**
	* Uporabljena abeceda.
	*/
	static String alphabet;
	
	/**
	* Frekvence èrk v uporabljenem jeziku.
	*/
	static double[] freq;
	
	/**
	* Tabela številskih vrednosti za èrke.
	*/
	static Hashtable<Character,Integer> back;
	
	/**
	* Kriptogram.
	*/
	static String text;
	
	
	/**
	* Prebere kriptogram iz datoteke. Zavrže vse znake z ASCII kodo pod 65.
	*
	* @param file			ime datoteke.
	* @throws IOException	ob napaki pri branju.
	*/
	public static void readFile(String file) throws IOException {
		FileReader in = new FileReader(file);
		int c;
		StringBuilder sb = new StringBuilder();
		while ((c = in.read()) != -1) {
			if (c > 64) sb.append((char)c);
		}
		text = sb.toString();
	}
	
	/**
	* Nastavi abecedo. Izraèuna še tabelo številskih vrednosti za èrke.
	*
	* @param idx		številka abecede.
	*/
	public static void setAlphabet(int idx) {
		alphabet = ALPHABETS[idx];
		freq = FREQS[idx];
		back = new Hashtable<Character,Integer>();
		for (int i=0; i < alphabet.length(); i++) {
			back.put(alphabet.charAt(i),i);
		}
	}
	
	/**
	* Vrne številsko vrednost danega znaka.
	*
	* @param c		znak.
	* @return		številska vrednost znaka.
	*/
	public static int getLetterIndex(char c) {
		return back.get(c);
	}
	
	/**
	* Vrne èrko z dano številsko vrednostjo.
	*
	* @param i		indeks.
	* @return		znak.
	*/
	public static char getLetterAt(int i) {
		return alphabet.charAt(i);
	}
	
	/**
	* Vrne kriptogram.
	*
	* @return		kriptogram.
	*/
	public static String getText() {
		return text;
	}
	
	/**
	* Prišteje znaka.
	*
	* @param a			prvi znak.
	* @param b			drugi znak.
	* @return			vsota znakov.
	*/
	public static char add(char a, char b) {
		return alphabet.charAt((back.get(a)+back.get(b))%alphabet.length());
	}
	
	/**
	* Odšteje znaka.
	*
	* @param a			prvi znak.
	* @param b			drugi znak.
	* @return			razlika znakov.
	*/
	public static char sub(char a, char b) {
		return alphabet.charAt((back.get(a)-back.get(b)+alphabet.length())%alphabet.length());
	}
}