/*
 This is the TetrisGame class that creates an instance
 of the tetris game.
 Brian Richard-Ikediashi
 11/28/2023
 */

import java.util.*;
import java.io.*;
import javax.swing.*;

public class TetrisGame implements SavableGame {
    
    TetrisBrick fallingBrick;
    private int rows;
    private int cols;
    private int numBrickTypes;
    private int[][] background;
    protected int state;
    protected boolean gameOver = false;
    private boolean nameCollected = false;
    protected int score;
    private TetrisLeaderboard leaderBoard;
    private ArrayList<String> files = new ArrayList<>();
    
    Random randomGen = new Random();    

    public TetrisGame(int rs, int cs){ 
        
        rows = rs;
        cols = cs;
        background = new int[rs][cs];
        
        initBoard();
        recoverGames();
        spawnBrick();
   
    }
    
    public TetrisBrick getFallingBrick(){
        return fallingBrick;
    }
    
    public int getFallingBrickColor(){
        return fallingBrick.getColorNumber();
    }
    
    public int getNumSegs()
    {
       return fallingBrick.numSegments;
    }
    
    public void initBoard(){
        for(int seg = 0; seg<getRows(); seg++)
        {
            for(int seg2 = 0; seg2<getCols(); seg2++)
            {
                background[seg][seg2] =-1;
            }
        }
    }
    
    public int fetchBoardPosition(int row, int col){
        return background [row][col]; 
    }
    
    public void newGame(){
        
        score = 0;
        state = 1;
        nameCollected = false;
        gameOver = false;
        initBoard();
        spawnBrick();
        fallingBrick.moveDown();

        if (!validateMove()) {
            fallingBrick.moveUp(); 
            spawnBrick();
        }
        
        if (gameOver == true) {
            endGame(gameOver);
            nameCollected = true;                            
        }
        
    }
    
    public void endGame(boolean gameStatus) {
        
        if (gameStatus) {
            TetrisLeaderboard leaderBoard = new TetrisLeaderboard(this);
            gameOver = false;
        }
    }

