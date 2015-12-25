package neat5000;

public class Synapse implements Cloneable {
    private Neuron sourceNeuron;
    private Neuron destinationNeuron;
    private double weight;

    public Synapse(Neuron sourceNeuron, Neuron destinationNeuron, double weight) {
        this.sourceNeuron = sourceNeuron;
        this.destinationNeuron = destinationNeuron;
        this.weight = weight;
    }


    public Neuron getSource() {
        return this.sourceNeuron;
    }

    public Neuron getDestination() { return this.destinationNeuron; }

    public double getWeight() {
        return this.weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setSourceNeuron(Neuron sourceNeuron) {
        this.sourceNeuron = sourceNeuron;
    }

    public void setDestinationNeuron(Neuron destinationNeuron) {
        this.destinationNeuron = destinationNeuron;
    }
}
