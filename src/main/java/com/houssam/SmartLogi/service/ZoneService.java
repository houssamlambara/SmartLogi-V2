package com.houssam.SmartLogi.service;

import com.houssam.SmartLogi.dto.ZoneDTO;
import com.houssam.SmartLogi.mapper.ZoneMapper;
import com.houssam.SmartLogi.model.Zone;
import com.houssam.SmartLogi.repository.ZoneRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ZoneService {

    private final ZoneRepository repository;
    private final ZoneMapper mapper;

    public ZoneService(ZoneRepository repository, ZoneMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public ZoneDTO createZone(ZoneDTO dto) {
        Zone entity = mapper.toEntity(dto);
        Zone saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    public Page<ZoneDTO> getAllZones(Pageable pageable){
        return repository.findAll(pageable)
                .map(mapper::toDTO);
    }
    
    public List<ZoneDTO> getAllZones(){
        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public ZoneDTO getZoneById(String id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElse(null);
    }

    public ZoneDTO updateZone(String id, ZoneDTO dto){
        Zone zone = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zone introuvable avec l'ID " + id));

        zone.setNom(dto.getNom());
        zone.setCodePostal(dto.getCodePostal());

        Zone updated = repository.save(zone);
        return mapper.toDTO(updated);
    }

    public void deleteZone(String id){
        repository.deleteById(id);
    }

}
