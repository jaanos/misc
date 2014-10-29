package racgr.labyrinth;

/**
* Umetna inteligenca osebka.
*
* @author Lovro Šubelj, 63040296
*/
public class CharAI implements AI {
	/**
	* Osebek, ki se premika.
	*/
	Object3D obj;
	
	/**
	* Položaj zadnje obiskane celice po dolžini.
	*/
	private int ilast;
	
	/**
	* Položaj zadnje obiskane celice po širini.
	*/
	private int jlast;
	
	/**
	* Konstruktor.
	*
	* @param obj	osebek, ki se premika
	*/
	public CharAI(Object3D obj) {
		this.obj = obj;
	}
	
	/**
	* Premikanje osebka - umetna inteligenca osebka.
	* 
	* Osebek se poskuša gibati proti vratom ali pa proti osebku, ki poseduje kij.
	* Izogiba se hoji nazaj ter "vrtenju na mestu". Išèe najdaljšo pot, ki jo je
	* moè nadaljevati - pot ki ni slepa.
	*/
	public void invoke() {
		int imid = Labyrinth.cellsInLength+(int)((obj.bb.z+obj.bb.d/2)/CELL_SIZE)-1;
		int jmid = (int)((obj.bb.x+obj.bb.w/2)/CELL_SIZE);
		
		// Èe je osebek prviè v neki celici, se odloèi v katero smer bo šel.
		// Izogiba se hoji nazaj.
		if ((imid != ilast || jmid != jlast) && imid >= 0 && imid <= Labyrinth.cellsInLength-1 && jmid >= 0 && jmid <= Labyrinth.cellsInWidth-1) {
			ilast = imid;
			jlast = jmid;
			
			int up = 0;
			int down = 0;
			int left = 0;
			int right = 0;
			
			while (Labyrinth.labyrinth[imid-up][jmid] == 0) {
				up++;
				if (imid-up < 0)
					break;
			}
			up--;
			while (up > 1 && Labyrinth.labyrinth[imid-up][jmid+1] + Labyrinth.labyrinth[imid-up][jmid-1] != 0)
				up--;
			
			while (Labyrinth.labyrinth[imid+down][jmid] == 0) {
				down++;
				if (imid+down >= Labyrinth.labyrinth.length)
					break;
			}
			down--;
			while (down > 1 && Labyrinth.labyrinth[imid+down][jmid+1] + Labyrinth.labyrinth[imid+down][jmid-1] != 0)
				down--;
			
			while (Labyrinth.labyrinth[imid][jmid+right] == 0) {
				right++;
				if (jmid+right >= Labyrinth.labyrinth[imid].length)
					break;
			}
			right--;
			while (right > 1 && Labyrinth.labyrinth[imid+1][jmid+right] + Labyrinth.labyrinth[imid-1][jmid+right] != 0)
				right--;
			
			while (Labyrinth.labyrinth[imid][jmid-left] == 0) {
				left++;
				if (jmid-left < 0)
					break;
			}
			left--;
			while (left > 1 && Labyrinth.labyrinth[imid+1][jmid-left] + Labyrinth.labyrinth[imid-1][jmid-left] != 0)
				left--;
			
			float prcnt = 0.10f;
			float power = 1.41f;
			int moveToI = 0, moveToJ = 0;
			
			if (obj.hasBat) { // move towards the door
//				System.out.println("imin: "+imin+", imax: "+imax+", jmin: "+jmin+", jmax: "+jmax);
//				System.out.println(up+" "+down+" "+left+" "+right);
				moveToI = Labyrinth.doorsLength;
				moveToJ = Labyrinth.doorsWidth;
			}
			else // move towards the player with a bat
				for (int i=1; i < Labyrinth.chars.size(); i++)
					if (Labyrinth.chars.get(i).hasBat) {
						moveToI = Labyrinth.cellsInLength+(int)((Labyrinth.chars.get(i).bb.z+Labyrinth.chars.get(i).bb.d/2)/CELL_SIZE)-1;
						moveToJ = (int)((Labyrinth.chars.get(i).bb.x+Labyrinth.chars.get(i).bb.w/2)/CELL_SIZE);
						break;
					}
				
			float tmp = Math.abs(moveToI - imid) / (0.0f + Math.abs(moveToI - imid) + Math.abs(moveToJ - jmid));
			if (tmp > prcnt)
				if (moveToI - imid < 0)
					up = (int)Math.pow(up, power);
				else
					down = (int)Math.pow(down, power);
			
			tmp = 1 - tmp;
			if (tmp > prcnt)
				if (moveToJ - jmid < 0)
					left = (int)Math.pow(left, power);
				else
					right = (int)Math.pow(right, power);
			
			int UP = 0;
			int DOWN = 180;
			int LEFT = 90;
			int RIGHT = 270;
				
			if (obj.eyeLeftRight == UP) {
				int max = Math.max(left,Math.max(up,right));
				if (max == 0)
					obj.eyeLeftRight = DOWN; //go back
				else if (max == up)
					obj.eyeLeftRight = UP;
				else if (max == left)
					obj.eyeLeftRight = LEFT;
				else if (max == right)
					obj.eyeLeftRight = RIGHT;
			}
			else if (obj.eyeLeftRight == DOWN) {
				int max = Math.max(right,Math.max(down,left));
				if (max == 0)
					obj.eyeLeftRight = UP; //go back
				else if (max == down)
					obj.eyeLeftRight = DOWN;
				else if (max == right)
					obj.eyeLeftRight = RIGHT;
				else if (max == left)
					obj.eyeLeftRight = LEFT;
			}
			else if (obj.eyeLeftRight == LEFT) {
				int max = Math.max(down,Math.max(left,up));
				if (max == 0)
					obj.eyeLeftRight = RIGHT; //go back
				else if (max == left)
					obj.eyeLeftRight = LEFT;
				else if (max == down)
					obj.eyeLeftRight = DOWN;
				else if (max == up)
					obj.eyeLeftRight = UP;
			}
			else if (obj.eyeLeftRight == RIGHT) {
				int max = Math.max(up,Math.max(right,down));
				if (max == 0)
					obj.eyeLeftRight = LEFT; //go back
				else if (max == right)
					obj.eyeLeftRight = RIGHT;
				else if (max == up)
					obj.eyeLeftRight = UP;
				else if (max == down)
					obj.eyeLeftRight = DOWN;
			}
		}

		// Premakne osebek naprej. 
		obj.moveX = -(float)(CAMERA_STEP / SLOW_DOWN * Math.sin(obj.eyeLeftRight * Math.PI / 180.0f));
		obj.moveZ = -(float)(CAMERA_STEP / SLOW_DOWN * Math.cos(obj.eyeLeftRight * Math.PI / 180.0f));

		// Nakljuèni premiki v levo in desno - da se ne zatakne kje. 
		switch ((int)(Math.random() * 15)){
			case 0: //levo
				obj.moveZ += (float)(CAMERA_STEP / (2*SLOW_DOWN) * Math.sin(obj.eyeLeftRight * Math.PI / 180.0f));
				obj.moveX -= (float)(CAMERA_STEP / (2*SLOW_DOWN) * Math.cos(obj.eyeLeftRight * Math.PI / 180.0f));
				break;
			case 1: //desno
				obj.moveZ -= (float)(CAMERA_STEP / (2*SLOW_DOWN) * Math.sin(obj.eyeLeftRight * Math.PI / 180.0f));
				obj.moveX += (float)(CAMERA_STEP / (2*SLOW_DOWN) * Math.cos(obj.eyeLeftRight * Math.PI / 180.0f));
				break;
			case 14: //zamah s kijem:)
				obj.swing();
		}
		
	}
	
}
