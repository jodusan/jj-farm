package neatbrain;

public class Synapse {
    private Neuron sourceNeuron;
    private double weight;

    public Synapse(Neuron sourceNeuron, double weight) {
        this.sourceNeuron = sourceNeuron;
        this.weight = weight;
    }

    public Neuron getSourceNeuron() {
        return this.sourceNeuron;
    }

    public double getWeight() {
        return this.weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
