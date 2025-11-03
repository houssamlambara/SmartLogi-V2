package com.houssam.SmartLogi.repository;

import com.houssam.SmartLogi.model.Colis;
import com.houssam.SmartLogi.model.Livreur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LivreurRepository extends JpaRepository<Livreur, String> {

    @Query("""
SELECT l FROM Livreur l
WHERE 
    LOWER(l.nom) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
    LOWER(l.prenom) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
    LOWER(l.telephone) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
    LOWER(l.vehicule) LIKE LOWER(CONCAT('%', :keyword, '%'))
""")
    Page<Livreur> searchLivreurs(@Param("keyword") String keyword, Pageable pageable);
}
