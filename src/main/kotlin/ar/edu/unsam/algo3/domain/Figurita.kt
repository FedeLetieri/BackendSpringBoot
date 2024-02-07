package ar.edu.unsam.algo3.domain
import ar.edu.unsam.algo3.errors.ErrorHandler
import ar.edu.unsam.algo3.generic.coincideParcialemente
import ar.edu.unsam.algo3.generic.esExactamente
import ar.edu.unsam.algo3.generic.esIgual
import ar.edu.unsam.algo3.generic.esPar

class Figurita(
    var numero: Int,
    var onFire: Boolean,
    var nivelImpresion: NivelImpresion,
    var jugador: Jugador,
): ErrorHandler, Entidad {
    companion object {
        val VALOR_BASE = 100.0
        val FACTOR_ONFIRE = 1.2
        val FACTOR_ESPAR = 1.1
        val FACTOR_IMPRESION_NO_BAJA = 0.85
    }

    init{
        this.validar()
    }

    override var id: Int = Entidad.ID_INICIAL
    override fun validar(){
        this.nroNegativo(numero,"NUMERO")

        var attrs: List<String> = listOf("ON FIRE", "NIVEL DE IMPRESIÓN", "JUGADOR")
        listOf(onFire, nivelImpresion, jugador).zip(attrs).forEach{(campo, nombreCampo) -> this.campoNulo(campo,nombreCampo)}
    }

    fun valoracion() = this.valoracionBase() + this.jugador.valoracionJugador()
    fun valoracionBase(): Double = valorPorEsPar() * factorNivelImpresion()

    fun valorPorOnFire() = VALOR_BASE * factorOnFire()
    fun factorOnFire() = if(onFire) FACTOR_ONFIRE else 1.0

    fun valorPorEsPar() = valorPorOnFire() * factorEsPar()
    fun factorEsPar() = if(esPar()) FACTOR_ESPAR else 1.0
    fun esPar() = numero.esPar()

    fun factorNivelImpresion() = if(esNivel(NivelImpresion.BAJA)) 1.0 else FACTOR_IMPRESION_NO_BAJA
    fun esNivel(nivel: NivelImpresion) = nivelImpresion == nivel

    fun jugadorCamisetaPar() = jugador.esParCamiseta()
    fun jugadorConSeleccionConCoparPar() = jugador.seleccionConCopasPar()
    fun seleccion(): Seleccion = jugador.seleccion
    fun esImpresionALTA() = esNivel(NivelImpresion.ALTA)
    fun tieneJugadorLeyenda() = jugador.esLeyenda()
    fun tieneJugadorPromesa() = jugador.esPromesaDeFutbol()

    fun esIgualAlPais(pais: Seleccion) = this.seleccion() == pais

    fun numeroIgual(numeroAComparar: Int) = this.numero == numeroAComparar  
    fun descripcionFigurita() = "nro:${this.numero} de valoracion:${this.valoracion()} de seleccion: ${this.seleccion()}"

    override fun cumpleCriterioBusqueda(value: String): Boolean =
        jugador.nombre.coincideParcialemente(value)
                || this.jugador.apellido.coincideParcialemente(value)
                || this.numero.esIgual(value)
                || this.jugador.seleccion.pais.esExactamente(value)
}

enum class NivelImpresion {
    BAJA, MEDIA, ALTA
}