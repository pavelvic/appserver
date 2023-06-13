package com.darin.appserver.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sensors")
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "sensors")
    @JsonIgnore
    private Set<Pool> pools = new HashSet<>();


    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "sensors_metrics",
            joinColumns = {@JoinColumn(name = "sensor_id")},
            inverseJoinColumns = {@JoinColumn(name = "metric_id")})
    private Set<Metric> metrics = new HashSet<>();

    public Sensor() {

    }

    public Set<Metric> getMetrics() {
        return metrics;
    }

    public void setMetrics(Set<Metric> metrics) {
        this.metrics = metrics;
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

    public Set<Pool> getPools() {
        return pools;
    }

    public void setPools(Set<Pool> pools) {
        this.pools = pools;
    }

    public void addMetric(Metric metric) {
        this.metrics.add(metric);
        metric.getSensors().add(this);
    }

    public void removeMetric(long metricId) {
        Metric metric = this.metrics.stream().filter(m -> m.getId() == metricId).findFirst().orElse(null);
        if (metric != null) {
            this.metrics.remove(metric);
            metric.getSensors().remove(this);
        }
    }
}