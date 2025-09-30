package org.estaciona.rapido.Scenario;

public class Scenario {
    public String name;
    public long capacity;
    public String periodString;

    public Scenario(){}
    public Scenario(String name, long capacity, String periodString)
    {
        this.name = name;
        this.capacity = capacity;
        this.periodString = periodString;
    }

}
