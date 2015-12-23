package neat;

import java.util.ArrayList;

public class Neuron
{
    public ArrayList<Synapse> inputs = new ArrayList<>();
    public ArrayList<Synapse> outputs = new ArrayList<>();
    public boolean bias=false;
    public double value=0.0;


    public static double sigmoid(double weightedSum)
    {
        return 1.0 / (1 + Math.exp(-1.0 * weightedSum));
    }

//    public void activateNeuron(double input)
//    {
//        if(value>0.5)
//        {
//            for(Synapse l : outputs)
//            {
//                l.neuron.activateNeuron(value);
//            }
//        }
//    }
}
