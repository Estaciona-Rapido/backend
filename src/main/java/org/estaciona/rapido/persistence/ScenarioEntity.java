package org.estaciona.rapido.persistence;

import java.time.OffsetDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="scenario")
@NamedQuery(name = "ScenarioEntities.getCurrentScenarios",
    query = "SELECT s FROM ScenarioEntity s WHERE :tmsZ BETWEEN s.start and s.end")
public class ScenarioEntity {

    public ScenarioEntity() {};

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_scenario")
    public long id;

    @Column(name = "scenario_name", nullable = false, length = 50)
    public String name;

    @Column(name = "capacity", nullable = false)
    public long capacity;

    /**In minutes*/
    @Column(name = "tolerance", nullable = false)
    public int tolerance;

    @Column(name = "scenario_start", nullable = false)
    public OffsetDateTime start;

    @Column(name = "scenario_end", nullable = false)
    public OffsetDateTime end;

    @OneToMany(mappedBy = "scenario")
    public List<BusinessHourEntity> businessHours;

    @OneToMany(mappedBy = "scenario")
    public List<PriceModelEntity> prices;
}
