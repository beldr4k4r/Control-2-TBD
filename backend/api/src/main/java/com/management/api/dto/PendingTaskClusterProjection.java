package com.management.api.dto;

public interface PendingTaskClusterProjection {
    Integer getClusterId();
    Long getTaskCount();
    Double getCentroidLongitude();
    Double getCentroidLatitude();
}