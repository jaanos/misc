package racgr.labyrinth;

import org.lwjgl.opengl.GL11;

/**
* Piramida na kocki.
*
* @author Janoš Vidali, 63040303
* @author Lovro Šubelj, 63040296
*/
public class PyraCube extends Object3D {
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
	public PyraCube(float x, float y, float z, Texture t) {
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
			GL11.glVertex3f(bb.x+bb.w, bb.y+bb.h/2, bb.z+bb.d);
			/*if (tex)*/ GL11.glTexCoord2f(0.0f, 1.0f);
			GL11.glVertex3f(bb.x, bb.y+bb.h/2, bb.z+bb.d);
		
			// Back side.
//			GL11.glColor3f(0.0f, 1.0f, 0.0f); // Green
			GL11.glColor3f(1.0f, 1.0f, 1.0f); // White
		
			/*if (tex)*/ GL11.glTexCoord2f(0.0f, 0.0f);
			GL11.glVertex3f(bb.x+bb.w, bb.y, bb.z);
			/*if (tex)*/ GL11.glTexCoord2f(1.0f, 0.0f);
			GL11.glVertex3f(bb.x, bb.y, bb.z);
			/*if (tex)*/ GL11.glTexCoord2f(1.0f, 1.0f);
			GL11.glVertex3f(bb.x, bb.y+bb.h/2, bb.z);
			/*if (tex)*/ GL11.glTexCoord2f(0.0f, 1.0f);
			GL11.glVertex3f(bb.x+bb.w, bb.y+bb.h/2, bb.z);
			
			// Left side.
//			GL11.glColor3f(0.0f, 0.0f, 1.0f); // Blue
			GL11.glColor3f(1.0f, 1.0f, 1.0f); // White
		
			/*if (tex)*/ GL11.glTexCoord2f(0.0f, 0.0f);
			GL11.glVertex3f(bb.x, bb.y, bb.z);
			/*if (tex)*/ GL11.glTexCoord2f(1.0f, 0.0f);
			GL11.glVertex3f(bb.x, bb.y, bb.z+bb.d);
			/*if (tex)*/ GL11.glTexCoord2f(1.0f, 1.0f);
			GL11.glVertex3f(bb.x, bb.y+bb.h/2, bb.z+bb.d);
			/*if (tex)*/ GL11.glTexCoord2f(0.0f, 1.0f);
			GL11.glVertex3f(bb.x, bb.y+bb.h/2, bb.z);
		
			// Right side.
//			GL11.glColor3f(1.0f, 1.0f, 0.0f); // Yellow
			GL11.glColor3f(1.0f, 1.0f, 1.0f); // White
		
			/*if (tex)*/ GL11.glTexCoord2f(0.0f, 0.0f);
			GL11.glVertex3f(bb.x+bb.w, bb.y, bb.z+bb.d);
			/*if (tex)*/ GL11.glTexCoord2f(1.0f, 0.0f);
			GL11.glVertex3f(bb.x+bb.w, bb.y, bb.z);
			/*if (tex)*/ GL11.glTexCoord2f(1.0f, 1.0f);
			GL11.glVertex3f(bb.x+bb.w, bb.y+bb.h/2, bb.z);
			/*if (tex)*/ GL11.glTexCoord2f(0.0f, 1.0f);
			GL11.glVertex3f(bb.x+bb.w, bb.y+bb.h/2, bb.z+bb.d);
			
			// Bottom side.
//			GL11.glColor3f(0.0f, 1.0f, 1.0f); // Cyan
			GL11.glColor3f(1.0f, 1.0f, 1.0f); // White
		
			GL11.glVertex3f(bb.x, bb.y, bb.z);
			GL11.glVertex3f(bb.x+bb.w, bb.y, bb.z);
			GL11.glVertex3f(bb.x+bb.w, bb.y, bb.z+bb.d);
			GL11.glVertex3f(bb.x, bb.y, bb.z+bb.d);
		
		GL11.glEnd();
		
		GL11.glBegin(GL11.GL_TRIANGLES);

			// Front side.
//			GL11.glColor3f(1.0f, 0.0f, 0.0f); // Red
			GL11.glColor3f(1.0f, 1.0f, 1.0f); // White
			
			/*if (tex)*/ GL11.glTexCoord2f(0.0f, 0.0f);
			GL11.glVertex3f(bb.x, bb.y+bb.h/2, bb.z+bb.d);
			/*if (tex)*/ GL11.glTexCoord2f(1.0f, 0.0f);
			GL11.glVertex3f(bb.x+bb.w, bb.y+bb.h/2, bb.z+bb.d);
			/*if (tex)*/ GL11.glTexCoord2f(0.5f, 1.0f);
			GL11.glVertex3f(bb.x+bb.w/2, bb.y+bb.h, bb.z+bb.d/2);
			
			// Back side.
//			GL11.glColor3f(0.0f, 1.0f, 0.0f); // Green
			GL11.glColor3f(1.0f, 1.0f, 1.0f); // White
			
			/*if (tex)*/ GL11.glTexCoord2f(0.0f, 0.0f);
			GL11.glVertex3f(bb.x+bb.w, bb.y+bb.h/2, bb.z);
			/*if (tex)*/ GL11.glTexCoord2f(1.0f, 0.0f);
			GL11.glVertex3f(bb.x, bb.y+bb.h/2, bb.z);
			/*if (tex)*/ GL11.glTexCoord2f(0.5f, 1.0f);
			GL11.glVertex3f(bb.x+bb.w/2, bb.y+bb.h, bb.z+bb.d/2);
				
			// Left side.
//			GL11.glColor3f(0.0f, 0.0f, 1.0f); // Blue
			GL11.glColor3f(1.0f, 1.0f, 1.0f); // White
			
			/*if (tex)*/ GL11.glTexCoord2f(0.0f, 0.0f);
			GL11.glVertex3f(bb.x, bb.y+bb.h/2, bb.z);
			/*if (tex)*/ GL11.glTexCoord2f(1.0f, 0.0f);
			GL11.glVertex3f(bb.x, bb.y+bb.h/2, bb.z+bb.d);
			/*if (tex)*/ GL11.glTexCoord2f(0.5f, 1.0f);
			GL11.glVertex3f(bb.x+bb.w/2, bb.y+bb.h, bb.z+bb.d/2);
			
			// Right side.
//			GL11.glColor3f(1.0f, 1.0f, 0.0f); // Yellow
			GL11.glColor3f(1.0f, 1.0f, 1.0f); // White
			
			/*if (tex)*/ GL11.glTexCoord2f(0.0f, 0.0f);
			GL11.glVertex3f(bb.x+bb.w, bb.y+bb.h/2, bb.z+bb.d);
			/*if (tex)*/ GL11.glTexCoord2f(1.0f, 0.0f);
			GL11.glVertex3f(bb.x+bb.w, bb.y+bb.h/2, bb.z);
			/*if (tex)*/ GL11.glTexCoord2f(0.5f, 1.0f);
			GL11.glVertex3f(bb.x+bb.w/2, bb.y+bb.h, bb.z+bb.d/2);

		GL11.glEnd();
	        	
		// Nariše kij.
		if (hasBat) {
			GL11.glPushMatrix();
			GL11.glTranslatef(bb.x+bb.w/2, bb.y+bb.h, bb.z+bb.d/2);
				
			Labyrinth.bat.render();
			
			GL11.glPopMatrix();
		}
	}
	
	/**
	* Akcija ob trku.
	*
	* Postavi objekt na pozicijo pred trkom.
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