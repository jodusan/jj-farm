package neat;

import java.util.ArrayList;


public class Sensor
{
    public ArrayList<Synapse> outputs;
    public String name;
    public double input;

    public Sensor(ArrayList<Synapse> outputs, String name, double input) {
        this.outputs = outputs;
        this.name = name;
        this.input = input;
    }

}
