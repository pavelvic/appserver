package com.darin.appserver.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

//TODO Refactor: merge with class User
@Entity
@Table(name = "users")
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

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "users_pools",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "pool_id")})
    @JsonIgnore
    private Set<Pool> pools = new HashSet<>();

    public UserCrud() {

    }

    public UserCrud(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<RoleCrud> getRoleCruds() {
        return roleCruds;
    }

    public void setRoleCruds(Set<RoleCrud> roleCruds) {
        this.roleCruds = roleCruds;
    }

    public Set<Pool> getPools() {
        return pools;
    }

    public void setPools(Set<Pool> pools) {
        this.pools = pools;
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

    public void addPool(Pool pool) {
        this.pools.add(pool);
        pool.getUserCruds().add(this);
    }

    public void removePool(long poolId) {
        Pool pool = this.pools.stream().filter(p -> p.getId() == poolId).findFirst().orElse(null);
        if (pool != null) {
            this.pools.remove(pool);
            pool.getUserCruds().remove(this);
        }
    }
}