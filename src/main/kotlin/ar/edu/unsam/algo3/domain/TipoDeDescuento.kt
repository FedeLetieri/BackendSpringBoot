package ar.edu.unsam.algo3.domain

import ar.edu.unsam.algo3.errors.ErrorHandler
import java.time.DayOfWeek
import java.time.LocalDate

interface TipoDeDescuento {
    fun descuento(cantidadSobres: Int = 0): Double = if(condicionDescuento(cantidadSobres)) cantidadDescuento(cantidadSobres) else 0.0
    fun condicionDescuento(cantidadSobres: Int): Boolean
    fun cantidadDescuento(cantidadSobres: Int): Double
}

object Jueves: TipoDeDescuento {
    override fun condicionDescuento(cantidadSobres: Int) = diaActual() == DayOfWeek.THURSDAY.value
    override fun cantidadDescuento(cantidadSobres: Int): Double = 10.0
    fun diaActual(): Int = LocalDate.now().dayOfWeek.value
}

object Primeros10DiasMes: TipoDeDescuento {
    override fun condicionDescuento(cantidadSobres: Int) = fechaActual() <= 10
    override fun cantidadDescuento(cantidadSobres: Int): Double = 5.0
    fun fechaActual(): Int = LocalDate.now().dayOfMonth
}

object CompraMayor200Sobres: TipoDeDescuento {
    override fun condicionDescuento(cantidadSobres: Int) = cantidadSobres > 200
    override fun cantidadDescuento(cantidadSobres: Int): Double = 45.0
}

object SinDescuento: TipoDeDescuento {
    override fun condicionDescuento(cantidadSobres: Int) = false
    override fun cantidadDescuento(cantidadSobres: Int) = 0.0
    override fun descuento(cantidadSobres: Int) = cantidadDescuento(cantidadSobres)
}

class Combinatoria(var tiposDeDescuento: MutableSet<TipoDeDescuento>): TipoDeDescuento, ErrorHandler {

    init { this.validar() }
    override fun validar() { this.setVacio(tiposDeDescuento, "TIPOS DE DESCUENTO") }

    override fun condicionDescuento(cantidadSobres: Int): Boolean {
        sumaDescuentos(cantidadSobres)
        return tiposDeDescuento.any { tipoDeDescuento -> tipoDeDescuento.condicionDescuento(cantidadSobres) }
    }
    override fun cantidadDescuento(cantidadSobres: Int) = minOf(50.0, sumaDescuentos(cantidadSobres))
    fun sumaDescuentos(cantidadSobres: Int) = tiposDeDescuento.sumOf { it.descuento(cantidadSobres) }
}