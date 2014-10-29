package racgr.labyrinth;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
* Kamera za pogled okoli igralca.
*
* @author Lovro Šubelj, 63040296
* @author Janoš Vidali, 63040303
*/
class Camera extends Object3D implements AI {
	/**
	* Igralec, ki upravlja s kamero.
	*/
	public Object3D player;
	
	/**
	* Konstruktor.
	*
	* @param player		igralec, ki upravlja s kamero
	*/
	public Camera(Object3D player) {
		super(0,WALL_HEIGHT + CAMERA_ABOVE_WALLS_MINIMUM,0,0,0,0,false);
		this.player = player;
		ai = this;
	}
	
	/**
	* Metoda za risanje.
	*
	* Kamere ne rišemo.
	*/
	public void render() {}
		
	/**
	* Akcija ob trku.
	*
	* Ni akcije.
	*
	* @param x		vgrez po x
	* @param y		vgrez po y
	* @param z		vgrez po z
	* @param both	ali bosta oba objekta reagirala
	* @param mx		premik po x
	* @param my		premik po y
	* @param mz		premik po z
	*/
	protected void collideAction(float x, float y, float z, boolean both, float mx, float my, float mz) {}
	
	/**
	* Premikanje kamere.
	*
	* <ul>
	*	<li>W: premik naprej</li>
	*	<li>S: premik nazaj</li>
	*	<li>A: premik levo</li>
	*	<li>D: premik desno</li>
	*	<li>R: premik gor</li>
	*	<li>F: premik dol</li>
	*	<li>miška: obraèanje pogleda</li>
	* </ul>
	*/
	public void invoke() {
		// Updates camera horizontal rotation.
		float lookAngleLeftRightChange = -(float)Mouse.getDX() / PIXELS_FOR_DEGREE;
		eyeLeftRight += lookAngleLeftRightChange;
		eyeLeftRight = eyeLeftRight%360;

		// Updates camera vertical rotation.
		eyeUpDown += (float)Mouse.getDY() / PIXELS_FOR_DEGREE;
		
		// Updates camera position due to pressed keys. 
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			bb.x -= (float)(CAMERA_STEP * Math.sin(eyeLeftRight * Math.PI / 180.0f));
			bb.z -= (float)(CAMERA_STEP * Math.cos(eyeLeftRight * Math.PI / 180.0f));
		} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			bb.x += (float)(CAMERA_STEP * Math.sin(eyeLeftRight * Math.PI / 180.0f));
			bb.z += (float)(CAMERA_STEP * Math.cos(eyeLeftRight * Math.PI / 180.0f));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			bb.z += (float)(CAMERA_STEP * Math.sin(eyeLeftRight * Math.PI / 180.0f));
			bb.x -= (float)(CAMERA_STEP * Math.cos(eyeLeftRight * Math.PI / 180.0f));
		} else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			bb.z -= (float)(CAMERA_STEP * Math.sin(eyeLeftRight * Math.PI / 180.0f));
			bb.x += (float)(CAMERA_STEP * Math.cos(eyeLeftRight * Math.PI / 180.0f));
		}
		
		if ((bb.x-player.bb.x)*(bb.x-player.bb.x) + (bb.z-player.bb.z)*(bb.z-player.bb.z) > MAX_CAMERA_FROM_PLAYER*MAX_CAMERA_FROM_PLAYER) {
			float k = MAX_CAMERA_FROM_PLAYER/(float)Math.sqrt(((bb.x-player.bb.x)*(bb.x-player.bb.x) + (bb.z-player.bb.z)*(bb.z-player.bb.z)));
			bb.x = player.bb.x + k*(bb.x-player.bb.x);
			bb.z = player.bb.z + k*(bb.z-player.bb.z);
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
			bb.y += CAMERA_STEP;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_F)) {
			bb.y -= CAMERA_STEP;
		}
		
		// We only allow vertical rotation in some range.
		if (eyeUpDown  > CAMERA_MAX_LOOK_ANGLE_UP_DOWN)
			eyeUpDown = CAMERA_MAX_LOOK_ANGLE_UP_DOWN;
		else if (eyeUpDown  < CAMERA_MIN_LOOK_ANGLE_UP_DOWN)
			eyeUpDown = CAMERA_MIN_LOOK_ANGLE_UP_DOWN;	
		
		// Camera must be above the walls.
		if (bb.y <= WALL_HEIGHT + CAMERA_ABOVE_WALLS_MINIMUM)
			bb.y = WALL_HEIGHT + CAMERA_ABOVE_WALLS_MINIMUM;
		if (bb.y >= 2*WALL_HEIGHT + CAMERA_ABOVE_WALLS_MINIMUM)
			bb.y = 2*WALL_HEIGHT + CAMERA_ABOVE_WALLS_MINIMUM;
	}
}