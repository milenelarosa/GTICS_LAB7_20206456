package com.example.lab7_20206456.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "players")
@Getter
@Setter
public class Jugador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "playerId", nullable = false)
    private Integer playerId;

    @Column(name = "name", nullable = true)
    private String name;

    @Column(name = "mmr", nullable = true)
    private Integer mmr;

    @Column(name = "position", nullable = true)
    private Integer position;

    @Column(name = "region", nullable = true)
    private String region;
}
