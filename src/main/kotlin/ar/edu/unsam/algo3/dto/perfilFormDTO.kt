package ar.edu.unsam.algo3.dto

import ar.edu.unsam.algo3.domain.*
import java.time.LocalDate


data class PerfilFormDTO(
    val nombre:String,
    val apellido:String,
    val fechaDeNacimiento: LocalDate,
    val email:String,
    val direccion: Direccion,
    val distanciaDeCercania:Double,
    val tipoDeUsuario: String,
    val listadoNacionalista: MutableSet<Seleccion>?,
    val jugadorFanatico: InfoJugadorDTO?
    )

fun Usuario.toPerfilFormDTO() = PerfilFormDTO(
    nombre=this.nombre,
    apellido=this.apellido,
    fechaDeNacimiento= this.fechaNacimiento,
    email=this.email,
    direccion=this.direccion,
    distanciaDeCercania=this.distanciaDeBusqueda,
    tipoDeUsuario=this.factoryTipoDeUsuarioToString(this.tipoDeUsuario),
    listadoNacionalista= if (this.tipoDeUsuario is Nacionalista) (this.tipoDeUsuario as Nacionalista).seleccionesFavoritas else null,
    jugadorFanatico = if (this.tipoDeUsuario is Fanatico) (this.tipoDeUsuario as Fanatico).jugadorFavorito.toInfoJugadorDTO() else null,
    )

fun Usuario.factoryTipoDeUsuarioToString(tipo: TipoDeUsuario): String {
    return when (tipo) {
        is Par -> "Par"
        is Nacionalista -> "Nacionalista"
        is Conservador -> "Conservador"
        is Fanatico -> "Fanatico"
        is Desprendido -> "Desprendido"
        is Apostador -> "Apostador"
        is Interesado -> "Interesado"
        is Cambiante -> "Cambiante"
        else -> "Desconocido"
    }
}

fun Usuario.factoryTipoDeUsuarioFromString(tipoStr: String, jugador: Jugador?, listadoNacionalista: MutableSet<Seleccion>?): TipoDeUsuario {
    return when (tipoStr) {
        "Par" -> Par
        "Nacionalista" -> Nacionalista(listadoNacionalista!!)
        "Conservador" -> Conservador
        "Fanatico" -> Fanatico(jugador!!)
        "Desprendido" -> Desprendido
        "Apostador" -> Apostador
        "Interesado" -> Interesado
        "Cambiante" -> Cambiante
        else -> throw IllegalArgumentException("Tipo de usuario desconocido: $tipoStr")
    }
}