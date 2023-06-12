package com.darin.appserver.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

//TODO Refactor: merge with class User
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class UserCrud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<RoleCrud> roleCruds = new HashSet<>();

    public UserCrud(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public void addRoleCrud(RoleCrud roleCrud) {
        this.roleCruds.add(roleCrud);
        roleCrud.getUserCruds().add(this);
    }

    public void removeRoleCrud(long RoleCrudId) {
        RoleCrud roleCrud = this.roleCruds.stream().filter(r -> r.getId() == RoleCrudId).findFirst().orElse(null);
        if (roleCrud != null) {
            this.roleCruds.remove(roleCrud);
            roleCrud.getUserCruds().remove(this);
        }
    }
}