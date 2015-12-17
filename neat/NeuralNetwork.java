package neat;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Nikola on 12/17/2015.
 */
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
