import java.awt.Color;
import javax.sound.sampled.*;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.swing.Timer;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener{

	//How big screen will be
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;	
	//How big objects on screen will be
	static final int UNIT_SIZE = 15;
	//How many units will fit on screen
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
	//How fast/slow will game be
	static final int DELAY = 75;
	//create array that holds body parts of snake
	final int x[] = new int[GAME_UNITS]; //this array holds x co-ordinates
	final int y[] = new int[GAME_UNITS]; //this array hold y co-ordinates
	//initial body parts on snake
	int bodyParts = 6;
	//How many apples have been eaten (always starts at zero)
	int applesEaten = 0;
	//Positioning of apples (x and y co-ordinates)
	int appleX;
	int appleY;
	//Direction of snake (starts going right)
	char direction = 'R';
	//Boolean for if the game is running or not
	boolean running = false;
	//creating instances of timer and random
	Random random;
	Timer timer;
	JLabel scoreLabel;
	int scoreNum = 0;

	GamePanel(){
		random = new Random();
		
		//Set a label to keep track of score and add it to the panel
		scoreLabel = new JLabel();
		scoreLabel.setForeground(Color.WHITE);
		scoreLabel.setBackground(Color.WHITE);
		scoreLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		scoreLabel.setBounds(0, -100, 250, 250);
		scoreLabel.setText("Points scored: " + scoreNum);
		scoreLabel.setVisible(true);
		//System.out.println("im here");
		
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setBackground(Color.BLACK);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		this.setLayout(null);
		this.add(scoreLabel);
		startGame();
		backgroundMusic();
	}
	
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY,this);
		timer.start();

		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		
		if(running) {
			//for(int i=0;i<SCREEN_WIDTH / UNIT_SIZE;i++) {
				//g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
				//g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
			//}
				g.setColor(Color.RED);
				g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
		
		for(int i = 0;i< bodyParts;i++) {
			if(i == 0) {
				g.setColor(Color.GREEN);
				g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
			}
			else {
				g.setColor(new Color(45,180,0));
				g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
			}
		}
	}
		else {
			gameOver(g);
		}
}
	
	public void newApple() {
		appleX = random.nextInt((int)(SCREEN_WIDTH / UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT / UNIT_SIZE))*UNIT_SIZE;
	}
	
	public void move() {
		for(int i=bodyParts;i>0;i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		switch (direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
		
	}
	
	public void pointSound() {
		try {
		File file = new File("Apple_Eaten.wav");
		AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
		Clip clip = AudioSystem.getClip();
		clip.open(audioStream);
		clip.start();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	public void backgroundMusic() {
		try {
		File file2 = new File("Background_Music.wav");
		AudioInputStream bgStream = AudioSystem.getAudioInputStream(file2);
		Clip bgClip = AudioSystem.getClip();
		bgClip.open(bgStream);
		bgClip.start();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	public void checkApple(){
		if((x[0] == appleX) && y[0] == appleY) {
			
			//Body parts, Apples eaten, and Score counter all go up by one 
			bodyParts++;
			applesEaten++;
			scoreNum++;
			
			//update the score counter every time an apple is eaten
			scoreLabel.setText("Points scored: " + scoreNum);
			
			//A new apple is created
			newApple();
			
			//
			pointSound();
		}
		
	}
	
	public void checkCollisions() {
		//check if head collides with body
		for(int i = bodyParts;i>0;i--) {
			if ((x[0] == x[i]&& (y[0] == y[i]))) {
				running = false;
			}
		}
		//check if head touches left border
		if(x[0] < 0) {
			running = false;
		}
		//check if head touches right border
		if(x[0] > SCREEN_WIDTH) {
			running = false;
		}
		//check if head touches top
		if(y[0] < 0) {
			running = false;
	}
		//check if head touches bottom
		if(y[0] > SCREEN_HEIGHT) {
			running = false;
		}
	
		if (!running){
			timer.stop();
		}
	
	}
	
	
	
	public void gameOver(Graphics g) {
		
		//Game Over Text
		g.setColor(Color.red);
		g.setFont(new Font("def_font",Font.BOLD, 75));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
	}
	
	public void ScoreLabel() {
		//scoreLabel = new JLabel();
		//scoreLabel.setForeground(Color.WHITE);
		//scoreLabel.setBounds(100, 0, 250, 250);
		//scoreLabel.setText(String.valueOf(scoreNum));
		//scoreLabel.setVisible(true);
		//System.out.println("im here");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		@Override 
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
			}
		}
	}
}
