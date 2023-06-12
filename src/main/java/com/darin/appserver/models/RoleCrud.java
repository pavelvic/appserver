package com.darin.appserver.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

//TODO Refactor: merge with class Role
@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
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
}
