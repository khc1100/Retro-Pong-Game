package ppPackage;
import java.awt.Color;

import acm.graphics.GPoint;
import acm.program.GraphicsProgram;
import static ppPackage.ppSimParams.*;
/**
 * 
 * @author KeithCrochetiere this code was taken/inspired from ECSE-202 F2021 Assignment 4 & previous assignments & tutorials with Katrina
 * This class extend the ppPaddle class which means it can use all the variables, methods and constructors.
 * This class has the constructor for LPaddle and it is where the run method determining Agent lag is.
 *
 */
public class ppPaddleAgent extends ppPaddle{

	ppBall myBall;				//Instance variable for myBall
	
	//Constructor for the ppPaddleAgent taken from Assignment 4
	public ppPaddleAgent (double X, double Y, Color myColor, ppTable myTable, GraphicsProgram GProgram) {
		//error means that we need to use the ppPaddle constructor
		//create a ppPaddle
		super(X, Y, myColor, myTable, GProgram);
		
			
	}
	/***
	 * This is a run method that returns void.
	 * This run method is in charge of the agent Lag making it playable
	 * The middle of the Paddle will be set to the balls location every AgentLag iterations
	 * making the response time 1/AgentLag of the ball move
	 * taken from Assignment 4
	 */
	public void run() {
		
		AgentLag = slider.getValue();		//Get Value of the slider	
		int ballSkip = 0;					//start at zero
			
			while (true) {
			if (ballSkip++ >= AgentLag) {
			//Get Ball position
			double Y = myBall.getP().getY();
			
			//setP(X, Y);
			this.setP(new GPoint(this.getP().getX(),Y));
		
			//reset ballSkip
			
			ballSkip=0;
			}
			GProgram.pause(TICK*TSCALE);//Pause the program
		}
			
	
	}
		/***
		 * This method returns void. This method sets myBall to the ball instance variable in ppPaddleAgent
		 * @param myBall
		 */
	public void attachBall(ppBall myBall) {
		this.myBall = myBall;
	}
}


