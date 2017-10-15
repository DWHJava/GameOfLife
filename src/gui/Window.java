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
import javax.swing.SwingUtilities;

public class Window extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 7446192599263749847L;

	final Dimension WINDOW_SIZE = new Dimension(600, 600);
	final static int SIZE = 12;
	final static int LIVE_CELLS = SIZE * 4;

	final static long waitTime = 250;
	static Boolean isRunning = false;

	static List<Integer> liveCellLocations = new ArrayList<Integer>();

	static List<Integer> cellsToDie = new ArrayList<Integer>();
	static List<Integer> cellsToBirth = new ArrayList<Integer>();

	boolean tempTest = false;

	JPanel game = new JPanel();
	JPanel optionPanel = new JPanel();

	JButton next = new JButton("Next");
	JButton autoRun = new JButton("Auto Run");
	JButton clear = new JButton("Clear");
	JButton reset = new JButton("Reset");

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
		autoRun.addActionListener(this);
		clear.addActionListener(this);
		reset.addActionListener(this);

		optionPanel.add(next);
		optionPanel.add(autoRun);
		optionPanel.add(clear);
		optionPanel.add(reset);

		add(game, BorderLayout.CENTER);
		add(optionPanel, BorderLayout.SOUTH);

		setVisible(true);

		setLiveCells();
	}

	// Generate buttons
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

	// Preset live cells
	private void setLiveCells()
	{
		clearBoard();
		for (int i = 0; i < LIVE_CELLS; i++)
		{
			liveCellLocations.add(getUniqueRand());
			buttons[liveCellLocations.get(i)].setBackground(Color.BLUE);
		}
	}

	// Method to create unique random numbers
	private static int getUniqueRand()
	{
		int randomNumber = 1 + (int) (Math.random() * ((SIZE * SIZE) - 1));

		for (int i = 0; i < liveCellLocations.size(); i++)
		{
			if (liveCellLocations.get(i) == randomNumber)
				return getUniqueRand();
		}

		return randomNumber;
	}

	// Calculates how many live cells around cell
	private static int getLiveCellNumber(int position)
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

	// Updates cells after iteration
	private static void updateCells()
	{
		birthCells();
		killCells();
	}

	// Birth cells
	private static void birthCells()
	{
		for (int i = 0; i < cellsToBirth.size(); i++)
		{
			// change color and add cell position
			buttons[cellsToBirth.get(i)].setBackground(Color.BLUE);
			liveCellLocations.add(cellsToBirth.get(i));
		}

		cellsToBirth.clear();
	}

	// Kill cells
	private static void killCells()
	{
		for (int i = 0; i < cellsToDie.size(); i++)
		{
			// change color
			buttons[cellsToDie.get(i)].setBackground(Color.WHITE);

			// Remove new dead cell position
			if (liveCellLocations.contains(cellsToDie.get(i)))
				liveCellLocations.remove(liveCellLocations.indexOf(cellsToDie.get(i)));
		}

		cellsToDie.clear();
	}

	// Completes a life iteration
	private void doLifeIteration()
	{
		// Check every cell
		for (int j = 0; j < (SIZE * SIZE); j++)
		{
			int cellNumber = getLiveCellNumber(j);

			// check if cell is a live
			if (liveCellLocations.contains(j))
			{
				// check if cell will die
				if (cellNumber < 2 || cellNumber > 3)
				{
					// cell to die
					if (!cellsToDie.contains(j))
					{
						cellsToDie.add(j);
					}
				}
			}

			// else cell is dead
			else
			{
				// check if cell will birth
				if (cellNumber == 3)
				{
					// cell to birth
					if (!cellsToBirth.contains(j))
					{
						cellsToBirth.add(j);
					}
				}
			}
		}

		// stop autoRun if there are no changes
		if (cellsToBirth.isEmpty() && cellsToDie.isEmpty())
			setIsRunning(false);

		// update visuals
		updateCells();
	}

	// Clears the board
	private static void clearBoard()
	{
		liveCellLocations.clear();
		cellsToBirth.clear();
		cellsToDie.clear();

		for (int i = 0; i < SIZE * SIZE; i++)
		{
			buttons[i].setBackground(Color.WHITE);
		}
	}

	// Auto run for waitTime
	private void autoRun()
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{

				doLifeIteration();
				try
				{
					Thread.sleep(waitTime);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}

				if (isRunning)
					autoRun();
			}

		});

	}

	private void setIsRunning(boolean running)
	{
		if (!running)
		{
			isRunning = false;
			autoRun.setText("Auto Run");

		} else
		{
			isRunning = true;
			autoRun.setText("Stop");

			autoRun();
		}

		next.setEnabled(!isRunning);
		reset.setEnabled(!isRunning);
		clear.setEnabled(!isRunning);

	}

	@Override
	public void actionPerformed(ActionEvent source)
	{
		if (isRunning && source.getSource() != autoRun)
			return;
		// Check what button is being pressed
		if (source.getSource() == next)
		{
			doLifeIteration();
		} else if (source.getSource() == autoRun)
		{
			setIsRunning(!isRunning);

		} else if (source.getSource() == reset)
		{
			setLiveCells();
		} else if (source.getSource() == clear)
		{
			clearBoard();
		} else
		{
			for (int i = 0; i < buttons.length; i++)
			{
				if (source.getSource() == buttons[i] && liveCellLocations.contains(i))
				{
					cellsToDie.add(i);
					updateCells();
				} else if (source.getSource() == buttons[i])
				{
					cellsToBirth.add(i);
					updateCells();
				}
			}

		}
	}

}