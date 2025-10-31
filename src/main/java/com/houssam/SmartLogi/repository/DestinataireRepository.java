package com.houssam.SmartLogi.repository;

import com.houssam.SmartLogi.model.Colis;
import com.houssam.SmartLogi.model.Destinataire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DestinataireRepository extends JpaRepository<Destinataire, String> {
}
