package neat;

/**
 * Created by Nikola on 12/17/2015.
 */
public class Mutation
{
    NeuralNetwork network;
    public void addRandomSynapse()
    {
        Neuron neuron1 = network.neurons.get((int)Math.random()*network.neurons.size());
        Neuron neuron2 = network.neurons.get((int)Math.random()*network.neurons.size());
        Synapse sin=new Synapse(neuron2,Math.random());
        neuron1.outputs.add(sin);
        network.synapses.add(sin);
    }

    public void addRandomInbeetwenNeuron()
    {
        Neuron firstNeuron = network.neurons.get((int)Math.random()*network.neurons.size());
        Synapse secondNeuronSynapse = firstNeuron.outputs.get((int)Math.random()*firstNeuron.outputs.size());
        Neuron addedNeuron = new Neuron();
        

    }
}
