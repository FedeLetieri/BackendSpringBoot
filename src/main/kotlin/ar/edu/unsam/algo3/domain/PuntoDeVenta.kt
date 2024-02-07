package ar.edu.unsam.algo3.domain

import ar.edu.unsam.algo2.worldcapp.*
import ar.edu.unsam.algo3.errors.ErrorHandler
import ar.edu.unsam.algo3.errors.StockNoSuficiente
import ar.edu.unsam.algo3.generic.esExactamente
import kotlin.math.ceil

abstract class PuntoDeVenta(
    var nombre: String,
    var direccion: Direccion,
    var stockSobres: Int,
    var pedidosPendientes: MutableList<Pedido>


) : Entidad, ErrorHandler {
    companion object {
        val COSTO_MINIMO_FIGURITA: Double = 170.0
        val KM_MINIMO: Double = 10.0
        val COSTO_MINIMO_10_KM: Double = 1000.0
        val COSTO_ADICIONAL_POR_KM: Double = 100.0
    }

    override var id: Int = Entidad.ID_INICIAL

    init {
        this.validar()
    }

    override fun validar() {
        this.stringVacio(nombre, "NOMBRE")
        this.nroNegativo(stockSobres, "STOCK SOBRES")
    }

    fun hayDisponibilidad(): Boolean = stockSobres > 0

    fun agregarPedidoPendiente(pedido: Pedido) = pedidosPendientes.add(pedido)

    fun removerPedidoPendiente(pedido: Pedido) = pedidosPendientes.remove(pedido)


    fun importeACobrar(usuario: Usuario, cantidadSobres: Int): Double {
        this.stockDeSobresParaCantidadDeSobresRequerida(cantidadSobres)
        return factorFijo(usuario, cantidadSobres) * factorCambiante(cantidadSobres)
    }

    fun stockDeSobresParaCantidadDeSobresRequerida(cantidadSobres: Int) {
        if (stockSobres < cantidadSobres) throw StockNoSuficiente()
    }

    fun factorFijo(usuario: Usuario, cantidadSobres: Int) =
        (COSTO_MINIMO_FIGURITA * cantidadSobres) + costoEnvio(usuario)

    abstract fun factorCambiante(cantidadSobres: Int): Double

    fun costoEnvio(usuario: Usuario) =
        maxOf(0.0, diferenciaDeDistancia(usuario)) * COSTO_ADICIONAL_POR_KM + COSTO_MINIMO_10_KM

    fun diferenciaDeDistancia(usuario: Usuario) = ceil(distanciaEnvio(usuario) - KM_MINIMO)
    fun distanciaEnvio(usuario: Usuario):Double = this.direccion.distanciaEntre(usuario.direccion)

    fun esActivo(diasParaIngresarPedidos: Int): Boolean = hayDisponibilidad() ||
            tienePedidosSinProcesar() ||
            hayPedidosPendientesQueIngresanPronto(diasParaIngresarPedidos)

    fun hayPedidosPendientesQueIngresanPronto(diasParaIngresarPedidos: Int) =
        pedidosPendientes.any { pedido -> pedido.pendiente(diasParaIngresarPedidos) }

    fun tienePedidosSinProcesar() = pedidosPendientes.any { pedido -> pedido.sinProcesar() }
    override fun cumpleCriterioBusqueda(value: String): Boolean = nombre.esExactamente(value)

    fun actualizarStock() {
        pedidosSinProcesar().toList().forEach {
            stockSobres += it.cantidadSobres
            removerPedidoPendiente(it)
        }
    }

    fun pedidosSinProcesar() = pedidosPendientes.filter { it.sinProcesar() }
}


class Libreria(
    nombre: String,
    direccion: Direccion,
    stockSobres: Int,
    pedidosPendientes: MutableList<Pedido>
) :
    PuntoDeVenta(
        nombre, direccion, stockSobres,
        pedidosPendientes
    ) {

    companion object {
        val diasParaIngresarPedidos: Int = 10
        val porcentajePedidosPendientePronto = 1.10
        val porcentajePedidosPendienteNoPronto = 1.05
    }

    override fun factorCambiante(cantidadSobres: Int): Double =
        if (hayPedidosPendientesQueIngresanPronto(diasParaIngresarPedidos)) porcentajePedidosPendientePronto else porcentajePedidosPendienteNoPronto

}

class Kiosco(
    nombre: String,
    direccion: Direccion,
    stockSobres: Int,
    pedidosPendientes: MutableList<Pedido>,
    var tieneEmpleados: Boolean
) :
    PuntoDeVenta(
        nombre, direccion, stockSobres,
        pedidosPendientes

    ) {
    override fun factorCambiante(cantidadSobres: Int): Double = if (tieneEmpleados) 1.25 else 1.1
}

class Supermercado(
    nombre: String,
    direccion: Direccion,
    stockSobres: Int,
    pedidosPendientes: MutableList<Pedido>,
    var tipoDeDescuento: TipoDeDescuento
) :
    PuntoDeVenta(
        nombre, direccion, stockSobres,
        pedidosPendientes

    ) {
    override fun factorCambiante(cantidadSobres: Int): Double = 1 - tipoDeDescuento.descuento(cantidadSobres) / 100
}