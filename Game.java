/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

// imports necessary libraries for Java swing
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
    private int bombs = 10;
    private int size = 9;
    private String[] scores;
    private String bScores;
    private String iScores;
    private String eScores;
    
	public void run() {
		// top-level frame in which game components live
		final JFrame frame = new JFrame("Minesweeper");
		frame.setLocation(200, 200);
		
		// game board
        final Board board = new Board(size, bombs);
        frame.add(board, BorderLayout.CENTER);
        
		// instructions and settings panel
        final JPanel settings_panel = new JPanel();
        frame.add(settings_panel, BorderLayout.SOUTH);
        
        // instructions window
        final JFrame instrFrame = new JFrame("Instructions");
        instrFrame.setSize(400, 400);
        instrFrame.setLocation(200, 200);
        
        // settings window
        final JFrame setFrame = new JFrame("Settings");
        setFrame.setSize(400, 400);
        setFrame.setLocation(200, 200);
        
        // high scores window
        final JFrame scoreFrame = new JFrame("High Scores");
        scoreFrame.setSize(280, 160);
        scoreFrame.setLocation(400, 360);
        highscores();
        JTabbedPane scoresPane = new JTabbedPane();
        bScores = "<html>" + scores[0] + "<br>" + scores[1] + "<br>" + scores[2] + "<br>"
                + scores[3] + "<br>" + scores[4] + "</html>";
        final JLabel beginner = new JLabel(bScores);
        scoresPane.addTab("Beginner", beginner);
        iScores = "<html>" + scores[5] + "<br>" + scores[6] + "<br>" + scores[7] + "<br>"
                + scores[8] + "<br>" + scores[9] + "</html>";
        final JLabel intermediate = new JLabel(iScores);
        scoresPane.addTab("Intermediate", intermediate);
        eScores = "<html>" + scores[10] + "<br>" + scores[11] + "<br>" + scores[12] + "<br>"
                + scores[13] + "<br>" + scores[14] + "</html>";
        final JLabel expert = new JLabel(eScores);
        scoresPane.addTab("Expert", expert);
        scoreFrame.add(scoresPane);
        
        // create instructions button functionality
		final JButton instructions = new JButton("Instructions");
		instructions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String message = "Welcome to Minesweeper, CIS120 Edition!\n\n";
                message += "The game is simple: avoid the bombs. Left-click on\n"
                        + "a square to reveal its state (i.e. if it has a bomb, or\n"
                        + "the number of bombs adjacent to it if it doesn't). If\n"
                        + "you think a square has a bomb, right-click on it to\n"
                        + "flag it.\n\n"
                        + "To win, you need to correctly flag all of the bombs\n"
                        + "in the game, and you can keep track of your progress\n"
                        + "using the counter on the left. If you win, your score\n"
                        + "will be recorded as the number of seconds it took you\n"
                        + "to complete the game. If your score is in the top five,\n"
                        + "you'll be asked to enter your name to be added to the\n"
                        + "high scores list.\n\n"
                        + "There are three difficulties, and you can choose which\n"
                        + "one to play at in the settings menu. And if you ever\n"
                        + "want to start a new game, click the \"new game\" button\n"
                        + "at the top.\n\n"
                        + "Good luck!";
                JOptionPane.showMessageDialog(instrFrame, message, "Instructions",
                        JOptionPane.PLAIN_MESSAGE);
            }
        });
		
		// create settings button functionality
		final JButton settings = new JButton("Settings");
		settings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String message = "Select a level:";
                String[] choices = {"Beginner (9 x 9 with 10 bombs)",
                        "Intermediate (16 x 16 with 40 bombs)", "Expert (22 x 22 with 100 bombs)"};
                String level = (String)JOptionPane.showInputDialog(setFrame, message, "Settings",
                        JOptionPane.PLAIN_MESSAGE, null, choices, "Name");
                // update game size and number of bombs based on user's choice
                if (level != null) {
                    char c = level.charAt(0);
                    if (c == 'B') {
                        bombs = 10;
                        size = 9;
                        board.setLevel(c);
                    }
                    if (c == 'I') {
                        bombs = 40;
                        size = 16;
                        board.setLevel(c);
                    }
                    if (c == 'E') {
                        bombs = 100;
                        size = 22;
                        board.setLevel(c);
                    }
                }
            }
        });
		
		// create high scores button functionality
        final JButton highscores = new JButton("High Scores");
        highscores.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // update all scores
                highscores();
                bScores = "<html>" + scores[0] + "<br>" + scores[1] + "<br>" + scores[2] + "<br>"
                        + scores[3] + "<br>" + scores[4] + "</html>";
                iScores = "<html>" + scores[5] + "<br>" + scores[6] + "<br>" + scores[7] + "<br>"
                        + scores[8] + "<br>" + scores[9] + "</html>";
                eScores = "<html>" + scores[10] + "<br>" + scores[11] + "<br>" + scores[12]
                        + "<br>" + scores[13] + "<br>" + scores[14] + "</html>";
                beginner.setText(bScores);
                intermediate.setText(iScores);
                expert.setText(eScores);
                scoreFrame.setVisible(true);
            }
        });
		settings_panel.add(instructions);
		settings_panel.add(settings);
		settings_panel.add(highscores);
		
		// reset button
		final JPanel control_panel = new JPanel();
		frame.add(control_panel, BorderLayout.NORTH);
		final JButton reset = new JButton("New Game");
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    board.reset();
			}
		});
		control_panel.add(reset);

		// put the frame on the screen
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		// start game
		board.reset();
	}
	
	/**
	 * highscores() reads the game's past high scores from the provided file and updates the Game's
	 * scores array
	 */
	public void highscores() {
	    scores = new String[15];
	    try {
            BufferedReader in = new BufferedReader(new FileReader("highscores.txt"));
            // read first line, which will always be "High Scores"
            in.readLine();
            for (int i = 0; i < 15; i++) {
                // read line that gives the level
                if (i % 5 == 0) {
                    in.readLine();
                }
                String score = in.readLine();
                scores[i] = score;
            }
            in.close();
          } catch (IOException e) {
            System.out.println("error while checking document: " + e.getMessage());
          }
	}

	/*
	 * Main method run to start and run the game. Initializes the GUI elements
	 * specified in Game and runs it. IMPORTANT: Do NOT delete! You MUST include
	 * this in the final submission of your game.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Game());
	}
}
