package com.management.api.repositories;

import com.management.api.models.Sector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import org.locationtech.jts.geom.Point;

@Repository
public interface SectorRepository extends JpaRepository<Sector, Long> {
    Optional<Sector> findBySectorName(String name);
    Optional<Sector> findBySectorLocation(Point location);
}