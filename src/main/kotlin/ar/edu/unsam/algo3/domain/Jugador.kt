package ar.edu.unsam.algo3.domain

import ar.edu.unsam.algo3.errors.ErrorHandler
import ar.edu.unsam.algo3.generic.aniosTranscurridos
import ar.edu.unsam.algo3.generic.coincideParcialemente
import ar.edu.unsam.algo3.generic.esPar
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

class Jugador(
    var nombre: String,
    var apellido: String,
    var fechaNacimiento: LocalDate,
    var nroCamiseta: Int,
    var seleccion: Seleccion,
    var posicion: Posicion,
    var anioDebutSeleccion: LocalDate,
    var altura: Double,
    var peso: Double,
    var esLider: Boolean,
    var cotizacion: Double
) : ErrorHandler, Entidad {
    init {
        this.validar()
    }

    override var id: Int = Entidad.ID_INICIAL

    companion object {
        val FACTOR_ALTURA = 1.80
    }

    fun pais(): String = seleccion.pais.take(3).uppercase()
    fun edad() = fechaNacimiento.aniosTranscurridos()
    fun valoracionJugador(): Double = posicion.valoracionJugador(this)

    fun antiguedad(): Int = anioDebutSeleccion.aniosTranscurridos()
    fun esCampeonDelMundo(): Boolean = seleccion.esCampeonDelMundo()
    fun cotizacionSuperaLos(cantidad: Double, tope: Double): Boolean = cantidad > tope

    fun esLigero(): Boolean = peso in 65.0..70.0

    fun alturaMayor(): Boolean = (this.altura >= FACTOR_ALTURA)

    fun esParCamiseta(): Boolean = nroCamiseta.esPar()
    fun nroCamisetaEntre(inicio: Int, fin: Int): Boolean = nroCamiseta in inicio..fin

    fun esPromesaDeFutbol(): Boolean {
        return this.edad() <= 22 &&
                !this.cotizacionSuperaLos(cotizacion, 20_000_000.0) &&
                this.antiguedad() <= 2
    }

    fun esLeyenda(): Boolean {
        return this.antiguedad() >= 10 &&
                (this.cotizacionSuperaLos(cotizacion, 20_000_000.0) ||
                        (this.nroCamisetaEntre(5, 10) && esLider))

    }

    fun seleccionConCopasPar() = seleccion.totalDeCopas().esPar()

    override fun validar() {
        var attrs: List<String> = listOf("NOMBRE", "APELLIDO", "AÑO DE DEBUT")
        listOf(nombre, apellido, anioDebutSeleccion).zip(attrs).forEach { unAtributo -> this.campoNulo(unAtributo, "") }

        var attrs2: List<String> = listOf("NRO DE CAMISETA", "ALTURA", "PESO", "COTIZACIÓN")
        listOf(nroCamiseta, altura, peso, cotizacion).zip(attrs2)
            .forEach { (campo, nombreCampo) -> this.nroNegativo(campo, nombreCampo) }

        var attrs3: List<String> = listOf("FECHA DE NACIMIENTO", "AÑO DEBUT DE SELECCIÓN")
        listOf(fechaNacimiento, anioDebutSeleccion).zip(attrs3)
            .forEach { (campo, nombreCampo) -> this.fechaPosterior(campo, nombreCampo) }

        this.nroEntre(nroCamiseta, 1, 99, attrs2[0])
    }

    override fun cumpleCriterioBusqueda(value: String): Boolean =
        nombre.coincideParcialemente(value) ||
                apellido.coincideParcialemente(value)

}
