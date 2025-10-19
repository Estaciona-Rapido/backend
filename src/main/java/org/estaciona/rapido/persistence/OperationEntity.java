package org.estaciona.rapido.persistence;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "operation")
public class OperationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_operation")
    public long id;

    @Column(name = "entry", nullable = false)
    public OffsetDateTime entry;
    
    @Column(name = "leave", nullable = true)
    public OffsetDateTime leave;
    
    @Column(name="plate", nullable = false, length = 7)
    public String plate;

    @Column(name = "total", nullable = true, precision = 38, scale = 2)
    public BigDecimal total;

    @ManyToOne
    @JoinColumn(name = "id_price_model", nullable = false)
    public PriceModelEntity price_model;
}
