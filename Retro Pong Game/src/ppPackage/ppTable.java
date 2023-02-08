package ppPackage;
import acm.graphics.GPoint;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;
import java.awt.Color;
import static ppPackage.ppSimParams.*;

/**
 * 
 * @author KeithCrochetiere This Class is where we build the bottom floor
 * This is so that we can reduce clutter in the main program
 * It also contains the W2S, S2W & new screen methods
 */
public class ppTable { 
	
	GraphicsProgram GProgram;
	
		//Constructor Given in ECSE-202 F2021 Assignment 2
	public ppTable (GraphicsProgram GProgram) {
		this.GProgram = GProgram;
		
		// Create the ground plane
		GRect gPlane = new GRect(0,HEIGHT,WIDTH+OFFSET,3);	// A thick line HEIGHT pixels down from the top
    	gPlane.setColor(Color.BLACK);
    	gPlane.setFilled(true);
    	GProgram.add(gPlane);
    	
    	
	}
	


//PRIVATE METHODS

/***
 * This code was taken from Sim 2 provided by frank ferrie.
 * 
 * Method to convert from world to screen coordinates.
 * @param P a point object in world coordinates
 * @return p the corresponding point object in screen coordinates
 */

public GPoint W2S (GPoint P) {
	
	return new GPoint((P.getX()-Xmin)*Xs,ymax-(P.getY()-Ymin)*Ys);
}

/***
 * Method to convert from Screen to world coordinates
 * This code was inspired by Katrina Poulin's code from the tutorial
 * @param P a point object in screen coordinates
 * @return p the corresponding object in World coordinates
 */
public GPoint S2W (GPoint P) {
	
	return new GPoint(P.getX()/Xs+Xmin,(ymax-P.getY())/Ys+Ymin);
}
/***
 * This method returns void.
 * When called this method will remove everything on the screen and add the ground plane
 */
public void newScreen() {
	// Erases Screen, draws a new ground plane
	GProgram.removeAll();
	
	//add Ground Plane
	GRect gPlane = new GRect(0,HEIGHT,WIDTH+OFFSET,3);	// A thick line HEIGHT pixels down from the top
	gPlane.setColor(Color.BLACK);
	gPlane.setFilled(true);
	GProgram.add(gPlane);
}

}