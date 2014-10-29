package racgr.labyrinth;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.glu.Cylinder;

/**
* Bratranec Itt.
*
* @author Lovro Šubelj, 63040296
*/
public class CousinItt extends Object3D {
	/**
	* Tekstura bratranca - lasje.
	*/
	Texture t;
	
	/**
	* Konstruktor.
	*
	* @param x	X koordinata spodnjega levega oglišèa
	* @param y	Y koordinata spodnjega levega oglišèa
	* @param z	Z koordinata spodnjega levega oglišèa
	* @param t	tekstura
	*/
	public CousinItt(float x, float y, float z, Texture t) {
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
		
		t.bind();
		GL11.glTranslatef(bb.x+bb.w/2, 0, bb.z+bb.d/2);
		
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		
		Cylinder cyl = new Cylinder();
		cyl.setTextureFlag(true);
		
		GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
		cyl.draw(bb.w/2, bb.w/2, bb.h-bb.w/2, SLICES_AND_STACKS, SLICES_AND_STACKS);
		GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
		
		GL11.glTranslatef(0.0f, bb.h-bb.w/2, 0.0f);
		
		org.lwjgl.opengl.glu.Sphere sph = new org.lwjgl.opengl.glu.Sphere();
		sph.setTextureFlag(true);
		
		GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
		sph.draw(bb.w/2, SLICES_AND_STACKS, SLICES_AND_STACKS);
		GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
		
		GL11.glTranslatef(0.0f, bb.w/2, 0.0f);
		
		org.lwjgl.opengl.glu.Disk dsk = new org.lwjgl.opengl.glu.Disk();
		sph.setTextureFlag(false);
		
		GL11.glColor3f(148.0f/256.0f, 148.0f/256.0f, 148.0f/256.0f);
		
		GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
		dsk.draw(0.0f, 0.130f, SLICES_AND_STACKS, SLICES_AND_STACKS);
		GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);

		GL11.glColor3f(0.0f, 0.0f, 0.0f);
		
		GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
		dsk.draw(0.130f, 0.175f, SLICES_AND_STACKS, SLICES_AND_STACKS);
		GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);

		sph.draw(0.12f, SLICES_AND_STACKS, SLICES_AND_STACKS);
		
		// Nariše kij.
		if (hasBat) {
			GL11.glTranslatef(0.0f, 0.12f, 0.0f);
			
			GL11.glRotatef(this.eyeLeftRight, 0.0f, 1.0f, 0.0f);
			
			Labyrinth.bat.render();
		}
		
		GL11.glPopMatrix();
	}
	
	/**
	* Akcija ob trku.
	*
	* Postavi bratranca na pozicijo pred trkom.
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