package game;

import engine.GameFrameBolji;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FlappyBird extends GameFrameBolji {

    int birdsAlive = Resources.NO_OF_BIRDS;
    ArrayList<Bird> testPtica = new ArrayList<>();
    Tube tubes = new Tube();
    Ground ground = new Ground();

    public FlappyBird(int sizeX, int sizeY) {
        super("JJFarms finest chicken", 1280, 720);

        for (int i = 0; i < Resources.NO_OF_BIRDS; i++) {
            testPtica.add(new Bird());
        }

        Resources.getInstance();

        startThread();
    }

    public void resetLevel()
    {
        tubes = new Tube();
        ground = new Ground();
        Resources.reset();
        testPtica.sort((b1,b2) -> ((Integer)b1.fitness).compareTo((Integer)b2.fitness));
        for(int i = 0;i<Resources.NO_OF_BIRDS/2;i++)
        {
            testPtica.get(i).reset();
            testPtica.get(i+Resources.NO_OF_BIRDS/2).reset();
            testPtica.get(i+Resources.NO_OF_BIRDS/2).setBirdNetwork(testPtica.get(i).getBirdNetwork().copy());
        }
        birdsAlive = Resources.NO_OF_BIRDS;
    }

    @Override
    public void handleWindowInit() {

    }

    @Override
    public void handleWindowDestroy() {

    }

    @Override
    public void render(Graphics2D g, int sw, int sh) {

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.drawImage(Resources.BACKGROUND_IMAGE, 0, 0, 1280, 720, null);
        for (Bird b : testPtica) b.render(g);

        tubes.render(g);
        ground.render(g);
        g.drawString("Birds alive: " +birdsAlive+" Columns passed: "+Resources.fitnessPillars, 50,50);
    }

    @Override
    public void update() {
        int tempBirdAlive = Resources.NO_OF_BIRDS;
        for (Bird b : testPtica) {
            if (b.dead) tempBirdAlive--;
            b.update();
        }
        birdsAlive = tempBirdAlive;
        if(birdsAlive==0) resetLevel();
        tubes.update();
        ground.update();

    }

    @Override
    public void handleMouseDown(int x, int y, GFMouseButton button) {

    }

    @Override
    public void handleMouseUp(int x, int y, GFMouseButton button) {

    }

    @Override
    public void handleMouseMove(int x, int y) {

    }

    @Override
    public void handleKeyDown(int keyCode) {
        if (keyCode == KeyEvent.VK_SPACE) {
            for (Bird b : testPtica) b.readyFlap();
        }
    }

    @Override
    public void handleKeyUp(int keyCode) {
        if (keyCode == KeyEvent.VK_SPACE) {
            for (Bird b : testPtica) b.flap();
        }
    }


    //    @Override
//    public void handleKeyDown(int keyCode) {
//        if(keyCode == KeyEvent.VK_SPACE)
//        {
//            testPtica.flap();
//        }
//    }
//
//    @Override
//    public void handleKeyUp(int keyCode) {
//        if(keyCode == KeyEvent.VK_SPACE)
//        {
//            testPtica.readyFlap();
//        }
//    }
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

    public static void main(String[] args) {
        FlappyBird game = new FlappyBird(1280, 720);
        game.setUpdateRate(60);
        game.setDoubleBuffered(true);
        game.initGameWindow();
    }
}
