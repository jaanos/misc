package racgr.labyrinth;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.glu.Cylinder;

/**
* Stožec.
*
* @author Lovro Šubelj, 63040296
*/
public class Cone extends Object3D {
	/**
	* Tekstura osebka.
	*/
	Texture t;
	
	/**
	* Konstruktor.
	*
	* @param x	X koordinata spodnjega levega oglišèa navideznega kvadra, ki omejuje stožec
	* @param y	Y koordinata spodnjega levega oglišèa navideznega kvadra, ki omejuje stožec
	* @param z	Z koordinata spodnjega levega oglišèa navideznega kvadra, ki omejuje stožec
	* @param t	tekstura
	*/
	public Cone(float x, float y, float z, Texture t) {
		super(x,y,z,0.8f,0.8f,0.8f,true);
		this.t = t;
		eyex = 0.4f;
		eyey = 2.5f;
		eyez = 0.4f;
	}
	
	/**
	* Metoda za risanje.
	*/
	public void render() {
		GL11.glPushMatrix();
		GL11.glTranslatef(bb.x+bb.w/2, bb.y, bb.z+bb.d/2);
		
		t.bind();
		GL11.glColor3f(1.0f, 1.0f, 1.0f); // White
		
		Cylinder cyl = new Cylinder();
		cyl.setTextureFlag(true);
		
		GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
		cyl.draw(bb.w/2, 0.0f, bb.h, SLICES_AND_STACKS, SLICES_AND_STACKS);
		GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
		
		// Nariše kij.
		if (hasBat) {
			GL11.glTranslatef(0.0f, bb.h, 0.0f);
			
			GL11.glRotatef(this.eyeLeftRight, 0.0f, 1.0f, 0.0f);
			
			Labyrinth.bat.render();
		}
		
		GL11.glPopMatrix();
	}
	
	/**
	* Akcija ob trku.
	*
	* Postavi stožec na pozicijo pred trkom.
	*
	* @param x		vgrez po x
	* @param y		vgrez po y
	* @param z		vgrez po z
	*/
	/*protected void collideAction(float x, float y, float z) {
		bb.x += x;
		bb.y += y;
		bb.z += z;
	}*/
}