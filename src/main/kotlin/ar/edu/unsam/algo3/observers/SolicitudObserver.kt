package ar.edu.unsam.algo3.observers

import ar.edu.unsam.algo3.domain.*
import ar.edu.unsam.algo3.services.Mail
import ar.edu.unsam.algo3.services.MailSender

interface SolicitudObserver {
    fun notificarSolicitud(usuario: Usuario, figurita: Figurita)
}

class RegistraRepetidasObserver(): SolicitudObserver {
    var figuritasRepetidasReservadas: MutableList<Figurita> = mutableListOf()

    override fun notificarSolicitud(usuario: Usuario, figurita: Figurita) {
        if ( listaNoVacia() && esMayorOIgualQueAlguna(usuario, figurita) && !usuario.tieneFiguritasRepetidas() ) {
            val figuritaARegistrar = menosValiosaRegalable(usuario)

            usuario.registrarRepetida(figuritaARegistrar)
            figuritasRepetidasReservadas.remove(figuritaARegistrar)
        }
    }

    fun listaNoVacia(): Boolean = figuritasRepetidasReservadas.isNotEmpty()
    fun esMayorOIgualQueAlguna(usuario: Usuario, figurita: Figurita): Boolean = figuritasRegalables(usuario).any({ figurita.valoracion() >= it.valoracion() })
    fun menosValiosaRegalable(usuario: Usuario): Figurita = figuritasRegalables(usuario).minBy({ it.valoracion() })
    fun figuritasRegalables(usuario: Usuario): List<Figurita> = figuritasRepetidasReservadas.filter({ usuario.esRegalable(it) })

    fun agregarFigurita(usuario: Usuario, figurita: Figurita){
        usuario.validarFiguritaRepetida(figurita)
        figuritasRepetidasReservadas.add(figurita)
    }
    fun eliminarFigurita(figurita: Figurita){ figuritasRepetidasReservadas.remove(figurita) }
}


class UltimasSolicitudesObserver() : SolicitudObserver {
    companion object{
        val cantidadFiguritasDeLaMismaSeleccion: Int = 3
    }

    val listaDePaisesSolicitados: MutableList<Seleccion> = mutableListOf()

    override fun notificarSolicitud(usuario: Usuario, figurita: Figurita) {
        val paisSolicitado: Seleccion = figurita.seleccion()

        listaDePaisesSolicitados.add(paisSolicitado)
        if (ultimasSolicitudesConMismaSeleccion(paisSolicitado) && (!usuario.tieneAlbumCompleto())) {
            cambiarANacionalista(usuario, paisSolicitado)
            reiniciarUltimasSolicitudes()
        }
    }

    fun cambiarANacionalista(usuario: Usuario, seleccion: Seleccion) {
        usuario.cambiarTipoDeUsuario(Nacionalista(mutableSetOf(seleccion)))
    }

    fun ultimasSolicitudesConMismaSeleccion(pais: Seleccion): Boolean =
        cantidadDeSolicitudesSuficientes() &&
                listaDePaisesSolicitados.takeLast(cantidadFiguritasDeLaMismaSeleccion).all { it.esIgualAlPais(pais) }

    fun cantidadDeSolicitudesSuficientes(): Boolean =
        listaDePaisesSolicitados.size >= cantidadFiguritasDeLaMismaSeleccion

    fun reiniciarUltimasSolicitudes() {
        listaDePaisesSolicitados.clear()
    }
}

class SeConvierteADesprendidoObserver(private val limiteFiguritasARegalar: Int) : SolicitudObserver {
    override fun notificarSolicitud(usuario: Usuario, figurita: Figurita) {
        if (usuario.tieneAlbumCompleto() && superaLimiteDeFiguritasARegalar(usuario)) {
            usuario.cambiarTipoDeUsuario(Desprendido)
        }
    }

    fun superaLimiteDeFiguritasARegalar(usuario: Usuario) = usuario.cantidadFiguritasARegalar() > limiteFiguritasARegalar
}

class LlenoAlbumObserver(val mailSender: MailSender): SolicitudObserver {
    override fun notificarSolicitud(usuario: Usuario, figurita: Figurita) {
        if (usuario.tieneAlbumCompleto()) {
            enviarMailFelicitaciones(usuario, figurita)
        }
    }

    fun enviarMailFelicitaciones(usuario: Usuario, figurita: Figurita) {
        val contenidoCorreo =
            "Hola, ${usuario.nombre} ${usuario.apellido}, completo el album con la figurita ${figurita.descripcionFigurita()}"

        mailSender.sendMail(
            Mail(
                "info@worldcapp.com.ar",
                usuario.email,
                "¡Felicitaciones por completar el álbum!",
                contenidoCorreo
            )
        )
    }
}

object TriplicaDistanciaObserver : SolicitudObserver {
    
    override fun notificarSolicitud(usuario: Usuario, figurita: Figurita) {
        if (leFaltanCincoOMenos(usuario)) {
            usuario.distanciaDeBusqueda *= 3
            usuario.eliminarSolicitudObserver(this)
        }
    }

    fun leFaltanCincoOMenos(usuario: Usuario) = usuario.figuritasFaltantes.size <= 5

}