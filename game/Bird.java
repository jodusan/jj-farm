package game;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class Bird {
    private int xPos=100;
    private int yPos=160;
    private int currImg=0;
    private int delay=0;
    boolean dead;
    private boolean flapReady=false;
    private double velocity=-5;
    private BufferedImage[] birdImages=null;

    Bird()
    {

        BufferedImage birdImage = null;
        try {
            birdImage = ImageIO.read(new File("assets/bird.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        birdImages = new BufferedImage[]
                {
                        upscale(birdImage.getSubimage(0, 0, 36, 26)),
                        upscale(birdImage.getSubimage(36, 0, 36, 26)),
                        upscale(birdImage.getSubimage(72, 0, 36, 26))
                };

        double imgScale = 1.5;
        birdImages[0] = toBufferedImage(birdImages[0].getScaledInstance((int)(birdImages[0].getWidth()/ imgScale),(int)(birdImages[0].getHeight()/ imgScale),1));
        birdImages[1] = toBufferedImage(birdImages[1].getScaledInstance((int)(birdImages[1].getWidth()/ imgScale),(int)(birdImages[1].getHeight()/ imgScale),1));
        birdImages[2] = toBufferedImage(birdImages[2].getScaledInstance((int)(birdImages[2].getWidth()/ imgScale),(int)(birdImages[2].getHeight()/ imgScale),1));

    }
    private double linearInterpolation(double a, double b, double f)
    {
        return a + f * (b - a);
    }

    public void render(Graphics2D g)
    {

        AffineTransform at = new AffineTransform();
        at.translate(xPos,yPos);
        at.rotate(linearInterpolation(Math.PI / 2 - 0.3, -Math.PI / 2, (velocity + 15) / 21));
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
            double jumpVelocity = 3;
            yPos-= jumpVelocity *velocity;

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
