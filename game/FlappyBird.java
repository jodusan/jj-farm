package game;

import engine.GameFrameBolji;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

class SayHello extends TimerTask {
    Bird birdie;
    Random p;
    SayHello(Bird birdie) {
        this.birdie = birdie;
        p = new Random();
    }
    public void run() {
        int a = p.nextInt(2);
        if(true) {
            this.birdie.flap();
        }
    }
}
public class FlappyBird extends GameFrameBolji {


    Bird testPtica;
    Tube tubes = new Tube();
    Ground ground = new Ground();
    Timer timer;
    Resources res;
    public FlappyBird(int sizeX, int sizeY) {
        super("JJFarms finest chicken", 1280, 720);
        testPtica = new Bird();
        Resources.getInstance();

        startThread();
        Timer timer = new Timer();
        timer.schedule(new SayHello(testPtica), 0, 278);
    }

    @Override
    public void handleWindowInit() {

    }

    @Override
    public void handleWindowDestroy() {

    }

    @Override
    public void render(Graphics2D g, int sw, int sh)
    {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.drawImage(Resources.BACKGROUND_IMAGE, 0, 0, 1280, 720 , null);
        testPtica.render(g);
        tubes.render(g);
        ground.render(g);
    }

    @Override
    public void update() {
        testPtica.update();
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
        if(keyCode == KeyEvent.VK_SPACE) {
            testPtica.readyFlap();
        }
    }

    @Override
    public void handleKeyUp(int keyCode) {
        if(keyCode == KeyEvent.VK_SPACE) {
            testPtica.flap();
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
