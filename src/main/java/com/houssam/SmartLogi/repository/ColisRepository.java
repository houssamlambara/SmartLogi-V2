package com.houssam.SmartLogi.repository;

import com.houssam.SmartLogi.enums.Prioriter;
import com.houssam.SmartLogi.enums.Statut;
import com.houssam.SmartLogi.model.Colis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColisRepository extends JpaRepository<Colis, String>, JpaSpecificationExecutor<Colis> {

    // Recherche par relations
    List<Colis> findByDestinataireId(String destinataireId);
    Page<Colis> findByDestinataireId(String destinataireId, Pageable pageable);
    
    List<Colis> findByLivreurId(String livreurId);
    Page<Colis> findByLivreurId(String livreurId, Pageable pageable);
    
    List<Colis> findByClientExpediteurId(String clientId);

    List<Colis> findByStatut(Statut statut);
    List<Colis> findByPriorite(Prioriter priorite);
    List<Colis> findByZoneId(String zoneId);
    List<Colis> findByVilleDestinationContainingIgnoreCase(String ville);

    // Recherche par mot-clé (description OU ville)
    List<Colis> findByDescriptionContainingIgnoreCaseOrVilleDestinationContainingIgnoreCase(
        String descKeyword,
        String villeKeyword
    );

    // Filtrage pagination (pour le gestionnaire)
    @Query("""
SELECT c FROM Colis c
WHERE 
    (:statut IS NULL OR c.statut = :statut)
    AND (:zoneId IS NULL OR c.zone.id = :zoneId)
    AND (:villeDestination IS NULL OR LOWER(c.villeDestination) LIKE LOWER(CONCAT('%', :villeDestination, '%')))
    AND (:priorite IS NULL OR c.priorite = :priorite)
ORDER BY c.id
""")
    Page<Colis> filterColis(
            @Param("statut") Statut statut,
            @Param("zoneId") String zoneId,
            @Param("villeDestination") String villeDestination,
            @Param("priorite") Prioriter priorite,
            Pageable pageable
    );

    @Query("""
SELECT c FROM Colis c
LEFT JOIN c.livreur l
LEFT JOIN c.clientExpediteur ce
LEFT JOIN c.destinataire d
WHERE 
    LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
    LOWER(c.villeDestination) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
    LOWER(l.nom) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
    LOWER(ce.nom) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
    LOWER(d.nom) LIKE LOWER(CONCAT('%', :keyword, '%'))
""")
    Page<Colis> searchColis(@Param("keyword") String keyword, Pageable pageable);

}
