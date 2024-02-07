package ar.edu.unsam.algo3.dto

import ar.edu.unsam.algo2.worldcapp.Pedido
import ar.edu.unsam.algo3.domain.*
import ar.edu.unsam.algo3.services.UsuarioService

data class PuntoDeVentaDto(
    val nombre: String,
    val direccion: Direccion,
    val distancia: Double,
    val stock: Int,
    val precio: Double,
    val tipo:String
)

fun PuntoDeVenta.toDTO(usuarioService: UsuarioService, idUsuario: Int) = PuntoDeVentaDto(
    nombre = this.nombre,
    direccion = this.direccion,
    distancia = this.distanciaEnvio(usuarioService.getUsuarioById(idUsuario)),
    stock = this.stockSobres,
    precio = this.costoEnvio(usuarioService.getUsuarioById(idUsuario)),
    tipo = this::class.simpleName!!)



data class PuntoDeVentaAdminDto(
    val id: Int,
    val nombre: String,
    val direccion: Direccion,
    val stockSobres: Int,
    val tipo:String,
)

fun PuntoDeVenta.toAdminDTO() = PuntoDeVentaAdminDto(
    id = this.id,
    nombre = this.nombre,
    direccion = this.direccion,
    stockSobres = this.stockSobres,
    tipo = this::class.simpleName!!)


fun PuntoDeVentaAdminDto.factoryTipoDePuntoDeVenta(tipo: String, puntoDeVentaAdminDto: PuntoDeVentaAdminDto): PuntoDeVenta {
    val pedidos: MutableList<Pedido> = mutableListOf()
    return when (tipo) {
        "Kiosko" -> Kiosco(puntoDeVentaAdminDto.nombre, puntoDeVentaAdminDto.direccion, puntoDeVentaAdminDto.stockSobres, pedidos, false)
        "Libreria" -> Libreria(puntoDeVentaAdminDto.nombre, puntoDeVentaAdminDto.direccion, puntoDeVentaAdminDto.stockSobres, pedidos)
        "Supermercado" -> Supermercado(puntoDeVentaAdminDto.nombre, puntoDeVentaAdminDto.direccion, puntoDeVentaAdminDto.stockSobres, pedidos, SinDescuento)
        else -> throw IllegalArgumentException("Tipo de punto de venta desconocido: $tipo")
    }
}