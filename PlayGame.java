package roland;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;

public class PlayGame extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	JMenuItem resumeGame, pauseGame, restartGame, exit;
	LobPong lobpong;
	JMenu fileMenu;
	JMenuBar jmb;
	
	public PlayGame() {
		
		setTitle("Lob Pong");
		lobpong = new LobPong();
		jmb = new JMenuBar();
		
		//file menu
		String path=System.getProperty("user.dir")+"/"+"images/";
		
		pauseGame = new JMenuItem("Pause", new ImageIcon(path+"pause.png"));
		resumeGame = new JMenuItem("Resume", new ImageIcon(path+"play.png"));
		exit = new JMenuItem("Exit", new ImageIcon("exit.png"));
		fileMenu = new JMenu("File");
		fileMenu.add(restartGame = new JMenuItem("Restart Game"));
		fileMenu.add(pauseGame);
		fileMenu.add(resumeGame);
		fileMenu.addSeparator();
		fileMenu.add(exit);
		restartGame.setMnemonic('R');
		pauseGame.setMnemonic('P');
		resumeGame.setMnemonic('R');
		exit.setMnemonic('E');
				
		
		
		
		
		jmb.add(fileMenu);
		
		//setting key board shortcuts
		resumeGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
		pauseGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PAUSE, ActionEvent.CTRL_MASK));
		restartGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		
		
		//registering listeners
		pauseGame.addActionListener(this);
		resumeGame.addActionListener(this);
		restartGame.addActionListener(this);
		exit.addActionListener(this);
		
		
		
		setSize(800, 623);
		setResizable(false);
		setLocationRelativeTo(null);
		setJMenuBar(jmb);
		add(lobpong);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
		
	

	@Override
	public void actionPerformed(ActionEvent e) {
		double x=0, y=0;
		if(e.getSource()==pauseGame) {
			x=lobpong.xPositionBall=lobpong.getXPositionBall();
			y=lobpong.yPositionBall=lobpong.getYPositionBall();
			lobpong.timer.stop();
			lobpong.clocktimer.stop();
		}
		if(e.getSource()==resumeGame) {
			lobpong.clocktimer.start(); 
			lobpong.resumeGame(x,y);
			lobpong.clocktimer.start();
		}
		if(e.getSource()==restartGame) {
			lobpong.restartGame();
		}
		if(e.getSource()==exit) {
			System.exit(0);
		}
		
		
	}
	
	public static void main(String[] args) {
		System.out.println();

		new PlayGame();
	}
}
