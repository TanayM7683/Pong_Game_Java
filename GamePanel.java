package Pong;


//imports
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable {

    static final int GAME_WIDTH = 1000;
    //multiplied by 5/9 because of ration between hieght and width of an offical table
    static final int GAME_HEIGHT = (int )(GAME_WIDTH* (0.5555));
    static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    static final int BALL_DIAMETER = 20;
    static final int Paddle_WIDTH = 25;
    static final int Paddle_HEIGHT = 100;
    Thread gameThread;
    Image image;
    Graphics graphics;
    Random random;
    Padels paddle1;
    Padels paddle2;
    Ball ball1;
    Score score1;
    static final int WIN_SCORE = 11;
    boolean gameOver = false;
    String winnerText = "";

    //constuctor
    GamePanel() {

        newPaddles();
        newBall();
        score1 = new Score(GAME_WIDTH,GAME_HEIGHT);
        this.setFocusable(true);
        this.addKeyListener(new AL() {});
        this.setPreferredSize(SCREEN_SIZE);

        gameThread = new Thread(this);
        gameThread.start();
    };

    public void newBall() {
        random = new Random();
        ball1 = new Ball(
                (GAME_WIDTH / 2) - (BALL_DIAMETER / 2),
                (GAME_HEIGHT / 2) - (BALL_DIAMETER / 2),
                BALL_DIAMETER,
                BALL_DIAMETER
        );
    }

    public void newPaddles() {
        paddle1 = new Padels(0,(GAME_HEIGHT/2)-(Paddle_HEIGHT/2), Paddle_WIDTH, Paddle_HEIGHT,001);
        paddle2 = new Padels(GAME_WIDTH-Paddle_WIDTH,(GAME_HEIGHT/2)-(Paddle_HEIGHT/2), Paddle_WIDTH, Paddle_HEIGHT,002);

    }

    public void paint(Graphics g) {
        image = createImage(getWidth(), getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image, 0, 0, this);

    }

    public void draw(Graphics g) {
        paddle1.draw(g);
        paddle2.draw(g);
        ball1.draw(g);
        score1.draw(g);

        if(gameOver){
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.setColor(Color.YELLOW);
            g.drawString(winnerText, GAME_WIDTH/2 - 200, GAME_HEIGHT/2);
        }
    }
    public void move () {
        paddle1.move();
        paddle2.move();
        ball1.move();

    }

    public void checkCollision() {
        //stops paddles at window edges

        if (paddle1.y<=0)
            paddle1.y=0;
        if (paddle1.y >= (GAME_HEIGHT - Paddle_HEIGHT)){
            paddle1.y=GAME_HEIGHT - Paddle_HEIGHT;}

        if (paddle2.y<=0)
            paddle2.y=0;
        if (paddle2.y >= (GAME_HEIGHT - Paddle_HEIGHT))
            paddle2.y=GAME_HEIGHT - Paddle_HEIGHT;

        //bounce ball off top and bottom window
        if (ball1.y <=0)
            ball1.setYdirection(-ball1.yVelocty);

        if (ball1.y >= (GAME_HEIGHT - BALL_DIAMETER))
            ball1.setYdirection(-ball1.yVelocty);

        //bounces ball off paddles
        if (ball1.intersects(paddle1)){
            ball1.xVelocty = Math.abs(ball1.xVelocty);
            ball1.xVelocty ++; //for greater difficulty
            if (ball1.yVelocty > 0)
                ball1.yVelocty++;
            else
                ball1.yVelocty--;
            ball1.setXdirection(ball1.xVelocty);
            ball1.setYdirection(ball1.yVelocty);
        }

        if (ball1.intersects(paddle2)){
            ball1.xVelocty = Math.abs(ball1.xVelocty);
            ball1.xVelocty ++; //for greater difficulty
            if (ball1.yVelocty > 0)
                ball1.yVelocty++;
            else
                ball1.yVelocty--;
            ball1.setXdirection(-ball1.xVelocty);
            ball1.setYdirection(ball1.yVelocty);
        }



        if (ball1.x <= 0) {
            score1.player2++;

            if(score1.player2 == WIN_SCORE){
                gameOver = true;
                winnerText = "PLAYER 2 WINS!";
            } else {
                newPaddles();
                newBall();
            }
        }

        if (ball1.x >= GAME_WIDTH - BALL_DIAMETER) {
            score1.player1++;

            if(score1.player1 == WIN_SCORE){
                gameOver = true;
                winnerText = "PLAYER 1 WINS!";
            } else {
                newPaddles();
                newBall();
            }
        }

    }

    public void run () {
        //game loop
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        while (true) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            if (delta >= 1) {
                if(!gameOver) {
                    move();
                    checkCollision();
                }
                repaint();
                delta--;
            }
        }
    }

    public class AL extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            paddle1.keyPressed(e);
            paddle2.keyPressed(e);
        }

        public void keyReleased(KeyEvent e) {
            paddle1.keyReleased(e);
            paddle2.keyReleased(e);
        }
    }
}
