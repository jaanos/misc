package racgr.labyrinth;

import org.lwjgl.opengl.GL11;

/**
* Krogla.
*
* @author Lovro Šubelj, 63040296
*/
public class Sphere extends Object3D {
	/**
	* Tekstura osebka.
	*/
	Texture t;
	
	/**
	* Konstruktor.
	*
	* @param x	X koordinata spodnjega levega oglišèa navidezne kocke, ki omejuje kroglo
	* @param y	Y koordinata spodnjega levega oglišèa navidezne kocke, ki omejuje kroglo
	* @param z	Z koordinata spodnjega levega oglišèa navidezne kocke, ki omejuje kroglo
	* @param t	tekstura
	*/
	public Sphere(float x, float y, float z, Texture t) {
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
		GL11.glTranslatef(bb.x+bb.w/2, bb.y+bb.h/2, bb.z+bb.d/2);
		
		t.bind();
		GL11.glColor3f(1.0f, 1.0f, 1.0f); // White
		
		org.lwjgl.opengl.glu.Sphere sph = new org.lwjgl.opengl.glu.Sphere();
		sph.setTextureFlag(true);
		
		sph.draw(bb.w/2, SLICES_AND_STACKS, SLICES_AND_STACKS);
		
		// Nariše kij.
		if (hasBat) {
			GL11.glTranslatef(0.0f, bb.h/2, 0.0f);
			
			GL11.glRotatef(this.eyeLeftRight, 0.0f, 1.0f, 0.0f);
			
			Labyrinth.bat.render();
		}
		
		GL11.glPopMatrix();
	}	
}