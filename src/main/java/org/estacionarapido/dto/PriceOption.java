package org.estacionarapido.dto;

import java.math.BigDecimal;

public record PriceOption (long id, String name, int frequencyValue, String frequencyType, BigDecimal value) {}
