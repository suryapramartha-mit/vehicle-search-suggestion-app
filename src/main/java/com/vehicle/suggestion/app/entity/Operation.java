package com.vehicle.suggestion.app.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "brand")
    private String brand;

    @Column(name = "model")
    private String model;

    @Column(name = "engine")
    private String engine;

    @Column(name = "year_start")
    private Integer yearStart;

    @Column(name = "year_end")
    private Integer yearEnd;

    @Column(name = "distance_start")
    private Double distanceStart; // in kilometers

    @Column(name = "distance_end")
    private Double distanceEnd; // in kilometers

    @Column(name = "name")
    private String name; // e.g., "Oil Change"

    @Column(name = "approx_cost")
    private Double approxCost;

    @Column(name = "description")
    private String description;

    @Column(name = "time")
    private Integer time; // in hours

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "brand", referencedColumnName = "brand", insertable = false, updatable = false),
            @JoinColumn(name = "model", referencedColumnName = "model", insertable = false, updatable = false),
            @JoinColumn(name = "engine", referencedColumnName = "engine", insertable = false, updatable = false)
    })
    private Vehicle vehicle;

}
