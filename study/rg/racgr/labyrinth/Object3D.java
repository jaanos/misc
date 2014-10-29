package racgr.labyrinth;

import org.lwjgl.opengl.GL11;

/**
* Abstraktni razred za 3D objekte.
*
* @author Janoš Vidali, 63040303
* @author Lovro Šubelj, 63040296
*/
abstract class Object3D implements Constants {	
	/**
	* Vsebujoèa škatla.
	*/
	BoundingBox bb;
	
	/**
	* Umetna inteligenca.
	*/
	AI ai;
	
	/**
	* X koordinata oèesa.
	*/
	float eyex;
	
	/**
	* Y koordinata oèesa.
	*/
	float eyey;
	
	/**
	* Z koordinata oèesa.
	*/
	float eyez;
	
	/**
	* Kot pogleda gor/dol.
	*/
	float eyeUpDown;
	
	/**
	* Kot pogleda levo/desno.
	*/
	float eyeLeftRight;
	
	/**
	* Ali ima objekt lahko kij za razbijanje zidov - živ objekt.
	*/
	boolean liveObject;
	
	/**
	* Ali ima objekt kij za razbijanje zidov.
	*/
	boolean hasBat;
	
	/**
	* Èas prejetja kija za razbijanje zidov v milisekundah.
	*/
	private long batRecTime;
		
	/**
	* Premik ob zadnjem trku.
	*/
	float[] collideMove = new float[3];
	
	/**
	* Zadnji premik po X.
	*/
	float moveX;
	
	/**
	* Zadnji premik po Y.
	*/
	float moveY;
	
	/**
	* Zadnji premik po Z.
	*/
	float moveZ;
	
	/**
	* Konstruktor.
	*/
	protected Object3D() {}
	
	/**
	* Konstruktor.
	*
	* @param x				X koordinata spodnjega levega zadnjega oglišèa
	* @param y				Y koordinata spodnjega levega zadnjega oglišèa
	* @param z				Z koordinata spodnjega levega zadnjega oglišèa
	* @param w				širina (po X)
	* @param h				višina (po Y)
	* @param d				globina (po Z)
	* @param liveObject		ali je živ objekt
	*/
	public Object3D(float x, float y, float z, float w, float h, float d, boolean liveObject) {
		bb = new BoundingBox(x, y, z, w, h, d);
		this.liveObject = liveObject;
	}
	
	/**
	* Detekcja trka.
	*
	* Preveri za trke in sproži ustrezne akcije na obeh objektih
	* ter prenos kija.
	*
	* @param obj	objekt, s katerim preverjamo trk
	* @param th		meja, na katero se lahko objekta približata
	* @param both	ali bosta oba objekta reagirala
	* @return		ali je prišlo do trka
	*/
	public boolean collide(Object3D obj, float th, boolean both) {
		float[] c = collision(obj, th, both);
		if (c != null) {
			float mx = moveX;
			float my = moveY;
			float mz = moveZ;
			collideAction(-c[0],-c[1],-c[2],both,obj.moveX,obj.moveY,obj.moveZ);
			obj.collideAction(c[0],c[1],c[2],both,mx,my,mz);
			 
			if (liveObject && obj.liveObject)
				if (giveBat())
					obj.receiveBat();
				else if (obj.giveBat())
					receiveBat();
			return true;
		}
		return false;
	}

