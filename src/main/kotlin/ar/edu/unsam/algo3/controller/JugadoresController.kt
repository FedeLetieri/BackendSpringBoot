package ar.edu.unsam.algo3.controller

import ar.edu.unsam.algo3.domain.Jugador
import ar.edu.unsam.algo3.dto.*
import ar.edu.unsam.algo3.services.JugadorService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin("*")
class JugadoresController {

    @Autowired
    lateinit var jugadorService: JugadorService

    @GetMapping("/jugadores")
    @Operation(summary = "Devuelve todos los jugadores disponibles")
    fun getJugadores(): List<InfoJugadorDTO> {
        return jugadorService.getJugadorDTO()
    }

    @PostMapping("/jugadores/agregar")
    @Operation(summary = "Agrega un jugador")
    fun postJugador(@RequestBody jugador: JugadorFormDTO) {
        jugadorService.addJugador(jugador)
        ResponseEntity.ok("Jugador agregado exitosamente.");
    }

    @GetMapping("/jugadores_all_info")
    @Operation(summary = "Devuelve la información completa de los jugadores")
    fun getJugadoresAll(@RequestParam(required = false) filtro:String?): List<JugadorDTO> {
        return jugadorService.jugadoresAllInfo(filtro)
    }

    @DeleteMapping("/jugadores/eliminar")
    @Operation(summary = "Elimina un jugador")
    fun deleteJugador(@RequestParam id: Int) {
        jugadorService.deleteJugador(id)
        ResponseEntity.ok("Jugador eliminado exitosamente.");
    }

    @PutMapping("/jugadores/editar")
    @Operation(summary = "Edita un jugador")
    fun editJugador(@RequestBody jugadorActualizado: JugadorFormDTO) {
        jugadorService.editJugador(jugadorActualizado)
        ResponseEntity.ok("Jugador editado exitosamente.");
    }

    @GetMapping("/jugador")
    @Operation(summary = "Devuelve el jugador por ID")
    fun getJugadorByID(@RequestParam id: Int): JugadorDTO {
        return jugadorService.jugadorByID(id)
    }
}