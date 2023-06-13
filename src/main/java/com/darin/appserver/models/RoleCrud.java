package com.darin.appserver.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

//TODO Refactor: merge with class Role
@Entity
@Table(name = "roles")
public class RoleCrud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "roleCruds")
    @JsonIgnore
    private Set<UserCrud> userCruds = new HashSet<>();

    public RoleCrud() {

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

    public Set<UserCrud> getUserCruds() {
        return userCruds;
    }

    public void setUserCruds(Set<UserCrud> userCruds) {
        this.userCruds = userCruds;
    }
}
