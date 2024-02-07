package ar.edu.unsam.algo3.dto

import ar.edu.unsam.algo3.domain.Usuario

data class PerfilUsuarioDTO(val username:String, val imagen:String)

fun Usuario.toPerfilUsuarioDTO() = PerfilUsuarioDTO(username=this.username, imagen=this.imagen,)