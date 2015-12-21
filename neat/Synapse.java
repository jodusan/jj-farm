package neat;

public class Synapse
{
    public Neuron neuron;
    public double weight;

    /**
     *
     *
     * @param neuron
     * Pointer to next neuron
     * @param weight
     * Synapse weight
     * */
    public Synapse(Neuron neuron, double weight) {
        this.neuron = neuron;
        this.weight = weight;
    }
}
