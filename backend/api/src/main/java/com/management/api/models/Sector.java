package com.management.api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import org.locationtech.jts.geom.Point;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sectors")
public class Sector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sector")
    private Long idSector;

    @Column (unique = true)
    private String sectorName;

    @Column(columnDefinition = "geometry(Point, 4326)")
    private Point sectorLocation;

    @OneToMany(mappedBy = "sector")
    private List<Tasks> tasks = new ArrayList<>();
}