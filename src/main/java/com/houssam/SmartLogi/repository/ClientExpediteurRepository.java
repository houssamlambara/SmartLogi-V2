package com.houssam.SmartLogi.repository;

import com.houssam.SmartLogi.model.ClientExpediteur;
import com.houssam.SmartLogi.model.Colis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientExpediteurRepository extends JpaRepository<ClientExpediteur, String> {

    @Query("""
        SELECT c FROM ClientExpediteur c
        WHERE 
            LOWER(c.nom) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(c.prenom) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(c.telephone) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(c.adresse) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    Page<ClientExpediteur> searchClients(@Param("keyword") String keyword, Pageable pageable);
}
