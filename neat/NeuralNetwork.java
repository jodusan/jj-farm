package neat;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class NeuralNetwork
{
    ArrayList<Neuron> neurons;
    ArrayList<Sensor> sensors;
    ArrayList<Synapse> synapses;

    public NeuralNetwork()
    {

    }

    public void forwardPropagation()
    {
        Queue<Neuron> nextElement = new LinkedList<>();
        ArrayList<Neuron> visited=new ArrayList<>();
        for (Sensor sen : sensors)
        {
            for (Synapse sn : sen.outputs)
            {
                nextElement.add(sn.neuron);
                visited.add(sn.neuron);
            }
        }
        while (!nextElement.isEmpty())
        {
            Neuron currentElement = nextElement.poll();
            currentElement.value=0;
            for(Synapse in : currentElement.inputs)
            {
                currentElement.value += in.weight*in.neuron.value;
            }
            currentElement.value = currentElement.sigmoid(currentElement.value);
            for(Synapse sn : currentElement.outputs)
            {
                if(!visited.contains(sn.neuron))
                {
                    nextElement.add(sn.neuron);
                    visited.add(sn.neuron);
                }
            }
        }
    }

}
