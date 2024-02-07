package ar.edu.unsam.algo3.services

import ar.edu.unsam.algo3.domain.Figurita
import ar.edu.unsam.algo3.domain.Jugador
import ar.edu.unsam.algo3.domain.Seleccion
import ar.edu.unsam.algo3.dto.*
import ar.edu.unsam.algo3.generic.toLocalDate
import ar.edu.unsam.algo3.repository.repositorioJugador
import ar.edu.unsam.algo3.repository.repositorioPosicion
import ar.edu.unsam.algo3.repository.repositorioSeleccion
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDate

@Service
class JugadorService {

    @Autowired
    lateinit var jugadorRepository: repositorioJugador

    @Autowired
    private lateinit var repositorioPosicion: repositorioPosicion

    @Autowired
    private lateinit var repositorioSeleccion: repositorioSeleccion

    fun getJugadorDTO(): List<InfoJugadorDTO> = jugadorRepository.coleccion.map { it.toInfoJugadorDTO() }

    fun addJugador(jugador: JugadorFormDTO) = jugadorRepository.create(jugador.toJugador())

    fun jugadoresAllInfo(filtro: String?): List<JugadorDTO> {
        return if (filtro.isNullOrEmpty()) {
            jugadorRepository.coleccion.map { it.toJugadorDTO() }
        } else {
            jugadorRepository.coleccion.filter { jugador ->
                jugador.cumpleCriterioBusqueda(filtro)
            }.map { it.toJugadorDTO() }
        }
    }

    fun deleteJugador(id: Int) {
        val jugadorAEliminar = jugadorRepository.getById(id)
        jugadorRepository.delete(jugadorAEliminar)
    }

    fun editJugador(jugadorEditado: JugadorFormDTO) {
        jugadorRepository.update(jugadorEditado.toJugador())
    }

    fun jugadorByID(id: Int): JugadorDTO {
        return jugadorRepository.getById(id).toJugadorDTO()
    }

    fun JugadorFormDTO.toJugador(): Jugador {
        val nuevoJugador: Jugador = Jugador(
            this.nombre,
            this.apellido,
            this.nacimiento.toLocalDate(),
            this.camiseta.toInt(),
            repositorioSeleccion.find(this.seleccion)!!,
            repositorioPosicion.find(this.posicion)!!,
            LocalDate.of(this.debut.toInt(), 1, 1),
            this.altura.toDouble(),
            this.peso.toDouble(),
            this.lider,
            this.cotizacion.toDouble(),
        )
        nuevoJugador.id = this.id
        return nuevoJugador
    }
}