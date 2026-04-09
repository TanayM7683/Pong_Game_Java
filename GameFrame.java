package Pong;


//imports
import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    GamePanel Panel = new GamePanel();

    //constructor
    GameFrame() {
        Panel = new GamePanel();
        this.add(Panel);
        this.setTitle("Pong Game");
        this.setResizable(false);
        this.setBackground(Color.blue);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);

    };
}
