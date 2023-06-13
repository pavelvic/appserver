package com.darin.appserver.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "pools")
public class Pool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "pools_sensors",
            joinColumns = {@JoinColumn(name = "pool_id")},
            inverseJoinColumns = {@JoinColumn(name = "sensor_id")})
    private Set<Sensor> sensors = new HashSet<>();

    public Pool() {

    }

    public Pool(String name, String address) {
        this.name = name;
        this.address = address;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(Set<Sensor> sensors) {
        this.sensors = sensors;
    }

    public void addSensor(Sensor sensor) {
        this.sensors.add(sensor);
        sensor.getPools().add(this);
    }

    public void removeSensor(long sensorId) {
        Sensor sensor = this.sensors.stream().filter(r -> r.getId() == sensorId).findFirst().orElse(null);
        if (sensor != null) {
            this.sensors.remove(sensor);
            sensor.getPools().remove(this);
        }
    }
}
