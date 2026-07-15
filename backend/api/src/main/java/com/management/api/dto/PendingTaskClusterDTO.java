package com.management.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PendingTaskClusterDTO {
    private Integer clusterId;
    private Long taskCount;
    private Double centroidLongitude;
    private Double centroidLatitude;
}