	/**
	* Trk z zidovi.
	*
	* Preveri, ali je objekt zaradi trkov z drugimi objekti morda
	* prešel skozi zid. Èe je temu tako, ga postavi na zadnje mesto
	* pred zidom, kjer se je lahko znašel.
	*/
	public void collideWalls() {
		//Ali smo zleteli skozi zid ob zadnjem trku?
		float mX = Math.abs(collideMove[0]);
		float mZ = Math.abs(collideMove[2]);
		//System.out.println(collideMove[0]+" "+collideMove[2]);
		try {
			if (mX >= CELL_SIZE || mZ >= CELL_SIZE || ((mX > 0 || mZ > 0)
				&& Labyrinth.labyrinth[Labyrinth.cellsInLength+(int)(bb.z/CELL_SIZE)][(int)(bb.x/CELL_SIZE)] > 0
				&& Labyrinth.labyrinth[Labyrinth.cellsInLength+(int)((bb.z+bb.d)/CELL_SIZE)][(int)(bb.x/CELL_SIZE)] > 0
				&& Labyrinth.labyrinth[Labyrinth.cellsInLength+(int)(bb.z/CELL_SIZE)][(int)((bb.x+bb.w)/CELL_SIZE)] > 0
				&& Labyrinth.labyrinth[Labyrinth.cellsInLength+(int)((bb.z+bb.d)/CELL_SIZE)][(int)((bb.x+bb.w)/CELL_SIZE)] > 0)) {
				float stepX = collideMove[0];
				float stepZ = collideMove[2];
				float x = bb.x - collideMove[0];
				float z = bb.z - collideMove[2];
				float oldx, oldz;
				int i = Labyrinth.cellsInLength+(int)(z/CELL_SIZE);
				int j = (int)(x/CELL_SIZE);
				if (mX > mZ) {
					stepX /= mX;
					stepZ /= mX;
					if (collideMove[0] > 0) {
						while (x <= bb.x) {
							oldx = x;
							oldz = z;
							if (stepZ != 0 && i != Labyrinth.cellsInLength+(int)((z+stepZ)/CELL_SIZE)) {
								if (stepZ > 0) {
									i++;
								} else {
									i--;
								}
								z = (i - Labyrinth.cellsInLength)*CELL_SIZE;
								x += (z - oldz)/stepZ*stepX;
							} else {
								x += stepX;
								z += stepZ;
								j++;
							}
							if (Labyrinth.labyrinth[i][j] > 0) {
								if (x != oldx) {
									bb.x = j*CELL_SIZE-bb.w;
									bb.z = z-(x-bb.x)/stepX*stepZ;
								} else {
									if (oldz > z) {
										bb.z = (i - Labyrinth.cellsInLength)*CELL_SIZE - bb.d;
									} else {
										bb.z = (i+1 - Labyrinth.cellsInLength)*CELL_SIZE;
									}
									bb.x = x - (z-bb.z)/stepZ*stepX;
								}
								break;
							}
						}
					} else {
						while (x >= bb.x) {
							oldx = x;
							oldz = z;
							if (stepZ != 0 && i != Labyrinth.cellsInLength+(int)((z+stepZ)/CELL_SIZE)) {
								if (stepZ > 0) {
									i++;
								} else {
									i--;
								}
								z = (i - Labyrinth.cellsInLength)*CELL_SIZE;
								x += (z - oldz)/stepZ*stepX;
							} else {
								x += stepX;
								z += stepZ;
								j--;
							}
							if (Labyrinth.labyrinth[i][j] > 0) {
								if (x != oldx) {
									bb.x = (j+1)*CELL_SIZE;
									bb.z = z-(x-bb.x)/stepX*stepZ;
								} else {
									if (oldz > z) {
										bb.z = (i - Labyrinth.cellsInLength)*CELL_SIZE - bb.d;
									} else {
										bb.z = (i+1 - Labyrinth.cellsInLength)*CELL_SIZE;
									}
									bb.x = x - (z-bb.z)/stepZ*stepX;
								}
								break;
							}
						}
					}
				} else {
					stepX /= mZ;
					stepZ /= mZ;
					if (collideMove[2] > 0) {
						while (z <= bb.z) {
							oldx = x;
							oldz = z;
							if (stepX != 0 && j != (int)((x+stepX)/CELL_SIZE)) {
								if (stepX > 0) {
									j++;
								} else {
									j--;
								}
								x = j*CELL_SIZE;
								z += (x - oldx)/stepX*stepZ;
							} else {
								x += stepX;
								z += stepZ;
								i--;
							}
							if (Labyrinth.labyrinth[i][j] > 0) {
								if (z != oldz) {
									bb.z = (i - Labyrinth.cellsInLength)*CELL_SIZE - bb.d;
									bb.x = x-(z-bb.z)/stepZ*stepX;
								} else {
									if (oldx > x) {
										bb.x = j*CELL_SIZE-bb.w;
									} else {
										bb.x = (j+1)*CELL_SIZE;
									}
									bb.x = x - (z-bb.z)/stepZ*stepX;
								}
								break;
							}
						}
					} else {
						while (x >= bb.x) {
							oldx = x;
							oldz = z;
							if (stepX != 0 && j != (int)((x+stepX)/CELL_SIZE)) {
								if (stepX > 0) {
									j++;
								} else {
									j--;
								}
								x = j*CELL_SIZE;
								z += (x - oldx)/stepX*stepZ;
							} else {
								x += stepX;
								z += stepZ;
								i++;
							}
							if (Labyrinth.labyrinth[i][j] > 0) {
								if (z != oldz) {
									bb.z = (i+1 - Labyrinth.cellsInLength)*CELL_SIZE;
									bb.x = x-(z-bb.z)/stepZ*stepX;
								} else {
									if (oldx > x) {
										bb.x = j*CELL_SIZE-bb.w;
									} else {
										bb.x = (j+1)*CELL_SIZE;
									}
									bb.x = x - (z-bb.z)/stepZ*stepX;
								}
								break;
							}
						}
					}
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {}
		
		int imin = Labyrinth.cellsInLength+(int)(bb.z/CELL_SIZE) - 2;
		int imax = Labyrinth.cellsInLength+(int)((bb.z+bb.d)/CELL_SIZE) + 2;
		int jmin = (int)(bb.x/CELL_SIZE) - 2;
		int jmax = (int)((bb.x+bb.w)/CELL_SIZE) + 2;
		if (imin < 0) imin = 0;
		if (imax < 0) imax = 0;
		if (imin >= Labyrinth.cellsInLength) imin = Labyrinth.cellsInLength;
		if (imax >= Labyrinth.cellsInLength) imax = Labyrinth.cellsInLength;
		if (jmin < 0) jmin = 0;
		if (jmax < 0) jmax = 0;
		if (jmin >= Labyrinth.cellsInWidth) jmin = Labyrinth.cellsInWidth;
		if (jmax >= Labyrinth.cellsInWidth) jmax = Labyrinth.cellsInWidth;

		for(int i = imin; i < imax; i++) {
			for(int j = jmin; j < jmax; j++) {
				if (Labyrinth.labyrinth[i][j] == 1 || (j == jmin && Labyrinth.labyrinth[i][j] == 2) || (i == imin && Labyrinth.labyrinth[i][j] == 3)) {
					collide(Labyrinth.walls[i][j], 0.5f, false);
				}
			}
		}
	}
	
	/**
	* Detekcija trka.
	*
	* Preveri, ali se vsebujoèi škatli sekata.
	*
	* @param obj	objekt, s katerim preverjamo trk
	* @param th		meja, na katero se lahko objekta približata
	* @param both	ali bosta oba objekta reagirala
	* @return		null, èe trka ni, oziroma array 3 floatov
	*				z globinami vgreza po x, y, z
	*/
	protected float[] collision(Object3D obj, float th, boolean both) {
		/*if (obj instanceof MultipartObject) {
			obj.collide(this, th, both);
			return null;
		}*/
		return bb.intersection(obj.bb, th);
	}
	
	/**
	* Akcija ob trku.
	*
	* Glede na premik obeh objektov amortizira njun odboj.
	*
	* @param x		vgrez po x
	* @param y		vgrez po y
	* @param z		vgrez po z
	* @param both	ali bosta oba objekta reagirala
	* @param mx		premik po x
	* @param my		premik po y
	* @param mz		premik po z
	*/
	protected void collideAction(float x, float y, float z, boolean both, float mx, float my, float mz) {
		float nx = 1;
		float ny = 1;
		float nz = 1;
		if (both) {
			if (mx*moveX > 0) nx = moveX/(moveX+mx);
			if (my*moveY > 0) ny = moveY/(moveY+my);
			if (mz*moveZ > 0) nz = moveZ/(moveZ+mz);
		}
		
		/*if (moveX != 0 || moveZ != 0) {
			if (this == Viewer.player) System.out.println("igralec:");
			System.out.println(bb.x+" "+bb.z+" "+x+" "+z+" "+moveX+" "+moveZ+" "+n);
		}*/
		//Upoštevajmo smer premikanja
		if (moveX*x >= 0) x = 0;
		if (moveY*y >= 0) y = 0;
		if (moveZ*z >= 0) z = 0;
		
		bb.x += x*nx;
		bb.y += y*ny;
		bb.z += z*nz;
		if (both) {
			collideMove[0] += x*nx;
			collideMove[1] += y*ny;
			collideMove[2] += z*nz;
			moveX += x*nx;
			moveY += y*ny;
			moveZ += z*nz;
		}
	}
		
	/**
	* Metoda za risanje.
	*/
	public abstract void render();
		
	/**
	* Metoda za gledanje.
	*
	* Postavi gledišèe na trenutno pozicijo.
	*/
	public void look() {
		GL11.glRotatef(-eyeUpDown, 1, 0, 0);
		GL11.glRotatef(-eyeLeftRight, 0, 1, 0);
		GL11.glTranslatef(-(eyex+bb.x), -(eyey+bb.y), -(eyez+bb.z));
	}
	
	/**
	* Metoda za premikanje.
	*/	
	public void move() {
		moveX = 0;
		moveY = 0;
		moveZ = 0;
		ai.invoke();
		bb.x += moveX;
		bb.y += moveY;
		bb.z += moveZ;
	}
	
	/**
	* Metoda za prejetje kija.
	*/	
	public void receiveBat() {
		batRecTime = System.currentTimeMillis();
		hasBat = true;
	}
	
	/**
	* Metoda za odvzem kija.
	* 
	* @return		<i>true</i>, èe je objekt lahko dal kij, 
	* 				in <i>false</i> sicer
	*/	
	public boolean giveBat() {
		if (hasBat && System.currentTimeMillis()-batRecTime>BAT_HOLD_TIME){
			hasBat = false;
			batRecTime = 0;
			Labyrinth.bat.isSwinging = false;
			Labyrinth.bat.swingAngle = 0;
			return true;
		}
		return false;
	}
	
	/**
	* Metoda za zamah s kijem.
	*/	
	public void swing() {
		if (hasBat) Labyrinth.bat.swing();
	}
	
}