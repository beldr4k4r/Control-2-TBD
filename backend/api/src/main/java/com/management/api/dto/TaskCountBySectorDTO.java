package com.management.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaskCountBySectorDTO {
    private Long sectorId;
    private String sectorName;
    private Long taskCount;
}
