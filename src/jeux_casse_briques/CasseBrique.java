package jeux_casse_briques;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CasseBrique extends JPanel implements KeyListener, ActionListener {
    private Timer timer;
    private int score = 0;

    // Ball
    private int ballX = 120, ballY = 350;
    private int ballDirX = -1, ballDirY = -2;
    private final int BALL_SIZE = 20;

    // Paddle
    private int paddleX = 310;
    private final int PADDLE_WIDTH = 100;
    private final int PADDLE_HEIGHT = 10;

    // Bricks
    private boolean[][] bricks;
    private int brickRows = 3;
    private int brickCols = 7;
    private int brickWidth = 80;
    private int brickHeight = 30;

    public CasseBrique() {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        // Crée une grille de briques
        bricks = new boolean[brickRows][brickCols];
        for (int i = 0; i < brickRows; i++) {
            for (int j = 0; j < brickCols; j++) {
                bricks[i][j] = true;
            }
        }

        timer = new Timer(8, this);
        timer.start();
    }

    @Override
    public void paint(Graphics g) {
        // Fond
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);

        // Dessine les briques
        for (int i = 0; i < brickRows; i++) {
            for (int j = 0; j < brickCols; j++) {
                if (bricks[i][j]) {
                    int brickX = j * brickWidth + 80;
                    int brickY = i * brickHeight + 50;
                    g.setColor(Color.red);
                    g.fillRect(brickX, brickY, brickWidth, brickHeight);
                    g.setColor(Color.black);
                    g.drawRect(brickX, brickY, brickWidth, brickHeight);
                }
            }
        }

        // Paddle
        g.setColor(Color.green);
        g.fillRect(paddleX, 550, PADDLE_WIDTH, PADDLE_HEIGHT);

        // Balle
        g.setColor(Color.yellow);
        g.fillOval(ballX, ballY, BALL_SIZE, BALL_SIZE);

        // Score
        g.setColor(Color.white);
        g.setFont(new Font("Serif", Font.BOLD, 20));
        g.drawString("Score: " + score, 590, 30);

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();

        // Déplacement de la balle
        ballX += ballDirX;
        ballY += ballDirY;

        // Collisions mur gauche/droite
        if (ballX < 0 || ballX > 670) ballDirX = -ballDirX;
        // Collision plafond
        if (ballY < 0) ballDirY = -ballDirY;

        // Collision paddle
        if (new Rectangle(ballX, ballY, BALL_SIZE, BALL_SIZE)
                .intersects(new Rectangle(paddleX, 550, PADDLE_WIDTH, PADDLE_HEIGHT))) {
            ballDirY = -ballDirY;
        }

        // Collision briques
        for (int i = 0; i < brickRows; i++) {
            for (int j = 0; j < brickCols; j++) {
                if (bricks[i][j]) {
                    int brickX = j * brickWidth + 80;
                    int brickY = i * brickHeight + 50;
                    Rectangle brickRect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                    Rectangle ballRect = new Rectangle(ballX, ballY, BALL_SIZE, BALL_SIZE);

                    if (ballRect.intersects(brickRect)) {
                        bricks[i][j] = false;
                        score += 5;
                        ballDirY = -ballDirY;
                    }
                }
            }
        }

        // Défaite
        if (ballY > 570) {
            timer.stop();
            System.out.println("Game Over");
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (paddleX >= 600) paddleX = 600;
            else paddleX += 20;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (paddleX <= 10) paddleX = 10;
            else paddleX -= 20;
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Casse-Briques");
        CasseBrique game = new CasseBrique();
        frame.setBounds(10, 10, 700, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(game);
        frame.setVisible(true);
    }
}
