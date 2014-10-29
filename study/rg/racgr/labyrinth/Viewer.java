package racgr.labyrinth;

import java.util.Vector;
import java.util.Scanner;
import java.io.File;
//import java.io.FileWriter;
import java.io.IOException;
import org.lwjgl.Sys;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.glu.GLU;

/**
* Razred za prikazovanje.
*
* @author Lovro Šubelj, 63040296
* @author Janoš Vidali, 63040303
*/
public class Viewer implements Constants {
	/**
	* Ali naj program konèa.
	*/
	private static boolean finished;
		
	/**
	* Ali je igre konec.
	*/
	private static boolean over;
		
	/**
	* Ali smo šele pognali igro.
	*/
	private static boolean start = true;
		
	/**
	* Ali naj se pokaže menu.
	*/
	private static boolean menu = true;
	
	/**
	* Tekst, ki naj se pokaže.
	*/
	private static String message;
	
	/**
	* Izbira v menuju.
	*/
	private static int menuSelect = 1;
		
	/**
	* Tekst menuja.
	*/
	private static String[] menuText = {"Nadaljuj","Nova igra","Izhod"};

	/**
	* Natanènost zemljevida.
	*/
	private static float MAP_DETAIL;
		
	/**
	* Pisava menuja.
	*/
	private static GLFont font;
		
	/**
	* Datoteka, iz katere se naloži mapa.
	*/
	private static String file;
	
	/**
	* Metoda za zagon programa.
	*
	* @param args		parametri: <pre>-fullscreen</pre> za celozaslonski naèin,
						<pre>-load &lt;ime_datoteke&gt;</pre> za nalaganje labirinta iz datoteke
	*/
	public static void main(String[] args) {
		boolean fullscreen = false;
//		boolean random = false;
		boolean load = false;
//		boolean save = false;
		for (String s : args) {
			if (s.equals("-fullscreen")) {
				fullscreen = true;
				continue;
			}
//			if (s.equals("-random")) {
//				random = true;
//				continue;
//			}
			
			if (load /*|| save*/) file = s;
			
			if (s.equals("-load")) load = true;
//			if (s.equals("-save")) save = true;
		}
		if (file == null) {
			if (load) load = false;
//			if (save) file = "map.out";
		}
		
	    try {
	    	init(fullscreen,load);
	    	run();
	    } catch (Exception e) {
	    	e.printStackTrace(System.err);
	    	Sys.alert(TITLE, "An error occured and the " + TITLE + " will exit.");
	    } finally {
	    	cleanup();
	    }
	    
	    System.exit(0);
	}
	 
