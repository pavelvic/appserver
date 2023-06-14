package com.darin.appserver.repository;

import com.darin.appserver.models.UserCrud;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//TODO Refactor: merge with class UserRepository
public interface UserCrudRepository extends JpaRepository<UserCrud, Long> {
    List<UserCrud> findUsersByRoleCrudsId(Long roleId);
    List<UserCrud> findUsersByPoolsId(Long poolId);
}