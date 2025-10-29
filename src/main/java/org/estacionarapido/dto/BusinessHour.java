package org.estacionarapido.dto;

import java.time.LocalTime;

public record BusinessHour(long id, boolean isActivated, short startWeekDay, short endWeekDay, LocalTime startTime, LocalTime endTime) {}
