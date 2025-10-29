package org.estacionarapido.dto;

import java.math.BigDecimal;

public record PriceModelProposal(String name, boolean isActivated, BigDecimal value, int frequencyValue, String frequencyType) {}
