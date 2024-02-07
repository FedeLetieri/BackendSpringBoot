package ar.edu.unsam.algo3.domain

import ar.edu.unsam.algo3.errors.ErrorHandler
import org.uqbar.geodds.Point

class Direccion(
    var provincia:String,
    var localidad:String,
    var calle:String,
    var numero:Int,
    var ubicacion: Point
): ErrorHandler {
    init{
        this.validar()
    }

    override fun validar(){
        val attrs: List<String> = listOf("PROVINCIA", "LOCALIDAD", "CALLE")
        listOf(provincia, localidad, calle).zip(attrs).forEach{ (campo, nombreCampo) -> this.stringVacio(campo, nombreCampo) }
        this.nroNegativo(numero,"NUMERO")
        this.campoNulo(ubicacion,"UBICACION")
    }

    fun distanciaEntre(direccion: Direccion) = ubicacion.distance(direccion.ubicacion)
}