package roland;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Paths;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.*;

public class LobPong extends JPanel implements ActionListener, KeyListener {
	private static final long serialVersionUID = 1L;
	//Random object
	Random random = new Random();





	//Lob Pong Paddle settings

	//paddle height and width
	final double widthPaddle = 150;
	final double heightPaddle = 20;
	//velocity of the pad
	double xVelocityPaddle = 0;
	double xVelocityPaddleRange = (double)(random.nextInt(15)+12);
	//top left corner of the pad
	double xPositionPaddle = 322;

	final double yPositionPaddle = 551;




	//Lob Pong Ball settings

	//ball radius/size
	final double ballSize = 16;
	//top left corner of the enscribed rectangle to the ball (ellipse)
	double xPositionBall = 100;
	double yPositionBall = 0;		//277
	//time
	double time = 0.5;
	//angle
	double angle = 0.8;
	//gravity
	final double gravity = 9.8;
	//velocities of the ball in the x and y direction and the general velocity
	double velocityBall = random.nextInt(8)+13;		//creates velocity between 13 - 20

	double xVelocityBall = velocityBall*Math.cos(angle);
	double yVelocityBall = velocityBall*Math.sin(angle) - gravity*time;
	//stores the original velocity for future usage when someone loses a life and more

	//distances moved in x and y direction by the ball
	double xDistanceBall;
	double yDistanceBall;
	//scaleFactor
	double xScaleFactor = 1.8;
	double yScaleFactor = 2.5;


	//score settings
	int score = 0;
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	int livesRemaining = 3;
	//label to keep track of score
	JLabel labelScore;
	//boolean gameOver
	boolean gameOver=false;
	JLabel  timerLabel;
	Timer clocktimer;
	
	
	//lives settings
	int xPositionLife;
	int yPositionLife;
	int sizeLife = 25;
	BufferedImage image, image2;
	
	//clock settings
	int timeCountSecond=0;
	int timeCountMinutes=0;
	int level_increment=0;
	boolean isNewLevel = false;

	//Timer
	Timer timer = new Timer(50, this);


	@Override
	public void paintComponents(Graphics g) {
		super.paintComponents(g);
	}

