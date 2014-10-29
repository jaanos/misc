package racgr.labmaker;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
* Pogled labirinta.
*
* @author	Janoš Vidali, 63040303
*/
class LabyrinthView extends JPanel implements MouseListener, MouseMotionListener {
	/**
	* Panel, ki mu pripada.
	*/
	LabMaker panel;
	
	/**
	* Ali rišemo.
	*/
	private boolean draw = false;
	
	/**
	* Ali brišemo.
	*/
	private boolean delete = false;
	
	/**
	* Konstruktor.
	*
	* @param panel		panel, ki mu pripada
	*/
	public LabyrinthView(LabMaker panel) {
		this.panel = panel;
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	/**
    * Metoda za risanje.
    *
    * @param g		risalna površina
    */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(Color.WHITE);
		g.setColor(Color.BLACK);
		g.drawRect(1,1,panel.width*panel.zoom+1,panel.length*panel.zoom+1);
		for (int i=0; i < panel.length; i++) {
			for (int j=0; j < panel.width; j++) {
				if (Character.isUpperCase(panel.labyrinth[i][j]) && panel.labyrinth[i][j] != 'X' && panel.labyrinth[i][j] != 'V') {
					Objects.drawBat(g,2+panel.zoom*j,2+panel.zoom*i,panel.zoom);
				}
				switch (panel.labyrinth[i][j]) {
					case 'X':
						Objects.drawWall(g,2+panel.zoom*j,2+panel.zoom*i,panel.zoom);
						break;
					case 'V':
						Objects.drawDoor(g,2+panel.zoom*j,2+panel.zoom*i,panel.zoom);
						break;
					case 'i':
					case 'I':
						Objects.drawCousinItt(g,2+panel.zoom*j,2+panel.zoom*i,panel.zoom);
						break;
					case 's':
					case 'S':
						Objects.drawCone(g,2+panel.zoom*j,2+panel.zoom*i,panel.zoom);
						break;
					case 'k':
					case 'K':
						Objects.drawCube(g,2+panel.zoom*j,2+panel.zoom*i,panel.zoom);
						break;
					case 'r':
					case 'R':
						Objects.drawPyraCube(g,2+panel.zoom*j,2+panel.zoom*i,panel.zoom);
						break;
					case 'p':
					case 'P':
						Objects.drawPyramide(g,2+panel.zoom*j,2+panel.zoom*i,panel.zoom);
						break;
					case 'o':
					case 'O':
						Objects.drawSphere(g,2+panel.zoom*j,2+panel.zoom*i,panel.zoom);
						break;
				}
			}
		}
		if (panel.grid) {
			g.setColor(Color.BLACK);
			for (int i=1; i < panel.length; i++) {
				g.drawLine(2,2+panel.zoom*i,panel.width*panel.zoom+1,2+panel.zoom*i);
			}
			for (int j=1; j < panel.width; j++) {
				g.drawLine(2+panel.zoom*j,2,2+panel.zoom*j,panel.length*panel.zoom+1);
			}
		}
	}
	
	/**
    * Izvede ustrezno akcijo ob miškinem kliku.
	*
    * @param e		dogodek
    */
	public void mouseClicked(MouseEvent e) {}
		
	/**
    * Izvede ustrezno akcijo ob pritisku na miškino tipko.
	*
    * @param e		dogodek
    */
    public void mousePressed(MouseEvent e) {
		if (e.getButton() == e.BUTTON3) {
			delete = true;
			draw = false;
		} else {
			draw = true;
			delete = false;
		}
		if (delete) {
			int j = (e.getX()-2)/panel.zoom;
			int i = (e.getY()-2)/panel.zoom;
			if (i >= 0 && j >= 0 && i < panel.length && j < panel.width && panel.labyrinth[i][j] != ' ' && panel.labyrinth[i][j] != 'X') {
				if (panel.xPlayer == i && panel.yPlayer == j) {
					panel.xPlayer = panel.yPlayer = -1;
				}
				if (panel.xBat == i && panel.yBat == j) {
					panel.xBat = panel.yBat = -1;
				}
				if (panel.xDoor == i && panel.yDoor == j) {
					panel.xDoor = panel.yDoor = -1;
				}
				panel.labyrinth[i][j] = panel.labyrinth[i][j] == 'V' ? 'X' : ' ';
				delete = false;
				panel.repaint();
				return;
			}
		}
		mouseDragged(e);
	}
		
