package racgr.labmaker;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;


/**
* Izdelovalec labirintov.
*
* @author	Janoš Vidali, 63040303
*/
class LabMaker extends JPanel implements ActionListener, ChangeListener {
	/**
	* Ime datoteke.
	*/
	JLabel file = new JLabel();
	
	/**
	* Pogled na labirint.
	*/
	JScrollPane sp;
	
	/**
	* Gumb za nalaganje.
	*/
	JButton load = new JButton("Odpri");
	
	/**
	* Gumb za shranjevanje.
	*/
	JButton save = new JButton("Shrani");
	
	/**
	* Gumb za shranjevanje pod drugim imenom.
	*/
	JButton saveas = new JButton("Shrani kot");
	
	/**
	* Gumb za pošišèenje.
	*/
	JButton clear = new JButton("Poèisti");
	
	/**
	* Polje za širino.
	*/
	JTextField widthField = new JTextField();
	
	/**
	* Polje za dolžino.
	*/
	JTextField lengthField = new JTextField();
	
	/**
	* Znak x.
	*/
	JLabel labelX = new JLabel("x");
	
	/**
	* Gumb za spreminjanje velikosti labirinta.
	*/
	JButton confirm = new JButton("Potrdi");
	
	/**
	* Oznaka za poveèavo.
	*/
	JLabel labelZoom = new JLabel("Poveèava:");
	
	/**
	* Drsnik za uravnavanje velikosti pogleda.
	*/
	JSlider scroll = new JSlider(10, 30, 20);
	
	/**
	* Stikalo za vklop in izklop mreže.
	*/
	JCheckBox gridCheck = new JCheckBox("Pokaži mrežo",true);
	
	/**
	* Okno za odpiranje in zapiranje datoteke.
	*/
	JFileChooser chooser = new JFileChooser(".");
	
	/**
	* Gumbi za risanje objektov.
	*/
	JToggleButton[] buttons = {
		new JToggleButton("Zid",new Objects.Wall()),
		new JToggleButton("Vrata",new Objects.Door()),
		new JToggleButton("Kij",new Objects.Bat()),
		new JToggleButton("Bratranec Itt",new Objects.CousinItt()),
		new JToggleButton("Stožec",new Objects.Cone()),
		new JToggleButton("Kocka",new Objects.Cube()),
		new JToggleButton("Pirakocka",new Objects.PyraCube()),
		new JToggleButton("Piramida",new Objects.Pyramide()),
		new JToggleButton("Krogla",new Objects.Sphere())
	};
	
	/**
	* Trenutno izbrani element.
	*/
	private JComponent focus;
	
	/**
	* Pogled na labirint;
	*/
	LabyrinthView view;
	
	/**
	* Širina labirinta.
	*/
	int width;
	
	/**
	* Dolžina labirinta.
	*/
	int length;
	
	/**
	* Velikost celice.
	*/
	int zoom = 20;
	
	/**
	* Tabela z labirintom.
	*/
	char[][] labyrinth;
	
	/**
	* Ali naj se prikaže mreža.
	*/
	boolean grid = true;
	
	/**
	* X koordinata igralca.
	*/
	int xPlayer = -1;
	
	/**
	* Y koordinata igralca.
	*/
	int yPlayer = -1;
	
	/**
	* X koordinata vrat.
	*/
	int xDoor = -1;
	
	/**
	* Y koordinata vrat.
	*/
	int yDoor = -1;
	
	/**
	* X koordinata kija.
	*/
	int xBat = -1;
	
	/**
	* Y koordinata kija.
	*/
	int yBat = -1;
	
	/**
	* Objekt, ki ga rišemo.
	*/
	int mode = 0;
	
	/**
	* Trenutno odprta datoteka.
	*/
	File openFile;
	
	/**
	* Okno programa.
	*/
	JFrame frame;
	
