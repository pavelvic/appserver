package com.darin.appserver.repository;

import com.darin.appserver.models.RoleCrud;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//TODO Refactor: merge with class RoleRepository
public interface RoleCrudRepository extends JpaRepository<RoleCrud, Long> {
    List<RoleCrud> findRoleCrudsByUserCrudsId(Long userCrudId);
}
