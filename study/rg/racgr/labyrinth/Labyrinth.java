package racgr.labyrinth;

import java.util.Vector;

/**
* Razred globalnih spremenljivk.
*
* @author Lovro Šubelj, 63040296
* @author Janoš Vidali, 63040303
*/
public class Labyrinth {
	/**
	* Opis zidov labirinta.
	*
	* <ul>
	*	<li>0: ni zidu</li>
	*	<li>1: zaèetek zidu</li>
	*	<li>2: nadaljevanje zidu v smeri x osi</li>
	*	<li>3: nadaljevanje zidu v smeri z osi</li>
	* </ul>
	*/
	static byte[][] labyrinth;
		
	/**
	* Tabela zidov.
	*
	* Objekt zidu za vsako celico. Èe ni zidu, je vrednost <i>null</i>.
	*/
	static Wall[][] walls;
	
	/**
	* Širina v celicah.
	*/
	static int cellsInWidth;
		
	/**
	* Dolžina v celicah.
	*/
	static int cellsInLength;
	
	/**
	* Položaj vrat po širini.
	*/
	static int doorsWidth;
		
	/**
	* Položaj vrat po dolžini.
	*/
	static int doorsLength;
	
	/**
	* Zaèetni položaj po širini.
	*/
//	static int startWidth;
		
	/**
	* Zaèetni položaj po dolžini.
	*/
//	static int startLength;
	
	/**
	* Objekt igralca.
	*/
	static Object3D player;
	
	/**
	* Seznam vseh likov.
	*/
	static Vector<Object3D> chars;
	
	/**
	* Tabela tekstur.
	*/
	static Texture[] tex = new Texture[15];
	
	/**
	* Baseballski kij.
	*/
	static Bat bat;
	
}