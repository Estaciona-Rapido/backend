package org.estacionarapido.dto;

import java.util.List;

public record Scenario (long id, String name, boolean open, long capacity, List<PriceOption> prices) {}
