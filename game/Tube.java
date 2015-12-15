package game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;


public class Tube {
    int pillarNum=4;
    int xPos[]=new int[pillarNum];
    int yPos[]=new int[pillarNum];

    int gapDistance=160;
    int offset=400;



    private BufferedImage pillar1 = Resources.TUBE_UP_IMAGE;
    private BufferedImage pillar2 = Resources.TUBE_DOWN_IMAGE;
    private BufferedImage ground = Resources.GROUND_IMAGE;

    private int randY()
    {
        Random r = new Random();
        return r.nextInt(220)-280;
    }

    public Tube()
    {
        for(int i=0;i<pillarNum;i++)
        {
            xPos[i]=1024+i*offset;
            yPos[i]=randY();
        }
    }

    public void render(Graphics2D g) {
        for(int i=0;i<pillarNum;i++)
        {
            g.drawImage(pillar1, xPos[i], yPos[i], Resources.TUBE_WIDTH, Resources.TUBE_HEIGHT, null);
            g.drawImage(pillar2, xPos[i], yPos[i] + Resources.TUBE_HEIGHT + gapDistance, Resources.TUBE_WIDTH, Resources.TUBE_HEIGHT, null);
        }
        g.drawImage(ground, 0, Resources.HEIGHT - 100, Resources.FLOOR_WIDTH, Resources.FLOOR_HEIGHT, null);
        g.drawImage(ground, Resources.FLOOR_WIDTH - 1, Resources.HEIGHT - 100, Resources.FLOOR_WIDTH, Resources.FLOOR_HEIGHT, null);
    }

    public void update() {

        for(int i=0;i<pillarNum;i++){
            xPos[i]-=1;
            if(xPos[i]<=-176)
            {
                xPos[i]=1024;
                yPos[i]=randY();
            }
        }

    }
}
