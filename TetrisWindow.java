/*
 This is the code for the TetrisWindow class that creates
 the window that displays the tertis game.
 Brian Richard-Ikediashi
 11/28/2023
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TetrisWindow extends JFrame {
    
    private int win_width = 400;
    private int win_height = 450;
    private int game_rows = 20;
    private int game_cols = 12;
    private TetrisGame game;
    private TetrisDisplay display;
    private TetrisLeaderboard leaderBoard;

    public TetrisWindow() {

        setBoardSize();
        game = new TetrisGame(game_rows, game_cols);
        display = new TetrisDisplay(game);
        leaderBoard = new TetrisLeaderboard(game);

        this.setTitle("My Tetris Game: Brian Richard-Ikediashi");
        this.setSize(win_width, win_height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(display, BorderLayout.CENTER);
        initMenus();
        
        this.setVisible(true);
        
    }
    
    private void setBoardSize() {
        String inputRows = JOptionPane.showInputDialog("Enter the number of rows for the Tetris board:");
        String inputCols = JOptionPane.showInputDialog("Enter the number of columns for the Tetris board:");

        try {
            game_rows = Integer.parseInt(inputRows);
            game_cols = Integer.parseInt(inputCols);
            if(game_rows <10 || game_cols <6){
                JOptionPane.showMessageDialog(null, "Invalid input. Using default board size.");
                game_rows = 20; // Default rows
                game_cols = 12; // Default columns
            }
        } 
        catch (NumberFormatException e) {
        // Handle invalid input
            JOptionPane.showMessageDialog(null, "Invalid input. Using default board size.");
            game_rows = 20; 
            game_cols = 12; 
        }
    }

    public void initMenus()
    {
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        
        JMenu gameMenu = new JMenu("Game");
        menuBar.add(gameMenu);
        
        JMenuItem saveGame = new JMenuItem("Save");
        gameMenu.add(saveGame); 
        
        JMenuItem retrieveGame = new JMenuItem("Retrieve");
        gameMenu.add(retrieveGame); 
        
        JMenuItem newGame = new JMenuItem("New");
        gameMenu.add(newGame); 
        
        JMenu score = new JMenu("Score");
        menuBar.add(score);
        
        JMenuItem highScore = new JMenuItem("High Score");
        score.add(highScore); 
        
        JMenuItem clearScore = new JMenuItem("Clear Score");
        score.add(clearScore); 
        
        newGame.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent ae)
                    {
                       game.newGame();
                    }
                });
        saveGame.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent ae)
                    {
                       String file_name = JOptionPane.showInputDialog("Please enter"
                               + " the name of the file you would like to save yoour game as:");
                       game.saveToFile(file_name+".dat");
                       return;
                    }
                });
        retrieveGame.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent ae)
                    {
                       game.retrieveFromFile();
                       return;
                    }
                });
        highScore.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                JOptionPane.showMessageDialog(null,
                        leaderBoard.scoreBoard(leaderBoard.getScores_file()),
                        "High Scores", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        clearScore.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                leaderBoard.removeScores();
            }
        });
        this.setJMenuBar(menuBar);    
        
    }
    
    public static void main(String[] args){
        TetrisWindow window = new TetrisWindow();
    }
    
}
