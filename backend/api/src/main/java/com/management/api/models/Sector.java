package com.management.api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import org.locationtech.jts.geom.Point;
// Importa JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @JsonIgnore
    @Column(columnDefinition = "geometry(Point, 4326)")
    private Point sectorLocation;

    @JsonIgnore 
    @OneToMany(mappedBy = "sector")
    private List<Tasks> tasks = new ArrayList<>();
}