package ar.edu.unsam.algo2.worldcapp

import ar.edu.unsam.algo3.domain.Seleccion
import ar.edu.unsam.algo3.repository.Repositorio
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

class ActualizadorSelecciones(val repoSelecciones: Repositorio<Seleccion>, var serviceSeleccion: ServiceSelecciones) {
    fun actualizarSelecciones() {
        convertirALista().forEach({ creaOActualizaRegitro(it) })

    }

    fun creaOActualizaRegitro(registro : Seleccion) = if(registro.esNuevo()) repoSelecciones.create(registro) else repoSelecciones.update(registro)

    fun convertirALista(): List<Seleccion> {
        return ObjectMapper().readValue(serviceSeleccion.getSelecciones())
    }
}

interface ServiceSelecciones {
    fun getSelecciones(): String
}