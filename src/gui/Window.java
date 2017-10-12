package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window extends JFrame implements ActionListener {
	private static final long serialVersionUID = 7446192599263749847L;
	
	final Dimension WINDOW_SIZE = new Dimension(600,600);
	final static int SIZE = 12;
	final static int LIVE_CELLS = SIZE * 4;
	
	static List<Integer> liveCellLocations = new ArrayList<Integer>();
	
	static List<Integer> cellsToDie = new ArrayList<Integer>();
	static List<Integer> cellsToBirth = new ArrayList<Integer>();
		
	
	boolean tempTest = false;
	
	JPanel game = new JPanel();
	JPanel optionPanel = new JPanel();
	JButton next = new JButton("Next");
	JButton toggle = new JButton("Toggle");
	JButton clear = new JButton("Clear");
	JButton changingSize = new JButton("Size Settings");
	
	static JButton[] buttons = new JButton[SIZE * SIZE];
	
	public Window() 
	{
		super("Game of Life");
		setSize(WINDOW_SIZE);
		setResizable(false);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.setLayout(new GridLayout(SIZE, SIZE));
		generateButtons();
		next.addActionListener(this);
		toggle.addActionListener(this);
		clear.addActionListener(this);
		optionPanel.add(next);
		optionPanel.add(toggle);
		optionPanel.add(clear);
		add(game, BorderLayout.CENTER);
		add(optionPanel, BorderLayout.SOUTH);
		setVisible(true);
		setLiveCells();
	}
	
	//Generate buttons
	private void generateButtons() 
	{
		for (int i = 0; i < buttons.length; i++) 
		{
			buttons[i] = new JButton();
			buttons[i].addActionListener(this);
			game.add(buttons[i]);
			buttons[i].setBackground(Color.WHITE);
		}
	}
	
	//Preset live cells
	private void setLiveCells()
	{
		for (int i = 0; i < LIVE_CELLS; i++)
		{
			liveCellLocations.add(getUniqueRand());
			buttons[liveCellLocations.get(i)].setBackground(Color.BLUE);
		}

	}
	
	//Method to create unique random numbers
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
	
	//Calculates how many live cells around cell
	public static int getLiveCellNumber(int position) 
	{
		int totalCellCount = 0;

		int row = position % SIZE;
		int column = position / SIZE;

		for (int i = 0; i < liveCellLocations.size(); i++)
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
	
	//Updates cells after iteration
	public static void updateCells() 
	{
		//Kill Cells
		for (int i = 0; i < cellsToDie.size(); i++) 
		{
			//change color
			buttons[cellsToDie.get(i)].setBackground(Color.WHITE);
			
			//Remove new dead cell position
			if (liveCellLocations.contains(cellsToDie.get(i)))
				liveCellLocations.remove(liveCellLocations.indexOf(cellsToDie.get(i)));
		}
		
		//Birth Cells
		for (int i = 0; i < cellsToBirth.size(); i++) 
		{
			buttons[cellsToBirth.get(i)].setBackground(Color.BLUE);
			liveCellLocations.add(cellsToBirth.get(i));
		}
		
		cellsToBirth.clear();
		cellsToDie.clear();
	}
	
	private static void doLifeIteration() {
		//Check every cell
		for (int j = 0; j < (SIZE*SIZE); j++) {
			int cellNumber = getLiveCellNumber(j);
			
			//check if cell is a live
			if (liveCellLocations.contains(j)) {
				//check if cell will die
				if (cellNumber < 2 || cellNumber > 3) {
					//cell to die
					if (!cellsToDie.contains(j)) {
						cellsToDie.add(j);
					}
				}
			} 
			//else cell is dead
			else {
				//check if cell will birth
				if (cellNumber == 3) {
					//cell to birth
					if (!cellsToBirth.contains(j)) {
						cellsToBirth.add(j);
					}
				}
			}
			//update visuals
			
		}
		updateCells();
	}
	
	private static void clearBoard() {		
		liveCellLocations.clear();
		cellsToBirth.clear();
		cellsToDie.clear();
		
		for (int i = 0; i < SIZE * SIZE; i++) {
			buttons[i].setBackground(Color.WHITE);
		}		
	}
	
	private static void testing(int j) {
		int cellNumber = getLiveCellNumber(j);
		if (liveCellLocations.contains(j)) {
			//check if cell will die
			if (cellNumber < 2 || cellNumber > 3) {
				//cell to die
				if (!cellsToDie.contains(j)) {
					System.out.println("It should be killed");
				}
			}
		} 
		//else cell is dead
		else {
			//check if cell will birth
			if (cellNumber == 3) {
				//cell to birth
				if (!cellsToBirth.contains(j)) {
					System.out.println("It should be birthed");
				}
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent source) {
		
		//Check if next button is pressed
		if (source.getSource() == next) {
			doLifeIteration(); 
		} else if (source.getSource() == toggle) {
			// TODO Autorun doesn't work fix it
			tempTest = !tempTest;
		} else if (source.getSource() == clear) {
			clearBoard();
		} else {
			for (int i = 0; i < buttons.length; i++) {
				if (source.getSource() == buttons[i] && liveCellLocations.contains(i)) {
					if (!tempTest) {
						cellsToDie.add(i);
						updateCells();
					} else {
						testing(i);
					}
				} else if (source.getSource() == buttons[i]) {
					if (!tempTest) {
						cellsToBirth.add(i);
						updateCells();
					} else {
						testing(i);
					}
				}
			}
			//updateCells();
		}
	}
}
