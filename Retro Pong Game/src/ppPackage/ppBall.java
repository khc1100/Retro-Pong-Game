package ppPackage;
import java.awt.Color;
import acm.graphics.GOval;
import acm.graphics.GPoint;
import acm.program.GraphicsProgram;
import static ppPackage.ppSimParams.*;
/**
 * 
 * @author KeithCrochetiere this code was taken/inspired from ECSE-202 F2021 Assignment 4 & previous solutions 
 * This Class is where the ball is created and has all of the calculations to adjust its trajectory from collisions ...etc.
 * This is where the bulk of the Java project it executed and contains a lot of the methods.
 *
 */

public class ppBall extends Thread{
	
	// Instance variables taken from ECSE-202 F2021 Assignment 3 & 2
	private double Xinit;					//Initial position of ball - X
	private double Yinit;					//Initial position of ball - Y
	private double Vo;						//Initial velocity (Magnitude)
	private double theta;					//Initial direction
	private double loss;					//Energy loss on collision
	private Color color;					//Color of ball
	private GraphicsProgram GProgram; 		//Instance of ppSim Class (this)
	GOval myBall;							//Graphics object representing ball
	ppPaddle myPaddle;						//Graphics object representing paddle
	ppTable myTable;						//Graphics object representing table
	ppPaddle LPaddle;					//Graphics object representing LPaddle
	double Vx; 							//Instance variables for Velocity in X and Y 
	double Vy;							//Instance variables Position in X and Y and initial Position in X and Y
	double X;
	double Xo;
	double Y;
	double Yo;
	boolean running;					//running boolean
	/***
	 * This is the constructor for the ppBall class
	 * It takes in Xinit, Yinit, Vo, theta, loss, Color, GProgram and myTable to create a object myBall
	 */
	public ppBall(double Xinit, double Yinit, double Vo, double theta, double loss,
			Color color, GraphicsProgram GProgram,ppTable myTable) {
		this.Xinit=Xinit; 					// Copy constructor parameters to instance variables Taken from ECSE-202 F2021 Assignment 3
		this.Yinit=Yinit;
		this.Vo=Vo; 
		this.theta=theta; 
		this.loss=loss; 
		this.color=color; 
		this.GProgram=GProgram;
		this.myTable=myTable;
	
		}
	/**
	 * This is the run method of ppBall 
	 * This is where all of the calculations for the balls position and velocity are 
	 * as well as the collision statements.
	 */
	public void run() {
		
		// Initialize program variables add this to ppBall run method
		
				Xo = Xinit;							// Set initial X position
				Yo = Yinit;							// Set initial Y position
				double time = 0;							// Time starts at 0 and counts up							
				double Vt = bMass*g / (4*Pi*bSize*bSize*k); // Terminal velocity
				double Vox=Vo*Math.cos(theta*Pi/180);		// X component of velocity
				double Voy=Vo*Math.sin(theta*Pi/180);		// Y component of velocity
				
				// Create the ball
				
				GPoint p = W2S(new GPoint(Xinit,Yinit));		
				double ScrX = p.getX();						// Convert simulation to screen coordinates
				double ScrY = p.getY();			
				GOval myBall = new GOval(ScrX,ScrY,2*bSize*Xs,2*bSize*Ys);
				myBall.setColor(color);
				myBall.setFilled(true);
				GProgram.add(myBall);
				GProgram.pause(TICK*TSCALE);
				
				running = true;						// Initial state = falling.
				
				double KEx = 100;							// Initializing The Energy to be bigger than ETHR
				double KEy = 100;
				double PE = 100;
				
				//The while loop that updates position of the ball white running=true
				while (running) {
					
					
					X = Vox*Vt/g*(1-Math.exp(-g*time/Vt));				// Update relative position
					Y = Vt/g*(Voy+Vt)*(1-Math.exp(-g*time/Vt))-Vt*time;
		    		Vx = Vox*Math.exp(-g*time/Vt);						// Update velocity
		    		Vy = (Voy+Vt)*Math.exp(-g*time/Vt)-Vt;
		    		
		    		
		    		
		    		// Check if Y goes over the Y boundary
		    		if (Yo+Y>Ymax) {
	            		running=false;					//Stop the while loop
	            		
	            		if (Vx>0) {						//if ball velocity is positive then it was the Agent who hit it e.g my point
	            			myScore++;
	            		}
	            		if (Vx<0) agentScore++;				//other wise it is the agents point
	            		
	            	}
		    		
		    		
		    		// Collision with floor
		       	 
		            if (Y+Yo < bSize) {
		            	
		            	KEx = 0.5*bMass*Vx*Vx*(1-loss);								// Updated Energy after Collision with floor
		            	KEy = 0.5*bMass*Vy*Vy*(1-loss);
		            	PE = 0;
		            	
		            	Vox=Math.sqrt(2*KEx/bMass);									// Update the Velocity for X and Y
		            	Voy=Math.sqrt(2*KEy/bMass);
		            
		            	if (Vx<0) Vox=-Vox;											// Maintains the X direction 
		            
		            	time = 0; 													// time is reset at every collision
		            	Xo += X; 													// need to accumulate distance between collisions
		            	Yo = bSize; 												// the absolute position of the ball on the ground
		            	X = 0; 														// (X,Y) is the instantaneous position along an arc,
		            	Y = 0; 														// Absolute position is (Xo+X,Yo+Y).
		            	
		            // The simulation will only end on the floor.
		            	
		            	if((KEx+KEy+PE)<ETHR) running=false;						// Terminate if insufficient energy.
		            }
		            
		            // Collision with right paddle
		        	
		            if (Vx>0 && Xo+X>(myPaddle.getP().getX())-bSize-ppPaddleW/2) 	//If ball reaches the x value of the paddle
		            
		            	{
		            	if (myPaddle.contact(Xo+X,Yo+Y)) {							//Check for contact with Contact method
		            		
		            	KEx = 0.5*bMass*Vx*Vx*(1-loss);								// Updated Energy after Collision with floor
		            	KEy = 0.5*bMass*Vy*Vy*(1-loss);
		            	PE = bMass*g*Y;
		            	
		            	
		            	Vox=Math.sqrt(2*KEx/bMass);									// Update the Velocity for X and Y
		            	Voy=Math.sqrt(2*KEy/bMass);
		            	
		            	// Update the Velocity for X and Y with the Gain from the paddle
		            	Vox=Vox*ppPaddleXgain;
		            	Voy=Voy*ppPaddleYgain*myPaddle.getV().getY();
		            	
		            	if (Vox>VoxMAX) Vox=VoxMAX;									// Make sure Vox is less than VoxMAX
		            	
		            	// Reset the variables
		            	time = 0;
		            	Xo = myPaddle.getP().getX()-bSize-ppPaddleW/2;
		            	Yo += Y;
		            	Vox=-Vox;
		            	X = 0; 														// (X,Y) is the instantaneous position along an arc,
		            	Y = 0; 		
		            	}
		            	else 
		            	{
		            		//right paddle missed means
		            		running=false;
		            		agentScore++;
		            		
		            	}
		            }
		            
		            // Collision with left paddle
		            
		            if (Vx<0 && Xo+X<(LPaddle.getP().getX())+bSize+ppPaddleW/2) {
		            	
		            	if (LPaddle.contact(Xo+X,Yo+Y)) {
		            		
		            	
		            	KEx = 0.5*bMass*Vx*Vx*(1-loss);								// Updated Energy after Collision with floor
		            	KEy = 0.5*bMass*Vy*Vy*(1-loss);
		            	PE = bMass*g*Y;
		            	
		            	Vox=Math.sqrt(2*KEx/bMass);									// Update the Velocity for X and Y
		            	Voy=Math.sqrt(2*KEy/bMass);
		      								
		            	
		            	Vox=Vox*LPaddleXgain;
		            	Voy=Voy*LPaddleYgain;
		            	
		            	
		            	if (Vox>VoxMAX) Vox=VoxMAX;									// Make sure Vox is less than VoxMAX
		            	
		            	if (Vy<0)Voy=-Voy;											// Check the velocity of the ball
		            	
		            	//reset variables
		            	time = 0;
		            	Xo = LPaddle.getP().getX()+bSize+ppPaddleW/2;
		            	Yo += Y;
		            	X = 0; 														// (X,Y) is the instantaneous position along an arc,
		            	Y = 0; 														// Absolute position is (Xo+X,Yo+Y).
		            	}
		            	else 
		            	{
		            		//Left paddle missed means
		            		
		            		running=false;
		            		myScore++;
		            	}
		            
		            	
		            	
		            }
		        	
		    	// Update the position of the ball.  Plot a tick mark at current location.
		        	
		    		p = W2S(new GPoint(Xo+X-bSize,Yo+Y+bSize));		// Get current position in screen coordinates
		    		ScrX = p.getX();
		    		ScrY = p.getY();
		    		myBall.setLocation(ScrX,ScrY);
		    		
		    		p = W2S(new GPoint(Xo+X,Yo+Y));					// Get current position in screen coordinates
		    		ScrX = p.getX();
		    		ScrY = p.getY();
		    	
		    		if(traceButton.isSelected())trace(ScrX,ScrY);	// Code to see if the button is toggled and traces accordingly
		    		
		    		time += TICK;									// update time
		    		
		    		
		    		// Pause display 
		    		GProgram.pause(TICK*TSCALE);					// Pause program for TICK*TSCALE mS
		    		ppSim.updateLabel();
				}
				
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
     * This code was taken from Sim 2 provided by frank ferrie. 
     * 
     * A simple method to plot a dot at the current location in screen coordinates
     * @param scrX
     * @param scrY
     */	
	
	private void trace(double ScrX, double ScrY) {
		GOval pt = new GOval(ScrX,ScrY,PD,PD);
		pt.setColor(Color.BLACK);
		pt.setFilled(true);
		GProgram.add(pt);	
	}
	/***
	 * This method returns void, This method creates an instance of myPaddle from the ppPaddle class
	 * @param myPaddle
	 */
	
	public void setRightPaddle(ppPaddle myPaddle) {
		this.myPaddle=myPaddle;
	}
	/***
	 * This method returns void, This method creates an instance of LPaddle from the ppPaddle class
	 * @param LPaddle
	 */
	public void setLeftPaddle(ppPaddle LPaddle) {
		this.LPaddle = LPaddle;
	}
	/***
	 * This method returns a GPoint of the absolute position of the X and Y values of the ball in world coordinates
	 * @return GPoint
	 */
	public GPoint getP() {
		
		return new GPoint(Xo+X,Yo+Y);	
	}
	/***
	 * This method returns a GPoint of the Velocity of the ball in terms of X and Y in world coordinates
	 * @return GPoint
	 */
	public GPoint getV() {
		
		return new GPoint(Vy, Vx);
	}
	/**
	 * This method returns void. This method can be called in any class and allows us to kill the while loop in ppBall
	 * by setting running=false
	 */
	public void kill() {
		running=false;
		
	}
	


}