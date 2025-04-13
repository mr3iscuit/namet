package com.biscuit.namet.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
