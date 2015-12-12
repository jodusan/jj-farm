package game;

import javafx.scene.transform.Affine;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Bird {
    int xPos=100;
    int yPos=160;
    int currImg=0;
    int delay=0;
    int jumpDuration=3;
    double imgScale=1.5;
    boolean dead;
    boolean flapReady=false;
    double jumpVelocity = 3;
    double velocity=-5;
    double angle;
    BufferedImage birdImage=null;
    BufferedImage birdImages[]=null;

    Bird()
    {

        try {
            birdImage = ImageIO.read(new File("assets/bird.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        birdImages = new BufferedImage[]
                {
                        upscale(birdImage.getSubimage(0,0,36,26)),
                        upscale(birdImage.getSubimage(36, 0, 36, 26)),
                        upscale(birdImage.getSubimage(72,0,36,26))
                };

        birdImages[0] = toBufferedImage(birdImages[0].getScaledInstance((int)(birdImages[0].getWidth()/imgScale),(int)(birdImages[0].getHeight()/imgScale),1));
        birdImages[1] = toBufferedImage(birdImages[1].getScaledInstance((int)(birdImages[1].getWidth()/imgScale),(int)(birdImages[1].getHeight()/imgScale),1));
        birdImages[2] = toBufferedImage(birdImages[2].getScaledInstance((int)(birdImages[2].getWidth()/imgScale),(int)(birdImages[2].getHeight()/imgScale),1));

    }
    double lerp(double a, double b, double f)
    {
        return a + f * (b - a);
    }

    public void render(Graphics2D g)
    {

        AffineTransform at = new AffineTransform();
        at.translate(xPos,yPos);
        at.rotate(lerp(Math.PI/2-0.3,-Math.PI/2,(((double)velocity)+15)/21));
        at.translate(-birdImages[0].getWidth()/2,-birdImages[0].getHeight()/2);

        if(velocity>0)
        {

            g.drawImage(birdImages[currImg],at,null);
            if(delay>2)
            {
                currImg=(currImg+1)%3;
                delay=0;
            }
            delay++;
        }
        else g.drawImage(birdImages[1],at,null);
    }

    public void update() {

        if(velocity>=-15)
            velocity-=1;
        if(velocity>0)
        {
            yPos-=jumpVelocity*velocity;

        }
        else
        {
            //velocity=-10;
            yPos-=velocity;
        }

    }
    public void readyFlap()
    {
        flapReady=true;
    }

    public void flap()
    {
        if(flapReady)
        {
            //jumpDuration=5;
            velocity=6;
            flapReady=false;
        }
    }

    private static BufferedImage upscale(final Image image) {
        return toBufferedImage(image.getScaledInstance(image.getWidth(null) * 2,
                image.getHeight(null) * 2, Image.SCALE_FAST));
    }

    private static BufferedImage toBufferedImage(final Image image) {
        final BufferedImage buffered = new BufferedImage(image.getWidth(null),
                image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        buffered.getGraphics().drawImage(image, 0, 0, null);
        return buffered;
    }

}
