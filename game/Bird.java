package game;

import java.awt.*;

public class Bird {
    int xPos;
    int yPos;
    boolean dead;
    double velocity;
    double angle;

    public void render(Graphics2D g) {
        g.setColor(Color.RED);
        g.fillRect(20,20,50,50);
    }

    public void update() {

    }
}
