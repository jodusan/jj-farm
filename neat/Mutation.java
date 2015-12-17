package neat;

public class Mutation
{
    NeuralNetwork network;

    /**
     * Selects a random neuron, which then randomly selects and adds either an
     * input or output synaptic link to another randomly selected element (neuron,
     * sensor, or actuator) in the NN system.
     */
    public void addSynapticConnection()
    {
        Neuron neuron1 = network.neurons.get((int)(Math.random()*network.neurons.size()));
        Neuron neuron2 = network.neurons.get((int)(Math.random()*network.neurons.size()));
        Synapse sin=new Synapse(neuron2,Math.random());
        neuron1.outputs.add(sin);
        network.synapses.add(sin);
    }

    /**
     *  Chooses a random neuron, then a random element that the neuron is connected to,
     *  disconnects the two, and then reconnect them through a newly created neuron.
     */
    public void splice() // add Random Neuron between Two Neurons
    {
        Neuron firstNeuron = network.neurons.get((int)(Math.random()*network.neurons.size()));
        Synapse secondNeuronSynapse = firstNeuron.outputs.get((int)(Math.random()*firstNeuron.outputs.size()));
        Neuron addedNeuron = new Neuron(); Neuron secondNeuron = secondNeuronSynapse.neuron;
        Synapse addedNeuronSynapse = new Synapse(secondNeuron,Math.random());
        addedNeuron.outputs.add(addedNeuronSynapse);
        secondNeuronSynapse.neuron=addedNeuron;
        addedNeuron.inputs.add(secondNeuronSynapse);
        secondNeuron.inputs.remove(secondNeuronSynapse);
        secondNeuron.inputs.add(addedNeuronSynapse);
    }

    /**
     * Generates a new neuron, and connects it to a random postsynaptic neuron
     * in the NN, and a random presynaptic neuron in the NN.
     */
    public void addNeuron()
    {
        Neuron addedNeuron = new Neuron();
        Neuron firstNeuron = network.neurons.get((int)(Math.random()*network.neurons.size()));
        Neuron secondNeuron = network.neurons.get((int)(Math.random()*network.neurons.size()));

        Synapse firstSynapse = new Synapse(addedNeuron,Math.random());
        firstNeuron.outputs.add(firstSynapse);
        addedNeuron.inputs.add(firstSynapse);

        Synapse secondSynapse = new Synapse(secondNeuron,Math.random());
        secondNeuron.inputs.add(secondSynapse);
        addedNeuron.outputs.add(secondSynapse);
    }

    /**
     * Chooses a random neuron without a bias, and adds a bias to its weights list.
     */
    public void addBias()
    {
        Neuron firstNeuron = network.neurons.get((int)(Math.random()*network.neurons.size()));
        firstNeuron.bias=true;
    }
}
