/*
 The super class for all the types of tetris bricks
 Brian Richard-Ikediashi
 11/28/2023
*/

import java.awt.*;

public abstract class TetrisBrick {
    
    protected int numSegments=4;
    protected int[][] position;
    protected Color color;
    protected int colorNum;
    protected int orientation;

    public TetrisBrick(int numSeg, int clrNum) {
        colorNum = clrNum;
    }
    
    public boolean moveDown(){
        for (int seg =0; seg<numSegments; seg++){
            position[seg][1]++;
        }
        return true;
    }
    
    public boolean moveUp(){
        for (int seg = 0; seg < numSegments; seg++){
            position [seg] [1]--;
        }
        return true;
    }
    
    public boolean moveRight()
    {
        for (int seg =0; seg <numSegments; seg++)
        {
            position [seg] [0]++;
        }
        return true;
    } 
    
    public boolean moveLeft()
    {
        for (int seg =0; seg <numSegments; seg++)
        {
            position [seg] [0]--;
        }
        return true;
    }
    
    public int getPosition(int row, int col) { 
        for(int seg = 0; seg< numSegments; seg++)
        {
            if(position[seg][1]==row && position[seg][0]==col)
            {
                return 1;
            }
        }
        return 0;
    }

    public int getColorNumber() {
        return colorNum;
    }
    
    public String toString() {
        String brickString = "";
        for (int seg = 0; seg < numSegments; seg++) {
            brickString += "(" + position[seg][0] + ", " + position[seg][1] + ") ";
        }
        brickString += "Color: " + colorNum + ", Orientation: " + orientation;
        return brickString;
    }

    public abstract void initPosition(int mid);  
    
    public abstract void rotate();
   
    public abstract void unrotate();
    
}
