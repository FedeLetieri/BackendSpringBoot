package ar.edu.unsam.algo3.domain

import ar.edu.unsam.algo3.errors.ErrorHandler
import ar.edu.unsam.algo3.generic.esExactamente
import com.fasterxml.jackson.annotation.JsonProperty

class Seleccion(
    @JsonProperty("pais") var pais: String,
    @JsonProperty("confederacion") var confederacion: Confederacion,
    @JsonProperty("copasDelMundo") var cantidadCopasMundo: Int,
    @JsonProperty("copasConfederacion") var cantidadCopasConfederacion: Int
) : ErrorHandler, Entidad {

    init {
        this.validar()
    }

    @JsonProperty("id") override var id: Int = Entidad.ID_INICIAL

    fun esCampeonDelMundo(): Boolean = cantidadCopasMundo > 0
    fun totalDeCopas(): Int = cantidadCopasMundo + cantidadCopasConfederacion
    override fun validar() {
        this.campoNulo(confederacion, "CONFEDERACION")
    }

    override fun cumpleCriterioBusqueda(value: String): Boolean = pais.esExactamente(value)

    fun esIgualAlPais(seleccionAComparar: Seleccion):Boolean = this == seleccionAComparar
}



enum class Confederacion {
    AFC, CAF, CONCACAF, CONMEBOL, OFC, UEFA
}