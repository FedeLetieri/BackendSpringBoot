package ar.edu.unsam.algo3.domain

import ar.edu.unsam.algo3.observers.SolicitudObserver
import ar.edu.unsam.algo3.errors.*
import ar.edu.unsam.algo3.generic.aniosTranscurridos
import ar.edu.unsam.algo3.generic.coincideParcialemente
import ar.edu.unsam.algo3.generic.esExactamente
import java.time.LocalDate

open class Usuario(
    var nombre: String,
    var apellido: String,
    var username: String,
    var fechaNacimiento: LocalDate,
    var email: String,
    var direccion: Direccion,
    var distanciaDeBusqueda:Double,
    var imagen: String,
    var password: String
): ErrorHandler, Entidad {
    var tipoDeUsuario: TipoDeUsuario = Desprendido
    var figuritasRepetidas: MutableList<Figurita> = mutableListOf()
    var figuritasFaltantes: MutableList<Figurita> = mutableListOf()
    val listaSolicitudObservers: MutableList<SolicitudObserver> = mutableListOf()

    init {
        this.validar()
    }

    override var id: Int = Entidad.ID_INICIAL


    override fun validar() {
        var attrs: List<String> = listOf("NOMBRE", "APELLIDO", "NOMBRE DE USUARIO", "E-MAIL")
        listOf(nombre, apellido, username, email).zip(attrs)
            .forEach { (campo, nombreCampo) -> this.stringVacio(campo, nombreCampo) }

        this.fechaPosterior(fechaNacimiento, "FECHA DE NACIMIENTO")

        this.campoNulo(direccion, "DIRECCIÓN")
    }

    companion object {
        val CANTIDAD_MAXIMA_FIGURITAS = 5
    }
    fun edad() = fechaNacimiento.aniosTranscurridos()

    fun proveer(usuario: Usuario, figurita: Figurita) {
        usuario.eliminarRepetida(figurita)
        this.eliminarFaltante(figurita)
    }

    fun solicitar(usuario: Usuario, figurita: Figurita) {
        this.validarSolicitud(usuario, figurita)
        this.proveer(usuario, figurita)
        notificarSolicitud(figurita)
    }

    fun notificarSolicitud(figurita: Figurita) {
        observers().forEach {
            it.notificarSolicitud(this, figurita)
        }
    }

    fun observers() = listaSolicitudObservers.toSet()

    fun cambiarTipoDeUsuario(tipoDeUsuarioNuevo: TipoDeUsuario) {
        this.tipoDeUsuario = tipoDeUsuarioNuevo
    }

    fun validarSolicitud(usuario: Usuario, figurita: Figurita) {
        validarEsFaltante(figurita)
        validarEsCercano(usuario)
        usuario.validarEsRepetida(figurita)
        usuario.validarPuedeRegalar(figurita)
    }

    fun validarEsFaltante(figurita: Figurita) {
        if (!esFaltante(figurita)) throw FiguritaNoFaltante()
    }

    fun validarEsCercano(usuario: Usuario) {
        if (!esCercano(usuario)) throw UsuarioNoCercano()
    }

    fun validarEsRepetida(figurita: Figurita) {
        if (!esRepetida(figurita)) throw FiguritaRepetida()
    }

    fun validarPuedeRegalar(figurita: Figurita) {
        if (!puedeRegalar(figurita)) throw FiguritaNoRegalable()
    }

    fun eliminarRepetida(figurita: Figurita) {
        figuritasRepetidas.remove(figurita)
    }

    fun eliminarFaltante(figurita: Figurita) {  
        validarEsFaltante(figurita)
        figuritasFaltantes.remove(figurita)
    }

    fun figuritasARegalar(): List<Figurita> = figuritasRepetidas.filter { figurita -> this.puedeRegalar(figurita) }
    fun cantidadFiguritasARegalar(): Int = figuritasARegalar().size
    fun puedeRegalar(figurita: Figurita): Boolean = tipoDeUsuario.puedeRegalar(this, figurita)
    fun esRegalable(figurita: Figurita): Boolean = this.puedeRegalar(figurita) && !this.esFaltante(figurita)

    fun esRepetida(figurita: Figurita): Boolean = figurita in figuritasRepetidas
    fun esFaltante(figurita: Figurita): Boolean = figurita in figuritasFaltantes

    fun esCercano(otroUsuario: Usuario): Boolean =
        direccion.distanciaEntre(otroUsuario.direccion) <= distanciaDeBusqueda

    fun registrarRepetida(figurita: Figurita) {
        this.validarFiguritaRepetida(figurita)
        figuritasRepetidas.add(figurita)
    }

    fun registrarFaltante(figurita: Figurita) {
        this.validarFiguritaFaltante(figurita)
        this.validarFiguritaYaEstaEnFaltante(figurita)
        figuritasFaltantes.add(figurita)
    }

    fun agregarSolicitudObserver(solicitudObserver: SolicitudObserver) {
        listaSolicitudObservers.add(solicitudObserver)
    }

    fun eliminarSolicitudObserver(solicitudObserver: SolicitudObserver) {
        listaSolicitudObservers.remove(solicitudObserver)
    }

    fun validarFiguritaRepetida(figurita: Figurita) {
        if (this.esFaltante(figurita)) {
            throw FiguritaEsFaltante()
        }
    }

    fun validarFiguritaFaltante(figurita: Figurita) {
        if (this.esRepetida(figurita) ) {
            throw FiguritaEsRepetida()
        }
    }

    fun validarFiguritaYaEstaEnFaltante(figurita:Figurita){
        if (this.esFaltante(figurita)){
            throw figuritaYaEstaEnFaltantes()
        }
    }

    fun topValoracion(): List<Figurita> = ordenarFiguritasSegunValoracion().take(CANTIDAD_MAXIMA_FIGURITAS)
    fun ordenarFiguritasSegunValoracion(): Set<Figurita> =
        figuritasRepetidas.sortedByDescending { it.valoracion() }.toSet()

    fun esActivo(): Boolean = !tieneAlbumCompleto() || tieneFiguritasRepetidas()
    fun tieneAlbumCompleto() = figuritasFaltantes.isEmpty()
    fun tieneFiguritasRepetidas() = !figuritasRepetidas.isEmpty()

    override fun cumpleCriterioBusqueda(value: String): Boolean =
        nombre.coincideParcialemente(value) ||
                apellido.coincideParcialemente(value) ||
                username.esExactamente(value)


    fun esMismoPassword(password: String) = password == this.password

    fun getFiguritaById(id: Int): Figurita? = this.figuritasARegalar().find { it.id == id }
}