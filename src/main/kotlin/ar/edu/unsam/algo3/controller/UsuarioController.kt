package ar.edu.unsam.algo3.controller

import ar.edu.unsam.algo3.domain.Figurita
import ar.edu.unsam.algo3.domain.Provincia
import ar.edu.unsam.algo3.domain.Seleccion
import ar.edu.unsam.algo3.domain.Usuario
import ar.edu.unsam.algo3.dto.*
import ar.edu.unsam.algo3.errors.ObjetoNoEncontrado
import ar.edu.unsam.algo3.generic.RespuestaPersonalizada
import ar.edu.unsam.algo3.repository.repositorioSeleccion
import ar.edu.unsam.algo3.services.JugadorService
import ar.edu.unsam.algo3.services.ProvinciaService
import ar.edu.unsam.algo3.services.SeleccionService
import ar.edu.unsam.algo3.services.UsuarioService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin("*")
class UsuarioController {

    @Autowired
    lateinit var usuarioService: UsuarioService

    @Autowired
    lateinit var proviciaService: ProvinciaService


    //Perfil
    @GetMapping("/infoUsuario")
    @Operation(summary = "Devuelve toda la info del usuario")
    fun getInfoUsuario(@RequestParam("id") idUsuarioABuscar: Int): UsuarioDTO {
        return usuarioService.getUsuarioById(idUsuarioABuscar).toUsuarioDTO()
    }

    //Perfil
    @GetMapping("/perfilUsuario")
    @Operation(summary = "Devuelve el perfil del usuario")
    fun getPerfilDeUsuario(@RequestParam("id") idUsuarioABuscar: Int): PerfilUsuarioDTO {
        return usuarioService.getUsuarioById(idUsuarioABuscar).toPerfilUsuarioDTO()
    }

    @PutMapping("/perfilUsuario")
    @Operation(summary = "Update del perfil de usuario")
    fun updatePerfilDeUsuario(@RequestParam("id") idUsuarioAModificar: Int, @RequestBody perfilUsuario: PerfilUsuarioDTO): PerfilUsuarioDTO {
        return usuarioService.actualizarPerfilUsuario(idUsuarioAModificar, perfilUsuario)
    }


    //Formulario

    @GetMapping("/perfilInfoGeneralUsuario")
    @Operation(summary = "Devuelve la informacion general del usuario")
    fun getInfoGeneralDeUsuario(@RequestParam("id") idUsuarioABuscar: Int): PerfilFormDTO {
        return usuarioService.getUsuarioById(idUsuarioABuscar).toPerfilFormDTO()
    }

    @PutMapping("/perfilInfoGeneralUsuario")
    @Operation(summary = "Update de informacion general del perfil de usuario")
    fun updateInfoGeneralPerfilDeUsuario(@RequestParam("id") idUsuarioAModificar: Int, @RequestBody infoGeneralPerfilUsuario: PerfilFormDTO): PerfilFormDTO {
        return usuarioService.actualizarInfoGeneralPerfilUsuario(idUsuarioAModificar, infoGeneralPerfilUsuario)
    }

    //Formulario - Provincias y localidades

    @GetMapping("/provincias")
    @Operation(summary = "Devuelve todas las provincias disponibles")
    fun getProvincias(): List<String> {
        return proviciaService.getListadoProvincias()
    }
    @GetMapping("/localidades")
    @Operation(summary = "Devuelve las localidades por nombre de provincia")
    fun getProvincias(@RequestParam("provincia") provinciaABuscar: String): MutableSet<String> {
        val localidades = proviciaService.getProvinciaByName(provinciaABuscar).firstOrNull()?.localidades
        if (localidades != null) {
            return localidades
        } else {
            throw ObjetoNoEncontrado("No se encontraron localidades para la provincia: $provinciaABuscar")
        }
    }

    //Figuritas Faltantes
    @GetMapping("/figuritasFaltantes")
    @Operation(summary = "Devuelve solo las figuritas que son faltantes para el usuario")
    fun soloFiguritasFaltantes(@RequestParam idUsuario:Int) = usuarioService.obtenerFiguritasFaltantes(idUsuario).map{ it.toFiguritaDTO()}

    @PutMapping("/figuritasFaltantes")
    @Operation(summary = "agregar la figurita a la lista de figuritas faltantes")
    fun agregarFiguritaFaltante(@RequestParam idUsuario:Int, @RequestParam idFiguritaFaltante:Int):ResponseEntity<RespuestaPersonalizada>{
        usuarioService.agregarFiguritaFaltante(idUsuario, idFiguritaFaltante)

        return ResponseEntity.ok(RespuestaPersonalizada("Figurita agregada a faltante"))
    }

    @DeleteMapping("/figuritasFaltantes")
    @Operation(summary = "Elimina una figurita por ID")
    fun deleteFigurita(@RequestParam idUsuario:Int, @RequestParam idFiguritaFaltante:Int): ResponseEntity<RespuestaPersonalizada> {
        usuarioService.eliminarFiguritaFaltante(idUsuario, idFiguritaFaltante)

        return ResponseEntity.ok(RespuestaPersonalizada("Figurita eliminada de faltante"))
    }


    //Figuritas Repetidas
    @GetMapping("/figuritasRepetidas")
    @Operation(summary = "Devuelve solo las figuritas que son repetidas para el usuario")
    fun soloFiguritasRepetidas(@RequestParam idUsuario:Int) = usuarioService.obtenerFiguritasRepetidas(idUsuario).map{ it.toFiguritaDTO() }

    @GetMapping("/uniqueFiguritasRepetidas")
    @Operation(summary = "Devuelve solo las figuritas que son repetidas para el usuario")
    fun uniqueFiguritasRepetidas(@RequestParam idUsuario:Int) = usuarioService.uniqueFiguritasRepetidas(idUsuario).map{ it.toFiguritaDTO() }

    @PutMapping("/figuritasRepetidas")
    @Operation(summary = "agregar la figurita a la lista de figuritas repetidas")
    fun agregarFiguritaRepetida(@RequestParam idUsuario:Int, @RequestParam idFiguritaRepetida:Int):ResponseEntity<RespuestaPersonalizada>{
        usuarioService.agregarFiguritaRepetida(idUsuario, idFiguritaRepetida)

        return ResponseEntity.ok(RespuestaPersonalizada("Figurita agregada a repetida"))
    }

    @DeleteMapping("/figuritasRepetidas")
    @Operation(summary = "Elimina una figurita por ID")
    fun deleteFiguritaRepetida(@RequestParam idUsuario:Int, @RequestParam idFiguritaRepetida:Int): ResponseEntity<RespuestaPersonalizada> {
        usuarioService.eliminarFiguritaRepetida(idUsuario, idFiguritaRepetida)

        return ResponseEntity.ok(RespuestaPersonalizada("Figurita eliminada de repetida"))
    }


}