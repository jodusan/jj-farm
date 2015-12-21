package neatbrain;


import java.util.ArrayList;
import java.util.List;

public class Neuron {
    private List<Synapse> inputs;
    private double weightedSum;
    private double output;

    public Neuron() {
        inputs = new ArrayList<Synapse>();
    }

    public void addInput(Synapse input) {
        inputs.add(input);
    }

    public List<Synapse> getInputs() {
        return this.inputs;
    }

    public double getOutput() {
        return this.output;
    }

    /* Use this for sensors */
    public void setOutput(double output) {
        this.output = output;
    }

    public void calculateWeightedSum() {
        weightedSum = 0;
        for(Synapse synapse : inputs) {
            weightedSum += synapse.getWeight() * synapse.getSourceNeuron().getOutput();
        }
    }

    public void activate() {
        calculateWeightedSum();
        output = sigmoid(weightedSum);
    }

    public static double sigmoid(double weight) {
        return 1.0 / (1+Math.exp(-1.0 * weight));
    }

}
