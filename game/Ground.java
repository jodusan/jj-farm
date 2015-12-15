package game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Ground {

    int groundTextures = 3;
    int xPos[]=new int[groundTextures];



    private BufferedImage ground = Resources.GROUND_IMAGE;

    public Ground()
    {
        for(int i=0; i<groundTextures; i++) {
            xPos[i]=i*Resources.FLOOR_WIDTH;
        }
    }

    public void render(Graphics2D g) {
        for(int i=0; i<groundTextures; i++) {
            g.drawImage(ground, xPos[i], Resources.HEIGHT - 100, Resources.FLOOR_WIDTH, Resources.FLOOR_HEIGHT, null);
        }
    }

    public void update() {

        for(int i=0;i<groundTextures;i++){
            xPos[i]-=3;
            if(xPos[i]<=-Resources.FLOOR_WIDTH)
            {
                xPos[i]=1280;
            }
        }

    }
}
