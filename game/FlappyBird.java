package game;

import engine.GameFrame;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Main Game class
 */
public class FlappyBird extends GameFrame {

    private Bird testBird;
    private Tube tube1 = new Tube();

    private FlappyBird() {
        super("JJFarms finest chicken", 1024, 420);
        setHighQuality(true);
        testBird = new Bird();
        startThread();

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
        testBird.render(g);
        tube1.render(g);
    }

    @Override
    public void update() {
        testBird.update();
        tube1.update();
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
        if(keyCode == KeyEvent.VK_SPACE)
        {
            testBird.flap();
        }
    }

    @Override
    public void handleKeyUp(int keyCode) {
        if(keyCode == KeyEvent.VK_SPACE)
        {
            testBird.readyFlap();
        }
    }

    public static void main(String[] args) {
        FlappyBird game = new FlappyBird();
        game.setHighQuality(true);
        game.setDoubleBuffered(true);
        game.initGameWindow();
    }
}
