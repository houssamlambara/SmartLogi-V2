package com.houssam.SmartLogi.repository;

import com.houssam.SmartLogi.model.Colis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColisRepository extends JpaRepository<Colis, Long> {

    List<Colis> findByDestinataireId(Long destinataireId);

}
