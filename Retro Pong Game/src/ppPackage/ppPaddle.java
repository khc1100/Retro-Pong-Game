package ppPackage;
import static ppPackage.ppSimParams.*;

import java.awt.Color;

import acm.graphics.GPoint;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;

/**
 * 
 * @author KeithCrochetiere this code was taken/inspired from ECSE-202 F2021 Assignment 4 & previous assignments
 * This Class is where the paddle is created and it has the code that handles the movement of the paddle
 * This Class also contains a lot of the methods used in the program.
 */
public class ppPaddle extends Thread{
	//Instance variables
	
	double X;						//Position of the ball - X
	double Y;						//Position of the ball - Y
	double Vx;						//Velocity of the ball in X
	double Vy;						//Velocity of the ball in Y
	Color myColor;
	GRect myPaddle;					//Graphics object representing paddle
	GraphicsProgram GProgram;		//Graphics object representing ball
	ppTable myTable;				//Graphics object representing table
	
	//representing paddle and adding to the display
	//Constructor for ppPaddle
	public ppPaddle (double X, double Y, Color myColor, ppTable myTable, GraphicsProgram GProgram) { 

		this.X=X;								// Copy constructor parameters to instance variables
		this.Y=Y;
		this.myTable=myTable;
		this.GProgram=GProgram;
		this.myColor=myColor;
		
		//World coordinates of upper left
		
		double upperLeftX=X - ppPaddleW/2;
		double upperLeftY=Y + ppPaddleH/2;
		
		//World coordinates to Screen coordinates
		GPoint p= myTable.W2S(new GPoint(upperLeftX,upperLeftY));
		
		//Get X and Y values in Screen
		double ScrX = p.getX();
		double ScrY = p.getY();
		
		//Create the paddle
		this.myPaddle= new GRect(ScrX,ScrY,ppPaddleW*Xs,ppPaddleH*Ys);
		myPaddle.setFilled(true);
		myPaddle.setColor(myColor);
		GProgram.add(myPaddle);
		
		
		
	}
	/***
	 * This is a run method that is used to find the velocity of the mouse 
	 * and equivalently the right paddle
	 */
	public void run() {
		double lastX=X;							//Create instance variables
		double lastY=Y;
		while (true) {
			Vx=(X-lastX)/TICK;					//Velocity of X
			Vy=(Y-lastY)/TICK;					//Velocity of Y
			lastX=X;
			lastY=Y; 
			GProgram.pause(TICK*TSCALE);		//Time to ms
			
			}
		
	}
	

	//PUBLIC METHODS
		/***
	 	* This method is called to get the Point(x,y) of an object in word Coordinates
	 	* @return GPoint X and Y of the paddle
	 	*/
		public GPoint getP() {
		
			return new GPoint (X,Y);
		
		}
	
		/***
		 * This method is the method that takes in the GPoint values in world coordinates
		 * and sets the location of the paddle accordingly
		 * @param GPoint P
		 */
	
		public void setP(GPoint P) {
			//update instance variables
			this.X = P.getX();
			this.Y = P.getY();
		
			double upperLeftX=X - ppPaddleW/2;
			double upperLeftY=Y + ppPaddleH/2;
		
			//Screen
			GPoint p= myTable.W2S(new GPoint(upperLeftX,upperLeftY));
			
			//Get in terms of Screen Coordinates 
			double ScrX = p.getX();
			double ScrY = p.getY();
		
			//move GRect instance
			this.myPaddle.setLocation(ScrX,ScrY);
			
		}
		
	
		/***
		 * This method is used to get the velocities of the Paddle as there X and Y values.
		 * 
		 * @return GPoint with x=Vx and y=Vy
		 */
		public GPoint getV() {
			
			return new GPoint (Vx,Vy);
		}
		
		/***
		 * This is a method that uses Vy calculated in the run method
		 * to find weather the ball should bounce upwards or downwards after hitting the paddle.
		 * @return -1 if Vy<0 and 1 if Vy>0
		 */
		public double getSgnVy() {
			
			if (Vy>=0)return 1;
			else return -1;
			
		}
		
		/***
		 * This method is used in the ppBall class to check for contact between the Ball and the Paddle
		 * This method gets called once the ball reaches the x value of the paddle 
		 * @param Sx
		 * @param Sy
		 * @return boolean==true if Yball is within the y range of paddle, else boolean==false
		 */
		public boolean contact(double Sx,double Sy) {
			
			// If the center of the ball is within the Paddle return true
			if (Sy >= Y-ppPaddleH/2 && Sy <= Y +ppPaddleH/2) 
			{
				return true;
			}
			//true if BallY is inn the paddlyY range false if not
			else return false;
			
		}
		
	}
		