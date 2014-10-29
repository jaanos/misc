package racgr.labyrinth;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.glu.Cylinder;

/**
* Baseball kij.
*
* @author Lovro Šubelj, 63040296
*/
public class Bat extends Object3D {
	/**
	* Tekstura kija.
	*/
	Texture t;
	
	/**
	* Velikost palice.
	*/
	private float stickSize;
	
	/**
	* Polmer tankega dela palice.
	*/
	private float bottomRadius;
	
	/**
	* Polmer debelega dela palice.
	*/
	private float topRadius;
		
	/**
	* Ali osebek zamahuje s kijem.
	*/
	boolean isSwinging;
	
	/**
	* Kot zamaha s kijem.
	* 
	* Predznak je odvisen od smeri zamaha.
	*/
	int swingAngle;
	
	/**
	* Konstruktor.
	* 
	* @param t	tekstura
	*/
	public Bat(Texture t) {
		this.t = t;
		
		stickSize = 0.5f;
		bottomRadius = 0.025f;
		topRadius = 0.05f;
	}
	
	/**
	* Metoda za risanje. 
	* 
	* Kij je narisan malce nagnjen, nad izhodišèem. Pred risanjem naj bo izhodišèe nad objektom,
	* ki mu želimo dodati kij.
	*/
	public void render() {
		GL11.glRotatef(this.eyeLeftRight, 0.0f, 1.0f, 0.0f);
			
		if (isSwinging) {
			if (swingAngle == 0)
				isSwinging = false;
			else if (swingAngle == 45) {
				swingAngle -= 9;
				swingAngle = -swingAngle;
			}
			else
				swingAngle += 9;
			
			GL11.glRotatef(-Math.abs(swingAngle), 1.0f, 0.0f, 0.0f);	
		}
		
		GL11.glPushMatrix();

		t.bind();		
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		
		GL11.glTranslatef(0.0f, 0.1f, 0.0f); //razmik
		
		GL11.glTranslatef(0.0f, stickSize/2, 0.0f);
		GL11.glRotatef(-30.0f, 1.0f, 0.0f, 0.0f);
		GL11.glTranslatef(0.0f, -stickSize/2, 0.0f);
		
		Cylinder cyl = new Cylinder();
		cyl.setTextureFlag(true);
		
		GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
		cyl.draw(bottomRadius+0.010f, bottomRadius, (stickSize * 2/5)/8, SLICES_AND_STACKS, SLICES_AND_STACKS);
		GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
		
		GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
		cyl.draw(bottomRadius, bottomRadius, stickSize * 2/5, SLICES_AND_STACKS, SLICES_AND_STACKS);
		GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
		
		GL11.glTranslatef(0.0f, stickSize * 2/5, 0.0f);
		
		GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
		cyl.draw(bottomRadius, topRadius, stickSize * 1/5, SLICES_AND_STACKS, SLICES_AND_STACKS);
		GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
		
		GL11.glTranslatef(0.0f, stickSize * 1/5, 0.0f);
		
		GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
		cyl.draw(topRadius, topRadius, stickSize * 2/5, SLICES_AND_STACKS, SLICES_AND_STACKS);
		GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
		
		GL11.glTranslatef(0.0f, stickSize * 2/5, 0.0f);
		
		org.lwjgl.opengl.glu.Sphere sph = new org.lwjgl.opengl.glu.Sphere();
		sph.setTextureFlag(true);
		
		sph.draw(topRadius, SLICES_AND_STACKS, SLICES_AND_STACKS);
		
		GL11.glPopMatrix();
	}
	
	/**
	* Metoda za zamah s kijem.
	*/	
	public void swing() {
		if (!isSwinging) {
			isSwinging = true;
			swingAngle = 9;
		}
	}
}