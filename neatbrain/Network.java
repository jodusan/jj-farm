package neatbrain;

/*
    Super TODO: Srediti ceo kod da bude lepo napisano i zakomentarisano :D
    Neke ideje povadjene odavdije:
    https://github.com/vivin/DigitRecognizingNeuralNetwork/blob/master/src/main/java/net/vivin/neural/NeuralNetwork.java

    inputees: oni na ciji output moze da se nadoveze
    outpuees: oni na ciji ulaz moze da se nadoveze
    multiputees: oni koji mogu i jedno i drugo :D
 */

import java.util.ArrayList;
import java.util.Random;

public class Network {
    private Random randomGen;
    private Neuron sensor1 = new Neuron();
    private Neuron sensor2 = new Neuron();
    private Neuron sensor3 = new Neuron();
    private ArrayList<Neuron> inputees = new ArrayList<Neuron>();
    private ArrayList<Neuron> outputees = new ArrayList<Neuron>();
    private ArrayList<Neuron> multiputees = new ArrayList<Neuron>();
    private Neuron actuator = new Neuron();

    public Network() {
        inputees.add(sensor1);
        inputees.add(sensor2);
        inputees.add(sensor3);
        outputees.add(actuator);
    }

    public void setInputs(double[] input) {
        if(input != null) {
            sensor1.setOutput(input[0]);
            sensor2.setOutput(input[1]);
            sensor3.setOutput(input[2]);
        }
    }

    public double getOutput() {
        return actuator.getOutput();
    }

    public void mutateAddNeuron() {
        Synapse synIn = new Synapse(getRandom(inputees),randomGen.nextDouble());
        Neuron temp = new Neuron();
        temp.addInput(synIn);
        inputees.add(temp);
        outputees.add(temp);
        multiputees.add(temp);
        Synapse synOut = new Synapse(temp, randomGen.nextDouble());
        getRandom(outputees).addInput(synOut);
    }

    public void mutateRemoveNeuron() {
        if(multiputees.size() != 0) {
            int index = randomGen.nextInt(multiputees.size());
            Neuron temp = multiputees.get(index);
            outputees.remove(temp);
            inputees.remove(temp);
            multiputees.remove(temp);
        }
    }

    public void mutateChangeWeights() {
        if(multiputees.size() != 0) {
            int index = randomGen.nextInt(multiputees.size());
            Neuron temp = multiputees.get(index);
            for(Synapse s : temp.getInputs()) {
                s.setWeight(randomGen.nextDouble());
            }
        }
    }

    public void mutate() {
        if(randomGen.nextDouble() > 0.4) {
            int mutationType = randomGen.nextInt(3);
            if (mutationType == 0)
                mutateAddNeuron();
            else if (mutationType == 1)
                mutateRemoveNeuron();
            else if (mutationType == 2)
                mutateChangeWeights();
        }
    }

    public Neuron getRandom(ArrayList<Neuron> neuronList) {
        int index = randomGen.nextInt(neuronList.size());
        Neuron temp = neuronList.get(index);
        return temp;
    }

}
