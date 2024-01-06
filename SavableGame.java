/*
 This is the interface with methods to be implemented in the 
 TetrisGame class to allow you save a game to file and retrieve
 a specific game.
 Brian Richard-Ikediashi
 11/27/2023
 */

public interface SavableGame {
    
    public void saveToFile(String fileName);
    public void retrieveFromFile();

}
