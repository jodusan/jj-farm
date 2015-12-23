package neat5000;

/**
    Super TODO: Srediti ceo kod da bude lepo napisano i zakomentarisano :D
    Neke ideje povadjene odavdije:
    https://github.com/vivin/DigitRecognizingNeuralNetwork/blob/master/src/main/java/net/vivin/neural/NeuralNetwork.java

    inputees: oni na ciji output moze da se nadoveze
    outpuees: oni na ciji ulaz moze da se nadoveze
    multiputees: oni koji mogu i jedno i drugo :D
 **/



import java.util.ArrayList;
import java.util.Random;

public class Network {
    private Random randomGen;
    private Neuron sensor1 = new Neuron();
    private Neuron sensor2 = new Neuron();
    private Neuron sensor3 = new Neuron();
    private ArrayList<Neuron> inputees = new ArrayList<Neuron>();
    private ArrayList<Neuron> outputees = new ArrayList<Neuron>();
    private ArrayList<Neuron> multiputees = new ArrayList<Neuron>();
    private Neuron actuator = new Neuron();

    public Network() {
        inputees.add(sensor1);
        inputees.add(sensor2);
        inputees.add(sensor3);
        outputees.add(actuator);
    }

    public void setupNetwork(Genome genome) {

    }


    public void setInputs(double[] input) {
        if(input != null) {
            sensor1.setOutput(input[0]);
            sensor2.setOutput(input[1]);
            sensor3.setOutput(input[2]);
        }
    }

    public double getOutput() {
        return actuator.getOutput();
    }

    /**
     *  synIN
     */
    public void mutateAddNeuron() {
        Neuron temp = new Neuron();

        Neuron source = getRandomNeuron(inputees);
        Synapse synIn = new Synapse(source, temp, randomGen.nextDouble());
        source.addOutput(synIn);
        temp.addInput(synIn);

        Neuron destination = getRandomNeuron(outputees);
        Synapse synOut = new Synapse(temp, destination, randomGen.nextDouble());
        destination.addInput(synOut);
        temp.addOutput(synOut);

        inputees.add(temp);
        outputees.add(temp);
        multiputees.add(temp);
    }

    public void mutateSplice() {
        Neuron source = getRandomNeuron(inputees);
        Synapse sourceSynapse = getRandomSynapse(source.getOutputs());
        Neuron destination = sourceSynapse.getDestinationNeuron();

        Neuron temp = new Neuron();

        sourceSynapse.setDestinationNeuron(temp);

        destination.getInputs().remove(sourceSynapse);

        Synapse tempSynapse = new Synapse(temp,destination,randomGen.nextDouble());

        temp.getOutputs().add(tempSynapse);
        destination.getInputs().add(tempSynapse);

        inputees.add(temp);
        outputees.add(temp);
        multiputees.add(temp);
    }

    public void mutateRemoveNeuron() {
        if(multiputees.size() != 0) {
            Neuron temp = getRandomNeuron(multiputees);
            for(Synapse syn : temp.getInputs())
            {
                syn.getSourceNeuron().getOutputs().remove(syn);
            }
            for(Synapse syn : temp.getOutputs())
            {
                syn.getDestinationNeuron().getInputs().remove(syn);
            }

            temp.getInputs().clear();
            temp.getOutputs().clear();

            outputees.remove(temp);
            inputees.remove(temp);
            multiputees.remove(temp);
        }
    }

    public void mutateChangeWeights() {
        if(multiputees.size() != 0) {
            Neuron temp = getRandomNeuron(multiputees);
            for(Synapse s : temp.getInputs()) {
                s.setWeight(randomGen.nextDouble());
            }
        }
    }

    public void mutate() {
        if(randomGen.nextDouble() > 0.4) {
            int mutationType = randomGen.nextInt(3);
            if (mutationType == 0)
                mutateAddNeuron();
            else if (mutationType == 1)
                mutateRemoveNeuron();
            else if (mutationType == 2)
                mutateChangeWeights();
        }
    }

    public Neuron getRandomNeuron(ArrayList<Neuron> neuronList) {
        int index = randomGen.nextInt(neuronList.size());
        return neuronList.get(index);
    }

    public Synapse getRandomSynapse(ArrayList<Synapse> synapseList) {
        int index = randomGen.nextInt(synapseList.size());
        return synapseList.get(index);
    }

}
