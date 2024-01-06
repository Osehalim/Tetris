/*
 This is the code for the stack brick sub class
 of the TetrisBrick super class
 Brian Richard-Ikediashi
 11/7/2023
 */

public class StackBrick extends TetrisBrick{
        
    public StackBrick(int col, int numSeg ,int clrNum){
        super(numSeg,clrNum);
        initPosition(col);
    }
    
    public void initPosition(int col){
        position = new int [][]{{col ,-1},
                                {col-1 , 0},
                                {col ,0},
                                {col+1 ,0}                           
        };
        
        orientation = 1;
    }
    
    public void rotate()
    {
        switch(orientation)
        {
            case 1: 
                position = new int [][] {  {position[0][0]+1 ,position[0][1]+1},
                                {position[1][0]+1   ,position[1][1]-1},
                                {position[2][0], position[2][1] },
                                {position[3][0] - 1 , position[3][1]+1}
                };
                orientation=2;
                break;
            case 2:  
                position = new int [][] {  {position[0][0]-1 ,position[0][1]+1},
                                {position[1][0]+1  ,position[1][1]+1},
                                {position[2][0], position[2][1] },
                                {position[3][0]-1 , position[3][1]-1}
                };
                orientation =3;
                break;
            case 3:  
                position = new int [][] {  {position[0][0]-1 ,position[0][1]-1},
                                {position[1][0]-1 ,position[1][1]+1},
                                {position[2][0], position[2][1]},
                                {position[3][0]+1 , position[3][1]-1}
                };
                orientation =4;
                break;
            case 4: 
                position = new int [][]{  {position[0][0]+1 ,position[0][1]-1},
                                {position[1][0]-1 ,position[1][1]-1},
                                {position[2][0], position[2][1]},
                                {position[3][0]+1 , position[3][1]+1}
                };
                orientation =1;
                break;
        }
    }
    
    public void unrotate()
    {
        rotate();
        rotate();
        rotate();       
    }  
 
}