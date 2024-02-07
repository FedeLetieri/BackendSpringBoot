package ar.edu.unsam.algo3.dto

import ar.edu.unsam.algo3.domain.*
import java.time.LocalDate

data class UsuarioDTO(
    val nombre:String,
    val apellido:String,
    val fechaDeNacimiento: LocalDate,
    val email:String,
    val direccion: Direccion,
    val distanciaDeCercania:Double,
    val tipoDeUsuario: String,
    val listadoNacionalista: MutableSet<Seleccion>?,
    val jugadorFanatico: InfoJugadorDTO?,
    val username:String,
    val imagen:String
)

fun Usuario.toUsuarioDTO() = UsuarioDTO(
    nombre=this.nombre,
    apellido=this.apellido,
    fechaDeNacimiento= this.fechaNacimiento,
    email=this.email,
    direccion=this.direccion,
    distanciaDeCercania=this.distanciaDeBusqueda,
    tipoDeUsuario=this.factoryTipoDeUsuarioToString(this.tipoDeUsuario),
    listadoNacionalista= if (this.tipoDeUsuario is Nacionalista) (this.tipoDeUsuario as Nacionalista).seleccionesFavoritas else null,
    jugadorFanatico = if (this.tipoDeUsuario is Fanatico) (this.tipoDeUsuario as Fanatico).jugadorFavorito.toInfoJugadorDTO() else null,
    username=this.username,
    imagen=this.imagen)

data class ExceptionDTO(val message: String)

fun Exception.toExceptionDTO() = ExceptionDTO(this.message ?: "Hubo un problema al realizar la solicitud")