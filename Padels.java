package Pong;


//imports
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Padels extends Rectangle{

    int id;
    int yVelocity;
    int speed = 10;

    Padels(int x, int y, int PADDLE_WIDTH, int PADDLE_HEIGHT, int id) {
        super(x, y, PADDLE_WIDTH, PADDLE_HEIGHT);
        this.id = id;

    }

    public void keyPressed(KeyEvent e) {
        switch (id) {
            case 1:
                if(e.getKeyCode() == KeyEvent.VK_W) {
                    setDirection(-speed);
                    move();
                }
                if(e.getKeyCode() == KeyEvent.VK_S) {
                setDirection(speed);
                move();
            }
                break;
            case 2:
                if(e.getKeyCode() == KeyEvent.VK_UP) {
                    setDirection(-speed);
                    move();
                }
                if(e.getKeyCode() == KeyEvent.VK_DOWN) {
                    setDirection(speed);
                    move();
                }
                break;
        }

    }
    public void keyReleased(KeyEvent e) {

    }

    public void setDirection(int direction) {
        yVelocity = direction;

    }

    public void move (){
        y = y +  yVelocity;

    }

    public void draw (Graphics g) {
        if (id == 1) {
            g.setColor(Color.RED);
        }else if (id == 2) {
            g.setColor(Color.BLACK);
        }
        g.fillRect(x,y,width,height);

    }
}
