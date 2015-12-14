package game;

import engine.GameFrame;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by dusan on 12/8/15.
 */
public class FlappyBird extends GameFrame {
    /**
     * Konstruktor za GameFrame, koji se mora pozvati iz naslijeđenih klasa
     *
     * @param title naslov prozora
     * @param sizeX širina u pikselima
     * @param sizeY visina u pikselima
     */

    private Bird testPtica;
    private Tube tube1 = new Tube();

    private FlappyBird(String title, int sizeX) {
        super("JJFarms finest chicken", 1024, 420);
        setHighQuality(true);
        testPtica = new Bird();
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
        testPtica.render(g);
        tube1.render(g);
    }

    @Override
    public void update() {
        testPtica.update();
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
            testPtica.flap();
        }
    }

    @Override
    public void handleKeyUp(int keyCode) {
        if(keyCode == KeyEvent.VK_SPACE)
        {
            testPtica.readyFlap();
        }
    }

    public static void main(String[] args) {
        FlappyBird game = new FlappyBird("JJFarms finest chicken", 1024);
        game.setHighQuality(true);
        game.setDoubleBuffered(true);
        game.initGameWindow();
    }
}
