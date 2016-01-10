package neat;


import game.Resources;

import java.util.ArrayList;

public class Neuron {
    private ArrayList<Synapse> inputs = new ArrayList<>();
    private ArrayList<Synapse> outputs = new ArrayList<>();
    private double weightedSum;
    private double output;

    public Neuron() {
        weightedSum=0.43;
        inputs = new ArrayList<>();
    }


    public double activate() {
        weightedSum = 0;
        for (Synapse synapse : inputs) {
            System.out.println("Synapse source"+synapse.getSource());
            System.out.println("Synapse dest"+synapse.getDestination());
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
        System.out.println("Dodao se input " + input.getSource() + " " + input.getDestination());
        System.out.println(inputs);
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
