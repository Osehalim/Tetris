/*
 This is the code for the square brick sub class
 of the TetrisBrick super class
 Brian Richard-Ikediashi
 11/7/2023
 */

public class SquareBrick extends TetrisBrick{
    
    public SquareBrick(int col, int numSeg ,int clrNum){
        super(numSeg,clrNum);
        initPosition(col);

    }
    
    public void initPosition(int col){
        position = new int [][]{{col-1 ,0},
                               {col-1, 1},
                               {col ,0},
                               {col, 1}                              
        };       
    }
    
    public void rotate(){
        
    }
    
    public void unrotate(){
        
    }
    
    
}
