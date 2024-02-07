package ar.edu.unsam.algo2.worldcapp

import ar.edu.unsam.algo3.errors.ErrorHandler
import ar.edu.unsam.algo3.generic.diasTranscurridos
import ar.edu.unsam.algo3.generic.fechaAnteriorAHoy
import java.time.LocalDate

data class Pedido(
    var cantidadSobres: Int,
    var fechaEntrega: LocalDate
): ErrorHandler {
    init {
        this.validar()
    }


    fun diasFaltantesParaFechaPactada() = fechaEntrega.diasTranscurridos()
    fun pendiente(diasParaIngresarPedidos: Int): Boolean = diasFaltantesParaFechaPactada() <= diasParaIngresarPedidos

    fun sinProcesar():Boolean = fechaEntrega.fechaAnteriorAHoy()

    override fun validar() {
        this.nroNegativo(cantidadSobres, "CANTIDAD SOBRES")
        this.campoNulo(cantidadSobres, "CANTIDAD SOMBRES")

        this.fechaAnterior(fechaEntrega, "FECHA DE ENTREGA")
    }
}