	/**
	* Konstruktor.
	*
	* @param file		datoteka, ki naj se odpre
	*/
	public LabMaker(String file, JFrame frame) {
		this.frame = frame;
		if (file == null || !readFile(new File(file))) {
			width = 100;
			length = 100;
			resetDimensionFields();
			clear();
		}
		view = new LabyrinthView(this);
		sp = new JScrollPane(view);
		load.addActionListener(this);
		save.addActionListener(this);
		saveas.addActionListener(this);
		clear.addActionListener(this);
		confirm.addActionListener(this);
		scroll.addChangeListener(this);
		gridCheck.addActionListener(this);
		for (JToggleButton tb : buttons) {
			tb.setHorizontalAlignment(JToggleButton.LEFT);
			tb.addActionListener(this);
		}
		buttons[0].setSelected(true);
	}
	
	/**
	* Metoda za zagon.
	*
	* @param args		vhodni argument: datoteka, ki naj se odpre
	*/
	public static void main(String[] args) {
		String file = null;
		if (args.length > 0) file = args[0];
		JFrame o = new JFrame("LabMaker");
		o.setSize(600,550);
        o.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        o.getContentPane().add(new LabMaker(file,o));
        o.setVisible(true);
	}
	
	/**
    * Metoda za risanje.
    *
    * @param g		risalna površina
    */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int w = getWidth();
		int h = getHeight();
		
		file.setBounds(5,5,w-150,20);
		sp.setBounds(5,30,w-150,h-50);
		
		load.setBounds(w-130,5,100,20);
		save.setBounds(w-130,30,100,20);
		saveas.setBounds(w-130,55,100,20);
		clear.setBounds(w-130,80,100,20);
		
		widthField.setBounds(w-130,120,40,20);
		labelX.setBounds(w-85,120,10,20);
		lengthField.setBounds(w-70,120,40,20);
		confirm.setBounds(w-130,145,100,20);
		
		labelZoom.setBounds(w-130,185,100,10);
		scroll.setBounds(w-130,210,100,20);
		gridCheck.setBounds(w-130,235,120,20);
		
		int hy = 250;
		for (JToggleButton tb : buttons) {
			tb.setBounds(w-135,hy+=25,125,20);
		}
		
		add(file);
		add(sp);
		add(load);
		add(save);
		add(saveas);
		add(clear);
		add(widthField);
		add(labelX);
		add(lengthField);
		add(confirm);
		add(labelZoom);
		add(scroll);
		add(gridCheck);
		for (JToggleButton tb : buttons) {
			add(tb);
		}
		
		view.setPreferredSize(new Dimension(zoom*width+4,zoom*length+4));
		view.revalidate();
		
