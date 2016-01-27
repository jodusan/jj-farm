/*---------------------------------------------------------
 * Class that has all needed assets, and some variables
 * ready-to-use. If config file should be used for settings
 * this class would be the one to read it and configure
 * all options.
 *---------------------------------------------------------*/
package game;


import neat.Neuron;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

public class Resources {

    /* Singleton */
    private static Resources instance = null;

    public static BufferedImage BACKGROUND_IMAGE;
    public static BufferedImage GROUND_IMAGE;
    public static BufferedImage TUBE_UP_IMAGE;
    public static BufferedImage TUBE_DOWN_IMAGE;
    public static BufferedImage[] BIRD_IMAGES;


    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final int BIRD_WIDTH = 44;
    public static final int BIRD_HEIGHT = 36;
    public static final int FLOOR_WIDTH = 672;
    public static final int FLOOR_HEIGHT = 224;
    public static final int FLOOR_OFFSET = 96;
    public static final int FLOOR_SPEED = 5;
    public static final int TUBE_WIDTH = 104;
    public static final int TUBE_HEIGHT = 640;
    public static final int TUBE_APERTURE = 200;
    public static final int TUBE_GAP_DISTANCE = 160;
    public static final int BIRD_X_POSITION = 100;
    public static final int BIRD_Y_POSITION = 100;
    public static final int NO_OF_BIRDS = 200;
    public static final boolean DEBUG = false;

    public static boolean IN_TUBE = false;
    public static Point CURRENT_TUBE = new Point(0, 0);

    public static HashMap<Neuron,Double> visitedNeurons=new HashMap<>();
    public static Point nextTube=new Point(0,0);

    public static int fitnessPillars =0;
    public static int NO_OF_BIRDS_ALIVE = 200;

    static {
        try {
            BACKGROUND_IMAGE = upscale(ImageIO.read(new File("assets/bg.png")));
            GROUND_IMAGE = upscale(ImageIO.read(new File("assets/ground.png")));
            final BufferedImage birdImage = ImageIO.read(new File("assets/bird.png"));
            BIRD_IMAGES = new BufferedImage[]{
                    upscale(birdImage.getSubimage(0, 0, 36, 26)),
                    upscale(birdImage.getSubimage(36, 0, 36, 26)),
                    upscale(birdImage.getSubimage(72, 0, 36, 26))};
            TUBE_UP_IMAGE = upscale(ImageIO.read(new File("assets/tube1.png")));
            TUBE_DOWN_IMAGE = upscale(ImageIO.read(new File("assets/tube2.png")));
        } catch (Exception e) {
            System.out.println("[ERROR] Cannot load resources!");
        }
    }

    protected Resources() {

    }

    public static Resources getInstance() {
        if (instance == null) {
            instance = new Resources();
        }
        return instance;
    }

    public static void reset()
    {
        fitnessPillars=0;
        nextTube=new Point(0,0);
        visitedNeurons=new HashMap<>();
        CURRENT_TUBE = new Point(0, 0);
        IN_TUBE = false;
    }

    /*---------------------------------------------------------
     * Explanation needed
     *---------------------------------------------------------*/
    private static BufferedImage toBufferedImage(final Image image) {
        final BufferedImage buffered = new BufferedImage(image.getWidth(null),
                image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        buffered.getGraphics().drawImage(image, 0, 0, null);
        return buffered;
    }

    /*---------------------------------------------------------
     * Explanation needed
     *---------------------------------------------------------*/
    private static BufferedImage upscale(final Image image) {
        return toBufferedImage(image.getScaledInstance(image.getWidth(null) * 2,
                image.getHeight(null) * 2, Image.SCALE_FAST));
    }

}
