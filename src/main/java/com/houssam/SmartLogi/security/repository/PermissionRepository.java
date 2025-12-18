package com.houssam.SmartLogi.security.repository;

import com.houssam.SmartLogi.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {

    Optional<Permission> findByName(String name);
    boolean existsByName(String name);
    List<Permission> findByNameIn(List<String> names);
}
