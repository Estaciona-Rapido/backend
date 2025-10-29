package org.estacionarapido.dto;

import java.time.LocalTime;

public record BusinessHourProposal(boolean isActivated, short startWeekDay, short endWeekDay, LocalTime startTime, LocalTime endTime) {}

