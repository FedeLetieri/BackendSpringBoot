package ar.edu.unsam.algo3.services

import ar.edu.unsam.algo3.dto.CountDataDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CountDataService {
    @Autowired
    lateinit var usuarioService: UsuarioService

    @Autowired
    private lateinit var figuritasService: FiguritasService

    @Autowired
    lateinit var sobresServices: SobresServices

    fun countData(userId: Int): CountDataDTO{
        val user = usuarioService.getUsuarioById(userId)
        return CountDataDTO(user.figuritasFaltantes.size,
            user.figuritasRepetidas.size,
            sobresServices.obtenerSobres("cercanos", userId, "").size,
            usuarioService.usuarioRepository.search("").size)   
    }

}