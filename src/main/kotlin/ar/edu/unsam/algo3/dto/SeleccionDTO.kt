package ar.edu.unsam.algo3.dto

import ar.edu.unsam.algo3.domain.Confederacion
import ar.edu.unsam.algo3.domain.Seleccion

class SeleccionDTO(val pais: String,
                   val confederacion: Confederacion,
                   val copasMundiales: Int,
                   val copasConfederacion: Int)

fun Seleccion.toDTO() = SeleccionDTO(this.pais, this.confederacion, this.cantidadCopasMundo, this.cantidadCopasConfederacion )


open class SeleccionFromDTO(val pais: String,
                            val confederacion: String,
                            val copasDelMundo: Int,
                            val id: Int = 0)

fun SeleccionFromDTO.toSeleccion() = Seleccion(this.pais, Confederacion.valueOf(this.confederacion), this.copasDelMundo, 1)