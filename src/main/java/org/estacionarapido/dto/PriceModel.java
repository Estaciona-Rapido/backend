package org.estacionarapido.dto;

import java.math.BigDecimal;

public record PriceModel (long id, String name, boolean isActivated, BigDecimal value, int frequencyValue, String frequencyType) {}
