package Pong;


//imports
import java.awt.*;
import java.util.*;

public class Ball extends Rectangle {

    Random random;
    int xVelocty;
    int yVelocty;
    int initialSpeed = 3;

    Ball(int x, int y, int width, int height) {
        super(x, y, width, height);
        random = new Random();
        int randomXdirection = random.nextInt(2);
        if (randomXdirection == 0)
            randomXdirection--;
            setXdirection(randomXdirection * initialSpeed);

        int randomYdirection = random.nextInt(2);
        if (randomYdirection == 0)
            randomYdirection--;
            setYdirection(randomYdirection*initialSpeed);

    }

    public void setXdirection(int randomXdirection) {
            xVelocty = randomXdirection;
        }


    public void setYdirection (int randomYdirection) {
        yVelocty = randomYdirection;
    }

    public void move() {
        x += xVelocty;
        y += yVelocty;

    }

    public void draw (Graphics g) {
        g.setColor(Color.white);
        g.fillOval(x,y,width,height);

    }
}
