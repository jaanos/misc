package racgr.labmaker;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
* Razred za risanje ikon objektov.
*
* @author	Janoš Vidali, 63040303
*/
class Objects {
	/**
	* Barva bratranca Itta.
	*/
	static final Color ITT = new Color(208,160,0);
	
	/**
	* Barva stožca.
	*/
	static final Color CONE = new Color(0,0,128);
	
	/**
	* Barva kocke.
	*/
	static final Color CUBE = new Color(0,160,160);
	
	/**
	* Barva piramide.
	*/
	static final Color PYRAMIDE = new Color(128,128,0);
	
	/**
	* Barva krogle.
	*/
	static final Color SPHERE = new Color(0,128,0);
	
	/**
	* Velikost ikone na gumbu.
	*/
	static final int SIZE = 15;
	
	/**
	* Abstraktni razred za ikono.
	*
	* @author	Janoš Vidali, 63040303
	*/
	abstract private static class Obj implements Icon {
		/**
		* Vrne višino ikone.
		*
		* @return		višina ikone
		*/
		public int getIconHeight() {
			return SIZE;
		}
		
		/**
		* Vrne širino ikone.
		*
		* @return		širina ikone
		*/
		public int getIconWidth() {
			return SIZE;
		}
	}
	
	/**
	* Ikona zidu.
	*
	* @author	Janoš Vidali, 63040303
	*/
	static class Wall extends Obj {
		/**
		* Nariše ikono.
		*
		* @param c		komponenta, na katero rišemo
		* @param g		risalna površina
		* @param x		X koordinata
		* @param y		Y koordinata
		*/
		public void paintIcon(Component c, Graphics g, int x, int y) {
			drawWall(g,x,y,SIZE);
		}
	}
	
	/**
	* Ikona vrat.
	*
	* @author	Janoš Vidali, 63040303
	*/
	static class Door extends Obj {
		/**
		* Nariše ikono.
		*
		* @param c		komponenta, na katero rišemo
		* @param g		risalna površina
		* @param x		X koordinata
		* @param y		Y koordinata
		*/
		public void paintIcon(Component c, Graphics g, int x, int y) {
			drawDoor(g,x,y,SIZE);
		}
	}
	
	/**
	* Ikona kija.
	*
	* @author	Janoš Vidali, 63040303
	*/
	static class Bat extends Obj {
		/**
		* Nariše ikono.
		*
		* @param c		komponenta, na katero rišemo
		* @param g		risalna površina
		* @param x		X koordinata
		* @param y		Y koordinata
		*/
		public void paintIcon(Component c, Graphics g, int x, int y) {
			drawBat(g,x,y,SIZE);
		}
	}
	
	/**
	* Ikona bratranca Itta.
	*
	* @author	Janoš Vidali, 63040303
	*/
	static class CousinItt extends Obj {
		/**
		* Nariše ikono.
		*
		* @param c		komponenta, na katero rišemo
		* @param g		risalna površina
		* @param x		X koordinata
		* @param y		Y koordinata
		*/
		public void paintIcon(Component c, Graphics g, int x, int y) {
			drawCousinItt(g,x,y,SIZE);
		}
	}
	
	/**
	* Ikona stožca.
	*
	* @author	Janoš Vidali, 63040303
	*/
	static class Cone extends Obj {
		/**
		* Nariše ikono.
		*
		* @param c		komponenta, na katero rišemo
		* @param g		risalna površina
		* @param x		X koordinata
		* @param y		Y koordinata
		*/
		public void paintIcon(Component c, Graphics g, int x, int y) {
			drawCone(g,x,y,SIZE);
		}
	}
	
	/**
	* Ikona kocke.
	*
	* @author	Janoš Vidali, 63040303
	*/
	static class Cube extends Obj {
		/**
		* Nariše ikono.
		*
		* @param c		komponenta, na katero rišemo
		* @param g		risalna površina
		* @param x		X koordinata
		* @param y		Y koordinata
		*/
		public void paintIcon(Component c, Graphics g, int x, int y) {
			drawCube(g,x,y,SIZE);
		}
	}
	
	/**
	* Ikona pirakocke.
	*
	* @author	Janoš Vidali, 63040303
	*/
	static class PyraCube extends Obj {
		/**
		* Nariše ikono.
		*
		* @param c		komponenta, na katero rišemo
		* @param g		risalna površina
		* @param x		X koordinata
		* @param y		Y koordinata
		*/
		public void paintIcon(Component c, Graphics g, int x, int y) {
			drawPyraCube(g,x,y,SIZE);
		}
	}
	
