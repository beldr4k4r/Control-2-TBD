package com.management.api.controllers;

import com.management.api.models.Sector;
import com.management.api.services.SectorService;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sectors")
public class SectorController {

    @Autowired
    private SectorService sectorService;

    @PostMapping
    public ResponseEntity<Sector> createSector(@RequestBody Sector sector) {
        Sector newSector = sectorService.createSector(sector);
        return ResponseEntity.status(HttpStatus.CREATED).body(newSector);
    }

    @GetMapping
    public ResponseEntity<List<Sector>> readSectors() {
        List<Sector> sectors = sectorService.readSectors();
        return ResponseEntity.ok(sectors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sector> readSectorById(@PathVariable Long id) {
        return sectorService.readSectorById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Sector> readSectorByName(@PathVariable String name) {
        return sectorService.readSectorByName(name)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/location")
    public ResponseEntity<Sector> readSectorByLocation(@RequestBody Point location) {
        return sectorService.readSectorByLocation(location)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sector> updateSector(@PathVariable Long id, @RequestBody Sector sector) {
        return sectorService.readSectorById(id)
                .map(existing -> {
                    sector.setIdSector(id);
                    Sector updated = sectorService.updateSector(sector);
                    return ResponseEntity.ok(updated);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSector(@PathVariable Long id) {
        return sectorService.readSectorById(id)
                .map(existing -> {
                    sectorService.deleteSector(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
