package racgr.labyrinth;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
* Nadzor igralca.
*
* @author Lovro Šubelj, 63040296
* @author Janoš Vidali, 63040303
*/
public class PlayerAI implements AI {
	/**
	* Objekt, ki ga nadzorujemo.
	*/
	Object3D obj;
	
	/**
	* Konstruktor.
	*
	* @param obj	objekt, ki ga nadzorujemo
	*/
	public PlayerAI(Object3D obj) {
		this.obj = obj;
	}
	
	/**
	* Premikanje igralca.
	*
	* <ul>
	*	<li>W: premik naprej</li>
	*	<li>S: premik nazaj</li>
	*	<li>A: premik levo</li>
	*	<li>D: premik desno</li>
	*	<li>miška: obraèanje pogleda</li>
	* </ul>
	*/
	public void invoke() {
		// Updates camera horizontal rotation.
		float lookAngleLeftRightChange = -(float)Mouse.getDX() / PIXELS_FOR_DEGREE;
		obj.eyeLeftRight += lookAngleLeftRightChange;
		obj.eyeLeftRight = obj.eyeLeftRight%360;

		// Updates camera position due to horizontal rotation change.
		/*float x = obj.eyex;
		float z = obj.eyez;
		obj.eyex = (float)(x * Math.cos(lookAngleLeftRightChange * Math.PI / 180.0f) - z * Math.sin(lookAngleLeftRightChange * Math.PI / 180.0f));
		obj.eyez = (float)(z * Math.cos(lookAngleLeftRightChange * Math.PI / 180.0f) + x * Math.sin(lookAngleLeftRightChange * Math.PI / 180.0f));*/

		// Updates camera vertical rotation.
		obj.eyeUpDown += (float)Mouse.getDY() / PIXELS_FOR_DEGREE;
		
		float step = CAMERA_STEP;
		if ((Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_S))
			&& (Keyboard.isKeyDown(Keyboard.KEY_A) || Keyboard.isKeyDown(Keyboard.KEY_D))) step /= SQRT_2;
		
		// Updates camera position due to pressed keys. 
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			//obj.eyez -= CAMERA_STEP;
			obj.moveX -= (float)(step * Math.sin(obj.eyeLeftRight * Math.PI / 180.0f));
			obj.moveZ -= (float)(step * Math.cos(obj.eyeLeftRight * Math.PI / 180.0f));
		} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			//obj.eyez += CAMERA_STEP;
			obj.moveX += (float)(step * Math.sin(obj.eyeLeftRight * Math.PI / 180.0f));
			obj.moveZ += (float)(step * Math.cos(obj.eyeLeftRight * Math.PI / 180.0f));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			//obj.eyex -= CAMERA_STEP;
			obj.moveZ += (float)(step * Math.sin(obj.eyeLeftRight * Math.PI / 180.0f));
			obj.moveX -= (float)(step * Math.cos(obj.eyeLeftRight * Math.PI / 180.0f));
		} else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			//obj.eyex += CAMERA_STEP;
			obj.moveZ -= (float)(step * Math.sin(obj.eyeLeftRight * Math.PI / 180.0f));
			obj.moveX += (float)(step * Math.cos(obj.eyeLeftRight * Math.PI / 180.0f));
		}
		
//		if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
//			//obj.eyey += CAMERA_STEP;
//			obj.bb.y += CAMERA_STEP;
//		} else if (Keyboard.isKeyDown(Keyboard.KEY_F)) {
//			//obj.eyey -= CAMERA_STEP;
//			obj.bb.y -= CAMERA_STEP;
//		}
		
		// We only allow vertical rotation in some range.
		if (obj.eyeUpDown  > CAMERA_MAX_LOOK_ANGLE_UP_DOWN)
			obj.eyeUpDown = CAMERA_MAX_LOOK_ANGLE_UP_DOWN;
		else if (obj.eyeUpDown  < CAMERA_MIN_LOOK_ANGLE_UP_DOWN)
			obj.eyeUpDown = CAMERA_MIN_LOOK_ANGLE_UP_DOWN;	
		
		// Camera must be above the floor and below the walls.
		/*if (obj.bb.y <= 0)
			obj.bb.y = 0;
		if (obj.bb.y + obj.eyey >= WALL_HEIGHT)
			obj.bb.y = WALL_HEIGHT - obj.eyey;
		*/
		//Morali bi znat iz pozicije lika izraèunat položaj kamere... DONE!
	}
}