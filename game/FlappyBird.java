package game;

import engine.GameFrame;

import java.awt.*;

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

    Bird testPtica;

    public FlappyBird(String title, int sizeX, int sizeY) {
        super(title, sizeX, sizeY);
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
    public void render(Graphics2D g, int sw, int sh) {
        testPtica.render(g);
    }

    @Override
    public void update() {

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

    }

    @Override
    public void handleKeyUp(int keyCode) {

    }

    public static void main(String[] args) {
        FlappyBird game = new FlappyBird("JJFarms finest chicken", 1024, 768);
        game.initGameWindow();
    }
}