		if (focus != null) focus.requestFocusInWindow();
	}
	
	/**
	* Metoda za branje iz datoteke.
	*
	* @param file		datoteka
	* @return			ali je odpiranje uspelo
	*/
	boolean readFile(File file) {
		try {
			Scanner sc = new Scanner(file);
			int lLength = sc.nextInt();
			int lWidth = sc.nextInt();
			boolean door = false;
			boolean bat = false;
			boolean player = false;
			boolean dooralert = false;
			boolean batalert = false;
			boolean playeralert = false;
			boolean alert = false;
			char[][] tmp = new char[2*lLength][2*lWidth];
			int xp = -1, yp = -1, xd = -1, yd = -1, xb = -1, yb = -1;
			String s;
			sc.nextLine();
			for (int i=0; i < lLength; i++) {
				s = sc.nextLine();
				for (int j=0; j < lWidth; j++) {
					tmp[i][j] = s.charAt(j);
					if ((i == 0 || j == 0 || i == lLength-1 || j == lWidth-1) && tmp[i][j] != 'V') tmp[i][j] = 'X';
					switch (tmp[i][j]) {
						case 'V':
							if (door) {
								if (!dooralert) {
									alert("Opozorilo: v datoteki je veè vrat. Upoštevam le prva.",JOptionPane.WARNING_MESSAGE);
									dooralert = true;
								}
								tmp[i][j] = 'X';
							} else {
								door = true;
								xd = i;
								yd = j;
							}
							break;
						case 'i':
						case 'I':
							if (player) {
								if (!playeralert) {
									alert("Opozorilo: v datoteki je veè igralcev. Upoštevam le prvega.",JOptionPane.WARNING_MESSAGE);
									playeralert = true;
								}
								tmp[i][j] = ' ';
							} else {
								player = true;
								xp = i;
								yp = j;
							}
							if (tmp[i][j] == 'i' || tmp[i][j] == ' ') break;
						case 'S':
						case 'K':
						case 'R':
						case 'P':
						case 'O':
							if (bat) {
								if (!batalert) {
									alert("Opozorilo: v datoteki je veè kijev. Upoštevam le prvega.",JOptionPane.WARNING_MESSAGE);
									batalert = true;
								}
								tmp[i][j] = Character.toLowerCase(tmp[i][j]);
							} else {
								bat = true;
								xb = i;
								yb = j;
							}
							break;
						case 's':
						case 'k':
						case 'r':
						case 'p':
						case 'o':
						case 'X':
						case ' ':
							break;
						default:
							tmp[i][j] = ' ';
							if (!alert) {
								alert("Opozorilo: v datoteki je neznan znak. Ignoriram.",JOptionPane.WARNING_MESSAGE);
								alert = true;
							}
							break;
					}
				}
			}
			
			if (!door) {
				alert("Opozorilo: v datoteki ni vrat.",JOptionPane.WARNING_MESSAGE);
			}
			if (!player) {
				alert("Opozorilo: v datoteki ni igralca.",JOptionPane.WARNING_MESSAGE);
			}
			if (!bat) {
				alert("Opozorilo: v datoteki ni kija.",JOptionPane.WARNING_MESSAGE);
			}
			
			labyrinth = tmp;
			length = lLength;
			width = lWidth;
			xPlayer = xp;
			yPlayer = yp;
			xDoor = xd;
			yDoor = yd;
			xBat = xb;
			yBat = yb;
			this.file.setText(file.getName());
			resetDimensionFields();
			sc.close();
			openFile = file;
			repaint();
			return true;
		} catch (Exception e) {
			alert("Napaka pri odpiranju datoteke: "+e,JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	
	/**
	* Metoda za shranjevanje v datoteko.
	*
	* @param file		datoteka
	* @return			ali je shranjevanje uspelo
	*/
	boolean saveFile(File file) {
		if (!checkForConsistency()) return false;
		try {
			FileWriter out = new FileWriter(file);
			out.write(length+" "+width+"\n");
			for (int i=0; i < length; i++) {
				for (int j=0; j < width; j++) {
					out.write(labyrinth[i][j]);
				}
				out.write('\n');
			}
			out.close();
			return true;
		} catch (IOException e) {
			alert("Napaka pri shranjevanju datoteke: "+e,JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	
	/**
	* Preveri, èe ima mapa igralca, kij in vrata.
	*
	* @return		ali se uporabnik strinja z morebitnimi pomanjkljivostmi
	*/
	boolean checkForConsistency() {
		if (xPlayer == -1 && yPlayer == -1 && !ask("V mapi ni igralca. Shranim vseeno?")) return false;
		if (xBat == -1 && yBat == -1 && !ask("V mapi ni kija. Shranim vseeno?")) return false;
		if (xDoor == -1 && yDoor == -1 && !ask("V mapi ni vrat. Shranim vseeno?")) return false;
		return true;
	}
	
	/**
	* Metoda za poèišèenje.
	*/
	void clear() {
		this.file.setText(null);
		openFile = null;
		if (labyrinth == null || labyrinth.length < length || labyrinth[0].length < width) {
			labyrinth = new char[2*length][2*width];
		}
		for (int i=1; i < length-1; i++) {
			for (int j=1; j < width-1; j++) {
				labyrinth[i][j] = ' ';
			}
		}
		for (int i=1; i < width; i++) {
			labyrinth[0][i-1] = 'X';
			labyrinth[length-1][i] = 'X';
		}
		for (int j=1; j < length; j++) {
			labyrinth[j][0] = 'X';
			labyrinth[j-1][width-1] = 'X';
		}
	}
	
	/**
	* Metoda za spreminjanje velikosti labirinta.
	*/
	void resize() {
		int oldWidth = width;
		int oldLength = length;
		readDimensions();
		if (width == oldWidth && length == oldLength) return;
		if (labyrinth.length < length || labyrinth[0].length < width) {
			if (oldWidth > width) oldWidth = width;
			if (oldLength > length) oldLength = length;
			char[][] tmp = labyrinth;
			clear();
			for (int i=0; i < oldLength; i++) {
				System.arraycopy(tmp[i],0,labyrinth[i],0,oldWidth);
			}
		} else {
			for (int i=oldLength; i < length-1; i++) {
				labyrinth[i][0] = 'X';
				for (int j=1; j < width-1; j++) {
					labyrinth[i][j] = ' ';
				}
			}
			for (int j=oldWidth; j < width-1; j++) {
				labyrinth[0][j] = 'X';
				for (int i=1; i < length-1; i++) {
					labyrinth[i][j] = ' ';
				}
			}
			for (int i=0; i < length; i++) {
				labyrinth[i][width-1] = 'X';
			}
			for (int j=0; j < width-1; j++) {
				labyrinth[length-1][j] = 'X';
			}
		}
		if (xDoor >= 0 && yDoor >= 0 && xDoor < length && yDoor < width) {
			labyrinth[xDoor][yDoor] = 'V';
		} else {
			xDoor = yDoor = -1;
		}
		if (xBat < 0 || yBat < 0 || xBat >= length || yBat >= width) {
			xBat = yBat = -1;
		}
		if (xPlayer < 0 || yPlayer < 0 || xPlayer >= length || yPlayer >= width) {
			xPlayer = yPlayer = -1;
		}
	}
	
	/**
	* Prebere dimenzije iz vnosnih polj.
	*/
	private void readDimensions() {
		int tmp;
		try {
			tmp = Integer.parseInt(widthField.getText());
			if (tmp > 4) {
				width = tmp;
			} else {
				width = 4;
			}
		} catch (NumberFormatException e) {}
		try {
			tmp = Integer.parseInt(lengthField.getText());
			if (tmp > 4) {
				length = tmp;
			} else {
				length = 4; 
			}
		} catch (NumberFormatException e) {}
		resetDimensionFields();
	}
	
	/**
	* Ponastavi oznaki z dimenzijo.
	*/
	private void resetDimensionFields() {
		widthField.setText(width+"");
		lengthField.setText(length+"");
	}
	
	/**
	* Opozorilo.
	*
	* @param s		besedilo opozorila
	*/
	void alert(String s, int messageType) {
		String title = null;
		switch (messageType) {
			case JOptionPane.WARNING_MESSAGE:
				title = "Opozorilo";
				break;
			case JOptionPane.ERROR_MESSAGE:
				title = "Napaka";
				break;
		}
		JOptionPane.showMessageDialog(frame,s,title,messageType);
	}
		
	/**
	* Vprašanje.
	*
	* @param s		besedilo vprašanja
	* @return		odgovor
	*/
	boolean ask(String s) {
		return JOptionPane.showConfirmDialog(frame,s,"Opozorilo",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
	}
	
	/**
    * Izvede ustrezno akcijo ob pritisku na gumb.
    *
    * @param e		dogodek
    */
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if (src == load) {
			if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				readFile(chooser.getSelectedFile());
			}
		} else if (src == save && openFile != null) {
			saveFile(openFile);
		} else if (src == save || src == saveas) {
			if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				if (saveFile(chooser.getSelectedFile())) {
					openFile = chooser.getSelectedFile();
					this.file.setText(openFile.getName());
				}
			}
		} else if (src == clear) {
			readDimensions();
			clear();
			repaint();
		} else if (src == confirm) {
			resize();
			repaint();
		} else if (src == gridCheck) {
			grid = gridCheck.isSelected();
			repaint();
		} else {
			for (int i=0; i < buttons.length; i++) {
				buttons[i].setSelected(src == buttons[i]);
				if (src == buttons[i]) mode = i;
			}
		}
		try {
			focus = (JComponent)src;
		} catch (ClassCastException ex) {}
	}
		
	/**
    * Izvede ustrezno akcijo ob spremembi položaja drsnika.
    *
    * @param e		dogodek
    */
	public void stateChanged(ChangeEvent e) {
		zoom = scroll.getValue();
		focus = scroll;
		repaint();
	}
}