// Author: Thomas Hoyt
// Fifteen Square project
// Class FifteenSquare simulates the physical board game fifteen square using a GUI interface.
// Players must move the fifteen squares into numerical order moving the empty spot into the lower left corner.
// Current games can be saved into the work space in a ".dat" file using the save button 
// and reloaded using the load button.

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class FifteenSquare extends JFrame 
{
	// Placement of the JButtons depends on these constants since a layout manager is not used
	static final int BOARD_WIDTH = 525;
	static final int BOARD_HEIGHT =715;
	static final int B_GAP = 10;
	static final int ACTION_BUTTON_HEIGHT = 50;

	static final int B_WIDTH_HEIGHT = (BOARD_WIDTH - (B_GAP * 6))/4;
	static final int ACTION_BUTTON_WIDTH = (BOARD_WIDTH - (B_GAP * 5))/3;
	
	// These two variables store the current row and column of the invisible zero JButton
	int zeroButtonRow = 0;
	int zeroButtonColumn = 0;
	
	// numberOfMoves stores the number of moves for the JLabel moves text
	// numberOfWins stores the number of wins for the users reference
	int numberOfMoves;
	int numberOfWins;
	
	// is true while shuffling false other wise is referenced by the isSolved method to ensure a winner is not declared while shuffling
	boolean shuffling;
	
	// Array of JButtons to represent the 15 numbered buttons and the invisible zero button
	public JButton [][] buttons = new JButton[4][4];
	
	// Four JButtons and one JLabel for the bottom section of the board
	JButton save = new JButton("SAVE");
	JButton help = new JButton("HELP");
	JButton load = new JButton("LOAD");
	JButton exit = new JButton("EXIT");
	JLabel moves = new JLabel("  Moves: 0");
	
	// main assembles the JFrame in which the entire board is contained
	public static void main(String[] args)
	{
		FifteenSquare board = new FifteenSquare();
		board.setSize(BOARD_WIDTH, BOARD_HEIGHT);
		board.setLocationRelativeTo(null);
		board.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		board.setTitle("Fifteen-Square by Thomas Hoyt");
		board.setVisible(true);

	}// end of main Assignment15Square
	
	// FifteenSquare constructor creates all of the JButtons registers them with listeners and places them in the JFrame (e.i. Board)
	public FifteenSquare()
	{
		// variables for the location of the numbered buttons.
		// The first button starts at (B_GAP, B_GAP) 
		int xLocation = B_GAP;
		int yLocation = B_GAP;
		
		
		// Font for the numbered buttons
		Font numberButtonFont = new Font("Button Font", Font.BOLD,40);
		
		// set the layout to null as per assignment requirements
		setLayout(null);
		
		
		// int used to add the proper text to each numbered JButton
		int count = 0;
		
		// Row/Column for loops creates each numbered button object and then adds it to the board(JFrame) in its proper location(i.e. row/column)
		for(int row = 0; row < 4; row++)
		{
			for(int column = 0; column < 4; column++)
			{
				JButton button = new JButton(Integer.toString(count));
				buttons[row][column] = button;
				buttons[row][column].setSize(B_WIDTH_HEIGHT, B_WIDTH_HEIGHT);
				buttons[row][column].setFont(numberButtonFont);
				add(buttons[row][column]);
				buttons[row][column].setLocation(xLocation, yLocation);
				buttons[row][column].setVisible(true);
				
				// after each button is put on the board the xLocation is incremented
				// in preparation for the location of the next button
				xLocation+= B_WIDTH_HEIGHT + B_GAP;
			
				// create and register listener to each number JButton 
				buttons[row][column].addActionListener(new NumberButtonListener());
				
				// increment the count so the numbers get the proper text 
				count++;
			}// end of inner for
			
			// after the inner for creates the rows the JButton x and y locations 
			// are set to start the next row below the previous row
			yLocation += B_WIDTH_HEIGHT + B_GAP;
			xLocation = B_GAP;
		}// end of outer for
		
		// set the 0 button invisible to represent the empty space on the board
		buttons[0][0].setVisible(false);
		
		// create font for the action buttons
		Font actionButton = new Font("action buttons", Font.BOLD, 20);
		
		// create and place the save button on the board
		save.setBounds(B_GAP, B_WIDTH_HEIGHT * 4 + B_GAP *6 , ACTION_BUTTON_WIDTH, ACTION_BUTTON_HEIGHT);
		save.setFont(actionButton);
		save.setVisible(true);
		add(save);
		save.addActionListener(new ProcessListener());
		
		// create and place the help button on the board
		help.setBounds(B_GAP * 2 + ACTION_BUTTON_WIDTH,B_WIDTH_HEIGHT * 4 + B_GAP *6 , ACTION_BUTTON_WIDTH, ACTION_BUTTON_HEIGHT);
		help.setFont(actionButton);
		help.setVisible(true);
		add(help);
		help.addActionListener(new ProcessListener());
		
		// create and place the load button on the board
		load.setBounds(B_GAP * 3 + ACTION_BUTTON_WIDTH * 2, B_WIDTH_HEIGHT * 4 + B_GAP *6, ACTION_BUTTON_WIDTH, ACTION_BUTTON_HEIGHT);
		load.setFont(actionButton);
		load.setVisible(true);
		add(load);
		load.addActionListener(new ProcessListener());
		
		// create and place the exit button on the board
		exit.setBounds(B_GAP * 2 + ACTION_BUTTON_WIDTH, B_WIDTH_HEIGHT * 4 + B_GAP * 7 + ACTION_BUTTON_HEIGHT, ACTION_BUTTON_WIDTH, ACTION_BUTTON_HEIGHT);
		exit.setFont(actionButton);
		exit.setVisible(true);
		add(exit);
		exit.addActionListener(new ProcessListener());
		
		// create and place the moves JLabel on the board
		moves.setBounds(B_GAP * 3 + ACTION_BUTTON_WIDTH * 2, B_WIDTH_HEIGHT * 4 + B_GAP * 7 + ACTION_BUTTON_HEIGHT, ACTION_BUTTON_WIDTH, ACTION_BUTTON_HEIGHT);
		moves.setFont(actionButton);
		moves.setVisible(true);
		add(moves);
		
		// shuffle the board on the initial set-up of the board
		shuffle();
		
	}// end of frame constructor
	
	// Inner listener class registered to each numbered JButton
	class NumberButtonListener implements ActionListener
	{
		// actionPerformed method gets (x,y) point of the button pressed
		@Override
		public void actionPerformed(ActionEvent e)
		{	
			// the (x,y) point of the currently clicked button is stored in x and y
			// then the row and column of the clicked button is found and stored in row and column
			int x = ((JButton)e.getSource()).getX();
			int y = ((JButton)e.getSource()).getY();
			int row = y/(B_WIDTH_HEIGHT + B_GAP);
			int column = x/(B_WIDTH_HEIGHT + B_GAP);
			
			// if the button clicked is a valid move the text of the moves JLabel is updated with the new number of moves
			if((row == zeroButtonRow && column != zeroButtonColumn ) || (row != zeroButtonRow && column == zeroButtonColumn))
				moves.setText("  Moves: " + (++numberOfMoves));
			
			// call moveClick to make the requested move
			moveClick(row, column);
			
		}// end of actionPerformed method
		
	}// end of NumberButtonListener class
	
	// inner listener class registered to each of the four action buttons on the bottom section of the board
	class ProcessListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			try
			{
				// if the save button is clicked the current state of the board and number of wins is saved in "your name".dat
				if(save == e.getSource())
				{
					String file = JOptionPane.showInputDialog(null, "Enter your name to save your game", "File Name", JOptionPane.QUESTION_MESSAGE);
					
					ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file + ".dat"));
					
					output.writeObject(buttons);
					output.writeObject(moves);
					output.writeObject(zeroButtonRow);
					output.writeObject(zeroButtonColumn);
					output.writeObject(numberOfWins);
					
					output.close();
				}// end of if for save button
				
			}// end of try
			
			catch(IOException ex)
			{
				System.out.print("IOException");
			}// end of catch
			
			// if the help button is clicked a option dialog box pops up with the message written below 
			if(help == e.getSource())
			{
				JOptionPane.showMessageDialog(null, "Help Buttons usually aren't much help, and neither is this one!"
						+ "\nJust line up the numbers in numerical order starting from the upper left corner"
						+ "\nUnless you forgot how to count?\n\n Current Number of Wins" + numberOfWins);
			}// end of if for help button
			
			try
			{
				// if the load button is clicked the last saved state of the game is read in from "your name".dat
				// and the current state of the board is changed to the state of the saved state
				// boolean variable save is set to false since the currently played game is no longer the the one saved in savedBoard.dat
				if(load == e.getSource())
				{
					String file = JOptionPane.showInputDialog(null, "Enter your name to open your saved game", "File Name", JOptionPane.QUESTION_MESSAGE);
					
					ObjectInputStream input = new ObjectInputStream(new FileInputStream(file + ".dat"));
					
					JButton[][] savedButtons = (JButton[][])(input.readObject());
					
					// for loop sets the text of the current numbered JButtons to the text of the saved numbered JButtons
					for(int row = 0; row < 4; row++)
					{
						for(int column = 0; column < 4; column++)
						{	
							buttons[row][column].setText(savedButtons[row][column].getText());
							
						}// inner column for loop
						
					}// outer row for loop
					
					// set the current JLabel to the saved JLabel
					JLabel savedMoves = (JLabel)(input.readObject());
					moves.setText(savedMoves.getText());
					
					buttons[zeroButtonRow][zeroButtonColumn].setVisible(true);
					
					int savedRow = (int)(input.readObject());
					int savedColumn = (int)(input.readObject());
					int savedNumberOfWins = (int)(input.readObject());
					
					zeroButtonRow = savedRow;
					zeroButtonColumn = savedColumn;
					buttons[zeroButtonRow][zeroButtonColumn].setVisible(false);
					numberOfWins = savedNumberOfWins;
					
					input.close();
					
				}// end of if for load button
				
			}// end of try
			
			catch(IOException ex)
			{
				JOptionPane.showMessageDialog(null, "Saved Game not Found");
				
			}// end of catch
			
			catch(ClassNotFoundException ex)
			{
				JOptionPane.showMessageDialog(null, "Class not found exception");
				
			}// end of catch
			
			if(exit == e.getSource())
			{

				int option = JOptionPane.showConfirmDialog(null, "Exit without Saving?");

				if(option == 0) System.exit(0);
				
				if(option == 1) return;
				
				if(option == 2) return;

			}// end of if for exit button
		
		}// end actionPerformed method
		
	}// end ProcessListener class
	
	// shuffle method shuffles the board to begin a new game
	private void shuffle()
	{
		// indicates shuffling is in progress
		shuffling = true;
		
		// first swap the 15 and 0 JButtons so the board is solvable
		// and set the zeroButton location variables to its new location 
		// only if this is the initial set-up of the board and not a new game after a win
		if(Integer.parseInt(buttons[0][0].getText()) == 0 && Integer.parseInt(buttons[3][3].getText()) == 15)
		{
			buttons[0][0].setText("15");
			buttons[3][3].setText("0");
			zeroButtonRow = 3;
			zeroButtonColumn = 3;
			
			// set the 0 button invisible to represent the empty space on the board
			buttons[3][3].setVisible(false);
			
			// set the 0 button visible to represent 15 JButton
			buttons[0][0].setVisible(true);
			
		}// end of if
		
		// both of these variables are used to store the current random
		// column and row generated they are both initialized at 3 because the invisible JButton's 
		// position in array buttons has just been set to buttons[3][3]
		int randomColumn = 3;
		int randomRow = 3;
		
		// Each iteration simulates two user clicks.
		// first a random column 0-3 is generated, then moveClick is evoked with the current randomRow initially 3 and the generated column
		// Then a second call to moveClick with the previously generated column and a new randomly generated row 0-3
		// switching between a random row to a random column ensures that each call to moveClick is a valid move (i.e. a click 
		// in the same row or column of the current zero invisible button)
		for(int count = 0; count < 100; count++)
		{
			randomColumn = (int)(Math.random()*4);
			moveClick(randomRow, randomColumn);
			
			randomRow= (int)(Math.random()*4);
			moveClick(randomRow,randomColumn);
		}// end of for
		
		// indicates shuffling is complete
		shuffling = false;
		
		numberOfMoves = 0;
		moves.setText("  Moves: " + (numberOfMoves));
		
	}// end of shuffle method
	
	// moveClick method's parameters are the currently clicked buttons row column
	// the method moves the text of each numbered JButton depending on where to simulate the moving of the tiles 
	void moveClick(int clickedRow, int clickedColumn)
	{
		// if statement moves the text of a row if the clicked JButton is in the 
		// same row as the zero invisible button AND is not in the same column as the zero invisible JButton
		if((clickedRow == zeroButtonRow) && (clickedColumn != zeroButtonColumn))
		{
			// setButtonColumn stores the JButton column currently getting its text set
			// getButtonColumn stores the JButton column of the JButton who's text is being retrieved
			int setButtonColumn= zeroButtonColumn;
			int getButtonColumn= zeroButtonColumn;
			
			// The loop count is set to the absolute difference between the current zero JButton and currently clicked button.
			// so that the right number of JButtons texts' are moved
			// Each iteration moves the text from one JButton to the one next to it. The direction of the move depends on 
			// where the board has been clicked to the left or right of the current zero invisible JButton which is decided by the ternary operator
			for(int count = Math.abs(zeroButtonColumn- clickedColumn); count > 0; count--)
			{
				getButtonColumn= ((zeroButtonColumn - clickedColumn) < 0)? (++getButtonColumn): (--getButtonColumn);
				buttons[zeroButtonRow][setButtonColumn].setText(buttons[zeroButtonRow][getButtonColumn].getText());
				setButtonColumn = getButtonColumn;
				
			}// end of for 
			
			// after moving the text the visibility of the old and new Zero JButton is set 
			buttons[zeroButtonRow][zeroButtonColumn].setVisible(true);
			zeroButtonRow = clickedRow;
			zeroButtonColumn= clickedColumn;
			buttons[zeroButtonRow][zeroButtonColumn].setText("0");
			buttons[zeroButtonRow][zeroButtonColumn].setVisible(false);
			
		}// end of if for rows
		
		// if statement moves the text of a column if the clicked JButton is not in the same row 
		// AND is in the same column as the zero invisible button
		if((clickedRow != zeroButtonRow) && (clickedColumn == zeroButtonColumn))
		{
			// setButtonRow stores the JButton Row currently getting its text set
			// getButtonRow stores the JButton Row of the JButton who's text is being retrieved
			int setButtonRow = zeroButtonRow;
			int getButtonRow = zeroButtonRow;
			
			// The loop count is set to the absolute difference between the current zero JButton and currently clicked button.
			// so that the right number of JButtons texts' are moved
			// Each iteration moves the text from one JButton to the one next to it. The direction of the move is depends on 
			// where the board has been clicked above or below the current zero invisible JButton and is decided by the ternary operator
			for(int count = Math.abs(zeroButtonRow - clickedRow); count > 0; count--)
			{
				getButtonRow = ((zeroButtonRow - clickedRow) < 0)? (++getButtonRow): (--getButtonRow);
				buttons[setButtonRow][zeroButtonColumn].setText(buttons[getButtonRow][zeroButtonColumn].getText());
				setButtonRow = getButtonRow;
				
			}// end of for 
			
			// after moving the text the visibility of the old and new Zero JButton is set
			buttons[zeroButtonRow][zeroButtonColumn].setVisible(true);
			zeroButtonRow = clickedRow;
			zeroButtonColumn= clickedColumn;
			buttons[zeroButtonRow][zeroButtonColumn].setText("0");
			buttons[zeroButtonRow][zeroButtonColumn].setVisible(false);
			
		}// end of if for columns
		
		// if the zero invisible JButton is currently in the bottom right corner the isSolved is called to check the board for a win
		if(zeroButtonRow == 3 && zeroButtonColumn == 3) isSolved();
			
	}// end of move method
	
	// isSolved method checks the board for a winner by summing up each column and checking to see if 
	// each column sum matches that of a winning configuration
	void isSolved()
	{
		// used to store the players input to the JOptionPane.showConfirmDialog box
		int option;
		
		//checked against the current buttons text
		int buttonCheck = 1;
		
		// if the board is being shuffled exit the isSolved method
		if(shuffling) return;
		
		// loops through each button on the button to check for a winning board
		// if there is a miss match exit the isSolved method
		// if buttonCheck reaches 15 exit the for loops we have a winner
		for(int row = 0; row < 4; row++)
		{
			for(int column =0; column < 4; column++)
			{
				if(buttonCheck++ != Integer.parseInt(buttons[row][column].getText())) return;
				if(buttonCheck == 15) break;
			}// inner row for
			
		}// outer column for
		
		// increment the players wins  
		numberOfWins++;
		
		// asks the player if they want to play again or not
		option = JOptionPane.showConfirmDialog(null, "Congratulations you Won!!"
				+ "\nDo you wish to play again?"
				+ "\nYes: shuffles the board to start a new game"
				+ "\nNo: Exits the game and saves your win\nCancel: Exits without saving your win"
				+ "\n\n#Wins-" + numberOfWins);
		
		// if they choose to play again the board is shuffled and the # of moves reset to zero
		if(option == 0)
		{
			shuffle();

		}
		
		// if they want to end the game and save the win
		if(option == 1)
		{
			try
			{
				// shuffles the board so that the game is not saved in the winning configuration
				// just the number of wins is saved
				shuffle();
				
				String file = JOptionPane.showInputDialog(null, "Enter your name to save your game", "File Name", JOptionPane.QUESTION_MESSAGE);
				
				ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file + ".dat"));
				
				output.writeObject(buttons);
				output.writeObject(moves);
				output.writeObject(zeroButtonRow);
				output.writeObject(zeroButtonColumn);
				output.writeObject(numberOfWins);
				
				output.close();
				
				System.exit(0);
			}
			
			catch(IOException ex)
			{
				return;
			}
		}
		
		// if user chooses to exit the game
		if(option == 2) System.exit(0);
		
	}// end of isSolved
	
}// end of Frame classer your name to save your game", "File Name", JOptionPane.QUESTION_MESSAGE);
				
				ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file + ".dat"));
				
				output.writeObject(buttons);
				output.writeObject(moves);
				output.writeObject(zeroButtonRow);
				output.writeObject(zeroButtonColumn);
				output.writeObject(numberOfWins);
				
				output.close();
				
				System.exit(0);
			}
			
			catch(IOException ex)
			{
				return;
			}
		}
		
		// if user chooses to exit the game
		if(option == 2) System.exit(0);
		
	}// end of isSolved
	
}// end of Frame class