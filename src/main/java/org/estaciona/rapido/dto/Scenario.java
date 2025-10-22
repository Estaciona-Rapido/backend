package org.estaciona.rapido.dto;

import java.util.List;

public class Scenario {
    public long id;
    public String name;
    public boolean open;
    public long capacity;
    public List<PriceOption> prices;

    public Scenario(){}
}
