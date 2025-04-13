package com.biscuit.namet.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@Data
@ToString
public class DataResponse {
    private Double temperature;
    private Double humidity;
    private Integer soil;
    private Double bmeTemperature;
    private Double bmeHumidity;
    private Double pressure;
    private Double gasResistance;
    private Double avgTemperature;
    private Double avgHumidity;
    private LocalDateTime createdAt;
}
