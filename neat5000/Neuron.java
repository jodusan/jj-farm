package neat5000;


import java.util.ArrayList;

public class Neuron {
    private ArrayList<Synapse> inputs;
    private ArrayList<Synapse> outputs;
    private double weightedSum;
    private double output;

    public Neuron() {
        inputs = new ArrayList<Synapse>();
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

    public void addInput(Synapse input) { inputs.add(input);  }

    public void addOutput(Synapse output) { outputs.add(output); }

    public ArrayList<Synapse> getInputs() {
        return this.inputs;
    }

    public ArrayList<Synapse> getOutputs() { return this.outputs; }

    public double getOutput() {
        return this.output;
    }

    /* Use this for sensors */
    public void setOutput(double output) {
        this.output = output;
    }


}