	/**
	* Ikona piramide.
	*
	* @author	Janoš Vidali, 63040303
	*/
	static class Pyramide extends Obj {
		/**
		* Nariše ikono.
		*
		* @param c		komponenta, na katero rišemo
		* @param g		risalna površina
		* @param x		X koordinata
		* @param y		Y koordinata
		*/
		public void paintIcon(Component c, Graphics g, int x, int y) {
			drawPyramide(g,x,y,SIZE);
		}
	}
	
	/**
	* Ikona krogle.
	*
	* @author	Janoš Vidali, 63040303
	*/
	static class Sphere extends Obj {
		/**
		* Nariše ikono.
		*
		* @param c		komponenta, na katero rišemo
		* @param g		risalna površina
		* @param x		X koordinata
		* @param y		Y koordinata
		*/
		public void paintIcon(Component c, Graphics g, int x, int y) {
			drawSphere(g,x,y,SIZE);
		}
	}
	
	/**
	* Nariše zid.
	*
	* @param g		risalna površina
	* @param x		X koordinata
	* @param y		Y koordinata
	* @param w		širina
	*/
	public static void drawWall(Graphics g, int x, int y, int w) {
		g.setColor(Color.GRAY);
		g.fillRect(x,y,w,w);
	}

	/**
	* Nariše vrata.
	*
	* @param g		risalna površina
	* @param x		X koordinata
	* @param y		Y koordinata
	* @param w		širina
	*/
	public static void drawDoor(Graphics g, int x, int y, int w) {
		g.setColor(Color.RED);
		g.fillRect(x,y,w,w);
	}
	
	/**
	* Nariše kij.
	*
	* @param g		risalna površina
	* @param x		X koordinata
	* @param y		Y koordinata
	* @param w		širina
	*/
	public static void drawBat(Graphics g, int x, int y, int w) {
		g.setColor(Color.GREEN);
		g.fillRect(x,y,w,w);
	}
	
	/**
	* Nariše bratranca Itta.
	*
	* @param g		risalna površina
	* @param x		X koordinata
	* @param y		Y koordinata
	* @param w		širina
	*/
	public static void drawCousinItt(Graphics g, int x, int y, int w) {
		g.setColor(ITT);
		g.fillOval(x+1,y+1,w-2,w-2);
		g.setColor(Color.BLACK);
		g.fillOval(x+w/4+1,y+w/4+1,w/2-1,w/2-1);
	}
	
	/**
	* Nariše stožec.
	*
	* @param g		risalna površina
	* @param x		X koordinata
	* @param y		Y koordinata
	* @param w		širina
	*/
	public static void drawCone(Graphics g, int x, int y, int w) {
		g.setColor(CONE);
		g.fillArc(x+1,y+1,w-2,w-2,180,180);
		g.fillPolygon(new int[] {x+1,x+w-1,x+w/2},
					  new int[] {y+w/2,y+w/2,y+1},3);
	}
	
	/**
	* Nariše kocko.
	*
	* @param g		risalna površina
	* @param x		X koordinata
	* @param y		Y koordinata
	* @param w		širina
	*/
	public static void drawCube(Graphics g, int x, int y, int w) {
		g.setColor(CUBE);
		g.fillRect(x+2,y+2,w-3,w-3);
	}
	
	/**
	* Nariše pirakocko.
	*
	* @param g		risalna površina
	* @param x		X koordinata
	* @param y		Y koordinata
	* @param w		širina
	*/
	public static void drawPyraCube(Graphics g, int x, int y, int w) {
		g.setColor(Color.ORANGE);
		g.fillRect(x+2,y+w/2,w-3,w/2-1);
		g.fillPolygon(new int[] {x+1,x+w-1,x+w/2},
					  new int[] {y+w/2,y+w/2,y+1},3);
	}
	
	/**
	* Nariše piramido.
	*
	* @param g		risalna površina
	* @param x		X koordinata
	* @param y		Y koordinata
	* @param w		širina
	*/
	public static void drawPyramide(Graphics g, int x, int y, int w) {
		g.setColor(PYRAMIDE);
		g.fillPolygon(new int[] {x+1,x+w-1,x+w/2},
					  new int[] {y+w-1,y+w-1,y+1},3);
	}
	
	/**
	* Nariše kroglo.
	*
	* @param g		risalna površina
	* @param x		X koordinata
	* @param y		Y koordinata
	* @param w		širina
	*/
	public static void drawSphere(Graphics g, int x, int y, int w) {
		g.setColor(SPHERE);
		g.fillOval(x+1,y+1,w-2,w-2);
	}
}