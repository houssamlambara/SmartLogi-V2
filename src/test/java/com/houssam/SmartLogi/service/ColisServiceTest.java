package com.houssam.SmartLogi.service;

import com.houssam.SmartLogi.dto.ColisDTO;
import com.houssam.SmartLogi.dto.ProduitDTO;
import com.houssam.SmartLogi.email.EmailService;
import com.houssam.SmartLogi.enums.Prioriter;
import com.houssam.SmartLogi.enums.Statut;
import com.houssam.SmartLogi.exception.ResourceNotFoundException;
import com.houssam.SmartLogi.mapper.ColisMapper;
import com.houssam.SmartLogi.model.*;
import com.houssam.SmartLogi.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

 class ColisServiceTest {

    @InjectMocks
    private ColisService colisService;

    @Mock
    private ColisRepository colisRepository;
    @Mock
    private ColisMapper colisMapper;
    @Mock
    private LivreurRepository livreurRepository;
    @Mock
    private ClientExpediteurRepository clientExpediteurRepository;
    @Mock
    DestinataireRepository destinataireRepository;
    @Mock
    ZoneRepository zoneRepository;
    @Mock
    private ProduitRepository produitRepository;
    @Mock
    ColisProduitRepository colisProduitRepository;
    @Mock
    private EmailService emailService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void creatColis_succes() {
        ColisDTO colisDTO = new ColisDTO();
        colisDTO.setLivreurId("livreur1");
        colisDTO.setClientExpediteurId("clientExpediteur1");
        colisDTO.setDestinataireId("destinataire1");
        colisDTO.setZoneId("zone1");
        colisDTO.setProductIds(Arrays.asList("produit1"));
        ProduitDTO nouveauProduitDTO = new ProduitDTO();
        nouveauProduitDTO.setNom("NouveauProduit");
        colisDTO.setNouveauxProduits(Arrays.asList(nouveauProduitDTO));

        Livreur livreurMock = mock(Livreur.class);
        ClientExpediteur clientMock = mock(ClientExpediteur.class);
        Destinataire destinataireMock = mock(Destinataire.class);
        Zone zoneMock = mock(Zone.class);
        Produit produitExist = mock(Produit.class);
        Produit produitNouveauMock = mock(Produit.class);

        when(clientMock.getEmail()).thenReturn("client@example.com");
        when(livreurRepository.findById("livreur1")).thenReturn(Optional.of(livreurMock));
        when(clientExpediteurRepository.findById("clientExpediteur1")).thenReturn(Optional.of(clientMock));
        when(destinataireRepository.findById("destinataire1")).thenReturn(Optional.of(destinataireMock));
        when(zoneRepository.findById("zone1")).thenReturn(Optional.of(zoneMock));
        when(produitRepository.findAllById(anyList())).thenReturn(List.of(produitExist));
        when(produitRepository.save(any(Produit.class))).thenReturn(produitNouveauMock);

        when(colisMapper.toEntity(colisDTO)).thenReturn(new Colis());
        when(colisRepository.save(any(Colis.class))).thenAnswer(i -> i.getArguments()[0]);
        when(colisMapper.toDTO(any(Colis.class))).thenReturn(colisDTO);

        ColisDTO result = colisService.createColis(colisDTO);

        assertNotNull(result);
        verify(colisRepository, times(1)).save(any(Colis.class));
        verify(emailService, times(1)).envoyerEmailColisCreer(any(Colis.class));
    }

    @Test
    void createColis_livreurNotFound(){
        ColisDTO colisDTO = new ColisDTO();
        colisDTO.setLivreurId("livreur1");
        when(livreurRepository.findById("livreur1")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> colisService.createColis(colisDTO));
    }

    @Test
    void getAllColis_succes(){
        Colis colis = new Colis();
        List<Colis> colisList = Arrays.asList(colis);
        Page<Colis> page = new PageImpl<>(colisList);
        when(colisRepository.findAll(any(PageRequest.class))).thenReturn(page);
        when(colisMapper.toDTO(colis)).thenReturn(new ColisDTO());

        Page<ColisDTO> result = colisService.getAllColis(PageRequest.of(0, 10));
        assertEquals(1,result.getContent().size());
    }

    @Test
    void getColisByDestinataireId_success() {
        String destinataireId = "destinataire1";
        Destinataire destinataireMock = mock(Destinataire.class);

        when(destinataireRepository.findById(destinataireId)).thenReturn(Optional.of(destinataireMock));
        Colis colis = new Colis();
        Page<Colis> page = new PageImpl<>(Arrays.asList(colis));
        when(colisRepository.findByDestinataireId(destinataireId, PageRequest.of(0,10))).thenReturn(page);
        when(colisMapper.toDTO(colis)).thenReturn(new ColisDTO());

        Page<ColisDTO> result = colisService.getColisByDestinataireId(destinataireId, PageRequest.of(0,10));
        assertEquals(1, result.getContent().size());
    }

    @Test
    void getColisByDestinataireId_notFound() {
        String destinataireId = "dest1";
        when(destinataireRepository.findById(destinataireId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> colisService.getColisByDestinataireId(destinataireId, PageRequest.of(0,10)));
    }

    @Test
    void updateStatut_success() {
        Colis colis = new Colis();
        colis.setStatut(Statut.Creer);

        when(colisRepository.findById("colis1")).thenReturn(Optional.of(colis));
        when(colisRepository.save(any(Colis.class))).thenAnswer(i -> i.getArguments()[0]);

        ColisDTO colisDTOResult = new ColisDTO();
        colisDTOResult.setStatut(Statut.Livrer);
        when(colisMapper.toDTO(any(Colis.class))).thenReturn(colisDTOResult);

        ColisDTO result = colisService.updateStatut("colis1", Statut.Livrer);

        assertNotNull(result);
        assertEquals(Statut.Livrer, result.getStatut());
        verify(colisRepository, times(1)).save(any(Colis.class));
    }

    @Test
    void updateStatut_notFound(){
        when(colisRepository.findById("colis1")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> colisService.updateStatut("colis1", Statut.Livrer));
    }

    @Test
    void deleteColis_success() {
        doNothing().when(colisRepository).deleteById("colis1");
        colisService.deleteColis("colis1");
        verify(colisRepository, times(1)).deleteById("colis1");
    }

    @Test
     void getColisByLivreurId_success(){
        String livreurId = "livreur1";
        Livreur livreurMock = mock(Livreur.class);

        when(livreurRepository.findById(livreurId)).thenReturn(Optional.of(livreurMock));

        Colis colis = new Colis();
        Page<Colis> page = new PageImpl<>(List.of(colis));
        when(colisRepository.findByLivreurId(livreurId, PageRequest.of(0, 10))).thenReturn(page);
        when(colisMapper.toDTO(colis)).thenReturn(new ColisDTO());

        Page<ColisDTO> result = colisService.getColisByLivreurId(livreurId, PageRequest.of(0,10));
        assertEquals(1,result.getContent().size());
    }

    @Test
     void getColisByLivreurId_notFound(){
        String livreurId = "livreur1";
        when(livreurRepository.findById(livreurId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> colisService.getColisByLivreurId(livreurId, PageRequest.of(0,10)));
    }

    @Test
     void getColisById_success(){
        Colis colis = new Colis();
        when(colisRepository.findById("colis1")).thenReturn(Optional.of(colis));
        when(colisMapper.toDTO(colis)).thenReturn(new ColisDTO());

        ColisDTO result = colisService.getColisById("colis1");
        assertNotNull(result);
    }

    @Test
     void getColisById_NotFound(){
        when(colisRepository.findById("colis1")).thenReturn(Optional.empty());
        assertNull(colisService.getColisById("colis1"));
    }

    @Test
     void filterColis_success(){
        Page<Colis> page = new PageImpl<>(List.of(new Colis()));
        when(colisRepository.filterColis(any(), any(), any(), any(), any())).thenReturn(page);
        when(colisMapper.toDTO(any())).thenReturn(new ColisDTO());

        Page<ColisDTO> result = colisService.filterColis(null, null, null, null, PageRequest.of(0, 10));
        assertEquals(1,result.getContent().size());
    }

    @Test
     void searchColis_WithKeyword(){
         Page<Colis> page = new PageImpl<>(List.of(new Colis()));
         when(colisRepository.searchColis("abc", PageRequest.of(0,10))).thenReturn(page);
         when(colisMapper.toDTO(any())).thenReturn(new ColisDTO());

         Page<ColisDTO> result = colisService.searchColis("abc", PageRequest.of(0, 10));
         assertEquals(1,result.getContent().size());
     }

     @Test
        void searchColis_KeywordEmpty(){
        Page<Colis> page = new PageImpl<>(List.of(new Colis()));
        when(colisRepository.findAll(any(PageRequest.class))).thenReturn(page);
        when(colisMapper.toDTO(any())).thenReturn(new ColisDTO());

        Page<ColisDTO> result = colisService.searchColis(" ", PageRequest.of(0, 10));
        assertEquals(1,result.getContent().size());
    }

    @Test
        void getColisCountByZone_success(){
        Zone zone = new Zone();
        zone.setId("zone1");
        zone.setNom("Zone Test");

        Colis colis = new Colis();
        colis.setZone(zone);

        when(colisRepository.findAll()).thenReturn(List.of(colis));
        Map<String, Long> result = colisService.getColisCountByZone();

        assertTrue(result.containsKey("Zone Test"));
        assertEquals(1L, result.get("Zone Test"));
    }

    @Test
     void getColisCountByStatut_success() {
        Colis colis = new Colis();
        colis.setStatut(Statut.Creer);

        when(colisRepository.findAll()).thenReturn(List.of(colis));
        Map<String, Long> result = colisService.getColisCountByStatut();

        assertTrue(result.containsKey(Statut.Creer.name()));
        assertEquals(1L, result.get(Statut.Creer.name()));
    }

    @Test
     void getColisCountByPriorite_success(){
        Colis colis = new Colis();
        colis.setPriorite(Prioriter.Normale);
        when(colisRepository.findAll()).thenReturn(List.of(colis));

        Map<String, Long> result = colisService.getColisCountByPriorite();

        assertTrue(result.containsKey("Normale"));
        assertEquals(1L, result.get("Normale"));
    }

    @Test
     void getStatistiqueLivreur_success(){
        String livreurId = "livreur1";
        Colis colis = new Colis();
        colis.setPoids(5.0);
        when(colisRepository.findByLivreurId("livreur1")).thenReturn(List.of(colis));

        Map<String, Object> result = colisService.getStatistiqueLivreur(livreurId);

        assertNotNull(result);
        assertEquals(1, result.get("NombreColis"));
        assertEquals(5.0, result.get("PoidsTotal"));
        verify(colisRepository).findByLivreurId("livreur1");
    }

     @Test
     void getStatistiqueZone_success() {
         Colis c1 = new Colis();
         c1.setPoids(2.5);

         when(colisRepository.findByZoneId("zone1")).thenReturn(List.of(c1));

         Map<String, Object> stats = colisService.getStatistiqueZone("zone1");

         assertEquals("zone1", stats.get("ZoneId"));
         assertEquals(1, stats.get("NombreColis"));
         assertEquals(2.5, (double) stats.get("PoidsTotal"));
     }


}
