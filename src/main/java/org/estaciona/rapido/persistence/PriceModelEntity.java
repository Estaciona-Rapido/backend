package org.estaciona.rapido.persistence;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "price_model")
public class PriceModelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_price_model")
    public long id;

    @Column(name = "model_name", nullable = false, length = 50)
    public String name;

    @Column(name = "activated", nullable = false)
    public boolean isActivated;

    @Column(name = "price", nullable = false, precision = 38, scale = 2)
    public BigDecimal value;

    @ManyToOne
    @JoinColumn(name = "id_scenario", nullable = false)
    public ScenarioEntity scenario;

    @OneToMany(mappedBy = "price_model")
    public List<OperationEntity> operations;
}
