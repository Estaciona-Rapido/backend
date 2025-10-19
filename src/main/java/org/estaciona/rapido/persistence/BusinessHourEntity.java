package org.estaciona.rapido.persistence;

import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="business_hour")
public class BusinessHourEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_business_hour")
    public long id;

    @Column(name = "activated",nullable = false)
    public boolean activated;

    @Column(name="start_week_day", nullable = false)
    public short startWeekDay;

    @Column(name="end_week_day", nullable = false)
    public short endWeekDay;

    @Column(name="start_time", nullable = false)
    public LocalTime startTime;

    @Column(name="end_time", nullable = false)
    public LocalTime endTime;

    @ManyToOne
    @JoinColumn(name="id_scenario", nullable = false)
    public ScenarioEntity scenario;

}
