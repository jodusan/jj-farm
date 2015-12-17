package neat;

import java.util.ArrayList;

/**
 * Created by Nikola on 12/17/2015.
 */
public class Neuron
{
    public ArrayList<Synapse> inputs = new ArrayList<>();
    public ArrayList<Synapse> outputs = new ArrayList<>();
    public double value=0.0;

    public void activateNeuron(double input)
    {
        if(value>0.5)
        {
            for(Synapse l : outputs)
            {
                l.neuron.activateNeuron(value);
            }
        }
    }
}
