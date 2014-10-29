package racgr.labyrinth;

import org.lwjgl.opengl.GL11;

/**
* Kocka.
*
* @author Janoš Vidali, 63040303
* @author Lovro Šubelj, 63040296
*/
public class Cube extends Object3D {
	/**
	* Tekstura osebka.
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
	public Cube(float x, float y, float z, Texture t) {
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
		t.bind();
		
		GL11.glBegin(GL11.GL_QUADS);
						
			// Front side.
//			GL11.glColor3f(1.0f, 0.0f, 0.0f); // Red
			GL11.glColor3f(1.0f, 1.0f, 1.0f); // White
		
			/*if (tex)*/ GL11.glTexCoord2f(0.0f, 0.0f);
			GL11.glVertex3f(bb.x, bb.y, bb.z+bb.d);
			/*if (tex)*/ GL11.glTexCoord2f(1.0f, 0.0f);
			GL11.glVertex3f(bb.x+bb.w, bb.y, bb.z+bb.d);
			/*if (tex)*/ GL11.glTexCoord2f(1.0f, 1.0f);
			GL11.glVertex3f(bb.x+bb.w, bb.y+bb.h, bb.z+bb.d);
			/*if (tex)*/ GL11.glTexCoord2f(0.0f, 1.0f);
			GL11.glVertex3f(bb.x, bb.y+bb.h, bb.z+bb.d);
		
			// Back side.
//			GL11.glColor3f(0.0f, 1.0f, 0.0f); // Green
			GL11.glColor3f(1.0f, 1.0f, 1.0f); // White
		
			/*if (tex)*/ GL11.glTexCoord2f(0.0f, 0.0f);
			GL11.glVertex3f(bb.x+bb.w, bb.y, bb.z);
			/*if (tex)*/ GL11.glTexCoord2f(1.0f, 0.0f);
			GL11.glVertex3f(bb.x, bb.y, bb.z);
			/*if (tex)*/ GL11.glTexCoord2f(1.0f, 1.0f);
			GL11.glVertex3f(bb.x, bb.y+bb.h, bb.z);
			/*if (tex)*/ GL11.glTexCoord2f(0.0f, 1.0f);
			GL11.glVertex3f(bb.x+bb.w, bb.y+bb.h, bb.z);
			
			// Left side.
//			GL11.glColor3f(0.0f, 0.0f, 1.0f); // Blue
			GL11.glColor3f(1.0f, 1.0f, 1.0f); // White
		
			/*if (tex)*/ GL11.glTexCoord2f(0.0f, 0.0f);
			GL11.glVertex3f(bb.x, bb.y, bb.z);
			/*if (tex)*/ GL11.glTexCoord2f(1.0f, 0.0f);
			GL11.glVertex3f(bb.x, bb.y, bb.z+bb.d);
			/*if (tex)*/ GL11.glTexCoord2f(1.0f, 1.0f);
			GL11.glVertex3f(bb.x, bb.y+bb.h, bb.z+bb.d);
			/*if (tex)*/ GL11.glTexCoord2f(0.0f, 1.0f);
			GL11.glVertex3f(bb.x, bb.y+bb.h, bb.z);
		
			// Right side.
//			GL11.glColor3f(1.0f, 1.0f, 0.0f); // Yellow
			GL11.glColor3f(1.0f, 1.0f, 1.0f); // White
		
			/*if (tex)*/ GL11.glTexCoord2f(0.0f, 0.0f);
			GL11.glVertex3f(bb.x+bb.w, bb.y, bb.z+bb.d);
			/*if (tex)*/ GL11.glTexCoord2f(1.0f, 0.0f);
			GL11.glVertex3f(bb.x+bb.w, bb.y, bb.z);
			/*if (tex)*/ GL11.glTexCoord2f(1.0f, 1.0f);
			GL11.glVertex3f(bb.x+bb.w, bb.y+bb.h, bb.z);
			/*if (tex)*/ GL11.glTexCoord2f(0.0f, 1.0f);
			GL11.glVertex3f(bb.x+bb.w, bb.y+bb.h, bb.z+bb.d);
			
			// Top side.
//			GL11.glColor3f(1.0f, 0.0f, 1.0f); // Magenta
			GL11.glColor3f(1.0f, 1.0f, 1.0f); // White
		
			/*if (tex)*/ GL11.glTexCoord2f(0.0f, 0.0f);
			GL11.glVertex3f(bb.x, bb.y+bb.h, bb.z+bb.d);
			/*if (tex)*/ GL11.glTexCoord2f(1.0f, 0.0f);
			GL11.glVertex3f(bb.x+bb.w, bb.y+bb.h, bb.z+bb.d);
			/*if (tex)*/ GL11.glTexCoord2f(1.0f, 1.0f);
			GL11.glVertex3f(bb.x+bb.w, bb.y+bb.h, bb.z);
			/*if (tex)*/ GL11.glTexCoord2f(0.0f, 1.0f);
			GL11.glVertex3f(bb.x, bb.y+bb.h, bb.z);
			
			// Bottom side.
//			GL11.glColor3f(0.0f, 1.0f, 1.0f); // Cyan
			GL11.glColor3f(1.0f, 1.0f, 1.0f); // White
		
			GL11.glVertex3f(bb.x, bb.y, bb.z);
			GL11.glVertex3f(bb.x+bb.w, bb.y, bb.z);
			GL11.glVertex3f(bb.x+bb.w, bb.y, bb.z+bb.d);
			GL11.glVertex3f(bb.x, bb.y, bb.z+bb.d);
			
			/*
			//Test: vidno polje
			//float sq = (float)Math.sqrt(3)+0.1f;
			float sq = 2.0f;
			//levo
			GL11.glColor3f(0.0f, 1.0f, 1.0f); // Cyan
			GL11.glVertex3f(bb.x+eyex, bb.y, bb.z-eyez+0.1f+bb.d);
			GL11.glVertex3f(bb.x+eyex-5, bb.y, bb.z-eyez+0.1f+bb.d-5*sq);
			GL11.glVertex3f(bb.x+eyex-5, bb.y+bb.h+eyey, bb.z-eyez+0.1f+bb.d-5*sq);
			GL11.glVertex3f(bb.x+eyex, bb.y+bb.h+eyey, bb.z-eyez+0.1f+bb.d);
			*/
		GL11.glEnd();
		
		// Nariše kij.
		if (hasBat) {
			GL11.glPushMatrix();
			GL11.glTranslatef(bb.x+bb.w/2, bb.y+bb.h, bb.z+bb.d/2);
			
			GL11.glRotatef(this.eyeLeftRight, 0.0f, 1.0f, 0.0f);
			
			Labyrinth.bat.render();
			
			GL11.glPopMatrix();
		}
	}
	
	/**
	* Akcija ob trku.
	*
	* Postavi kocko na pozicijo pred trkom.
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