	/**
	* Metoda za inicializacijo.
	*
	* @param fullscreen		ali naj se požene v celozaslonskem naèinu
	* @param load			ali naj se labirint naloži iz datoteke
	* @throws LWJGLException	ob napaki v knjižnici LWJGL
	* @throws IOException	ob napaki pri branju datoteke
	*/
	private static void init(boolean fullscreen, boolean load) throws LWJGLException, IOException {
		Display.setTitle(TITLE);
	    Display.setFullscreen(fullscreen);
	    Display.setVSyncEnabled(true);
	    Display.create();
	    
	    GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		GL11.glShadeModel(GL11.GL_SMOOTH); // Enable Smooth Shading
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		GL11.glClearDepth(1.0f);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glShadeModel(GL11.GL_SMOOTH);  
			
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(45.0f, 800.0f / 600.0f, 0.1f, 500.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
		
		font = new GLFont("Courier New");
		
		GL11.glPushMatrix();
		
		// Initializes parameters from user.
//		if (random &&false) {
//			Labyrinth.cellsInWidth = 24 * CELLS_FOR_SINGLE_TEXTURE_FOR_FLOOR;
//			Labyrinth.cellsInLength = 96 * CELLS_FOR_SINGLE_TEXTURE_FOR_FLOOR;
//			userLabyrinth = new boolean[Labyrinth.cellsInLength][Labyrinth.cellsInWidth];
//			for(int i = 0; i < Labyrinth.cellsInLength; i++)
//				for(int j = 0; j < Labyrinth.cellsInWidth; j++)
//					userLabyrinth[i][j] = j == Labyrinth.cellsInWidth - 1 || j == 0 || i == Labyrinth.cellsInLength - 1 || i == 0 || (int)(Math.random() * 4) == 0;
//			Labyrinth.startWidth = Labyrinth.cellsInWidth / 2;
//			Labyrinth.startLength = 96 * CELLS_FOR_SINGLE_TEXTURE_FOR_FLOOR - 2;
//			while (Labyrinth.startWidth < Labyrinth.cellsInWidth && userLabyrinth[Labyrinth.startLength][Labyrinth.startWidth]) Labyrinth.startWidth++;
//			if (Labyrinth.startWidth == Labyrinth.cellsInWidth) {
//				Labyrinth.startWidth = Labyrinth.cellsInWidth / 2;
//				while (Labyrinth.startWidth >= 0 && userLabyrinth[Labyrinth.startLength][Labyrinth.startWidth]) Labyrinth.startWidth--;
//			}
//			Labyrinth.doorsWidth = Labyrinth.cellsInWidth / 2;
//			Labyrinth.doorsLength = 0;
//			while (Labyrinth.doorsWidth < Labyrinth.cellsInWidth && userLabyrinth[1][Labyrinth.doorsWidth]) Labyrinth.doorsWidth++;
//			if (Labyrinth.doorsWidth == Labyrinth.cellsInWidth) {
//				Labyrinth.doorsWidth = Labyrinth.cellsInWidth / 2;
//				while (Labyrinth.doorsWidth >= 0 && userLabyrinth[1][Labyrinth.doorsWidth]) Labyrinth.startWidth--;
//			}
		
		// Initializes textures.
		Labyrinth.tex[0] = Texture.loadTexture("racgr\\labyrinth\\floor_bs.jpg", false);
		Labyrinth.tex[1] = Texture.loadTexture("racgr\\labyrinth\\walls_wb.jpg", false);
		Labyrinth.tex[2] = Texture.loadTexture("racgr\\labyrinth\\kompas_mask.bmp", false);
		Labyrinth.tex[3] = Texture.loadTexture("racgr\\labyrinth\\kompas3.bmp", false);
		Labyrinth.tex[4] = Texture.loadTexture("racgr\\labyrinth\\map_mask.bmp", false);
		Labyrinth.tex[5] = Texture.loadTexture("racgr\\labyrinth\\door_cr2.jpg", false);
		Labyrinth.tex[6] = Texture.loadTexture("racgr\\labyrinth\\bat3_mask.jpg", false);
		Labyrinth.tex[7] = Texture.loadTexture("racgr\\labyrinth\\bat3.jpg", false);
		Labyrinth.tex[8] = Texture.loadTexture("racgr\\labyrinth\\hair.jpg", false);
		Labyrinth.tex[9] = Texture.loadTexture("racgr\\labyrinth\\chars1.jpg", false);
		Labyrinth.tex[10] = Texture.loadTexture("racgr\\labyrinth\\chars.jpg", false);
		Labyrinth.tex[11] = Texture.loadTexture("racgr\\labyrinth\\chars2.jpg", false);
		Labyrinth.tex[12] = Texture.loadTexture("racgr\\labyrinth\\chars3.jpg", false);
		Labyrinth.tex[13] = Texture.loadTexture("racgr\\labyrinth\\chars5.jpg", false);
		Labyrinth.tex[14] = Texture.loadTexture("racgr\\labyrinth\\bat_tex.jpg", false);
		
		Labyrinth.bat = new Bat(Labyrinth.tex[14]);
		////////////////////////////////////////////
		//load = true;
		//file = "racgr\\labyrinth\\map.out";
		
		initializeLabyrinth();
		
//		if (save) saveMap(file);
		
		MAP_DETAIL = 0.0025f;
		
		Mouse.setGrabbed(true);
	}
	
	/**
	* Inicializira labirint.
	*
	* @throws IOException	ob napaki pri branju datoteke
	*/
	private static void initializeLabyrinth() throws IOException {
		Labyrinth.chars = new Vector<Object3D>();
		Labyrinth.chars.add(null);
		Labyrinth.chars.add(null);
		
		if (file != null) {
			loadMap(file);
		} else {
			Labyrinth.cellsInWidth = 31;
			Labyrinth.cellsInLength = 16;
			Labyrinth.doorsWidth = 4;
			Labyrinth.doorsLength = 0;
			boolean[][] labyrinth = new boolean[][] {
				{true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true},
				{true, false, false, false, false, false, false, false, false, true, false, false, false, false, false, false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, true},
				{true, false, false, false, false, false, false, false, false, true, false, false, false, false, false, false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, true},
				{true, false, false, true, true, true, true, false, false, true, false, false, true, true, true, true, false, false, true, false, false, true, true, true, true, true, true, true, false, false, true},
				{true, false, false, true, false, false, false, false, false, true, false, false, false, false, false, true, false, false, false, false, false, true, false, false, true, false, false, false, false, false, true},
				{true, false, false, true, false, false, false, false, false, true, false, false, false, false, false, true, false, false, false, false, false, true, false, false, true, false, false, false, false, false, true},
				{true, false, false, true, true, true, true, true, true, true, true, true, true, false, false, true, true, true, true, false, false, true, false, false, true, false, false, true, true, true, true},
				{true, false, false, false, false, false, true, false, false, false, false, false, true, false, false, false, false, false, true, false, false, false, false, false, true, false, false, true, false, false, true},
				{true, false, false, false, false, false, true, false, false, false, false, false, true, false, false, false, false, false, true, false, false, false, false, false, true, false, false, true, false, false, true},
				{true, true, true, true, false, false, true, false, false, true, true, true, true, false, false, true, false, false, true, true, true, true, true, true, true, false, false, true, false, false, true},
				{true, false, false, false, false, false, false, false, false, true, false, false, false, false, false, true, false, false, false, false, false, true, false, false, false, false, false, false, false, false, true},
				{true, false, false, false, false, false, false, false, false, true, false, false, false, false, false, true, false, false, false, false, false, true, false, false, false, false, false, false, false, false, true},
				{true, false, false, true, true, true, true, true, true, true, false, false, true, false, false, true, true, true, true, true, true, true, false, false, true, true, true, true, true, true, true},
				{true, false, false, false, false, false, false, false, false, false, false, false, true, false, false, false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, true},
				{true, false, false, false, false, false, false, false, false, false, false, false, true, false, false, false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, true},
				{true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true},
			};
			
			Labyrinth.player = new CousinItt(29*CELL_SIZE,0,-(Labyrinth.cellsInLength-14)*CELL_SIZE,Labyrinth.tex[8]);
			Labyrinth.player.ai = new PlayerAI(Labyrinth.player);
			Labyrinth.chars.set(0, new Camera(Labyrinth.player));
			Labyrinth.chars.set(1, Labyrinth.player);
			
			Object3D obj1 = new Cone(27*CELL_SIZE,0,-(Labyrinth.cellsInLength-14)*CELL_SIZE,Labyrinth.tex[12]);
			obj1.ai = new CharAI(obj1);
			Labyrinth.chars.add(obj1);
			
			Object3D obj2 = new Cube(25*CELL_SIZE,0,-(Labyrinth.cellsInLength-14)*CELL_SIZE,Labyrinth.tex[13]);
			obj2.ai = new CharAI(obj2);
			Labyrinth.chars.add(obj2);
			
			Object3D obj3 = new Sphere(23*CELL_SIZE,0,-(Labyrinth.cellsInLength-13)*CELL_SIZE,Labyrinth.tex[9]);
			obj3.ai = new CharAI(obj3);
			Labyrinth.chars.add(obj3);
			
			obj3.receiveBat();
			
			buildLabyrinth(labyrinth, Labyrinth.doorsLength, Labyrinth.doorsWidth);
		}
	}
	
	/**
	* Gradnja zidov iz tabele booleanov.
	* 
	* @param labyrinth		labirint, katerega zidove gradimo
	* @param doorsLength	položaj vrat po dolžini
	* @param doorsWidth		položaj vrat po širini
	*/
	private static void buildLabyrinth(boolean[][] labyrinth, int doorsLength, int doorsWidth) {
		Labyrinth.labyrinth = new byte[Labyrinth.cellsInLength][Labyrinth.cellsInWidth];
		Labyrinth.walls = new Wall[Labyrinth.cellsInLength][Labyrinth.cellsInWidth];
		
		int numberRight;
		int numberDown;
		int tmp;
		Wall wall;
		
		Labyrinth.labyrinth[doorsLength][doorsWidth] = 1;
		Labyrinth.walls[doorsLength][doorsWidth] = new BreakableWall(doorsWidth*CELL_SIZE, 0, -(Labyrinth.cellsInLength-doorsLength)*CELL_SIZE, CELL_SIZE, WALL_HEIGHT, CELL_SIZE, Labyrinth.tex[5]);
		
		for(int i = 0; i < Labyrinth.cellsInLength; i++) {
			for(int j = 0; j < Labyrinth.cellsInWidth; j++) {
				if (labyrinth[i][j] && (doorsLength != i || doorsWidth != j)) {
					Labyrinth.labyrinth[i][j] = 1;
					wall = Labyrinth.walls[i][j] = new Wall(j*CELL_SIZE, 0, -(Labyrinth.cellsInLength-i)*CELL_SIZE, CELL_SIZE, WALL_HEIGHT, CELL_SIZE, Labyrinth.tex[1]);
					
					numberRight = 0;
					tmp = j + 1;
					while (tmp < Labyrinth.cellsInWidth) {
						if (!labyrinth[i][tmp] || (doorsLength == i && doorsWidth == tmp))
							break;
						labyrinth[i][tmp] = false;
						Labyrinth.labyrinth[i][tmp] = 2;
						wall.bb.w += CELL_SIZE;
						Labyrinth.walls[i][tmp] = wall;
						
						numberRight++;
						tmp++;
					}
					labyrinth[i][j] = false;
						
					// Draws wall to the right or down. When there exists a wall to the right, which has
					// not yet been drawn, draws to the right... 
					if (numberRight == 0) {
						numberDown = 0;
						tmp = i + 1;
						while (tmp < Labyrinth.cellsInLength) {
							if (!labyrinth[tmp][j] || (doorsLength == tmp && doorsWidth == j))
								break;
							labyrinth[tmp][j] = false;
							Labyrinth.labyrinth[tmp][j] = 3;
							wall.bb.d += CELL_SIZE;
							Labyrinth.walls[tmp][j] = wall;
							numberDown++;
							tmp++;
						}
					}
				}
//				else if (labyrinth[i][j] && Labyrinth.doorsLength == i && Labyrinth.doorsWidth == j) {
//					Labyrinth.labyrinth[i][j] = 1;
//					Labyrinth.walls[i][j] = new BreakableWall(j*CELL_SIZE, 0, -(Labyrinth.cellsInLength-i)*CELL_SIZE, CELL_SIZE, WALL_HEIGHT, CELL_SIZE, Labyrinth.tex[5]);
//				}
			}
		}
	}
	
	/**
	* Shrani labirint v datoteko.
	*
	* V prvo vrstico napiše število vrstic in stolpcev labirinta,
	* zaèetni položaj igralca v dveh koordinatah, položaj vrat v 
	* dveh koordinatah, nato pa v vsako vrstico znak X, 
	* èe je tam zid, ali presledek, èe je tam prazen prostor.
	*
	* @param file		datoteka, kamor naj se shrani labirint
	*/
	/*private static void saveMap(String file) throws IOException {
		FileWriter out = new FileWriter(file);
		out.write(Labyrinth.cellsInLength+" "+Labyrinth.cellsInWidth+"\n");
		out.write(Labyrinth.startLength+" "+Labyrinth.startWidth+"\n");
		out.write(Labyrinth.doorsLength+" "+Labyrinth.doorsWidth+"\n");
		for (int i=0; i < Labyrinth.cellsInLength; i++) {
			for (int j=0; j < Labyrinth.cellsInWidth; j++) {
				out.write(userLabyrinth[i][j] ? 'X' : ' ');
			}
			out.write('\n');
		}
		out.close();
	}*/
	
	/**
	* Naloži labirint iz datoteke.
	*
	* Datoteka mora v prvi vrstici imeti napisani število vrstic
	* in število znakov v vsaki vrstici, nato pa še zaèetni položaj
	* igralca in položaj vrat v dveh koordinatah. Sledijo vrstice, 
	* ki opisujejo labirint. Pomen znakov (velika èrka pomeni, 
	* da osebek poseduje kij):
	* <ul>
	*	<li>X: zid</li>
	*	<li>i, I: bratranec Itt - igralec</li>
	*	<li>s, S: stožec - raèunalnik</li>
	*	<li>k, K: kocka - raèunalnik</li>
	*	<li>r, R: pirakocka - raèunalnik</li>
	*	<li>p, P: piramida - raèunalnik</li>
	*	<li>o, O: krogla - raèunalnik</li>
	* </ul>
	* Število vrstic mora biti vsaj enako podanemu številu, prav tako
	* mora biti vsaka vrstica vsaj tako dolga, kot je podano.
	*
	* @param file		datoteka, iz katere naj se naloži labirint
	* @throws IOException	ob napaki pri branju datoteke
	*/
	private static void loadMap(String file) throws IOException {
		Scanner sc = new Scanner(new File(file));
		Labyrinth.cellsInLength = sc.nextInt();
		Labyrinth.cellsInWidth = sc.nextInt();
		boolean batTaken = false;
		boolean[][] labyrinth = new boolean[Labyrinth.cellsInLength][Labyrinth.cellsInWidth];
		String s;
		sc.nextLine();
		for (int i=0; i < Labyrinth.cellsInLength; i++) {
			s = sc.nextLine();
			for (int j=0; j < Labyrinth.cellsInWidth; j++) {
				labyrinth[i][j] = false;
				
				Object3D obj;
				switch (s.charAt(j)) {
					case 'X': //zid
						labyrinth[i][j] = true;
						break;
					case 'V': //vrata
						Labyrinth.doorsLength = i;
						Labyrinth.doorsWidth = j;
						break;
					case 'i': //igralec
						Labyrinth.player = new CousinItt(j*CELL_SIZE,0,-(Labyrinth.cellsInLength-i)*CELL_SIZE,Labyrinth.tex[8]);
						Labyrinth.player.ai = new PlayerAI(Labyrinth.player);
						Labyrinth.chars.set(0, new Camera(Labyrinth.player));
						Labyrinth.chars.set(1, Labyrinth.player);
						break;
					case 'I':
						Labyrinth.player = new CousinItt(j*CELL_SIZE,0,-(Labyrinth.cellsInLength-i)*CELL_SIZE,Labyrinth.tex[8]);
						Labyrinth.player.ai = new PlayerAI(Labyrinth.player);
						Labyrinth.chars.set(0, new Camera(Labyrinth.player));
						Labyrinth.chars.set(1, Labyrinth.player); 
						if (!batTaken) {
							Labyrinth.player.receiveBat();
							batTaken = true;
						}
						break;
					case 's': // stožec
						obj = new Cone(j*CELL_SIZE,0,-(Labyrinth.cellsInLength-i)*CELL_SIZE,Labyrinth.tex[12]);
						obj.ai = new CharAI(obj);
						Labyrinth.chars.add(obj);
						break;
					case 'S':
						obj = new Cone(j*CELL_SIZE,0,-(Labyrinth.cellsInLength-i)*CELL_SIZE,Labyrinth.tex[12]);
						obj.ai = new CharAI(obj);
						Labyrinth.chars.add(obj);
						if (!batTaken) {
							obj.receiveBat();
							batTaken = true;
						}
						break;
					case 'k': // kocka
						obj = new Cube(j*CELL_SIZE,0,-(Labyrinth.cellsInLength-i)*CELL_SIZE,Labyrinth.tex[13]);
						obj.ai = new CharAI(obj);
						Labyrinth.chars.add(obj);
						break;
					case 'K':
						obj = new Cube(j*CELL_SIZE,0,-(Labyrinth.cellsInLength-i)*CELL_SIZE,Labyrinth.tex[13]);
						obj.ai = new CharAI(obj);
						Labyrinth.chars.add(obj);
						if (!batTaken) {
							obj.receiveBat();
							batTaken = true;
						}
						break;
					case 'r': // pirakocka
						obj = new PyraCube(j*CELL_SIZE,0,-(Labyrinth.cellsInLength-i)*CELL_SIZE,Labyrinth.tex[11]);
						obj.ai = new CharAI(obj);
						Labyrinth.chars.add(obj);
						break;
					case 'R':
						obj = new PyraCube(j*CELL_SIZE,0,-(Labyrinth.cellsInLength-i)*CELL_SIZE,Labyrinth.tex[11]);
						obj.ai = new CharAI(obj);
						Labyrinth.chars.add(obj);
						if (!batTaken) {
							obj.receiveBat();
							batTaken = true;
						}
						break;
					case 'p': // piramida
						obj = new Pyramide(j*CELL_SIZE,0,-(Labyrinth.cellsInLength-i)*CELL_SIZE,Labyrinth.tex[10]);
						obj.ai = new CharAI(obj);
						Labyrinth.chars.add(obj);
						break;
					case 'P':
						obj = new Pyramide(j*CELL_SIZE,0,-(Labyrinth.cellsInLength-i)*CELL_SIZE,Labyrinth.tex[10]);
						obj.ai = new CharAI(obj);
						Labyrinth.chars.add(obj);
						if (!batTaken) {
							obj.receiveBat();
							batTaken = true;
						}
						break;
					case 'o': // krogla
						obj = new Sphere(j*CELL_SIZE,0,-(Labyrinth.cellsInLength-i)*CELL_SIZE,Labyrinth.tex[9]);
						obj.ai = new CharAI(obj);
						Labyrinth.chars.add(obj);
						break;
					case 'O':
						obj = new Sphere(j*CELL_SIZE,0,-(Labyrinth.cellsInLength-i)*CELL_SIZE,Labyrinth.tex[9]);
						obj.ai = new CharAI(obj);
						Labyrinth.chars.add(obj);
						if (!batTaken) {
							obj.receiveBat();
							batTaken = true;
						}
						break;
					default:
						break;
				}
			}
		}
		sc.close();
		
		buildLabyrinth(labyrinth, Labyrinth.doorsLength, Labyrinth.doorsWidth);
	}
	 
	/**
	* Glavna zanka programa.
	*
	* @throws IOException	ob napaki pri branju datoteke
	*/
	private static void run() throws IOException {
		while (!finished) {
			Display.update();
	    	
	    	if (Display.isCloseRequested()) {
	    		finished = true;
	    	} 
	    	else if (Display.isActive()) {
	    		logic();
	    		render();
	  
	    		Display.sync(FRAMERATE);
	    	} 
	    	else {
	    		try {
	    			Thread.sleep(100);
	    		} catch (InterruptedException e) {}
	    		
	    		logic();
	    		if (Display.isVisible() || Display.isDirty()) {
	    			render();
	    		}
	    	}
	    }
	}
	 
	/**
	* Metoda za poèišèenje ob napaki.
	*/
	private static void cleanup() {
		Display.destroy();
	}
	 
	/**
	* Glavna metoda za nadzor dogajanja.
	*
	* <ul>
	*	<li>C: preklop na kamero</li>
	*	<li>X: preklop na igralca</li>
	*	<li>N: poveèanje natanènosti zemljevida</li>
	*	<li>M: zmanjšanje natanènosti zemljevida</li>
	*	<li>SPACE: razbijanje vrat - le èe igralec poseduje kij</li>
	* </ul>
	*
	* @throws IOException	ob napaki pri branju datoteke
	*/
	private static void logic() throws IOException {
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			if (!over && !start) {
				menu = !menu;
				menuSelect = 0;
			}
		}

		if (menu) {
			//Preberi miškin buffer
			Mouse.getDX();
			Mouse.getDY();
			
			if (message != null) {
				if (Keyboard.isKeyDown(Keyboard.KEY_RETURN) || Keyboard.isKeyDown(Keyboard.KEY_NUMPADENTER)) {
					message = null;
					menuSelect = 1;
				}
			} else {
				if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
					if (menuSelect > (over || start ? 1 : 0)) menuSelect--;
				} else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
					if (menuSelect < 2) menuSelect++;
				} else if (Keyboard.isKeyDown(Keyboard.KEY_RETURN) || Keyboard.isKeyDown(Keyboard.KEY_NUMPADENTER)) {
					switch (menuSelect) {
						case 0:
							menu = false;
							break;
						case 1:
							if (!start) initializeLabyrinth();
							menu = false;
							start = over = false;
							break;
						case 2:
							finished = true;
							break;
					}
				}
			}
		} else {
			if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) && Labyrinth.player.hasBat) {
				//Udarec s kijem - preverimo, èe je igralec zmagal
				int imin = Labyrinth.cellsInLength+(int)(Labyrinth.player.bb.z/CELL_SIZE)-1;
				int imax = Labyrinth.cellsInLength+(int)((Labyrinth.player.bb.z+Labyrinth.player.bb.d)/CELL_SIZE)-1;
				
				int jmin = (int)(Labyrinth.player.bb.x/CELL_SIZE);
				int jmax = (int)((Labyrinth.player.bb.x+Labyrinth.player.bb.w)/CELL_SIZE);
				
				if (imin == imax && jmin == jmax && Math.sqrt(Math.pow(imin - Labyrinth.doorsLength,2) + Math.pow(jmin - Labyrinth.doorsWidth,2)) <= 1) {
					over = true;
					menu = true;
					message = "Bravo... Prisli ste iz labirinta.";
				}
				else
					Labyrinth.player.swing();
			}
			
			Object3D obj;
			for (int i=2; i < Labyrinth.chars.size(); i++) {
				obj = Labyrinth.chars.get(i);
				if (obj.hasBat) {
					// Preverimo, èe je zmagal kateri drugi osebek
					int imin = Labyrinth.cellsInLength+(int)(obj.bb.z/CELL_SIZE)-1;
					int imax = Labyrinth.cellsInLength+(int)((obj.bb.z+obj.bb.d)/CELL_SIZE)-1;
					
					int jmin = (int)(obj.bb.x/CELL_SIZE);
					int jmax = (int)((obj.bb.x+obj.bb.w)/CELL_SIZE);
					
					if (imin == imax && jmin == jmax && Math.sqrt(Math.pow(imin - Labyrinth.doorsLength,2) + Math.pow(jmin - Labyrinth.doorsWidth,2)) <= 1) {
						over = true;
						menu = true;
						message = "Osebek stevilka "+(i-1)+" je nasel pot ven.\nOcitno danes ni vas dan:)";
					}
				}
			}
			
			if (Keyboard.isKeyDown(Keyboard.KEY_C)) {
				//Preklop na kamero
				Object3D cam = Labyrinth.chars.get(0);
				cam.bb.x = Labyrinth.player.bb.x;
				cam.bb.z = Labyrinth.player.bb.z;
				cam.eyeLeftRight = Labyrinth.player.eyeLeftRight;
				Labyrinth.player = cam;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_X)) {
				//Preklop na igralca
				Labyrinth.player = Labyrinth.chars.get(1);
			}
			
			// Prilagajanje natanènosti zemljevida.
			if (Keyboard.isKeyDown(Keyboard.KEY_N))
				MAP_DETAIL *= 1.1f;
			else if (Keyboard.isKeyDown(Keyboard.KEY_M))
				MAP_DETAIL /= 1.1f;
			
			Labyrinth.player.move();
			
			for (int i=1; i < Labyrinth.chars.size(); i++) {
				obj = Labyrinth.chars.get(i);
				if (i > 1) obj.move();
				for (int j=i+1; j < Labyrinth.chars.size(); j++) {
					obj.collide(Labyrinth.chars.get(j), 0.1f, true);
				}
				//obj.numCollisions = 0;
				obj.collideWalls();
				obj.collideMove[0] = 0;
				obj.collideMove[1] = 0;
				obj.collideMove[2] = 0;
			}
		}
	}
	
	/**
	* Glavna metoda za risanje.
	*/
	private static void render() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_ACCUM_BUFFER_BIT);
		GL11.glLoadIdentity();
		
		GL11.glPushMatrix();
		
		// Changes camera position and rotation.
		Labyrinth.player.look();

		//Izberemo zaèetek in konec izrisanega obmoèja
		//Zaenkrat rišemo vse
		int imin = 0;
		int imax = Labyrinth.cellsInLength;
		int jmin = 0;
		int jmax = Labyrinth.cellsInWidth;
		
		if (Labyrinth.player.eyeLeftRight < 0) Labyrinth.player.eyeLeftRight = 360 - (-Labyrinth.player.eyeLeftRight)%360;
		
		//int angle = (int)((Labyrinth.player.eyeLeftRight+45)/90);
		//if (Labyrinth.player.eyeLeftRight+45 < 0) angle += 3;
		
		int angle = (int)(Labyrinth.player.eyeLeftRight/30);
		//if (Labyrinth.player.eyeLeftRight < 0) angle += 11;
		
		float zacy = (Labyrinth.player.bb.x+Labyrinth.player.eyex)/CELL_SIZE;
		float zacx = Labyrinth.cellsInLength+(Labyrinth.player.bb.z+Labyrinth.player.eyez)/CELL_SIZE;
		//System.out.println("zacx: "+zacx+", zacy: "+zacy+", angle:"+Labyrinth.player.eyeLeftRight);
		
		//Test cullinga
		/*for(int i = imin; i < imax; i++) {
			for(int j = jmin; j < jmax; j++) {
				if (labyrinth[i][j] == 1 || (j == jmin && labyrinth[i][j] == 2) || (i == imin && labyrinth[i][j] == 3)) {
					walls[i][j].render(Labyrinth.player.bb.y+Labyrinth.player.eyey >= WALL_HEIGHT);
					walls[i][j].collide(Labyrinth.player);
				}
			}
		}*/

		
		//Easy culling
		//Pogledamo kje smo, in za sabo ne rišemo.
		switch (angle) {
			case 1:
				imax = (int)zacx+4;
			case 2:
			case 3:
			//case 1:
				jmax = (int)zacy+4;
				break;
			case 4:
				jmax = (int)zacy+4;
			case 5:
			case 6:
			//case 2:
				imin = (int)zacx-4;
				break;
			case 7:
				imin = (int)zacx-4;
			case 8:
			case 9:
			//case 3:
				jmin = (int)zacy-4;
				break;
			case 10:
				jmin = (int)zacy-4;
			case 11:
			case 0:
			//case 0:
				imax = (int)zacx+4;
				break;
		}
		
		//Naredi culling tako da dejansko deluje! Baje deluje!
		if (Labyrinth.player.bb.y+Labyrinth.player.eyey <= WALL_HEIGHT) {
			if (zacx < 0) zacx = 0;
			if (zacx >= Labyrinth.cellsInLength) zacx = Labyrinth.cellsInLength-1;
			if (zacy < 0) zacy = 0;
			if (zacy >= Labyrinth.cellsInWidth) zacy = Labyrinth.cellsInWidth-1;
			
			//Hard culling!
			//Izraèunamo skrajne meje vidnega.
			//Pozor: veèji koti škodijo!
			float k1 = (float)Math.tan(((Labyrinth.player.eyeLeftRight+30)%360)*Math.PI/180);
			float k2 = (float)Math.tan(((Labyrinth.player.eyeLeftRight-30)%360)*Math.PI/180);
			//System.out.println(angle+" "+Labyrinth.player.eyeLeftRight+" "+k1+" "+k2);
			//float k0 = k1;
			float x = zacx;
			float y = zacy;
			float k;
			int minx, maxx;
			int miny, maxy;
			Wall w;
			minx = maxx = (int)x;
			miny = maxy = (int)y;
			switch (angle) {
				case 1:
					if ((x != (int)x || y != (int)y) && (int)x != (int)(x-1/k1) && (k = ((int)x + x+((int)y-y)/k1)/2) != x) {
						x = k;
						y = zacy + k1*(x-zacx);
					} else {
						x -= 1/k1;
						y--;
					}
					try {
						while (k1 > k2-1 && k1 >= 0) {
							if (Labyrinth.labyrinth[(int)x][(int)y] > 0) {
								if (y < miny) miny = (int)y;
								w = Labyrinth.walls[(int)x][(int)y];
								//w.render();
								y = (w.bb.x+w.bb.w)/CELL_SIZE;
								x = Labyrinth.cellsInLength+w.bb.z/CELL_SIZE;
								if (x < minx) minx = (int)x;
								k1 = (y-zacy)/(x-zacx);
								//System.out.print(k1+" ");
								//System.out.println("1:"+Labyrinth.player.eyeLeftRight+","+x+","+y+","+zacx+","+zacy+","+k1+","+w+" ");
								try {
									if (Labyrinth.labyrinth[(int)x-1][(int)y] > 0) {
										x--;
										k1 = (y-zacy)/(x-zacx);
										continue;
									}
								} catch (ArrayIndexOutOfBoundsException e) { break; }
							}
							if (k1 > 1) {
								if ((x != (int)x || y != (int)y) && (int)x != (int)(x-1/k1) && (k = ((int)x + x+((int)y-y)/k1)/2) != x) {
									x = k;
									y = zacy + k1*(x-zacx);
								} else {
									x -= 1/k1;
									y--;
								}
							} else {
								if ((x != (int)x || y != (int)y) && (int)y != (int)(y-k1) && (k = ((int)y + y+((int)x-x)*k1)/2) != y) {
									y = k;
									x = zacx + (y-zacy)/k1;
								} else {
									x--;
									y -= k1;
								}
							}
							//System.out.print("1:"+x+","+y+","+zacx+","+zacy+","+k1+" ");
						}
					} catch (ArrayIndexOutOfBoundsException e) {}
					x = zacx;
					y = zacy;
					if ((x != (int)x || y != (int)y) && (int)y != (int)(y-k2) && (k = ((int)y + y+((int)x-x)*k2)/2) != y) {
						y = k;
						x = zacx + (y-zacy)/k2;
					} else {
						x--;
						y -= k2;
					}
					//Grd hack, ampak deluje
					try {
						while (k1+1 > k2 && k2 >= 0) {
							if (Labyrinth.labyrinth[(int)x][(int)y] > 0) {
								if (x < minx) minx = (int)x;
								w = Labyrinth.walls[(int)x][(int)y];
								//w.render();
								y = w.bb.x/CELL_SIZE;
								x = Labyrinth.cellsInLength+(w.bb.z+w.bb.d)/CELL_SIZE;
								if (y < miny) miny = (int)y;
								k2 = (y-zacy)/(x-zacx);
								//System.out.print(k2+" ");
								//System.out.println("2:"+Labyrinth.player.eyeLeftRight+","+x+","+y+","+zacx+","+zacy+","+k2+","+w+" ");
								try {
									if (Labyrinth.labyrinth[(int)x][(int)y-1] > 0) {
										y--;
										k2 = (y-zacy)/(x-zacx);
										continue;
									}
								} catch (ArrayIndexOutOfBoundsException e) { break; }
							}
							if (k2 < 1) {
								if ((x != (int)x || y != (int)y) && (int)y != (int)(y-k2) && (k = ((int)y + y+((int)x-x)*k2)/2) != y) {
									y = k;
									x = zacx + (y-zacy)/k2;
								} else {
									x--;
									y -= k2;
								}
							} else {
								if ((x != (int)x || y != (int)y) && (int)x != (int)(x-1/k2) && (k = ((int)x + x+((int)y-y)/k2)/2) != x) {
									x = k;
									y = zacy + k2*(x-zacx);
								} else {
									x -= 1/k2;
									y--;
								}
							}
						}
					} catch (ArrayIndexOutOfBoundsException e) {}
					//System.out.println();
					imin = minx-4;
					jmin = miny-4;
					break;
				case 2:
				case 3:
					if (k1 > -1) {
						if ((x != (int)x || y != (int)y) && (int)y != (int)(y+k1) && (k = ((int)y + y+((int)(x+1)-x)*k1)/2) != y) {
							y = k;
							x = zacx + (y-zacy)/k1;
						} else {
							x++;
							y += k1;
						}
					} else {
						if ((x != (int)x || y != (int)y) && (int)x != (int)(x-1/k1) && (k = ((int)(x+1) + x+((int)y-y)/k1)/2) != x) {
							x = k;
							y = zacy + k1*(x-zacx);
						} else {
							x -= 1/k1;
							y--;
						}
					}
					//System.out.print("k1: "+k1+" ");
					try {
						while (k1 <= 0) {
							//System.out.println("1:"+Labyrinth.player.eyeLeftRight+","+x+","+y+","+k1);
							if (Labyrinth.labyrinth[(int)x][(int)y] > 0) {
								if (x > maxx) maxx = (int)x;
								w = Labyrinth.walls[(int)x][(int)y];
								y = w.bb.x/CELL_SIZE;
								x = Labyrinth.cellsInLength+w.bb.z/CELL_SIZE;
								if (y < miny) miny = (int)y;
								k1 = (y-zacy)/(x-zacx);
								//System.out.print("k1: "+k1+" ");
								//System.out.println("1:"+Labyrinth.player.eyeLeftRight+","+x+","+y+","+zacx+","+zacy+","+k1+","+w+" ");
								try {
									if (Labyrinth.labyrinth[(int)x-1][(int)y-1] > 0) {
										y--;
										x--;
										k1 = (y-zacy)/(x-zacx);
										continue;
									}
								} catch (ArrayIndexOutOfBoundsException e) { break; }
							}
							if (k1 > -1) {
								if ((x != (int)x || y != (int)y) && (int)y != (int)(y+k1) && (k = ((int)y + y+((int)(x+1)-x)*k1)/2) != y) {
									y = k;
									x = zacx + (y-zacy)/k1;
								} else {
									x++;
									y += k1;
								}
							} else {
								if ((x != (int)x || y != (int)y) && (int)x != (int)(x-1/k1) && (k = ((int)(x+1) + x+((int)y-y)/k1)/2) != x) {
									x = k;
									y = zacy + k1*(x-zacx);
								} else {
									x -= 1/k1;
									y--;
								}
							}
						}
					} catch (ArrayIndexOutOfBoundsException e) {}
					x = zacx;
					y = zacy;
					if (k2 < 1) {
						if ((x != (int)x || y != (int)y) && (int)y != (int)(y-k2) && (k = ((int)y + y+((int)x-x)*k2)/2) != y) {
							y = k;
							x = zacx + (y-zacy)/k2;
						} else {
							x--;
							y -= k2;
						}
					} else {
						if ((x != (int)x || y != (int)y) && (int)x != (int)(x-1/k2) && (k = ((int)x + x+((int)y-y)/k2)/2) != x) {
							x = k;
							y = zacy + k2*(x-zacx);
						} else {
							x -= 1/k2;
							y--;
						}
					}
					//System.out.print("\nk2: "+k2+" ");
					try {
						while (k2 >= 0) {
							//System.out.println("2:"+Labyrinth.player.eyeLeftRight+","+x+","+y+","+k2);
							if (Labyrinth.labyrinth[(int)x][(int)y] > 0) {
								if (x < minx) minx = (int)x;
								w = Labyrinth.walls[(int)x][(int)y];
								y = w.bb.x/CELL_SIZE;
								x = Labyrinth.cellsInLength+(w.bb.z+w.bb.d)/CELL_SIZE;
								if (y < miny) miny = (int)y;
								k2 = (y-zacy)/(x-zacx);
								//System.out.print("k2: "+k2+" ");
								//System.out.println("2:"+Labyrinth.player.eyeLeftRight+","+x+","+y+","+zacx+","+zacy+","+k2+","+w+" ");
								try {
									if (Labyrinth.labyrinth[(int)x][(int)y-1] > 0) {
										y--;
										k2 = (y-zacy)/(x-zacx);
										continue;
									}
								} catch (ArrayIndexOutOfBoundsException e) { break; }
							}
							if (k2 < 1) {
								if ((x != (int)x || y != (int)y) && (int)y != (int)(y-k2) && (k = ((int)y + y+((int)x-x)*k2)/2) != y) {
									y = k;
									x = zacx + (y-zacy)/k2;
								} else {
									x--;
									y -= k2;
								}
							} else {
								if ((x != (int)x || y != (int)y) && (int)x != (int)(x-1/k2) && (k = ((int)x + x+((int)y-y)/k2)/2) != x) {
									x = k;
									y = zacy + k2*(x-zacx);
								} else {
									x -= 1/k2;
									y--;
								}
							}
						}
					} catch (ArrayIndexOutOfBoundsException e) {}
					//System.out.println();
					imax = maxx+4;
					imin = minx-4;
					jmin = miny-4;
					break;
				case 4:
					if ((x != (int)x || y != (int)y) && (int)y != (int)(y+k1) && (k = ((int)y + y+((int)(x+1)-x)*k1)/2) != y) {
						y = k;
						x = zacx + (y-zacy)/k1;
					} else {
						x++;
						y += k1;
					}
					try {
						while (k1 > k2-1 && k1 <= 0) {
							if (Labyrinth.labyrinth[(int)x][(int)y] > 0) {
								if (x > maxx) maxx = (int)x;
								w = Labyrinth.walls[(int)x][(int)y];
								//w.render();
								y = w.bb.x/CELL_SIZE;
								x = Labyrinth.cellsInLength+w.bb.z/CELL_SIZE;
								if (y < miny) miny = (int)y;
								k1 = (y-zacy)/(x-zacx);
								//System.out.print(k1+" ");
								//System.out.println("1:"+Labyrinth.player.eyeLeftRight+","+x+","+y+","+zacx+","+zacy+","+k1+","+w+" ");
								try {
									if (Labyrinth.labyrinth[(int)x-1][(int)y-1] > 0) {
										x--;
										y--;
										k1 = (y-zacy)/(x-zacx);
										continue;
									}
								} catch (ArrayIndexOutOfBoundsException e) { break; }
							}
							if (k1 > -1) {
								if ((x != (int)x || y != (int)y) && (int)y != (int)(y+k1) && (k = ((int)y + y+((int)(x+1)-x)*k1)/2) != y) {
									y = k;
									x = zacx + (y-zacy)/k1;
								} else {
									x++;
									y += k1;
								}
							} else {
								if ((x != (int)x || y != (int)y) && (int)x != (int)(x-k1) && (k = ((int)(x+1) + x+((int)y-y)/k1)/2) != x) {
									x = k;
									y = zacy + k1*(x-zacx);
								} else {
									x -= 1/k1;
									y--;
								}
							}
							//System.out.print("1:"+x+","+y+","+zacx+","+zacy+","+k1+" ");
						}
					} catch (ArrayIndexOutOfBoundsException e) {}
					x = zacx;
					y = zacy;
					if ((x != (int)x || y != (int)y) && (int)x != (int)(x-1/k2) && (k = ((int)(x+1) + x+((int)y-y)/k2)/2) != x) {
						x = k;
						y = zacy + k2*(x-zacx);
					} else {
						x -= 1/k2;
						y--;
					}
					//Grd hack, ampak deluje
					try {
						while (k1+1 > k2 && k2 <= 0) {
							if (Labyrinth.labyrinth[(int)x][(int)y] > 0) {
								if (y < miny) miny = (int)y;
								w = Labyrinth.walls[(int)x][(int)y];
								//w.render();
								y = (w.bb.x+w.bb.w)/CELL_SIZE;
								x = Labyrinth.cellsInLength+(w.bb.z+w.bb.d)/CELL_SIZE;
								if (x > maxx) maxx = (int)x;
								k2 = (y-zacy)/(x-zacx);
								//System.out.print(k2+" ");
								//System.out.println("2:"+Labyrinth.player.eyeLeftRight+","+x+","+y+","+zacx+","+zacy+","+k2+","+w+" ");
								try {
									if (Labyrinth.labyrinth[(int)x][(int)y] > 0) {
										continue;
									}
								} catch (ArrayIndexOutOfBoundsException e) { break; }
							}
							if (k2 < -1) {
								if ((x != (int)x || y != (int)y) && (int)x != (int)(x-1/k2) && (k = ((int)(x+1) + x+((int)y-y)/k2)/2) != x) {
									x = k;
									y = zacy + k2*(x-zacx);
								} else {
									x -= 1/k2;
									y--;
								}
							} else {
								if ((x != (int)x || y != (int)y) && (int)y != (int)(y+k2) && (k = ((int)y + y+((int)(x+1)-x)*k2)/2) != y) {
									y = k;
									x = zacx + (y-zacy)/k2;
								} else {
									x++;
									y += k2;
								}
							}
						}
					} catch (ArrayIndexOutOfBoundsException e) {}
					//System.out.println();
					imax = maxx+4;
					jmin = miny-4;
					break;
				case 5:
				case 6:
					if (k1 < 1) {
						if ((x != (int)x || y != (int)y) && (int)y != (int)(y+k1) && (k = ((int)(y+1) + y+((int)(x+1)-x)*k1)/2) != y) {
							y = k;
							x = zacx + (y-zacy)/k1;
						} else {
							x++;
							y += k1;
						}
					} else {
						if ((x != (int)x || y != (int)y) && (int)x != (int)(x+1/k1) && (k = ((int)(x+1) + x+((int)(y+1)-y)/k1)/2) != x) {
							x = k;
							y = zacy + k1*(x-zacx);
						} else {
							x += 1/k1;
							y++;
						}
					}
					//System.out.print("k1: "+k1+" ");
					try {
						while (k1 >= 0) {
							//System.out.println("1:"+Labyrinth.player.eyeLeftRight+","+x+","+y+","+k1);
							if (Labyrinth.labyrinth[(int)x][(int)y] > 0) {
								if (y > maxy) maxy = (int)y;
								w = Labyrinth.walls[(int)x][(int)y];
								y = w.bb.x/CELL_SIZE;
								x = Labyrinth.cellsInLength+(w.bb.z+w.bb.d)/CELL_SIZE;
								if (x > maxx) maxx = (int)x;
								k1 = (y-zacy)/(x-zacx);
								//System.out.print("k1: "+k1+" ");
								//System.out.println("1:"+Labyrinth.player.eyeLeftRight+","+x+","+y+","+zacx+","+zacy+","+k1+","+w+" ");
								try {
									if (Labyrinth.labyrinth[(int)x][(int)y-1] > 0) {
										y--;
										k1 = (y-zacy)/(x-zacx);
										continue;
									}
								} catch (ArrayIndexOutOfBoundsException e) { break; }
							}
							if (k1 < 1) {
								if ((x != (int)x || y != (int)y) && (int)y != (int)(y+k1) && (k = ((int)(y+1) + y+((int)(x+1)-x)*k1)/2) != y) {
									y = k;
									x = zacx + (y-zacy)/k1;
								} else {
									x++;
									y += k1;
								}
							} else {
								if ((x != (int)x || y != (int)y) && (int)x != (int)(x+1/k1) && (k = ((int)(x+1) + x+((int)(y+1)-y)/k1)/2) != x) {
									x = k;
									y = zacy + k1*(x-zacx);
								} else {
									x += 1/k1;
									y++;
								}
							}
						}
					} catch (ArrayIndexOutOfBoundsException e) {}
					x = zacx;
					y = zacy;
					if (k2 > -1) {
						if ((x != (int)x || y != (int)y) && (int)y != (int)(y+k2) && (k = ((int)y + y+((int)(x+1)-x)*k2)/2) != y) {
							y = k;
							x = zacx + (y-zacy)/k2;
						} else {
							x++;
							y += k2;
						}
					} else {
						if ((x != (int)x || y != (int)y) && (int)x != (int)(x-1/k2) && (k = ((int)(x+1) + x+((int)y-y)/k2)/2) != x) {
							x = k;
							y = zacy + k2*(x-zacx);
						} else {
							x -= 1/k2;
							y--;
						}
					}
					//System.out.print("\nk2: "+k2+" ");
					try {
						while (k2 <= 0) {
							//System.out.println("2:"+Labyrinth.player.eyeLeftRight+","+x+","+y+","+k2);
							if (Labyrinth.labyrinth[(int)x][(int)y] > 0) {
								if (y < miny) miny = (int)y;
								w = Labyrinth.walls[(int)x][(int)y];
								y = (w.bb.x+w.bb.w)/CELL_SIZE;
								x = Labyrinth.cellsInLength+(w.bb.z+w.bb.d)/CELL_SIZE;
								if (x > maxx) maxx = (int)x;
								k2 = (y-zacy)/(x-zacx);
								//System.out.print("k2: "+k2+" ");
								//System.out.println("2:"+Labyrinth.player.eyeLeftRight+","+x+","+y+","+zacx+","+zacy+","+k2+","+w+" ");
								try {
									if (Labyrinth.labyrinth[(int)x][(int)y] > 0) {
										continue;
									}
								} catch (ArrayIndexOutOfBoundsException e) { break; }
							}
							if (k2 > -1) {
								if ((x != (int)x || y != (int)y) && (int)y != (int)(y+k2) && (k = ((int)y + y+((int)(x+1)-x)*k2)/2) != y) {
									y = k;
									x = zacx + (y-zacy)/k2;
								} else {
									x++;
									y += k2;
								}
							} else {
								if ((x != (int)x || y != (int)y) && (int)x != (int)(x-1/k2) && (k = ((int)(x+1) + x+((int)y-y)/k2)/2) != x) {
									x = k;
									y = zacy + k2*(x-zacx);
								} else {
									x -= 1/k2;
									y--;
								}
							}
						}
					} catch (ArrayIndexOutOfBoundsException e) {}
					//System.out.println();
					imax = maxx+4;
					jmax = maxy+4;
					jmin = miny-4;
					break;
				case 7:
					if ((x != (int)x || y != (int)y) && (int)x != (int)(x+1/k1) && (k = ((int)(x+1) + x+((int)(y+1)-y)/k1)/2) != x) {
						x = k;
						y = zacy + k1*(x-zacx);
					} else {
						x += 1/k1;
						y++;
					}
					try {
						while (k1 > k2-1 && k1 >= 0) {
							if (Labyrinth.labyrinth[(int)x][(int)y] > 0) {
								if (y > maxy) maxy = (int)y;
								w = Labyrinth.walls[(int)x][(int)y];
								//w.render();
								y = w.bb.x/CELL_SIZE;
								x = Labyrinth.cellsInLength+(w.bb.z+w.bb.d)/CELL_SIZE;
								if (x > maxx) maxx = (int)x;
								k1 = (y-zacy)/(x-zacx);
								//System.out.print(k1+" ");
								//System.out.println("1:"+Labyrinth.player.eyeLeftRight+","+x+","+y+","+zacx+","+zacy+","+k1+","+w+" ");
								try {
									if (Labyrinth.labyrinth[(int)x][(int)y-1] > 0) {
										y--;
										k1 = (y-zacy)/(x-zacx);
										continue;
									}
								} catch (ArrayIndexOutOfBoundsException e) { break; }
							}
							if (k1 > 1) {
								if ((x != (int)x || y != (int)y) && (int)x != (int)(x+1/k1) && (k = ((int)(x+1) + x+((int)(y+1)-y)/k1)/2) != x) {
									x = k;
									y = zacy + k1*(x-zacx);
								} else {
									x += 1/k1;
									y++;
								}
							} else {
								if ((x != (int)x || y != (int)y) && (int)y != (int)(y+k1) && (k = ((int)(y+1) + y+((int)(x+1)-x)*k1)/2) != y) {
									y = k;
									x = zacx + (y-zacy)/k1;
								} else {
									x++;
									y += k1;
								}
							}
							//System.out.print("1:"+x+","+y+","+zacx+","+zacy+","+k1+" ");
						}
					} catch (ArrayIndexOutOfBoundsException e) {}
					x = zacx;
					y = zacy;
					if ((x != (int)x || y != (int)y) && (int)y != (int)(y+k2) && (k = ((int)(y+1) + y+((int)(x+1)-x)*k2)/2) != y) {
						y = k;
						x = zacx + (y-zacy)/k2;
					} else {
						x++;
						y += k2;
					}
					//Grd hack, ampak deluje
					try {
						while (k1+1 > k2 && k2 >= 0) {
							if (Labyrinth.labyrinth[(int)x][(int)y] > 0) {
								if (x > maxx) maxx = (int)x;
								w = Labyrinth.walls[(int)x][(int)y];
								//w.render();
								y = (w.bb.x+w.bb.w)/CELL_SIZE;
								x = Labyrinth.cellsInLength+w.bb.z/CELL_SIZE;
								if (y > maxy) maxy = (int)y;
								k2 = (y-zacy)/(x-zacx);
								//System.out.print(k2+" ");
								//System.out.println("2:"+Labyrinth.player.eyeLeftRight+","+x+","+y+","+zacx+","+zacy+","+k2+","+w+" ");
								try {
									if (Labyrinth.labyrinth[(int)x-1][(int)y] > 0) {
										x--;
										k2 = (y-zacy)/(x-zacx);
										continue;
									}
								} catch (ArrayIndexOutOfBoundsException e) { break; }
							}
							if (k2 < 1) {
								if ((x != (int)x || y != (int)y) && (int)y != (int)(y+k2) && (k = ((int)(y+1) + y+((int)(x+1)-x)*k2)/2) != y) {
									y = k;
									x = zacx + (y-zacy)/k2;
								} else {
									x++;
									y += k2;
								}
							} else {
								if ((x != (int)x || y != (int)y) && (int)x != (int)(x+1/k2) && (k = ((int)(x+1) + x+((int)(y+1)-y)/k2)/2) != x) {
									x = k;
									y = zacy + k2*(x-zacx);
								} else {
									x += 1/k2;
									y++;
								}
							}
						}
					} catch (ArrayIndexOutOfBoundsException e) {}
					//System.out.println();
					imax = maxx+4;
					jmax = maxy+4;
					break;
				case 8:
				case 9:
					if (k1 > -1) {
						if ((x != (int)x || y != (int)y) && (int)y != (int)(y-k1) && (k = ((int)(y+1) + y+((int)x-x)*k1)/2) != y) {
							y = k;
							x = zacx + (y-zacy)/k1;
						} else {
							x--;
							y -= k1;
						}
					} else {
						if ((x != (int)x || y != (int)y) && (int)x != (int)(x+1/k1) && (k = ((int)x + x+((int)(y+1)-y)/k1)/2) != x) {
							x = k;
							y = zacy + k1*(x-zacx);
						} else {
							x += 1/k1;
							y++;
						}
					}
					//System.out.print("k1: "+k1+" ");
					try {
						while (k1 <= 0) {
							//System.out.println("1:"+Labyrinth.player.eyeLeftRight+","+x+","+y+","+k1);
							if (Labyrinth.labyrinth[(int)x][(int)y] > 0) {
								if (x < minx) minx = (int)x;
								w = Labyrinth.walls[(int)x][(int)y];
								y = (w.bb.x+w.bb.w)/CELL_SIZE;
								x = Labyrinth.cellsInLength+(w.bb.z+w.bb.d)/CELL_SIZE;
								if (y > maxy) maxy = (int)y;
								k1 = (y-zacy)/(x-zacx);
								//System.out.print("k1: "+k1+" ");
								//System.out.println("1:"+Labyrinth.player.eyeLeftRight+","+x+","+y+","+zacx+","+zacy+","+k1+","+w+" ");
								try {
									if (Labyrinth.labyrinth[(int)x][(int)y] > 0) {
										continue;
									}
								} catch (ArrayIndexOutOfBoundsException e) { break; }
							}
							if (k1 > -1) {
								if ((x != (int)x || y != (int)y) && (int)y != (int)(y-k1) && (k = ((int)(y+1) + y+((int)x-x)*k1)/2) != y) {
									y = k;
									x = zacx + (y-zacy)/k1;
								} else {
									x--;
									y -= k1;
								}
							} else {
								if ((x != (int)x || y != (int)y) && (int)x != (int)(x+1/k1) && (k = ((int)x + x+((int)(y+1)-y)/k1)/2) != x) {
									x = k;
									y = zacy + k1*(x-zacx);
								} else {
									x += 1/k1;
									y++;
								}
							}
						}
					} catch (ArrayIndexOutOfBoundsException e) {}
					x = zacx;
					y = zacy;
					if (k2 < 1) {
						if ((x != (int)x || y != (int)y) && (int)y != (int)(y+k2) && (k = ((int)(y+1) + y+((int)(x+1)-x)*k2)/2) != y) {
							y = k;
							x = zacx + (y-zacy)/k2;
						} else {
							x++;
							y += k2;
						}
					} else {
						if ((x != (int)x || y != (int)y) && (int)x != (int)(x+1/k2) && (k = ((int)(x+1) + x+((int)(y+1)-y)/k2)/2) != x) {
							x = k;
							y = zacy + k2*(x-zacx);
						} else {
							x += 1/k2;
							y++;
						}
					}
					//System.out.print("\nk2: "+k2+" ");
					try {
						while (k2 >= 0) {
							//System.out.println("2:"+Labyrinth.player.eyeLeftRight+","+x+","+y+","+k2);
							if (Labyrinth.labyrinth[(int)x][(int)y] > 0) {
								if (x > maxx) maxx = (int)x;
								w = Labyrinth.walls[(int)x][(int)y];
								y = (w.bb.x+w.bb.w)/CELL_SIZE;
								x = Labyrinth.cellsInLength+w.bb.z/CELL_SIZE;
								if (y > maxy) maxy = (int)y;
								k2 = (y-zacy)/(x-zacx);
								//System.out.print("k2: "+k2+" ");
								//System.out.println("2:"+Labyrinth.player.eyeLeftRight+","+x+","+y+","+zacx+","+zacy+","+k2+","+w+" ");
								try {
									if (Labyrinth.labyrinth[(int)x-1][(int)y] > 0) {
										x--;
										k2 = (y-zacy)/(x-zacx);
										continue;
									}
								} catch (ArrayIndexOutOfBoundsException e) { break; }
							}
							if (k2 < 1) {
								if ((x != (int)x || y != (int)y) && (int)y != (int)(y+k2) && (k = ((int)(y+1) + y+((int)(x+1)-x)*k2)/2) != y) {
									y = k;
									x = zacx + (y-zacy)/k2;
								} else {
									x++;
									y += k2;
								}
							} else {
								if ((x != (int)x || y != (int)y) && (int)x != (int)(x+1/k2) && (k = ((int)(x+1) + x+((int)(y+1)-y)/k2)/2) != x) {
									x = k;
									y = zacy + k2*(x-zacx);
								} else {
									x += 1/k2;
									y++;
								}
							}
						}
					} catch (ArrayIndexOutOfBoundsException e) {}
					//System.out.println();
					imax = maxx+4;
					imin = minx-4;
					jmax = maxy+4;
					break;
				case 10:
					if ((x != (int)x || y != (int)y) && (int)y != (int)(y-k1) && (k = ((int)(y+1) + y+((int)x-x)*k1)/2) != y) {
						y = k;
						x = zacx + (y-zacy)/k1;
					} else {
						x--;
						y -= k1;
					}
					try {
						while (k1 > k2-1 && k1 <= 0) {
							if (Labyrinth.labyrinth[(int)x][(int)y] > 0) {
								if (x < minx) minx = (int)x;
								w = Labyrinth.walls[(int)x][(int)y];
								//w.render();
								y = (w.bb.x+w.bb.w)/CELL_SIZE;
								x = Labyrinth.cellsInLength+(w.bb.z+w.bb.d)/CELL_SIZE;
								if (y > maxy) maxy = (int)y;
								k1 = (y-zacy)/(x-zacx);
								//System.out.print(k1+" ");
								//System.out.println("1:"+Labyrinth.player.eyeLeftRight+","+x+","+y+","+zacx+","+zacy+","+k1+","+w+" ");
								try {
									if (Labyrinth.labyrinth[(int)x][(int)y] > 0) {
										continue;
									}
								} catch (ArrayIndexOutOfBoundsException e) { break; }
							}
							if (k1 > -1) {
								if ((x != (int)x || y != (int)y) && (int)y != (int)(y-k1) && (k = ((int)(y+1) + y+((int)x-x)*k1)/2) != y) {
									y = k;
									x = zacx + (y-zacy)/k1;
								} else {
									x--;
									y -= k1;
								}
							} else {
								if ((x != (int)x || y != (int)y) && (int)x != (int)(x+k1) && (k = ((int)x + x+((int)(y+1)-y)/k1)/2) != x) {
									x = k;
									y = zacy + k1*(x-zacx);
								} else {
									x += 1/k1;
									y++;
								}
							}
							//System.out.print("1:"+x+","+y+","+zacx+","+zacy+","+k1+" ");
						}
					} catch (ArrayIndexOutOfBoundsException e) {}
					x = zacx;
					y = zacy;
					if ((x != (int)x || y != (int)y) && (int)x != (int)(x+1/k2) && (k = ((int)x + x+((int)(y+1)-y)/k2)/2) != x) {
						x = k;
						y = zacy + k2*(x-zacx);
					} else {
						x += 1/k2;
						y++;
					}
					//Grd hack, ampak deluje
					try {
						while (k1+1 > k2 && k2 <= 0) {
							if (Labyrinth.labyrinth[(int)x][(int)y] > 0) {
								if (y > maxy) maxy = (int)y;
								w = Labyrinth.walls[(int)x][(int)y];
								//w.render();
								y = w.bb.x/CELL_SIZE;
								x = Labyrinth.cellsInLength+w.bb.z/CELL_SIZE;
								if (x < minx) minx = (int)x;
								k2 = (y-zacy)/(x-zacx);
								//System.out.print(k2+" ");
								//System.out.println("2:"+Labyrinth.player.eyeLeftRight+","+x+","+y+","+zacx+","+zacy+","+k2+","+w+" ");
								try {
									if (Labyrinth.labyrinth[(int)x-1][(int)y-1] > 0) {
										x--;
										y--;
										k2 = (y-zacy)/(x-zacx);
										continue;
									}
								} catch (ArrayIndexOutOfBoundsException e) { break; }
							}
							if (k2 < -1) {
								if ((x != (int)x || y != (int)y) && (int)x != (int)(x+1/k2) && (k = ((int)x + x+((int)(y+1)-y)/k2)/2) != x) {
									x = k;
									y = zacy + k2*(x-zacx);
								} else {
									x += 1/k2;
									y++;
								}
							} else {
								if ((x != (int)x || y != (int)y) && (int)y != (int)(y-k2) && (k = ((int)(y+1) + y+((int)x-x)*k2)/2) != y) {
									y = k;
									x = zacx + (y-zacy)/k2;
								} else {
									x--;
									y -= k2;
								}
							}
						}
					} catch (ArrayIndexOutOfBoundsException e) {}
					//System.out.println();
					imin = minx-4;
					jmax = maxy+4;
					break;
				case 11:
				case 0:
					if (k1 < 1) {
						if ((x != (int)x || y != (int)y) && (int)y != (int)(y-k1) && (k = ((int)y + y+((int)x-x)*k1)/2) != y) {
							y = k;
							x = zacx + (y-zacy)/k1;
						} else {
							x--;
							y -= k1;
						}
					} else {
						if ((x != (int)x || y != (int)y) && (int)x != (int)(x-1/k1) && (k = ((int)x + x+((int)y-y)/k1)/2) != x) {
							x = k;
							y = zacy + k1*(x-zacx);
						} else {
							x -= 1/k1;
							y--;
						}
					}
					//System.out.print("k1: "+k1+" ");
					try {
						while (k1 >= 0) {
							//System.out.println("1:"+Labyrinth.player.eyeLeftRight+","+x+","+y+","+k1);
							if (Labyrinth.labyrinth[(int)x][(int)y] > 0) {
								if (y < miny) miny = (int)y;
								w = Labyrinth.walls[(int)x][(int)y];
								y = (w.bb.x+w.bb.w)/CELL_SIZE;
								x = Labyrinth.cellsInLength+w.bb.z/CELL_SIZE;
								if (x < minx) minx = (int)x;
								k1 = (y-zacy)/(x-zacx);
								//System.out.print("k1: "+k1+" ");
								//System.out.println("1:"+Labyrinth.player.eyeLeftRight+","+x+","+y+","+zacx+","+zacy+","+k1+","+w+" ");
								try {
									if (Labyrinth.labyrinth[(int)x-1][(int)y] > 0) {
										x--;
										k1 = (y-zacy)/(x-zacx);
										continue;
									}
								} catch (ArrayIndexOutOfBoundsException e) { break; }
							}
							if (k1 < 1) {
								if ((x != (int)x || y != (int)y) && (int)y != (int)(y-k1) && (k = ((int)y + y+((int)x-x)*k1)/2) != y) {
									y = k;
									x = zacx + (y-zacy)/k1;
								} else {
									x--;
									y -= k1;
								}
							} else {
								if ((x != (int)x || y != (int)y) && (int)x != (int)(x-1/k1) && (k = ((int)x + x+((int)y-y)/k1)/2) != x) {
									x = k;
									y = zacy + k1*(x-zacx);
								} else {
									x -= 1/k1;
									y--;
								}
							}
						}
					} catch (ArrayIndexOutOfBoundsException e) {}
					x = zacx;
					y = zacy;
					if (k2 > -1) {
						if ((x != (int)x || y != (int)y) && (int)y != (int)(y-k2) && (k = ((int)(y+1) + y+((int)x-x)*k2)/2) != y) {
							y = k;
							x = zacx + (y-zacy)/k2;
						} else {
							x--;
							y -= k2;
						}
					} else {
						if ((x != (int)x || y != (int)y) && (int)x != (int)(x+1/k2) && (k = ((int)x + x+((int)(y+1)-y)/k2)/2) != x) {
							x = k;
							y = zacy + k2*(x-zacx);
						} else {
							x += 1/k2;
							y++;
						}
					}
					//System.out.print("\nk2: "+k2+" ");
					try {
						while (k2 <= 0) {
							//System.out.println("2:"+Labyrinth.player.eyeLeftRight+","+x+","+y+","+k2);
							if (Labyrinth.labyrinth[(int)x][(int)y] > 0) {
								if (y > maxy) maxy = (int)y;
								w = Labyrinth.walls[(int)x][(int)y];
								y = w.bb.x/CELL_SIZE;
								x = Labyrinth.cellsInLength+w.bb.z/CELL_SIZE;
								if (x < minx) minx = (int)x;
								k2 = (y-zacy)/(x-zacx);
								//System.out.print("k2: "+k2+" ");
								//System.out.println("2:"+Labyrinth.player.eyeLeftRight+","+x+","+y+","+zacx+","+zacy+","+k2+","+w+" ");
								try {
									if (Labyrinth.labyrinth[(int)x-1][(int)y-1] > 0) {
										x--;
										y--;
										k2 = (y-zacy)/(x-zacx);
										continue;
									}
								} catch (ArrayIndexOutOfBoundsException e) { break; }
							}
							if (k2 > -1) {
								if ((x != (int)x || y != (int)y) && (int)y != (int)(y-k2) && (k = ((int)(y+1) + y+((int)x-x)*k2)/2) != y) {
									y = k;
									x = zacx + (y-zacy)/k2;
								} else {
									x--;
									y -= k2;
								}
							} else {
								if ((x != (int)x || y != (int)y) && (int)x != (int)(x+1/k2) && (k = ((int)x + x+((int)(y+1)-y)/k2)/2) != x) {
									x = k;
									y = zacy + k2*(x-zacx);
								} else {
									x += 1/k2;
									y++;
								}
							}
						}
					} catch (ArrayIndexOutOfBoundsException e) {}
					//System.out.println();
					imax = maxx+4;
					jmax = maxy+4;
					jmin = miny-4;
					break;
			}
			//To je to:)
		}
		//System.out.println("imin: "+imin+", imax: "+imax+", jmin: "+jmin+", jmax: "+jmax);
		
		if (imin < 0) imin = 0;
		if (imax < 0) imax = 0;
		if (imin >= Labyrinth.cellsInLength) imin = Labyrinth.cellsInLength;
		if (imax >= Labyrinth.cellsInLength) imax = Labyrinth.cellsInLength;
		if (jmin < 0) jmin = 0;
		if (jmax < 0) jmax = 0;
		if (jmin >= Labyrinth.cellsInWidth) jmin = Labyrinth.cellsInWidth;
		if (jmax >= Labyrinth.cellsInWidth) jmax = Labyrinth.cellsInWidth;
		
		//System.out.println(zacx+" "+zacy+" "+Labyrinth.player.eyeLeftRight+" "+angle);
		
		Labyrinth.tex[0].bind();
		
		// ***** FLOOR *****
		GL11.glBegin(GL11.GL_QUADS);
	    
			// Floor.
			GL11.glColor3f(1.0f, 1.0f, 1.0f); // White
			
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex3i(0, 0, 0);
			
			GL11.glTexCoord2f(Labyrinth.cellsInWidth  / CELLS_FOR_SINGLE_TEXTURE_FOR_FLOOR, 0);
			GL11.glVertex3i(Labyrinth.cellsInWidth * CELL_SIZE, 0, 0);
			
			GL11.glTexCoord2f(Labyrinth.cellsInWidth  / CELLS_FOR_SINGLE_TEXTURE_FOR_FLOOR, -Labyrinth.cellsInLength / CELLS_FOR_SINGLE_TEXTURE_FOR_FLOOR);
			GL11.glVertex3i(Labyrinth.cellsInWidth * CELL_SIZE, 0, -Labyrinth.cellsInLength * CELL_SIZE);
			
			GL11.glTexCoord2f(0, -Labyrinth.cellsInLength / CELLS_FOR_SINGLE_TEXTURE_FOR_FLOOR);
			GL11.glVertex3i(0, 0, -Labyrinth.cellsInLength * CELL_SIZE);
			
		GL11.glEnd();
		
		// ***** WALLS *****
		for(int i = imin; i < imax; i++) {
			for(int j = jmin; j < jmax; j++) {
				if (Labyrinth.labyrinth[i][j] == 1 || (j == jmin && Labyrinth.labyrinth[i][j] == 2) || (i == imin && Labyrinth.labyrinth[i][j] == 3)) {
					Labyrinth.walls[i][j].render();
					//walls[i][j].collide(Labyrinth.player);
				}
			}
		}
		
		//Labyrinth.player.render();
		for (int i=1; i < Labyrinth.chars.size(); i++) {
			Labyrinth.chars.get(i).render();
		}
		
		GL11.glPopMatrix();
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		
		//Menu
		if (menu) {
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glPushMatrix();
			if (message == null) {
				GL11.glTranslatef(-0.2f, 0.1f, -2.0f);
				for (int i=0; i < menuText.length; i++) {
					if (i==0 && (over || start)) {
						GL11.glColor3f(0.25f, 0.25f, 0.25f);
					} else if (i == menuSelect) {
						GL11.glColor3f(1.00f, 0.25f, 0.25f);
					} else {
						GL11.glColor3f(0.75f, 0.75f, 0.75f);
					}
					font.glPrint(menuText[i]);
					GL11.glTranslatef(0.0f, -0.1f, 0.0f);
				}
			} else {
				GL11.glTranslatef(-1.0f, 0.1f, -2.0f);
				GL11.glColor3f(1.00f, 0.25f, 0.25f);
				font.glPrint(message);
			}
			GL11.glPopMatrix();
			GL11.glEnable(GL11.GL_CULL_FACE);
		}
		
		GL11.glBlendFunc(GL11.GL_DST_COLOR,GL11.GL_ZERO);
		
		GL11.glTranslatef(-5.0f, 3.4f, -10.0f);
		GL11.glRotatef(-Labyrinth.player.eyeLeftRight, 0, 0, 1);
		
		Labyrinth.tex[2].bind();
		GL11.glBegin(GL11.GL_QUADS);
	    
			// Kompas - maska
			GL11.glColor3f(1.0f, 1.0f, 1.0f); // White
			GL11.glTexCoord2f(0.0f, 0.0f);
			GL11.glVertex3f(-0.5f, -0.5f, 0.0f);
			GL11.glTexCoord2f(1.0f, 0.0f);
			GL11.glVertex3f(0.5f, -0.5f, 0.0f);
			GL11.glTexCoord2f(1.0f, 1.0f);
			GL11.glVertex3f(0.5f, 0.5f, 0.0f);
			GL11.glTexCoord2f(0.0f, 1.0f);
			GL11.glVertex3f(-0.5f, 0.5f, 0.0f);
			
		GL11.glEnd();
		
		GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
		Labyrinth.tex[3].bind();
		GL11.glBegin(GL11.GL_QUADS);
	    
			// Kompas
			GL11.glColor3f(1.0f, 1.0f, 1.0f); // White
			GL11.glTexCoord2f(0.0f, 0.0f);
			GL11.glVertex3f(-0.5f, -0.5f, 0.0f);
			GL11.glTexCoord2f(1.0f, 0.0f);
			GL11.glVertex3f(0.5f, -0.5f, 0.0f);
			GL11.glTexCoord2f(1.0f, 1.0f);
			GL11.glVertex3f(0.5f, 0.5f, 0.0f);
			GL11.glTexCoord2f(0.0f, 1.0f);
			GL11.glVertex3f(-0.5f, 0.5f, 0.0f);
			
		GL11.glEnd();
		
		//GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		
		GL11.glRotatef(Labyrinth.player.eyeLeftRight, 0, 0, 1);
		GL11.glTranslatef(0.0f, -1.1f, 0.0f);
		
		Labyrinth.tex[4].bind();
		GL11.glBegin(GL11.GL_QUADS);
		
			// Zemljevid
			GL11.glColor3f(1.0f, 1.0f, 1.0f); // White
			GL11.glTexCoord2f(0.0f, 0.0f);
			GL11.glVertex3f(-0.5f, -0.5f, 0.0f);
			GL11.glTexCoord2f(1.0f, 0.0f);
			GL11.glVertex3f(0.5f, -0.5f, 0.0f);
			GL11.glTexCoord2f(1.0f, 1.0f);
			GL11.glVertex3f(0.5f, 0.5f, 0.0f);
			GL11.glTexCoord2f(0.0f, 1.0f);
			GL11.glVertex3f(-0.5f, 0.5f, 0.0f);

		GL11.glEnd();
		
		GL11.glBegin(GL11.GL_LINES);
	    
			// Obroba zemljevida
			GL11.glColor3f(0.0f, 0.0f, 0.0f); // Black
			
			GL11.glVertex3f(-0.5f, -0.5f, 0.0f);
			GL11.glVertex3f(0.5f, -0.5f, 0.0f);
			
			GL11.glVertex3f(0.5f, 0.5f, 0.0f);
			GL11.glVertex3f(-0.5f, 0.5f, 0.0f);
			
			GL11.glVertex3f(0.5f, -0.5f, 0.0f);
			GL11.glVertex3f(0.5f, 0.5f, 0.0f);
			
			GL11.glVertex3f(-0.5f, -0.5f, 0.0f);
			GL11.glVertex3f(-0.5f, 0.5f, 0.0f);

		GL11.glEnd();
		
		GL11.glRotatef(-Labyrinth.player.eyeLeftRight, 0, 0, 1);
		
		if (Labyrinth.player == Labyrinth.chars.get(0))
			if (Math.sqrt(Math.pow((Labyrinth.chars.get(1).bb.x -Labyrinth.player.bb.x)*MAP_DETAIL,2) + Math.pow((Labyrinth.chars.get(1).bb.z -Labyrinth.player.bb.z)*MAP_DETAIL,2)) + 0.05f < 0.5f) {
				if (Labyrinth.chars.get(1).hasBat) {
					GL11.glBegin(GL11.GL_TRIANGLES);
						// Risanje igralca s kijem na zemljevidu
						
						GL11.glColor3f(222.0f/256.0f, 187.0f/256.0f, 0.0f/256.0f); // Gold
						
						GL11.glVertex3f(-0.025f+(Labyrinth.chars.get(1).bb.x -Labyrinth.player.bb.x)*MAP_DETAIL, -0.025f-(Labyrinth.chars.get(1).bb.z -Labyrinth.player.bb.z)*MAP_DETAIL, 0.0f);
						GL11.glVertex3f(0.025f+(Labyrinth.chars.get(1).bb.x -Labyrinth.player.bb.x)*MAP_DETAIL, -0.025f-(Labyrinth.chars.get(1).bb.z -Labyrinth.player.bb.z)*MAP_DETAIL, 0.0f);
						GL11.glVertex3f(0.0f+(Labyrinth.chars.get(1).bb.x -Labyrinth.player.bb.x)*MAP_DETAIL, 0.025f-(Labyrinth.chars.get(1).bb.z -Labyrinth.player.bb.z)*MAP_DETAIL, 0.0f);

					GL11.glEnd();
				}
				else {
					GL11.glBegin(GL11.GL_LINES);
						// Risanje igralca brez kija na zemljevidu
						
						GL11.glColor3f(0.0f, 0.0f, 0.0f); // Black
						
						GL11.glVertex3f(-0.025f+(Labyrinth.chars.get(1).bb.x -Labyrinth.player.bb.x)*MAP_DETAIL, -0.025f-(Labyrinth.chars.get(1).bb.z -Labyrinth.player.bb.z)*MAP_DETAIL, 0.0f);
						GL11.glVertex3f(0.025f+(Labyrinth.chars.get(1).bb.x -Labyrinth.player.bb.x)*MAP_DETAIL, -0.025f-(Labyrinth.chars.get(1).bb.z -Labyrinth.player.bb.z)*MAP_DETAIL, 0.0f);
		
						GL11.glVertex3f(0.025f+(Labyrinth.chars.get(1).bb.x -Labyrinth.player.bb.x)*MAP_DETAIL, -0.025f-(Labyrinth.chars.get(1).bb.z -Labyrinth.player.bb.z)*MAP_DETAIL, 0.0f);
						GL11.glVertex3f(0.0f+(Labyrinth.chars.get(1).bb.x -Labyrinth.player.bb.x)*MAP_DETAIL, 0.025f-(Labyrinth.chars.get(1).bb.z -Labyrinth.player.bb.z)*MAP_DETAIL, 0.0f);
					
						GL11.glVertex3f(0.0f+(Labyrinth.chars.get(1).bb.x -Labyrinth.player.bb.x)*MAP_DETAIL, 0.025f-(Labyrinth.chars.get(1).bb.z -Labyrinth.player.bb.z)*MAP_DETAIL, 0.0f);
						GL11.glVertex3f(-0.025f+(Labyrinth.chars.get(1).bb.x -Labyrinth.player.bb.x)*MAP_DETAIL, -0.025f-(Labyrinth.chars.get(1).bb.z -Labyrinth.player.bb.z)*MAP_DETAIL, 0.0f);
						
					GL11.glEnd();
				}
			}

		for (int i=2; i < Labyrinth.chars.size(); i++) {
			if (Math.sqrt(Math.pow((Labyrinth.chars.get(i).bb.x -Labyrinth.player.bb.x)*MAP_DETAIL,2) + Math.pow((Labyrinth.chars.get(i).bb.z -Labyrinth.player.bb.z)*MAP_DETAIL,2)) + 0.05f < 0.5f) {
				if (Labyrinth.chars.get(i).hasBat) {
					GL11.glBegin(GL11.GL_TRIANGLES);
						// Risanje igralca s kijem na zemljevidu
						
						GL11.glColor3f(222.0f/256.0f, 187.0f/256.0f, 0.0f/256.0f); // Gold
						
						GL11.glVertex3f(-0.025f+(Labyrinth.chars.get(i).bb.x -Labyrinth.player.bb.x)*MAP_DETAIL, -0.025f-(Labyrinth.chars.get(i).bb.z -Labyrinth.player.bb.z)*MAP_DETAIL, 0.0f);
						GL11.glVertex3f(0.025f+(Labyrinth.chars.get(i).bb.x -Labyrinth.player.bb.x)*MAP_DETAIL, -0.025f-(Labyrinth.chars.get(i).bb.z -Labyrinth.player.bb.z)*MAP_DETAIL, 0.0f);
						GL11.glVertex3f(0.0f+(Labyrinth.chars.get(i).bb.x -Labyrinth.player.bb.x)*MAP_DETAIL, 0.025f-(Labyrinth.chars.get(i).bb.z -Labyrinth.player.bb.z)*MAP_DETAIL, 0.0f);

					GL11.glEnd();
				}
				else {
					GL11.glBegin(GL11.GL_LINES);
						// Risanje igralca brez kija na zemljevidu
						
						GL11.glColor3f(0.0f, 0.0f, 0.0f); // Black
						
						GL11.glVertex3f(-0.025f+(Labyrinth.chars.get(i).bb.x -Labyrinth.player.bb.x)*MAP_DETAIL, -0.025f-(Labyrinth.chars.get(i).bb.z -Labyrinth.player.bb.z)*MAP_DETAIL, 0.0f);
						GL11.glVertex3f(0.025f+(Labyrinth.chars.get(i).bb.x -Labyrinth.player.bb.x)*MAP_DETAIL, -0.025f-(Labyrinth.chars.get(i).bb.z -Labyrinth.player.bb.z)*MAP_DETAIL, 0.0f);
		
						GL11.glVertex3f(0.025f+(Labyrinth.chars.get(i).bb.x -Labyrinth.player.bb.x)*MAP_DETAIL, -0.025f-(Labyrinth.chars.get(i).bb.z -Labyrinth.player.bb.z)*MAP_DETAIL, 0.0f);
						GL11.glVertex3f(0.0f+(Labyrinth.chars.get(i).bb.x -Labyrinth.player.bb.x)*MAP_DETAIL, 0.025f-(Labyrinth.chars.get(i).bb.z -Labyrinth.player.bb.z)*MAP_DETAIL, 0.0f);
					
						GL11.glVertex3f(0.0f+(Labyrinth.chars.get(i).bb.x -Labyrinth.player.bb.x)*MAP_DETAIL, 0.025f-(Labyrinth.chars.get(i).bb.z -Labyrinth.player.bb.z)*MAP_DETAIL, 0.0f);
						GL11.glVertex3f(-0.025f+(Labyrinth.chars.get(i).bb.x -Labyrinth.player.bb.x)*MAP_DETAIL, -0.025f-(Labyrinth.chars.get(i).bb.z -Labyrinth.player.bb.z)*MAP_DETAIL, 0.0f);
						
					GL11.glEnd();
				}
			}
		}
		
		GL11.glRotatef(Labyrinth.player.eyeLeftRight, 0, 0, 1);
		
		// Kij spodaj levo
		if (Labyrinth.chars.get(1).hasBat) {
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_DST_COLOR,GL11.GL_ZERO);
			
			GL11.glTranslatef(0.0f, -5.8f, 0.0f);
			
			Labyrinth.tex[6].bind();
			GL11.glBegin(GL11.GL_QUADS);
			
				GL11.glColor3f(1.0f, 1.0f, 1.0f); // White
				
				GL11.glTexCoord2f(0.0f, 0.0f);
				GL11.glVertex3f(-0.5f, -0.5f, 0.0f);
				
				GL11.glTexCoord2f(1.0f, 0.0f);
				GL11.glVertex3f(0.5f, -0.5f, 0.0f);
				
				GL11.glTexCoord2f(1.0f, 1.0f);
				GL11.glVertex3f(0.5f, 0.5f, 0.0f);
				
				GL11.glTexCoord2f(0.0f, 1.0f);
				GL11.glVertex3f(-0.5f, 0.5f, 0.0f);
				
			GL11.glEnd();
			
			GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
			
			Labyrinth.tex[7].bind();
			GL11.glBegin(GL11.GL_QUADS);
		    
				GL11.glColor3f(1.0f, 1.0f, 1.0f); // White
				
				GL11.glTexCoord2f(0.0f, 0.0f);
				GL11.glVertex3f(-0.5f, -0.5f, 0.0f);
				
				GL11.glTexCoord2f(1.0f, 0.0f);
				GL11.glVertex3f(0.5f, -0.5f, 0.0f);
				
				GL11.glTexCoord2f(1.0f, 1.0f);
				GL11.glVertex3f(0.5f, 0.5f, 0.0f);
				
				GL11.glTexCoord2f(0.0f, 1.0f);
				GL11.glVertex3f(-0.5f, 0.5f, 0.0f);
				
			GL11.glEnd();
			
			GL11.glDisable(GL11.GL_BLEND);
		}
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
	
}
