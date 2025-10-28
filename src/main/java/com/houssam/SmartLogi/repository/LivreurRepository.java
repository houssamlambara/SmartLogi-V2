package com.houssam.SmartLogi.repository;

import com.houssam.SmartLogi.model.Colis;
import com.houssam.SmartLogi.model.Livreur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LivreurRepository extends JpaRepository<Livreur, Long> {
}
