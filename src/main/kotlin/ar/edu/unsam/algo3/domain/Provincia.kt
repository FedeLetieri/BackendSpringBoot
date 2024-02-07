package ar.edu.unsam.algo3.domain

import ar.edu.unsam.algo3.generic.coincideParcialemente
import ar.edu.unsam.algo3.generic.esExactamente
import ar.edu.unsam.algo3.generic.esIgual

class Provincia(var nombre:String, var localidades:MutableSet<String>): Entidad{

    override var id: Int = Entidad.ID_INICIAL

    override fun cumpleCriterioBusqueda(value: String): Boolean = nombre.coincideParcialemente(value)
}
