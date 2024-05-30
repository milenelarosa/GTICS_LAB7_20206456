package com.example.lab7_20206456.controller;

import com.example.lab7_20206456.entity.Jugador;
import com.example.lab7_20206456.repository.JugadorRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/player")
public class JugadorController {

    final JugadorRepository jugadorRepository;


    public JugadorController(JugadorRepository jugadorRepository) {
        this.jugadorRepository = jugadorRepository;
    }

    //Listar todos los jugadores
    @GetMapping(value = {"/list", ""})
    public List<Jugador> listarTodosJugadores() {
        return jugadorRepository.findAll();
    }

    //Listar por region

    @GetMapping(value = "/{id}")
    public ResponseEntity<HashMap<String, List>> buscarEquipo(@PathVariable("id") String region) {
        try {

            List<Jugador> byId = jugadorRepository.findPorRegion(region);

            HashMap<String, List> respuesta = new HashMap<>();

            if (!byId.isEmpty()) {
                List<Jugador> byRegion = jugadorRepository.listarJugadoresPorRegion(region);
                respuesta.put("Jugadores", byRegion);
            }
            return ResponseEntity.ok(respuesta);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    // CREAR /jugador y /jugador/
    @PostMapping(value = {"", "/"})
    public ResponseEntity<HashMap<String, Object>> guardarJugador(
            @RequestBody Jugador jugador,
            @RequestParam(value = "fetchId", required = false) boolean fetchId) {

        HashMap<String, Object> responseJson = new HashMap<>();

        jugadorRepository.save(jugador);
        if (fetchId) {
            responseJson.put("id", jugador.getPlayerId());
        }
        responseJson.put("estado", "creado");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseJson);
    }

    @PutMapping(value = {"", "/"}, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<HashMap<String, Object>> actualizar(Jugador jugadorRecibido) {

        HashMap<String, Object> rpta = new HashMap<>();

        if (jugadorRecibido.getPlayerId() != null && jugadorRecibido.getPlayerId() > 0) {

            Optional<Jugador> byId = jugadorRepository.findById(jugadorRecibido.getPlayerId());
            if (byId.isPresent()) {
                Jugador jugadorFromDb = byId.get();

                if (jugadorRecibido.getName() != null)
                    jugadorFromDb.setName(jugadorRecibido.getName());

                if (jugadorRecibido.getRegion() != null)
                    jugadorFromDb.setRegion(jugadorRecibido.getRegion());

                if (jugadorRecibido.getPosition() != null)
                    jugadorFromDb.setPosition(jugadorRecibido.getPosition());

                if (jugadorRecibido.getMmr() != null)
                    jugadorFromDb.setMmr(jugadorRecibido.getMmr());

                jugadorRepository.save(jugadorFromDb);
                rpta.put("result", "ok");
                return ResponseEntity.ok(rpta);
            } else {
                rpta.put("result", "error");
                rpta.put("msg", "El ID del jugador enviado no existe");
                return ResponseEntity.badRequest().body(rpta);
            }
        } else {
            rpta.put("result", "error");
            rpta.put("msg", "debe enviar un jugador con ID");
            return ResponseEntity.badRequest().body(rpta);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HashMap<String, Object>> borrarJugador(@PathVariable("id") String idStr){
        HashMap<String, Object> rpta = new HashMap<>();
        try{
            int id = Integer.parseInt(idStr);
            Optional<Jugador> byId = jugadorRepository.findById(id);
            if(byId.isPresent()){
                jugadorRepository.deleteById(id);
                rpta.put("result","Borrado exitoso");
            }else{
                rpta.put("result","NO OK");
                rpta.put("msg","El ID enviado no existe");
            }
            return ResponseEntity.ok(rpta);
        }catch (NumberFormatException e){
            rpta.put("estado", "error");
            rpta.put("msg", "El ID debe ser un numero");
            return ResponseEntity.badRequest().body(null);
        }
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<HashMap<String, String>> gestionException(HttpServletRequest request) {
        HashMap<String, String> responseMap = new HashMap<>();
        if (request.getMethod().equals("POST") || request.getMethod().equals("PUT")) {
            responseMap.put("estado", "error");
            responseMap.put("msg", "Debe enviar un producto");
        }
        return ResponseEntity.badRequest().body(responseMap);
    }




}
