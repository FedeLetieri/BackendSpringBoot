package ar.edu.unsam.algo3.domain

import ar.edu.unsam.algo2.worldcapp.ActualizadorSelecciones
import ar.edu.unsam.algo3.services.Mail
import ar.edu.unsam.algo3.services.MailSender
import ar.edu.unsam.algo3.repository.Repositorio

abstract class Proceso(val tipoProceso: String, val mailSender: MailSender) {
    fun contenido(): String = "Se realizó el proceso: $tipoProceso"

    abstract fun realizar()

    fun ejecutar() {
        realizar()
        enviarMail()
    }

    fun enviarMail() {
        mailSender.sendMail(
            Mail("admin@worldcapp.com.ar", "admin@worldcapp.com.ar", contenido(), contenido())
        )
    }
}

class ActualizarSelecciones(
    tipoProceso: String,
    mailSender: MailSender,
    val actualizadorSelecciones: ActualizadorSelecciones
) : Proceso(tipoProceso, mailSender) {
    override fun realizar() {
        actualizadorSelecciones.actualizarSelecciones()
    }
}

class ActualizarOnFire(
    tipoProceso: String,
    mailSender: MailSender,
    val repoFigurita: Repositorio<Figurita>,
    val nroDeFiguritas: List<String>
) : Proceso(tipoProceso, mailSender) {
    override fun realizar() {
        cambiarAOnfire()
        updateAOnFire()
    }

    fun buscarFiguritas(): List<Figurita> = nroDeFiguritas.flatMap { repoFigurita.search(it) }
    fun cambiarAOnfire() {
        buscarFiguritas().forEach { it.onFire = true }
    }

    fun updateAOnFire() {
        buscarFiguritas().forEach { repoFigurita.update(it) }
    }
}

    class BorrarPuntosDeVentasInactivos(
        tipoProceso: String,
        mailSender: MailSender,
        val repoPuntosDeVentas: Repositorio<PuntoDeVenta>
) : Proceso(tipoProceso, mailSender) {
    val diasParaPedidosAFabricaPorRecibir: Int = 90
    override fun realizar() {
        puntosDeVentasInactivos().forEach { repoPuntosDeVentas.delete(it) }
    }

    fun puntosDeVentasInactivos(): List<PuntoDeVenta> =
        repoPuntosDeVentas.coleccion.filter { !(it.esActivo(diasParaPedidosAFabricaPorRecibir)) }
}

class BorrarUsuariosInactivos(tipoProceso: String, mailSender: MailSender, val repoUsuarios: Repositorio<Usuario>) :
    Proceso(tipoProceso, mailSender) {
    override fun realizar() {
        usuariosInactivos().forEach({ repoUsuarios.delete(it) })
    }

    fun usuariosInactivos(): List<Usuario> = repoUsuarios.coleccion.filter { !it.esActivo() }
}

class ActualizarStockSobresPuntosDeVenta(
    tipoProceso: String,
    mailSender: MailSender,
    val repoPuntosDeVentas: Repositorio<PuntoDeVenta>
) : Proceso(tipoProceso, mailSender) {
    override fun realizar() {
        puntosDeVenta().forEach { it.actualizarStock() }
    }

    fun puntosDeVenta() = repoPuntosDeVentas.coleccion
}