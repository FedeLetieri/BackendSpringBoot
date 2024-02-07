package ar.edu.unsam.algo3.services

import ar.edu.unsam.algo3.domain.Figurita
import ar.edu.unsam.algo3.domain.Provincia
import ar.edu.unsam.algo3.repository.repositorioProvincia
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProvinciaService {

    @Autowired
    lateinit var provinciaRepository: repositorioProvincia

    fun getProvinciaById(id:Int): Provincia = provinciaRepository.getById(id)

    fun getProvinciaByName(name:String): List<Provincia> = provinciaRepository.search(name)

    fun getListadoProvincias(): List<String> = provinciaRepository.coleccion.map { provincia -> provincia.nombre }
}