package racgr.labyrinth;

import org.lwjgl.opengl.GL11;

/**
* Zid, ki ga je moè razbiti.
*
* @author Lovro Šubelj, 63040296
* @author Janoš Vidali, 63040303
*/
public class BreakableWall extends Wall {
	/**
	* Konstruktor.
	*
	* @param x	X koordinata spodnjega levega oglišèa
	* @param y	Y koordinata spodnjega levega oglišèa
	* @param z	Z koordinata spodnjega levega oglišèa
	* @param w		širina (po X)
	* @param h		višina (po Y)
	* @param d		globina (po Z)
	* @param t		tekstura
	*/
	public BreakableWall(float x, float y, float z, float w, float h, float d, Texture t) {
		super(x, y, z, w, h, d, t);
	}
	
	/**
	* Metoda za risanje.
	*/
	public void render() {
		t.bind();
		GL11.glBegin(GL11.GL_QUADS);
						
			// Front side.
			GL11.glColor3f(1.0f, 1.0f, 1.0f); // White
		
			/*if (tex)*/ GL11.glTexCoord2f(0, 0);
			GL11.glVertex3f(bb.x, bb.y, bb.z+bb.d);
		
			/*if (tex)*/ GL11.glTexCoord2f(bb.w, 0);
			GL11.glVertex3f(bb.x+bb.w, bb.y, bb.z+bb.d);
		
			/*if (tex)*/ GL11.glTexCoord2f(bb.w, bb.h);
			GL11.glVertex3f(bb.x+bb.w, bb.y+bb.h, bb.z+bb.d);
		
			/*if (tex)*/ GL11.glTexCoord2f(0, bb.h);
			GL11.glVertex3f(bb.x, bb.y+bb.h, bb.z+bb.d);
		
			// Back side.
			GL11.glColor3f(1.0f, 1.0f, 1.0f); // White
		
			/*if (tex)*/ GL11.glTexCoord2f(0, 0);
			GL11.glVertex3f(bb.x+bb.w, bb.y, bb.z);
			
			/*if (tex)*/ GL11.glTexCoord2f(bb.w, 0);
			GL11.glVertex3f(bb.x, bb.y, bb.z);
			
			/*if (tex)*/ GL11.glTexCoord2f(bb.w, bb.h);
			GL11.glVertex3f(bb.x, bb.y+bb.h, bb.z);
			
			/*if (tex)*/ GL11.glTexCoord2f(0, bb.h);
			GL11.glVertex3f(bb.x+bb.w, bb.y+bb.h, bb.z);
			
			// Left side.
			GL11.glColor3f(1.0f, 1.0f, 1.0f); // White
		
			/*if (tex)*/ GL11.glTexCoord2f(0, 0);
			GL11.glVertex3f(bb.x, bb.y, bb.z);
			
			/*if (tex)*/ GL11.glTexCoord2f(bb.d, 0);
			GL11.glVertex3f(bb.x, bb.y, bb.z+bb.d);
			
			/*if (tex)*/ GL11.glTexCoord2f(bb.d, bb.h);
			GL11.glVertex3f(bb.x, bb.y+bb.h, bb.z+bb.d);
			
			/*if (tex)*/ GL11.glTexCoord2f(0, bb.h);
			GL11.glVertex3f(bb.x, bb.y+bb.h, bb.z);
		
			// Right side.
			GL11.glColor3f(1.0f, 1.0f, 1.0f); // White
		
			/*if (tex)*/ GL11.glTexCoord2f(0, 0);
			GL11.glVertex3f(bb.x+bb.w, bb.y, bb.z+bb.d);
			
			/*if (tex)*/ GL11.glTexCoord2f(bb.d, 0);
			GL11.glVertex3f(bb.x+bb.w, bb.y, bb.z);
			
			/*if (tex)*/ GL11.glTexCoord2f(bb.d, bb.h);
			GL11.glVertex3f(bb.x+bb.w, bb.y+bb.h, bb.z);
			
			/*if (tex)*/ GL11.glTexCoord2f(0, bb.h);
			GL11.glVertex3f(bb.x+bb.w, bb.y+bb.h, bb.z+bb.d);
			
			// Top side.
			GL11.glColor3f(1.0f, 1.0f, 1.0f); // White
		
			/*if (tex)*/ GL11.glTexCoord2f(0, 0);
			GL11.glVertex3f(bb.x, bb.y+bb.h, bb.z+bb.d);

			/*if (tex)*/ GL11.glTexCoord2f(0, bb.d);
			GL11.glVertex3f(bb.x+bb.w, bb.y+bb.h, bb.z+bb.d);

			/*if (tex)*/ GL11.glTexCoord2f(bb.w, bb.d);
			GL11.glVertex3f(bb.x+bb.w, bb.y+bb.h, bb.z);

			/*if (tex)*/ GL11.glTexCoord2f(bb.w, 0);
			GL11.glVertex3f(bb.x, bb.y+bb.h, bb.z);

		GL11.glEnd();
	}
	
}