/*
 This is the TetrisDisplay class that creates a 
 display of the board.
 11/28/2023
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;

public class TetrisDisplay extends JPanel {

    TetrisGame game;
    private boolean pause = false;
    private int start_x = 20;
    private int start_y = 12;
    private int cell_size = 12;
    private int speed = 20;
    private char keyWanted;
    private Timer timer;
    private Color[] colors = {Color.BLUE, Color.CYAN, Color.GREEN,
            Color.MAGENTA, Color.ORANGE, Color.YELLOW, Color.RED};

    public TetrisDisplay(TetrisGame gam) {

        game = gam;
        int delay = 300;
        timer = new Timer(delay, new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                cycleMove();
            }
        });
        timer.start();
        this.setBackground(Color.white);

        this.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                translateKey(e);
            }
        });
        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        // Calculate the center of the window
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        // Draw the Tetris board in the middle
        start_x = centerX - (cell_size * game.getCols()) / 2;
        start_y = centerY - (cell_size * game.getRows()) / 2;

        drawWell(g);
        drawBackground(g);
        drawFallingBrick(g);
        drawScore(g);
        drawGameOver(g);
    }

    private void drawGameOver(Graphics g) {
        if (game.gameOver) {
            // Display "Game Over" message in the middle of the board with a box
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            String gameOverMessage = "Game Over!";

            int messageX = start_x + (cell_size * game.getCols() - gameOverMessage.length() * 10) / 2;
            int messageY = start_y + (cell_size * game.getRows() / 2);

            // Draw a box around the "Game Over" message
            int boxWidth = gameOverMessage.length() * 20;
            int boxHeight = 50;

            g.fillRect(messageX - 10, messageY - 30, boxWidth, boxHeight);
            g.setColor(Color.BLACK);
            g.drawRect(messageX - 10, messageY - 30, boxWidth, boxHeight);

            // Draw the "Game Over" message centered inside the box
            g.setColor(Color.WHITE);
            g.drawString(gameOverMessage, messageX, messageY);
        }
    }

    private void drawWell(Graphics g) {

        g.setColor(Color.black);
        // right wall
        int wallWid = 12;
        g.fillRect(start_x - wallWid, start_y - wallWid, wallWid,
                cell_size * game.getRows() + 2 * wallWid);

        // Left Wall
        g.fillRect(start_x + cell_size * game.getCols(),
                start_y - wallWid, wallWid,
                cell_size * game.getRows() + 2 * wallWid);
        // Bottom Wall
        g.fillRect(start_x - wallWid,
                start_y - wallWid + cell_size * game.getRows() + wallWid,
                cell_size * game.getCols() + 2 * wallWid, wallWid);
    }

    private void drawFallingBrick(Graphics g) {

        for (int seg = 0; seg < game.getNumSegs(); seg++) {
            int cell_clr = game.getFallingBrickColor();

            g.setColor(colors[cell_clr]);
            g.fillRect(start_x + (game.getFallingBrick().position[seg][0] * cell_size),
                    start_y + (game.getSegRows(seg) * cell_size),
                    cell_size, cell_size);
            g.setColor(Color.BLACK);
            g.drawRect(start_x + (game.getFallingBrick().position[seg][0] * cell_size),
                    start_y + (game.getSegRows(seg) * cell_size), cell_size,
                    cell_size);
        }
    }

    public void drawBackground(Graphics g) {
        
        for (int row = 0; row < game.getRows(); row++) {
            for (int col = 0; col < game.getCols(); col++) {
                if (game.fetchBoardPosition(row, col) != -1) {
                    g.setColor(colors[game.fetchBoardPosition(row, col)]);
                } else {
                    g.setColor(Color.WHITE);
                }

                g.fillRect(start_x + col * cell_size,
                        start_y + row * cell_size, cell_size, cell_size);

                if (game.fetchBoardPosition(row, col) != -1) {
                    g.setColor(Color.BLACK);
                    g.drawRect(start_x + col * cell_size,
                            start_y + row * cell_size, cell_size, cell_size);
                }
            }
        }
    }

    public void drawScore(Graphics g) {
        
        int boxX = 10;
        int boxY = 10;
        int fontSize = 15;
        int boxOffset = 5;
        String scoreString = "Score: " + game.score;

        // Draw a box around the score string at the top left corner
        int boxWidth = scoreString.length() * 12;
        int boxHeight = 30;

        g.setColor(Color.WHITE);
        g.fillRect(boxX - 5, boxY - 20, boxWidth, boxHeight);
        g.setColor(Color.BLACK);
        g.drawRect(boxX - 5, boxY - 20, boxWidth, boxHeight);

        // Draw the score string inside the box
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, fontSize));
        g.drawString(scoreString, boxX + boxOffset, boxY + boxOffset);
    }

    private void translateKey(KeyEvent e) {
        int code = e.getKeyCode();
        int delay2 = 200;
        switch (code) {
            case KeyEvent.VK_SPACE:
                if (pause) {
                    timer.start();
                    game.state = 2;
                } else {
                    timer.stop();
                    game.state = 3;
                }

                pause = !pause;
                break;
            case KeyEvent.VK_LEFT:
                game.makeMove('l');
                break;
            case KeyEvent.VK_RIGHT:
                game.makeMove('r');
                break;
            case KeyEvent.VK_UP:
                game.makeMove('R');
                break;
            case KeyEvent.VK_N:
                game.newGame();
                break;
        }
    }

    private void cycleMove() {
        setFocusable(true);
        if (game.getState() == 1) {
            game.makeMove('a');
        } else {
            game.makeMove('d');
        }
        repaint();
    }
}
