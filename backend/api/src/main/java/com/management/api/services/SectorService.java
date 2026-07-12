package com.management.api.services;

import org.springframework.stereotype.Service;
import com.management.api.models.Sector;
import com.management.api.repositories.SectorRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class SectorService {

    @Autowired
    private SectorRepository sectorRepository;

    @Transactional
    public Sector createSector(Sector sector){
        return sectorRepository.save(sector);
    }

    public List<Sector> readSectors(){
        return sectorRepository.findAll();
    }

    public Optional<Sector> readSectorById(Long id){
        return sectorRepository.findById(id);
    }

    public Optional<Sector> readSectorByName(String name){
        return sectorRepository.findBySectorName(name);
    }

    public Optional<Sector> readSectorByLocation(Point location){
        return sectorRepository.findBySectorLocation(location);
    }
    
    @Transactional
    public Sector updateSector(Sector sector){
        return sectorRepository.save(sector);
    }

    @Transactional
    public void deleteSector(Long id){
        sectorRepository.deleteById(id);
    }
}