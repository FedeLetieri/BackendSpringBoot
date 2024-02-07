package ar.edu.unsam.algo3.services

import ar.edu.unsam.algo3.domain.Figurita
import ar.edu.unsam.algo3.domain.Usuario
import ar.edu.unsam.algo3.dto.*
import ar.edu.unsam.algo3.errors.figuritaEsFaltanteEnAlgunUsuario
import ar.edu.unsam.algo3.errors.figuritaEsRepetidaEnAlgunUsuario
import ar.edu.unsam.algo3.repository.*
import ar.edu.unsam.algo3.repository.repositorioUsuario

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UsuarioService{
    @Autowired
    lateinit var usuarioRepository: repositorioUsuario

    @Autowired
    lateinit var jugadorRepository: repositorioJugador

    @Autowired
    lateinit var figuritaService: FiguritasService


    fun getUsuarioById(id:Int): Usuario = usuarioRepository.getById(id)

    fun actualizarPerfilUsuario(idUsuario: Int, perfilUsuario: PerfilUsuarioDTO): PerfilUsuarioDTO {
        val usuarioExistente = usuarioRepository.getById(idUsuario)
        usuarioExistente.username = perfilUsuario.username
        usuarioExistente.imagen = perfilUsuario.imagen
        usuarioRepository.update(usuarioExistente)
        return usuarioExistente.toPerfilUsuarioDTO()
    }

    fun actualizarInfoGeneralPerfilUsuario(idUsuario: Int, infoGeneralPerfilUsuario: PerfilFormDTO): PerfilFormDTO {
        val usuarioExistente = usuarioRepository.getById(idUsuario)
        usuarioExistente.nombre = infoGeneralPerfilUsuario.nombre
        usuarioExistente.apellido = infoGeneralPerfilUsuario.apellido
        usuarioExistente.fechaNacimiento = infoGeneralPerfilUsuario.fechaDeNacimiento
        usuarioExistente.email = infoGeneralPerfilUsuario.email
        usuarioExistente.direccion = infoGeneralPerfilUsuario.direccion
        usuarioExistente.distanciaDeBusqueda = infoGeneralPerfilUsuario.distanciaDeCercania
        usuarioExistente.tipoDeUsuario = usuarioExistente.factoryTipoDeUsuarioFromString(
            infoGeneralPerfilUsuario.tipoDeUsuario,
            infoGeneralPerfilUsuario.jugadorFanatico?.let { jugadorRepository.getById(it.id) },
            infoGeneralPerfilUsuario.listadoNacionalista
        )

        usuarioRepository.update(usuarioExistente)
        return usuarioExistente.toPerfilFormDTO()
    }

    fun agregarFiguritaFaltante(idUsuario: Int,idFiguritaFaltante: Int){
        getUsuarioById(idUsuario).registrarFaltante(figuritaService.obtenerFiguritaPorId(idFiguritaFaltante))
    }

    fun agregarFiguritaRepetida(idUsuario: Int, idFiguritaRepetida: Int){
        getUsuarioById(idUsuario).registrarRepetida(figuritaService.obtenerFiguritaPorId(idFiguritaRepetida))
    }

    fun eliminarFiguritaFaltante(idUsuario: Int,idFiguritaFaltante: Int){
        getUsuarioById(idUsuario).eliminarFaltante(figuritaService.obtenerFiguritaPorId(idFiguritaFaltante))
    }

    fun eliminarFiguritaRepetida(idUsuario: Int,idFiguritaRepetida: Int){
        getUsuarioById(idUsuario).eliminarRepetida(figuritaService.obtenerFiguritaPorId(idFiguritaRepetida))
    }
    fun obtenerFiguritasFaltantes(idUsuario: Int): List<Figurita> =  getUsuarioById(idUsuario).figuritasFaltantes.sortedBy { it.id }
    fun obtenerFiguritasRepetidas(idUsuario: Int): List<Figurita> =  getUsuarioById(idUsuario).figuritasRepetidas.sortedBy { it.id }
    fun uniqueFiguritasRepetidas(idUsuario: Int) = obtenerFiguritasRepetidas(idUsuario).toSet()
    fun obtenerFiguritasRepetidasTodos(filterCalls: Map<String, String>): List<FiguritaConDuegnoDTO> = usuarioRepository.figuritasRepetidasDeTodos(filterCalls)

    fun validarFigurita(figurita: Figurita){
        usuarioRepository.coleccion.forEach {
            validarFiguritaFaltante(figurita,it)
            validarFiguritaRepetida(figurita,it)
        }
    }

    fun validarFiguritaFaltante(figurita: Figurita,usuario: Usuario) {
        if(usuario.esFaltante(figurita)) throw figuritaEsFaltanteEnAlgunUsuario(usuario)
    }



    fun validarFiguritaRepetida(figurita: Figurita,usuario: Usuario) {
      if(usuario.esRepetida(figurita)) throw figuritaEsRepetidaEnAlgunUsuario(usuario)
    }
}