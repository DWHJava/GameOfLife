package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window extends JFrame implements MouseListener {
	private static final long serialVersionUID = 7446192599263749847L;
	
	final Dimension WINDOW_SIZE = new Dimension(600,600);
	final static int SIZE = 12;
	final static int LIVE_CELLS = 48;
	
	static List<Integer> liveCellLocations = new ArrayList<Integer>();
	static List<Integer> alternateList = new ArrayList<Integer>();

	
	JPanel game = new JPanel();
	
	static JButton[] buttons = new JButton[SIZE * SIZE];
	
	public Window() {
		super("Game of Life");
		setSize(WINDOW_SIZE);
		setResizable(false);
		game.setLayout(new GridLayout(SIZE, SIZE));
		genButs();
		add(game);
		addMouseListener(this);
		setVisible(true);
		setLiveCells();
	}
	
	private void setLiveCells()
	{
		for (int i = 0; i < LIVE_CELLS; i++)
		{
			liveCellLocations.add(getUniqueRand());
			buttons[liveCellLocations.get(i)].setBackground(Color.BLUE);
		}

	}

	private void genButs() {
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new JButton();
			game.add(buttons[i]);
		}
		int i = 0;
		while (i < 5) {
			i ++;
		}
	}
	
	public static int getUniqueRand()
	{
		int randomNumber = 1 + (int) (Math.random() * ((SIZE * SIZE) - 1));

		for (int i = 0; i < liveCellLocations.size(); i++)
		{
			if (liveCellLocations.get(i) == randomNumber)
				return getUniqueRand();
		}

		return randomNumber;
	}
	
	public static int getLiveCellNumber(int position) 
	{
		int totalCellCount = 0;

		int row = position % SIZE;
		int column = position / SIZE;

		for (int i = 0; i < LIVE_CELLS; i++)
		{
			int cellPosition = liveCellLocations.get(i);
			int cellRow = cellPosition % SIZE;
			int cellColumn = cellPosition / SIZE;

			if (cellRow == row + 1 && cellColumn == column)
				totalCellCount++;
			if (cellRow == row - 1 && cellColumn == column)
				totalCellCount++;
			if (cellColumn == column + 1 && cellRow == row)
				totalCellCount++;
			if (cellColumn == column - 1 && cellRow == row)
				totalCellCount++;
			if (cellRow == row + 1 && cellColumn == column + 1)
				totalCellCount++;
			if (cellRow == row + 1 && cellColumn == column - 1)
				totalCellCount++;
			if (cellRow == row - 1 && cellColumn == column + 1)
				totalCellCount++;
			if (cellRow == row - 1 && cellColumn == column - 1)
				totalCellCount++;
		}
		
		return totalCellCount;
	}
	
	public static void updateCells() {
		liveCellLocations.clear();
		
		for (int i = 0; i < (SIZE*SIZE); i++) {
			buttons[i].setBackground(Color.WHITE);
		}
		
		for (int i = 0; i < alternateList.size(); i++) {
			liveCellLocations.add(alternateList.get(i));
			buttons[liveCellLocations.get(i)].setBackground(Color.BLUE);
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		for (int j = 0; j < (SIZE*SIZE); j++) {
			int cellNumber = getLiveCellNumber(j);
			
			for (int i = 0; i < liveCellLocations.size(); i++) {
				if (j == liveCellLocations.get(i)) {
					if (cellNumber < 2 || cellNumber > 3) {
						
					} else {
						alternateList.add(j);
					}
				} else {
					if (cellNumber == 3)
						alternateList.add(j);
				}
			}
		}
		updateCells();
	}
}
