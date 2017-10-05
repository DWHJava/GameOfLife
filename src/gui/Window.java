package gui;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window extends JFrame {
	private static final long serialVersionUID = 7446192599263749847L;
	
	final Dimension WINDOW_SIZE = new Dimension(600,600);
	final int SIZE = 12;
	
	JPanel game = new JPanel();
	
	JButton[] buttons = new JButton[SIZE * SIZE];
	
	public Window() {
		super("Game of Life");
		setSize(WINDOW_SIZE);
		setResizable(false);
		game.setLayout(new GridLayout(SIZE, SIZE));
		genButs();
		add(game);
		setVisible(true);
	}
	
	private void genButs() {
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new JButton();
			game.add(buttons[i]);
		}
	}
}
