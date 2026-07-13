package com.management.api.dto;

public interface TaskCountBySectorProjection {
    Long getSectorId();
    String getSectorName();
    Long getTaskCount();
}