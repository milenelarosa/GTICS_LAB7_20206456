package com.example.lab7_20206456.repository;

import com.example.lab7_20206456.entity.Jugador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JugadorRepository extends JpaRepository<Jugador,Integer> {

    @Query(nativeQuery = true, value = "SELECT * FROM lab7.players WHERE region = ? ORDER BY mmr DESC")
    List<Jugador> listarJugadoresPorRegion(String region);

    @Query(nativeQuery = true, value="SELECT * FROM lab7.players WHERE region = ?")
    List<Jugador> findPorRegion(String region);
}
