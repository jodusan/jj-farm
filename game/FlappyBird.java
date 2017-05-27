package game;

import engine.GameFrame;

import java.awt.*;
import java.util.ArrayList;

public class FlappyBird extends GameFrame {

    ArrayList<Bird> birdsList = new ArrayList<>();
    Tube tubes = new Tube();
    Ground ground = new Ground();

    public FlappyBird() {

        for (int i = 0; i < Resources.NO_OF_BIRDS; i++) {
            birdsList.add(new Bird());
        }

        Resources.getInstance();

        startThread();
    }

    public void resetLevel() {
        tubes = new Tube();
        ground = new Ground();
        Resources.reset();

        /* Sets up an anonimous sort function and sorts the array of birds accourding to fitness */
        birdsList.sort((bird1, bird2) -> ((Integer) bird2.fitness).compareTo((Integer) bird1.fitness));

        for (int i = 0; i < Resources.NO_OF_BIRDS / 2; i++) {
            birdsList.get(i).reset();
            birdsList.get(i + Resources.NO_OF_BIRDS / 2).reset();
            birdsList.get(i + Resources.NO_OF_BIRDS / 2).setBirdNetwork(birdsList.get(i).getBirdNetwork().copy());
            birdsList.get(i).birdNetwork.mutateChangeWeights();
            birdsList.get(i).birdNetwork.mutate();
        }
        Resources.NO_OF_BIRDS_ALIVE = Resources.NO_OF_BIRDS;
    }

    @Override
    public void render(Graphics2D g) {

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.drawImage(Resources.BACKGROUND_IMAGE, 0, 0, 1280, 720, null);

        for (Bird b : birdsList)
            b.render(g);

        tubes.render(g);
        ground.render(g);
        g.drawString("Birds alive: " + Resources.NO_OF_BIRDS_ALIVE + " Fitness: " + Resources.fitnessPillars, 50, 50);
    }

    @Override
    public void update() {

        for (Bird b : birdsList)
            b.update();

        if (Resources.NO_OF_BIRDS_ALIVE <= 0) resetLevel();
        tubes.update();
        ground.update();

    }

    public static void main(String[] args) {
        FlappyBird game = new FlappyBird();
        game.setDoubleBuffered(true);
        game.initGameWindow();
    }


}
