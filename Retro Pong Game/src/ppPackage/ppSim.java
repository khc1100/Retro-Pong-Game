package ppPackage;
import static ppPackage.ppSimParams.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;

import acm.graphics.GPoint;
import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;
/**
 * 
 * @author KeithCrochetiere 
 * This is a java project that creates a Ping-Pong game similar to the original pong game
 * with moving paddles and a ball with proper collisions physics
 * ppSim is the class that extends GraphicsProgram and is the class that contains the main.
 * This code was inspired by ECSE-202 F2021 Assignment 4 & previous assignments & Katrina Poulin's tutorials 
 */
public class ppSim extends GraphicsProgram{
	//Instance variables 
	ppTable myTable;				//Graphics object representing table
	ppPaddle myPaddle;				//Graphics object representing the Right paddle
	ppBall myBall;					//Graphics object representing ball
	Color myColor;					//Color
	RandomGenerator rgen;			//RandomGenerator
	ppPaddleAgent LPaddle;			//Left Paddle 
	
	
	/***
	 * This is the method that starts the ppSim program.
	 * @param args
	 */
	
	public static void main(String[] args) {
		new ppSim().start(args);
	}
	/***
	 * This is the method that Initializes everything in the program.
	 * This is where the buttons,table,paddles and ball get called 
	 */
	public void init() {
		
		// Resize the GraphicsProgram window
		this.resize(ppSimParams.WIDTH,ppSimParams.HEIGHT+OFFSET);
		
		//create the buttons, sliders & score board
		JButton Clear = new JButton ("Clear");
		JButton newServeButton = new JButton("New Serve");
		JButton quitButton = new JButton("Quit");
		traceButton = new JToggleButton("Trace",false);
		scoreBoard = new JLabel("Player: "+ myScore + " | agent: " + agentScore);
		slider = new JSlider(JSlider.HORIZONTAL,0,20,10);
		slider.setMajorTickSpacing(2);
		
		//add the buttons,sliders & score board
		add(Clear, SOUTH);
		add(newServeButton, SOUTH);
		add(traceButton,SOUTH);
		add(quitButton, SOUTH);
		add(scoreBoard,NORTH);
		add(new JLabel("-lag"),SOUTH);
		add(slider,SOUTH);
		add(new JLabel("+lag"),SOUTH);
		
		//Action listeners 
		addMouseListeners();
		addActionListeners();
		
		//random generator
		rgen = RandomGenerator.getInstance();
		rgen.setSeed(RSEED);
		
		//create table and call newGame;
		myTable = new ppTable(this);
		newGame();
		
		
	}
	/***
	 * This is the method that randomly generates parameters for the ball
	 * and returns a myBall with the newly generated paramters.
	 * 
	 * @return myBall
	 */
	ppBall newBall() {
		// generate random parameters
		
		Color iColor = Color.RED;
		double iYinit = rgen.nextDouble(YinitMIN,YinitMAX);
		double iLoss = rgen.nextDouble(EMIN,EMAX);
		double iVel = rgen.nextDouble(VoMIN,VoMAX); 
		double iTheta = rgen.nextDouble(ThetaMIN,ThetaMAX);
		//return myBall
		return myBall = new ppBall(Xinit,iYinit,iVel,iTheta,iLoss,iColor,this,myTable); 
	}
	
	/**
	 * This method returns void.
	 * This method is called at the start of the program & after every time "New Serve is called"
	 * It kills the old ball calls new screen and creates the paddles and starts the ball and paddles.
	 */
	public void newGame() {
		if (myBall != null) myBall.kill(); 		//Stop current game
		myTable.newScreen();
		myBall = newBall();
		myPaddle = new ppPaddle(ppPaddleXinit,ppPaddleYinit,Color.GREEN,myTable,this); 
		LPaddle = new ppPaddleAgent(LPaddleXinit, LPaddleYinit,Color.BLUE, myTable, this); 
		
		//Sets instance variable of the ball to the L paddle
		LPaddle.attachBall(myBall); 
		myBall.setRightPaddle(myPaddle); 
		myBall.setLeftPaddle(LPaddle); 
		pause(STARTDELAY); 
		myBall.start();
		LPaddle.start();
		myPaddle.start();
	}
	
	/***
	 *  Mouse Handler - a moved event moves the paddle up and down in Y 
	 *  This method takes the mouse coordinates and sets them to myPaddle
	 *  This was taken from Assignment 3
	 *  @Param Takes MouseEvent
	 */
	public void mouseMoved(MouseEvent e) {
		if (myTable==null||myPaddle==null) return;
		GPoint Pm = myTable.S2W(new GPoint(e.getX(),e.getY()));
		double PaddleX = myPaddle.getP().getX();
		double PaddleY = Pm.getY();
		myPaddle.setP(new GPoint(PaddleX,PaddleY));

	}
	/***
	 * Action Handler 
	 * This method checks for ActionEvents for the buttons
	 * and executes the code accordingly 
	 * @Param takes ActionEvent
	 */
	public void actionPerformed (ActionEvent e) {
		String command = e.getActionCommand(); 
		if (command.equals("New Serve")) {
			newGame();
		}
		else if (command.equals("Quit")) {
			System.exit(0);
		}
		else if (command.equals("Clear")) {
			clearScoreBoard();
		}
		}
	
	/***
	 * This method returns void.
	 * This method has the code to update the scoreBoard accordingly
	 */
	public static void updateLabel() {
		
		scoreBoard.setText("Player: "+ myScore + " | agent: " + agentScore);
		//label.setText("")
	}
	/**
	 * This method returns void.
	 * This method has the code to clear the scoreBoard
	 * 
	 */
	public static void clearScoreBoard() {
		//Set scores to 0
		myScore=0;
		agentScore=0;
		//update scoreBoard
		scoreBoard.setText("Player: "+ myScore + " | agent: " + agentScore);
	}
}

