package com.darin.appserver.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "metrics")
public class Metric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "critical_min_value")
    private long criticalMinValue;

    @Column(name = "critical_max_value")
    private long criticalMaxValue;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "metrics")
    @JsonIgnore
    private Set<Sensor> sensors = new HashSet<>();

    public Metric() {

    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCriticalMinValue() {
        return criticalMinValue;
    }

    public void setCriticalMinValue(long criticalMinValue) {
        this.criticalMinValue = criticalMinValue;
    }

    public long getCriticalMaxValue() {
        return criticalMaxValue;
    }

    public void setCriticalMaxValue(long criticalMaxValue) {
        this.criticalMaxValue = criticalMaxValue;
    }

    public Set<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(Set<Sensor> sensors) {
        this.sensors = sensors;
    }
}
