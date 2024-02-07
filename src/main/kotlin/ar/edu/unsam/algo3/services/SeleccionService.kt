package ar.edu.unsam.algo3.services

import ar.edu.unsam.algo3.domain.Confederacion
import ar.edu.unsam.algo3.domain.Seleccion
import ar.edu.unsam.algo3.domain.Usuario
import ar.edu.unsam.algo3.dto.ConfederacionDTO
import ar.edu.unsam.algo3.dto.SeleccionFromDTO
import ar.edu.unsam.algo3.dto.toSeleccion
import ar.edu.unsam.algo3.repository.repositorioSeleccion
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SeleccionService {

    @Autowired
    lateinit var seleccionRepository: repositorioSeleccion

    fun getSelecciones(search: String?) = if(search == null) seleccionRepository.getSelecciones() else seleccionRepository.getSelecciones(search)
    fun deleteSeleccion(id: String){
        val seleccion = seleccionRepository.getById(id.toInt())
        seleccionRepository.delete(seleccion)
    }

    fun updateSeleccion(seleccion: SeleccionFromDTO) {
        val seleccionObj = seleccion.toSeleccion()
        seleccionObj.id = seleccion.id!!
        seleccionRepository.update(seleccionObj)
    }
    fun createSeleccion(seleccion: SeleccionFromDTO) = seleccionRepository.create(seleccion.toSeleccion())
    fun getConfederaciones() = Confederacion.entries.map { ConfederacionDTO(it.name) }
}