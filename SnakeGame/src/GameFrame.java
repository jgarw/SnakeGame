import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class GameFrame extends JFrame {

	GameFrame(){
		GamePanel panel = new GamePanel();
		
		this.add(panel);
		this.setTitle("Snake Game");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
		
	}
	
}
