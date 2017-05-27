package game;


import neat.Network;
import neat.Neuron;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Bird {

    Network birdNetwork;
    int xPos = Resources.BIRD_X_POSITION;
    int yPos = Resources.BIRD_Y_POSITION;
    int currImg = 0;
    int delay = 0;
    double imgScale = 1.5;
    boolean dead;
    double jumpVelocity = 3;
    double velocity = -2.5;
    int fitness =0;

    BufferedImage birdImage = null;
    BufferedImage birdImages[] = null;

    Bird() {

        birdNetwork = new Network();
        setupNetwork();

        try {
            birdImage = ImageIO.read(new File("assets/bird.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        birdImages = new BufferedImage[]{
                upscale(birdImage.getSubimage(0, 0, 36, 26)),
                upscale(birdImage.getSubimage(36, 0, 36, 26)),
                upscale(birdImage.getSubimage(72, 0, 36, 26))
        };

        birdImages[0] = toBufferedImage(birdImages[0].getScaledInstance(
                        (int) (birdImages[0].getWidth() / imgScale),
                        (int) (birdImages[0].getHeight() / imgScale),
                        1)
        );

        birdImages[1] = toBufferedImage(birdImages[1].getScaledInstance((int) (birdImages[1].getWidth() / imgScale), (int) (birdImages[1].getHeight() / imgScale), 1));
        birdImages[2] = toBufferedImage(birdImages[2].getScaledInstance((int) (birdImages[2].getWidth() / imgScale), (int) (birdImages[2].getHeight() / imgScale), 1));

    }

    double lerp(double a, double b, double f) {
        return a + f * (b - a);
    }

    public void reset()
    {
        yPos=Resources.BIRD_Y_POSITION;
        dead=false;
        velocity=0;
    }

    public void setupNetwork()
    {
        ArrayList<Neuron> inputs = new ArrayList<>();
        inputs.add(new Neuron());
        inputs.add(new Neuron());
        inputs.add(new Neuron());
        birdNetwork.setSensors(inputs);
        double inputValues[] = new double[]{0.5, 0.5, 0.5};
        birdNetwork.setInputValues(inputValues);
    }

    public void render(Graphics2D g) {
        if (dead) return;
        AffineTransform at = new AffineTransform();
        at.translate(xPos, yPos);
        at.rotate(lerp(Math.PI / 2 - 0.3, -Math.PI / 2, ((velocity) + 15) / 21));
        at.translate(-birdImages[0].getWidth() / 2, -birdImages[0].getHeight() / 2);

        /* Bird animation when flapping */
        if (velocity > 0) {
            g.drawImage(birdImages[currImg], at, null);
            if (delay > 2) {
                currImg = (currImg + 1) % 3;
                delay = 0;
            }
            delay++;
        } else g.drawImage(birdImages[1], at, null);
    }

    public void update() {

        if (dead) return;

        double birdYPossition = (double)yPos/Resources.HEIGHT;
        double nextTubeHeigth = ((Resources.nextTube.getY() + Resources.TUBE_HEIGHT + Resources.TUBE_GAP_DISTANCE/2)-560+Resources.TUBE_HEIGHT)/(700); // 80-380
        double distanceFromNextTube = ((Resources.nextTube.getX() - Resources.BIRD_X_POSITION)%300)/300;
        double inputs[] = new double[]{birdYPossition-nextTubeHeigth,nextTubeHeigth-birdYPossition,distanceFromNextTube};
        birdNetwork.setInputValues(inputs);

        if (birdNetwork.propagate() > 0.6) {
            velocity = 3;
        }

        if (velocity >= -15) velocity -= 0.5;
        if (velocity > 0) { yPos -= jumpVelocity * velocity; }
        else {
            yPos -= velocity;
        }

        if (yPos > Resources.HEIGHT - 100 || yPos<0) {
            dead = true;
            Resources.NO_OF_BIRDS_ALIVE--;
            fitness=Resources.fitnessPillars;
        }
        if (Resources.IN_TUBE) {
            int leftBird = Resources.BIRD_X_POSITION;
            int rightBird = Resources.BIRD_X_POSITION + Resources.BIRD_WIDTH;
            int leftTube = (int) Resources.CURRENT_TUBE.getX()+50;
            int rightTube = (int) Resources.CURRENT_TUBE.getX() + Resources.TUBE_WIDTH-20;
            if ((leftBird <= rightTube && leftBird >= leftTube) || (rightBird <= rightTube && rightBird >= leftTube)) {
                int topTube = (int) Resources.CURRENT_TUBE.getY() + Resources.TUBE_HEIGHT-15;
                int bottomTube = topTube + Resources.TUBE_GAP_DISTANCE+30;
                int topBird = yPos - Resources.BIRD_HEIGHT / 2;
                int bottomBird = topBird + Resources.BIRD_HEIGHT;
                if (!(topTube < topBird) || !(bottomBird < bottomTube)) {
                    dead = true;
                    Resources.NO_OF_BIRDS_ALIVE--;
                    fitness=Resources.fitnessPillars;
                }
            } else {
                Resources.IN_TUBE = false;
            }
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

    public Network getBirdNetwork() {
        return birdNetwork;
    }

    public void setBirdNetwork(Network birdNetwork) {
        this.birdNetwork = birdNetwork;
        this.birdNetwork.mutate();
    }
}
