package neat;

/**
 * Created by Nikola on 12/17/2015.
 */
public class Synapse
{
    public Neuron neuron;
    public double weight;

    public Synapse(Neuron neuron, double weight) {
        this.neuron = neuron;
        this.weight = weight;
    }
}
