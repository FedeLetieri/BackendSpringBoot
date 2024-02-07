package ar.edu.unsam.algo3.dto

import ar.edu.unsam.algo3.domain.*

open class FiguritaDTO(
    val id: Int,
    val numero: Int,
    val onFire: Boolean,
    val nivelImpresion: NivelImpresion,
    val jugador: JugadorDTO,
    val valorBase: Double,
    val valorTotal: Double
)

data class FiguritaFormDTO(
    val id: Int,
    val numero: Int,
    val onFire: Boolean,
    val nivelImpresion: String,
    val idJugador: Int
)

class FiguritaConDuegnoDTO(
    id: Int,
    numero: Int,
    onFire: Boolean,
    nivelImpresion: NivelImpresion,
    jugador: JugadorDTO,
    valBase: Double,
    valTotal: Double,
    val duegno: DuegnoDTO
): FiguritaDTO(id, numero, onFire, nivelImpresion, jugador, valBase, valTotal)

class DuegnoDTO(val id: Int, val nombre: String)

fun Usuario.toDuegnoDTO() = DuegnoDTO(this.id, this.nombre)
fun Figurita.toFiguritaDTO() = FiguritaDTO(
    this.id,
    this.numero,
    this.onFire,
    this.nivelImpresion,
    this.jugador.toJugadorDTO(),
    this.valoracionBase(),
    this.valoracion()
)

fun Figurita.toFiguritaConDuegnoDTO(usuario: Usuario) = FiguritaConDuegnoDTO(
    this.id,
    this.numero,
    this.onFire,
    this.nivelImpresion,
    this.jugador.toJugadorDTO(),
    this.valoracionBase(),
    this.valoracion(),
    usuario.toDuegnoDTO()
)

fun FiguritaFormDTO.toFigurita(jugador:Jugador):Figurita = Figurita(
    numero = this.numero,
    onFire = this.onFire,
    nivelImpresion = this.factoryNivelDeImpresionFromString(this.nivelImpresion),
    jugador = jugador
)

fun FiguritaFormDTO.factoryNivelDeImpresionFromString(value : String): NivelImpresion{
    return when (value) {
        "BAJA" -> NivelImpresion.BAJA
        "MEDIA" -> NivelImpresion.MEDIA
        "ALTA" -> NivelImpresion.ALTA
        else -> throw IllegalArgumentException("Tipo de impresion desconocido: $value")
    }
}