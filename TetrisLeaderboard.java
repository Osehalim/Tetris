/*
 This is the Leaderboard class. It creates the leaderboard
 to display high scores for players.
 Brian Richard-Ikediashi
 11/28/2023
*/

import java.io.*;
import java.util.*;
import javax.swing.*;

public class TetrisLeaderboard {
    
    private TetrisGame game;
    private int score;
    private int[] highScores = new int[10];
    private String[] playerNames = new String[10];
    private String scores_file = "scores.dat";
    private boolean gameStatus;
    
    public TetrisLeaderboard(TetrisGame game){
        this.game = game;
        score = game.getScore();
        loadHighScores();
        if (game.getState() == 0){
            for (int playerIndex = 0; playerIndex < getPlayerNames().length; playerIndex++) {
                if (getHighScores()[playerIndex] == 0) {
                    //initialize playerNames array with default names
                    playerNames[playerIndex] = "Name";
                }
            }
            // Check if the current score is higher than the lowest high score
            if (score > highScores[highScores.length - 1]) {
                String playerName = JOptionPane.showInputDialog("Game Over! Enter your name:");

                updateHighScores(playerName, score);

                writeHighScoresToFile();
            }
        }   
    }
       
    private void writeDefaultScoresToFile() {
        try {
            FileWriter outWriter = new FileWriter(getScores_file(), false);
            
            // Writing default high scores with player names
            for (int defaultIndex = 0; defaultIndex < getHighScores().length; defaultIndex++) {
                outWriter.write("Player: DefaultPlayer" + (defaultIndex + 1) + "  Score: "+ (defaultIndex*0)  + "\n");
            }

            outWriter.close();
        } 
        catch (IOException ioe) {
            System.err.print("Error trying to write default high scores to file");
        }
    }
    
    public void enterScores(String playerName, int score) {
        updateHighScores(playerName, score);

        writeHighScoresToFile();        
    }
    
    public void removeScores() {
        try {
            // Call writeDefaultScoresToFile to reset scores to default values
            writeDefaultScoresToFile();

            // Open the file and close it to clear existing content
            FileWriter remove = new FileWriter(scores_file, false);
            remove.close();
        } 
        catch (IOException e) {
            System.err.print("Error finding file");
        }
    }


    private void updateHighScores(String playerName, int newScore) {
        int indexToInsert = -1;

        // Check where to insert the new score
        for (int newScoreChecker = 0; newScoreChecker < getHighScores().length; newScoreChecker++) {
            if (newScore > getHighScores()[newScoreChecker]) {
                indexToInsert = newScoreChecker;
                break;  // Stop checking once the position is found
            }
        }

        // Shift scores and names down to make space for the new score
        if (indexToInsert >= 0) {
            for (int scoreIndex = getHighScores().length - 1; scoreIndex > indexToInsert; scoreIndex--) {
                highScores[scoreIndex] = getHighScores()[scoreIndex - 1];
                playerNames[scoreIndex] = getPlayerNames()[scoreIndex - 1];
            }

        // Insert the new score with the player's name
            highScores[indexToInsert] = newScore;
            playerNames[indexToInsert] = playerName;
        }
    }

    private void writeHighScoresToFile() {
        try {
            FileWriter outWriter = new FileWriter(getScores_file(), false);
            for (int scoreIndex = 0; scoreIndex < getHighScores().length; scoreIndex++) {
                outWriter.write("Player: "+getPlayerNames()[scoreIndex] + "  " 
                        + "Score: "+ getHighScores()[scoreIndex] + "\n");
            }
            outWriter.close();
        } 
        catch (IOException ioe) {
            System.err.print("Error trying to write high scores to file");
        }
    }

    

    private void loadHighScores() {
        try {
            File fileConnection = new File(getScores_file());
            Scanner inScan = new Scanner(fileConnection);

            if (!inScan.hasNextLine()) {
                // If the file is empty, write default scores and player names
                writeDefaultScoresToFile();
            } 
            else {
                // Read the first 10 lines, each representing a player name and score
                for (int i = 0; i < getHighScores().length && inScan.hasNextLine(); i++) {
                    String scoreLine = inScan.nextLine();

                    String playerNamePrefix = "Player: ";
                    String scorePrefix = "Score: ";

                    // Find the indices of player name and score
                    int playerNameStart = scoreLine.indexOf(playerNamePrefix);
                    int scoreStart = scoreLine.indexOf(scorePrefix);

                    // Check if prefixes are found
                    if (playerNameStart != -1 && scoreStart != -1) {
                        playerNameStart += playerNamePrefix.length();
                        scoreStart += scorePrefix.length();

                        // Extract player name
                        String playerName = scoreLine.substring(playerNameStart, scoreStart - scorePrefix.length()).trim();

                        // Extract the numeric part of the score
                        String scoreStr = scoreLine.substring(scoreStart).trim();
                        int score;

                        try {
                            score = Integer.parseInt(scoreStr);
                        }
                        catch (NumberFormatException ex) {
                            System.err.println("Invalid score format in scores file: " + scoreLine);
                            continue;
                        }

                        playerNames[i] = playerName;
                        highScores[i] = score;
                    }    
                
                    else {
                        System.err.println("Invalid format in scores file: " + scoreLine);
                    }
                }
            }

            inScan.close();
        } 
        catch (FileNotFoundException e) {
            System.err.print("Error trying to open scores file");
        }
    }

    public String scoreBoard(String fName)
    {
        String stuff = "";
        try
        {
            File fileConnection = new File(fName);
            Scanner inScan = new Scanner(fileConnection);
            while(inScan.hasNextLine())
            {
                stuff += inScan.nextLine() + "\n";
            }
            inScan.close();
        }
        catch(FileNotFoundException e)
        {
            System.err.print("error trying to open file");
        }
        return stuff;
    }

    public int[] getHighScores() {
        return highScores;
    }

    public String[] getPlayerNames() {
        return playerNames;
    }

    public String getScores_file() {
        return scores_file;
    }

}
