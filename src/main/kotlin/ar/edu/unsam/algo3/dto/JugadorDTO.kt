package ar.edu.unsam.algo3.dto

import ar.edu.unsam.algo3.domain.Jugador
import ar.edu.unsam.algo3.generic.toLocalDate
import ar.edu.unsam.algo3.repository.repositorioPosicion
import ar.edu.unsam.algo3.repository.repositorioSeleccion
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate

data class JugadorDTO(  
    val id: Int,
    val nombre: String,
    val nroCamiseta: Int,
    val nacimiento: LocalDate,
    val seleccion: SeleccionDTO,
    val posicion: String,
    val peso: Double,
    val altura: Double,
    val precio: Double,
    val promesa: Boolean,
    val agnoDebut: Int,
    val lider: Boolean
)

data class InfoJugadorDTO(
    val id: Int,
    val nombre: String,
    val apellido: String
)

fun Jugador.toJugadorDTO() = JugadorDTO(
    this.id,
    this.nombre + " " + this.apellido,
    this.nroCamiseta,
    this.fechaNacimiento,
    this.seleccion.toDTO(),
    this.posicion.nombre,
    this.peso,
    this.altura,
    this.cotizacion,
    this.esPromesaDeFutbol(),
    this.anioDebutSeleccion.year,
    this.esLider
)

fun Jugador.toInfoJugadorDTO() = InfoJugadorDTO(
    this.id,
    this.nombre,
    this.apellido,
)

data class JugadorFormDTO(
    val id: Int,
    val altura: String,
    val apellido: String,
    val camiseta: String,
    val cotizacion: String,
    val debut: String,
    val lider: Boolean,
    val nacimiento: String,
    val nombre: String,
    val peso: String,
    val posicion: String,
    val seleccion: String,
)