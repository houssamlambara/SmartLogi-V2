package com.houssam.SmartLogi.repository;

import com.houssam.SmartLogi.model.Colis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColisProduitRepository extends JpaRepository<Colis,String> {
}
