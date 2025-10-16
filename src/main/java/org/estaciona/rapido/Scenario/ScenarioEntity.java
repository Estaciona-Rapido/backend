package org.estaciona.rapido.Scenario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="scenario")
public class ScenarioEntity {

    public ScenarioEntity() {};

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_scenario")
    public long id;

    @Column(name = "scenario_name", nullable = false)
    public String name;

    @Column(name = "capacity", nullable = false)
    public long capacity;

    @Column(name = "period", nullable = false)
    public String period;
}
