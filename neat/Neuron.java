package neat;


import game.Resources;

import java.util.ArrayList;

public class Neuron {
    private ArrayList<Synapse> inputs = new ArrayList<>();
    private ArrayList<Synapse> outputs = new ArrayList<>();
    double weightedSum;
    private double output;

    public Neuron() {
        weightedSum=0.65;
        inputs = new ArrayList<>();
    }


    public double activate() {
        weightedSum = 0;
        for (Synapse synapse : inputs) {
            if(!Resources.visitedNeurons.containsKey(synapse.getSource()))
            {
                Resources.visitedNeurons.put(synapse.getSource(),1.0);
                weightedSum += synapse.getWeight() * synapse.getSource().activate();
            }
        }
        if(inputs.size()==0) return output;
        return sigmoid(weightedSum);
    }

    public static double sigmoid(double weight) {
        return 1.0 / (1 + Math.exp(-1.0 * weight));
    }

    public void addInput(Synapse input) {
        inputs.add(input);
    }

    public void addOutput(Synapse output) {
        outputs.add(output);
    }

    public ArrayList<Synapse> getInputs() {
        return this.inputs;
    }

    public ArrayList<Synapse> getOutputs() {
        return this.outputs;
    }

    public double getOutput() {
        return this.output;
    }

    /* Use this for sensors */
    public void setOutput(double output) {
        this.output = output;
    }


}