	/**
    * Izvede ustrezno akcijo ob izpustitvi miškine tipke.
    *
    * @param e		dogodek
    */
	public void mouseReleased(MouseEvent e) {
		draw = false;
		delete = false;
	}
		
	/**
    * Izvede ustrezno akcijo ob premiku miške v okno.
    *
    * @param e		dogodek
    */
	public void mouseEntered(MouseEvent e) {}
		
	/**
    * Izvede ustrezno akcijo ob premiku miške iz okna.
    *
    * @param e		dogodek
    */
    public void mouseExited(MouseEvent e) {}
		
	/**
    * Izvede ustrezno akcijo ob premiku miške.
    *
    * @param e		dogodek
    */
    public void mouseMoved(MouseEvent e) {}
		
	/**
    * Izvede ustrezno akcijo ob vleèenju miške.
    *
    * @param e		dogodek
    */
    public void mouseDragged(MouseEvent e) {
		if (!delete && !draw) return;
		int j = (e.getX()-2)/panel.zoom;
		int i = (e.getY()-2)/panel.zoom;
		if (i < 0 || j < 0 || i >= panel.length || j >= panel.width) return;
		boolean border = i == 0 || j == 0 || i == panel.length-1 || j == panel.width-1;
		if (delete) {
			if (border || panel.labyrinth[i][j] != 'X') return;
			panel.labyrinth[i][j] = ' ';
		} else if (draw) {
			if (panel.mode != 1 && border) return;
			if (panel.labyrinth[i][j] != ' ') {
				switch (panel.mode) {
					case 0:
						if (panel.labyrinth[i][j] == 'V') {
							panel.xDoor = panel.yDoor = -1;
							break;
						}
					case 1:
						if (panel.labyrinth[i][j] == 'X') break;
					case 2:
						if (Character.isLowerCase(panel.labyrinth[i][j])) break;
					default:
						return;
				}
			} else if (panel.mode == 2) return;
			switch (panel.mode) {
				case 0:
					panel.labyrinth[i][j] = 'X';
					break;
				case 1:
					panel.labyrinth[i][j] = 'V';
					if (panel.xDoor != -1 && panel.yDoor != -1) panel.labyrinth[panel.xDoor][panel.yDoor] = 'X';
					panel.xDoor = i;
					panel.yDoor = j;
					break;
				case 2:
					panel.labyrinth[i][j] = Character.toUpperCase(panel.labyrinth[i][j]);
					if (panel.xBat != -1 && panel.yBat != -1) panel.labyrinth[panel.xBat][panel.yBat] = Character.toLowerCase(panel.labyrinth[panel.xBat][panel.yBat]);
					panel.xBat = i;
					panel.yBat = j;
					break;
				case 3:
					panel.labyrinth[i][j] = 'i';
					if (panel.xPlayer != -1 && panel.yPlayer != -1) {
						panel.labyrinth[panel.xPlayer][panel.yPlayer] = ' ';
						if (panel.xPlayer == panel.xBat && panel.yPlayer == panel.yBat) {
							panel.xBat = i;
							panel.yBat = j;
						}
					}
					panel.xPlayer = i;
					panel.yPlayer = j;
					break;
				case 4:
					panel.labyrinth[i][j] = 's';
					break;
				case 5:
					panel.labyrinth[i][j] = 'k';
					break;
				case 6:
					panel.labyrinth[i][j] = 'r';
					break;
				case 7:
					panel.labyrinth[i][j] = 'p';
					break;
				case 8:
					panel.labyrinth[i][j] = 'o';
					break;
			}
			if (panel.mode > 2 && panel.xBat == i && panel.yBat == j) panel.labyrinth[i][j] = Character.toUpperCase(panel.labyrinth[i][j]);
		}
		panel.repaint();
	}
}