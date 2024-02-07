package ar.edu.unsam.algo3.domain

interface Entidad {
    companion object {
        const val ID_INICIAL = 0
    }

    var id: Int

    fun cumpleCriterioBusqueda(value: String): Boolean

    fun esNuevo(): Boolean = id == 0
}