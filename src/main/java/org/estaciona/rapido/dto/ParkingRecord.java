package org.estaciona.rapido.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import jakarta.persistence.Column;

public class ParkingRecord {
    public boolean hasPaid;
    public Long id;
    public String plate;
    public OffsetDateTime entry;
    public OffsetDateTime leave;
    public BigDecimal total;


    public ParkingRecord(boolean hasPaid, long id, String plate, OffsetDateTime entry, OffsetDateTime leave, BigDecimal total)
    {
        this.hasPaid = hasPaid;
        this.id = id;
        this.plate = plate;
        this.entry = entry;
        this.leave = leave;
        this.total = total;
    }
}
