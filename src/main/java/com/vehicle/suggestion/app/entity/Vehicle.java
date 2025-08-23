package com.vehicle.suggestion.app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Table(name = "vehicle")
@Getter
public class Vehicle {

    @Column(name = "id", nullable = false, unique = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "brand")
    private String brand;

    @Column(name = "model")
    private String model;

    @Column(name = "engine")
    private String engine;

    @Column(name = "make_year")
    private Integer makeYear;

    @OneToMany
    @JoinColumns({
            @JoinColumn(name = "brand", referencedColumnName = "brand", insertable = false, updatable = false),
            @JoinColumn(name = "model", referencedColumnName = "model", insertable = false, updatable = false),
            @JoinColumn(name = "engine", referencedColumnName = "engine", insertable = false, updatable = false)
    })
    private List<Operations> operations = new ArrayList<>();

}