    public void spawnBrick() {
        int brickNum = randomGen.nextInt(7);
        int numSeg = 4;

        if (brickNum == 0 || brickNum == 1 || brickNum == 2 || brickNum == 6) {
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < cols; col++) {
                    if (background[row][col] != -1) {
                        gameOver = true;
                        state = 0;
                        if (nameCollected == false) {
                            endGame(gameOver);
                            nameCollected = true;
                        }
                    return;
                    }
                }
            }
        } 
        
        else if (brickNum == 3 || brickNum == 4 || brickNum == 5) {
            for (int row = 0; row < 2; row++) {
                for (int col = 0; col < cols; col++) {
                    if (background[row][col] != -1) {
                        gameOver = true;
                        state = 0;
                        if (nameCollected == false) {
                            endGame(gameOver);
                            nameCollected = true;                    
                        }
                        return;
                    }
                }
            }
        }

        int startingCol = cols / 2;

        while (startingCol + numSeg > cols) {
            startingCol--;
        }

        for (int seg = 0; seg < numSeg; seg++) {
            int row = 0;
            int col = startingCol + seg;

            for (int offset : new int[]{-1, 1}) {
                int surroundingCol = col + offset;
                if (surroundingCol >= 0 && surroundingCol < cols && background[row][surroundingCol] != -1) {
                    startingCol--;
                    seg = -1;
                    break;
                }
            }
        }

        if (brickNum == 0) {
            fallingBrick = new ElBrick(cols / 2, numSeg, brickNum);
        } 
        else if (brickNum == 1) {
            fallingBrick = new EssBrick(cols / 2, numSeg, brickNum);
        } 
        else if (brickNum == 2) {
            fallingBrick = new JayBrick(cols / 2, numSeg, brickNum);
        } 
        else if (brickNum == 3) {
            fallingBrick = new SquareBrick(cols / 2, numSeg, brickNum);
        } 
        else if (brickNum == 4) {
            fallingBrick = new LongBrick(cols / 2, numSeg, brickNum);
        } 
        else if (brickNum == 5) {
            fallingBrick = new StackBrick(cols / 2, numSeg, brickNum);
        } 
        else if (brickNum == 6) {
            fallingBrick = new ZeeBrick(cols / 2, numSeg, brickNum);
        }
    }

    
    public void makeMove(char code){
        switch(code)
        {
            case 'd':
                fallingBrick.moveDown();
                if (!validateMove() )
                {
                    fallingBrick.moveUp(); 
                    transferColor();
                    clearRows();
                    spawnBrick(); 
                }
                break;
                
            case 'r':
                fallingBrick.moveRight();
                if(!validateMove())
                {
                    fallingBrick.moveLeft();
                }
                break;
    
            case 'l':
                fallingBrick.moveLeft();
                if(!validateMove())
                {
                    fallingBrick.moveRight();
                }
                break;
    
            case 'R' :
                fallingBrick.rotate();
                if(!validateMove())
                {
                    fallingBrick.unrotate();
                   
                }
                break;
                
            case 'a':
                spawnBrick();
                break;
            }
        
        if(validSpawn()){
            state = 2;
        }
        else{
            state =3;
            checkForEnd();    
        }
    }
    
    public boolean validSpawn(){
        
        for(int seg =0; seg< fallingBrick.numSegments; seg ++){
            if(getSegCols(seg)<=0){
                return false;
            }
        } 
        return true;
    }
    
    public boolean validateMove(){
        for (int seg = 0; seg < fallingBrick.numSegments; seg++) {
            int row = fallingBrick.position[seg][1];
            int col = fallingBrick.position[seg][0];

            if (row < 0 || row >= getRows() || col < 0 || col >= getCols()) {
                return false;
            }

            int backGroundIdent = background[row][col];
            if (backGroundIdent != -1) {
                return false;
            }
        }

        return true;
    }
    
    private void transferColor(){      
        int backRow;
        int backCol;
        
        for(int seg = 0; seg<getNumSegs(); seg++){
            backRow = getSegRows(seg);
            backCol = getSegCols(seg);
            background[backRow][backCol] = getFallingBrickColor();
        }
    
    }
 
    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
    
    public int getState(){
        return state;
    }

    public int getSegRows(int row) {
        return fallingBrick.position[row][1];
    }
    
    public int getSegCols(int col) {
       return fallingBrick.position[col][0];
    }
    
    public int getScore(){
        return score;
    }

    public boolean checkForEnd(){
        boolean end = false;
        for(int seg=0; seg < getCols(); seg++){
            if(background[0][seg] !=0)
            {
                end = true;
            }
        }
        return end;
    }

    public boolean rowHasSpace(int row_num){
        
        boolean rowFull = true;
    
        for(int col = 0; col< cols; col++){
            if(background[row_num][col]== -1){
                rowFull = false;   
                return true;
            }
        }
        return false;
    }
    
    public void copyRow(int row_num){
        
        for(int col =0; col<getCols();col++){
            
            if(row_num ==getRows()-1){
                background[row_num][col]= 0;
            }
            else{
                background[row_num+1][col]= background[row_num][col];
                if (row_num<getRows()-1 &&rowHasColor(row_num,col) ==false){
                    background[row_num+1][col]= background[row_num][col];
                    row_num++;
                }
            }
        }
    }
    
    public boolean rowHasColor(int rowNum,int col){
        boolean rowColored = false;

            if(background[rowNum +1][col]!=0&& rowNum<rows){
                rowColored = true;
            }
        return rowColored;
    }
    
    public void copyAllRows(int rowNum)
    {
        for(int seg =rowNum-1; seg !=0;seg--)
        {
            copyRow(seg);
        }
    }
    
    public void clearRows(){    
       
        int clearedRows = 0;
       for(int row = getRows()-1;row>=0; row--)
       {
           if(rowHasSpace(row)==false)
           {
               copyAllRows(row);
               row++;
               clearedRows++;
           }
        }
        switch(clearedRows)
        {
           case 1: 
               score +=100;
               break;
           case 2:
               score += 300;
               break;
           case 3:
               score += 600;
               break;
           case 4:
               score+= 1200;
               break;
        }
    }
    
    public String toString(){
        
        String gameString = ""+background.length+" "+background[0].length+" "+score+"\n";
        for(int row = 0; row < background.length;row++)
        {
            for(int col = 0; col < background[0].length;col++)
            {
                gameString += background[row][col]+ " ";
            }
            gameString += "\n";
        }
        gameString = gameString.substring(0,gameString.length()-1 );
        return gameString;
    }
    
    private void writeToFile(String name){
        
        File fileConnection = new File("AllGames.dat");
        if(fileConnection.exists() && !fileConnection.canWrite()){
            System.err.print("!!!!!!!!!!!!!!!!!!!!!!!!!!!"
                            +"Error trying to open file"
                            +"!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            return;
        }
        try
        {
            FileWriter outWriter = new FileWriter(fileConnection,true);
            outWriter.write(name+"\n");
            outWriter.close();
        }
        catch(IOException ioe)
        {
            System.err.print("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
                            +"Error trying to write to file"
                            +"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
    }
    
    public void saveToFile(String fileName){
        File fileConnection = new File(fileName);
        if(fileConnection.exists() && !fileConnection.canWrite())
        {
            System.err.print("error trying to open file");
            return;
        }
        try
        {
            files.add(fileName);
            writeToFile(fileName);
            FileWriter outWriter = new FileWriter(fileConnection);
            outWriter.write(this.toString());
            outWriter.close();
        }
        catch(IOException ioe)
        {
            System.err.print("!!!!!!!!!!!!!!!!!!!!!!!!!"
                            +"Error trying to save file"
                            +"!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
    }
    
    public void retrieveFromFile() {
        
        try
        {
            Object[] fileGroup = files.toArray();
            int selectedFile = JOptionPane.showOptionDialog(null,
                    "Please pick a file to load game from","Retrieve Game",
                    0,0,null,fileGroup,fileGroup[0]);
            String fileName = fileGroup[selectedFile].toString();
            File fileConnection = new File(fileName);
            Scanner inScan = new Scanner(fileConnection);
            rows = inScan.nextInt();
            cols = inScan.nextInt();
            score = inScan.nextInt();
            background = new int[rows][cols];
            for(int row = 0;row<rows;row++)
            {
                for(int col=0;col<cols;col++)
                {
                    background[row][col] = inScan.nextInt();
                }
            }
        }
        catch(Exception e)
        {
            System.err.print("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
                            +"Error occured trying to retrieve from file"
                            +"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
    }
        
    private void recoverGames(){
        try
        {
            File fileConnection = new File("AllGames.dat");
            Scanner inScan = new Scanner(fileConnection);
            while(inScan.hasNextLine())
            {
                files.add(inScan.nextLine());
            }
            inScan.close();
        }
        catch(FileNotFoundException e)
        {
            System.err.print("!!!!!!!!!!!!!!!!!!!!!!!!!"
                            +"Error trying to open file"
                            +"!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
    }
    
}