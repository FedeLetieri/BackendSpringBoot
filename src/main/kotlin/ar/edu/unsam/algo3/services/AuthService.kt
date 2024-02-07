package ar.edu.unsam.algo3.services

import ar.edu.unsam.algo3.domain.Usuario
import ar.edu.unsam.algo3.dto.AuthDTO
import ar.edu.unsam.algo3.repository.repositorioUsuario
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AuthService {
    @Autowired
    lateinit var authRepository: repositorioUsuario

    fun validarLogin(loginRequest: AuthDTO): Int? {
        if (existeUsuario(loginRequest)) {
            val usuarioEncontrado = encontrarUsuario(loginRequest)
            if (usuarioEncontrado!!.esMismoPassword(loginRequest.password)) {
                return usuarioEncontrado.id
            }
        }
        return null
    }
    fun existeUsuario(loginRequest: AuthDTO) = todosLosUsername().contains(loginRequest.username)
    fun encontrarUsuario(loginRequest: AuthDTO) = todosLosUsuarios().find { it.username == loginRequest.username }
    fun todosLosUsuarios() = authRepository.coleccion
    fun todosLosUsername() = authRepository.coleccion.map { it.username }

    fun cerrarSesion(): Boolean = true
}