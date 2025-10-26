package org.estaciona.rapido.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class ParkingRecord {
    public Long id;
    public String plate;
    public OffsetDateTime entry;
    public OffsetDateTime leave;
    public BigDecimal total;

    public ParkingRecord(){}
    
    public ParkingRecord(long id, String plate, OffsetDateTime entry, OffsetDateTime leave, BigDecimal total)
    {
        this.id = id;
        this.plate = plate;
        this.entry = entry;
        this.leave = leave;
        this.total = total;   
    }

        public ParkingRecord(long id, String plate, OffsetDateTime entry)
    {
        this.id = id;
        this.plate = plate;
        this.entry = entry;  
    }
}
