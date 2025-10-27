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
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

@Entity
@Table(name = "operation")
@NamedQuery(name = "OperationEntities.getOccupancy",
    query = "SELECT COUNT(operation.id) FROM OperationEntity operation WHERE operation.hasPaid is FALSE")
@NamedQuery(name = "OperationEntities.filterParkedByPlate",
    query = "SELECT operation FROM OperationEntity operation WHERE operation.plate = :plate AND operation.hasPaid is FALSE")
@NamedQuery(name = "OperationEntities.getParkedByPlate",
    query = "SELECT operation FROM OperationEntity operation WHERE operation.plate = :plate AND operation.hasPaid is FALSE")
@NamedQuery(name = "OperationEntities.getByPlate",
    query = "SELECT operation FROM OperationEntity operation WHERE operation.plate = :plate")
public class OperationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_operation")
    public long id;

    @Column(name = "operation_entry", nullable = false)
    public OffsetDateTime entry;
    
    @Column(name = "operation_leave", nullable = true)
    public OffsetDateTime leave;
    
    @Column(name="plate", nullable = false, length = 7)
    public String plate;

    @Column(name = "total", nullable = true, precision = 38, scale = 2)
    public BigDecimal total;

    @Column(name = "paid", nullable = false)
    public boolean hasPaid = false;

    @ManyToOne
    @JoinColumn(name = "id_price_model", nullable = false)
    public PriceModelEntity price_model;
}