	public LobPong() {
		
		startMyTimer();
		setBackground(Color.BLACK);
		setForeground(Color.WHITE);
		addKeyListener(this);
		setFocusable(true);
		timerLabel= new JLabel("");
		timerLabel.setForeground(Color.green);
		timerLabel.setFont(new Font("Dialog",Font.BOLD | Font.ITALIC,13));
		add(timerLabel);
		add(new Label("\t"));

		labelScore = new JLabel("00"+getScore()+" ");
	 clocktimer= new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				timeCountSecond++;
				timeCountMinutes+=(timeCountSecond/60);
				if(timeCountSecond%60==0) {
					
					isNewLevel = true;

					timeCountSecond = 0;
				} else isNewLevel = false;


				String timer = String.format("Time elapsed %02d:%02d", timeCountMinutes,timeCountSecond);
				if(timeCountMinutes>level_increment&&timeCountSecond<10&&timeCountSecond%2==0){
					timerLabel.setForeground(Color.CYAN);
					timerLabel.setText("Level increased by "+timeCountMinutes);
					
					increaseSpeedBallPaddle();


				}else {
					    if( timeCountSecond>10)
					         level_increment=timeCountMinutes;
					timerLabel.setForeground(Color.GREEN);
					timerLabel.setText(timer);
				}


			}
		});
		clocktimer.start();
		labelScore.setForeground(Color.GREEN);
		labelScore.setFont(new Font("Dialog",Font.BOLD | Font.ITALIC,24));
		add(labelScore);
		
		
		//heart images
		try {
			String path=System.getProperty("user.dir")+"/"+"images/";
			

			image = ImageIO.read(new File(Paths.get(path+"heart1.PNG").toString()));
			image2 = ImageIO.read(new File(Paths.get(path+"heart2.PNG").toString()));
		} catch(IOException e) {
			
		}
	}
	
	//look at it to add more information and new balls
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;

		g2d.fill(new Ellipse2D.Double(xPositionBall, yPositionBall, ballSize, ballSize));
		
		g2d.setColor(Color.RED);
		g2d.fill(new Rectangle2D.Double(xPositionPaddle, yPositionPaddle, widthPaddle, heightPaddle));
		
		
		if (gameOver) {
			g2d.setFont(new Font("Arial", 1, 36));
			g2d.drawString("GAME OVER!!", getWidth()/2-80, getHeight()/2);
		}
		
		
		//draw lives remaining
		xPositionLife = 3*getWidth()/4;
		yPositionLife = labelScore.getHorizontalTextPosition();
		if(livesRemaining>=0) {
			g2d.drawImage(image2, xPositionLife+50, yPositionLife, sizeLife, sizeLife, this);
			g2d.drawImage(image2, xPositionLife+25, yPositionLife, sizeLife, sizeLife, this);
			g2d.drawImage(image2, xPositionLife, yPositionLife, sizeLife, sizeLife, this);
		}
		
		for(int i=1; i<=livesRemaining; i++) {
			g2d.drawImage(image, xPositionLife+(i-1)*25, yPositionLife, sizeLife, sizeLife, this);
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
		ballMoving();

		xPositionBall += xDistanceBall;
		yPositionBall += yDistanceBall;

		if (((xPositionPaddle+widthPaddle)<=getWidth())&&(xPositionPaddle>=0)) {
			xPositionPaddle += xVelocityPaddle;
			if((xPositionPaddle+widthPaddle)>getWidth()) {
				xPositionPaddle = getWidth()-widthPaddle;
			}
			if(xPositionPaddle<0) {
				xPositionPaddle = 0;
			}
		}
		
	}

	//movement of the ball
	public void ballMoving() {
		//if the ball hits the left or right sides of the window, negate its x speed
		if((xPositionBall)>(getWidth()-ballSize)||(xPositionBall<0)) {
			xVelocityBall=-xVelocityBall;
		}

		//if the ball hits the top side of the window, negate its y speed
		if((yPositionBall<0)) {
			yVelocityBall=-yVelocityBall;
		}

		/*with the bottom side of the window, the ball is conditioned at
		 * 1. if the ball is at same size as the paddle, reflect
		 * 2. if it is not, decrease the lives
		 * */
		if(yPositionBall >= (yPositionPaddle-ballSize) && yPositionBall >= (yPositionPaddle-3*ballSize/4)) {

			if((xPositionPaddle >= (xPositionBall+(ballSize/2)-widthPaddle)) && (xPositionPaddle <= (xPositionBall+(ballSize/2)))) {
				yVelocityBall=-yVelocityBall;
				score+=5;
				if(getScore()>=0&&getScore()<10) {
					labelScore.setText("00"+getScore());
				}
				else if(getScore()>=10&&getScore()<100) {
					if(getScore()==30 || getScore()==75) {
						increaseSpeedBall();
					}
					labelScore.setText("0"+getScore());
				}
				else if(getScore()>=100) {
					if(getScore()==100 || getScore()==150 || getScore()==250) {
						increaseSpeedBall();	
					}
					labelScore.setText(Integer.toString(getScore()));
				}
			}
			else {
				livesRemaining--;
				if(livesRemaining>0) {
					updateLivesLost();
				}
				if(livesRemaining<=0) {
					livesRemaining = 0;
					stopTimer();
					clocktimer.stop();

					gameOver = true;
				}
			}

		}
		xDistanceBall = xScaleFactor*(xVelocityBall)*time;
		yDistanceBall = yScaleFactor*yVelocityBall*time;
	}
	
	//stops timer if it is called
	public void stopTimer() {
		timer.stop();
	}
	
	//lives lost update
	public void updateLivesLost() {
		xPositionBall = 0;
		yPositionBall = 0;
		
		
		velocityBall = random.nextInt(4)+12;
		
		
		xVelocityBall = velocityBall*Math.cos(angle);
		yVelocityBall = velocityBall*Math.sin(angle) - gravity*time;
		
	}
	
	//increase the speeds of the ball as score increases
	public void increaseSpeedBall() {
		int increaseFactor = score/15;
		if(yVelocityBall<0) {
			if(xVelocityBall>0) {
				xVelocityBall+=timeCountMinutes*increaseFactor;
			}
			else if(xVelocityBall<0) {
				xVelocityBall-=timeCountMinutes*increaseFactor;
			}
			yVelocityBall-=timeCountMinutes*increaseFactor;
			xVelocityPaddleRange+=timeCountMinutes*increaseFactor;
		}
		else if(yVelocityBall>0) {
			if(xVelocityBall<0) {
				xVelocityBall-=timeCountMinutes*increaseFactor;
			}
			else if(xVelocityBall>0) {
				xVelocityBall+=timeCountMinutes*increaseFactor;
			}
			yVelocityBall+=timeCountMinutes*increaseFactor;
			xVelocityPaddleRange+=timeCountMinutes*increaseFactor;
		}
	}
	
	
	
	//increase the speed of the ball and paddle as levels increase
	public void increaseSpeedBallPaddle() {
		int increaseFactor = 5;
		if(isNewLevel) {
			if(yVelocityBall<0) {
				if(xVelocityBall>0) {
					xVelocityBall+=timeCountMinutes*increaseFactor;
				}
				else if(xVelocityBall<0) {
					xVelocityBall-=timeCountMinutes*increaseFactor;
				}
				yVelocityBall-=timeCountMinutes*increaseFactor;
				xVelocityPaddleRange+=timeCountMinutes*increaseFactor;
			}
			else if(yVelocityBall>0) {
				if(xVelocityBall<0) {
					xVelocityBall-=timeCountMinutes*increaseFactor;
				}
				else if(xVelocityBall>0) {
					xVelocityBall+=timeCountMinutes*increaseFactor;
				}
				yVelocityBall+=timeCountMinutes*increaseFactor;
				xVelocityPaddleRange+=timeCountMinutes*increaseFactor;
			}
		}
	}
	
	//decreases paddle velocity when left arrow is pressed
	public void leftArrow() {
		xVelocityPaddle =- xVelocityPaddleRange;
	}

	//increases paddle velocity when right arrow is pressed
	public void rightArrow() {
		xVelocityPaddle = xVelocityPaddleRange;
	}
	
	//set x velocity of the paddle to zero again
	public void arrowReleased() {
		xVelocityPaddle = 0;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();

		if(code == KeyEvent.VK_LEFT) {
			leftArrow();
		}

		if(code == KeyEvent.VK_RIGHT) {
			rightArrow();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if(code == KeyEvent.VK_LEFT || code == KeyEvent.VK_RIGHT)
			arrowReleased();
	}	
	
	//start timer
	public void startMyTimer() {
		timer.start();
	}
	

	public double getXPositionBall() {
		return xPositionBall;
	}
	
	public void resumeGame(double x, double y) {
		this.xDistanceBall=x;
		this.yDistanceBall=y;
		timer.start();
	}
	
	public void restartGame() {timeCountSecond=0;


		repaint();
		timeCountSecond=0;
		timeCountMinutes=0;
		clocktimer.start();

		if (gameOver)
			gameOver=false;
		this.xPositionBall=0;
		this.yPositionBall=0;
		this.livesRemaining=3;


		setScore(0);
		labelScore.setText("00"+getScore());
		resumeGame(xPositionBall,yPositionBall);

	}
	
	public double getYPositionBall() {
		return yPositionBall;
	}

}
