package com.biscuit.namet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder
@Data
@ToString
public class DataRequest {
    private Double temperature;
    private Double humidity;
    private Integer soil;

    @JsonProperty("bme_temperature")
    private Double bmeTemperature;

    @JsonProperty("bme_humidity")
    private Double bmeHumidity;

    private Double pressure;

    @JsonProperty("gas_resistance")
    private Double gasResistance;

    @JsonProperty("avg_temperature")
    private Double avgTemperature;

    @JsonProperty("avg_humidity")
    private Double avgHumidity;
}
