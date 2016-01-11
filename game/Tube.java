package game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;


public class Tube {
    int pillarNum = 6;
    int xPos[] = new int[pillarNum];
    int yPos[] = new int[pillarNum];
    int closestTube=0;
    int gapDistance = Resources.TUBE_GAP_DISTANCE;
    int offset = 300;
    private BufferedImage pillar1 = Resources.TUBE_UP_IMAGE;
    private BufferedImage pillar2 = Resources.TUBE_DOWN_IMAGE;

    private int randY() {
        Random r = new Random();
        return r.nextInt(300) - 560;
    }

    public Tube() {

        for (int i = 0; i < pillarNum; i++) {
            xPos[i] = 1024 + i * offset;
            yPos[i] = randY();
        }

    }

    public void render(Graphics2D g) {
        for (int i = 0; i < pillarNum; i++) {
            g.drawImage(pillar1, xPos[i], yPos[i], Resources.TUBE_WIDTH, Resources.TUBE_HEIGHT, null);
            g.drawImage(pillar2, xPos[i], yPos[i] + Resources.TUBE_HEIGHT + gapDistance, Resources.TUBE_WIDTH, Resources.TUBE_HEIGHT, null);
        }
    }

    public void update() {
        boolean InTube = false;
        Resources.fitnessPillars++;
        Resources.nextTube.x=xPos[closestTube];
        Resources.nextTube.y=yPos[closestTube];
        for (int i = 0; i < pillarNum; i++) {
            xPos[i] -= 3;
            if(xPos[closestTube]<Resources.BIRD_X_POSITION)
            {
                closestTube=(closestTube+1)%pillarNum;
                Resources.nextTube=new Point(xPos[closestTube],yPos[closestTube]);
            }
            int leftBird = Resources.BIRD_X_POSITION;
            int rightBird = Resources.BIRD_X_POSITION + Resources.BIRD_WIDTH;
            int leftTube = xPos[i];
            int rightTube = xPos[i] + Resources.TUBE_WIDTH;
            if ((leftBird <= rightTube && leftBird >= leftTube) || (rightBird <= rightTube && rightBird >= leftTube)) {
                Resources.CURRENT_TUBE.setLocation(xPos[i], yPos[i]);
                Resources.IN_TUBE = true;
                InTube = true;
            }
            if (xPos[i] <= -(300 - Resources.TUBE_WIDTH)) {
                xPos[i] = 1605;
                yPos[i] = randY();
            }
        }
        if (!InTube) {
            Resources.IN_TUBE = false;
        }
    }
}
