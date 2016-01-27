package game;

import engine.GameFrame;
import neat.Network;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

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
    public Network rouletteWheelSelection(int fitnessSum)
    {
        Random rand = new Random();

        int total=0;
        int randomSlice = rand.nextInt(fitnessSum);
        for(Bird b: birdsList)
        {
            total+=b.fitness;
            if(total>randomSlice)
            {
                return b.getBirdNetwork();
            }

        }
        return null;
    }
    public void resetLevel() {
        tubes = new Tube();
        ground = new Ground();
        Resources.reset();

        /* Sets up an anonimous sort function and sorts the array of birds accourding to fitness */
        //birdsList.sort((bird1, bird2) -> ((Integer) bird2.fitness).compareTo((Integer) bird1.fitness));

        int fitnessSum=0;

        for(Bird b : birdsList) {
            fitnessSum += b.fitness;
        }
        ArrayList<Network> newNetworkList = new ArrayList<>();
        for (int i = 0; i < Resources.NO_OF_BIRDS; i++) {
            newNetworkList.add(rouletteWheelSelection(fitnessSum).copy());
        }
        for (int i =0; i< Resources.NO_OF_BIRDS;i++)
        {
            birdsList.get(i).reset();
            birdsList.get(i).setBirdNetwork(newNetworkList.get(i));
            //birdsList.get(i).birdNetwork.mutateChangeWeights();
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
        g.drawString("Birds alive: " + Resources.NO_OF_BIRDS_ALIVE + " Columns passed: " + Resources.fitnessPillars, 50, 50);
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
