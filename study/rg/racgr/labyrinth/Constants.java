package racgr.labyrinth;

/**
* Konstante.
*
* @author Lovro Šubelj, 63040296
* @author Janoš Vidali, 63040303
*/
interface Constants {
	/**
	* Naslov okna.
	*/
	String TITLE = "Labyrinth";
	
	/**
	* Število slikic na sekundo.
	*/
	int FRAMERATE = 60;
	
	/**
	* Velikost celice.
	*/
	int CELL_SIZE = 2;
	
	/**
	* Višina zidu.
	*/
	int WALL_HEIGHT = 4;
	
	/**
	* Upoèasnitev nakljuèno premikajoèega osebka.
	*/
	int SLOW_DOWN = 6;
	
	/**
	* Število rezin in skladov za okrogle objekte.
	*/
	int SLICES_AND_STACKS = 48;
	
	/**
	* Število milisekund, ko objektu ni moè odvzeti kija.
	*/
	long BAT_HOLD_TIME = 5000;
	
	/**
	* Najmanjši razmik s tlemi.
	*/
	float CAMERA_ABOVE_WALLS_MINIMUM = 1.0f;
	
	/**
	* Premik igralca ob enem pritisku tipke.
	*/
	float CAMERA_STEP = 0.5f;
	
	/**
	* Najveèji pogled gor.
	*/
	float CAMERA_MAX_LOOK_ANGLE_UP_DOWN = 90.0f;
	
	/**
	* Najveèji pogled dol.
	*/
	float CAMERA_MIN_LOOK_ANGLE_UP_DOWN = -90.0f;
	
	/**
	* Potreben premik miške za stopinjo premika pogleda.
	*/
	float PIXELS_FOR_DEGREE = 6.0f;
	
	/**
	* Število celic znotraj ene ponovitve teksture tal.
	*/
	int CELLS_FOR_SINGLE_TEXTURE_FOR_FLOOR = 4;
	
	/**
	* Najveèji odmik kamere od igralca.
	*/
	float MAX_CAMERA_FROM_PLAYER = 20.0f;
	
	/**
	* Koren 2 v float obliki.
	*/
	float SQRT_2 = (float)Math.sqrt(2);
}