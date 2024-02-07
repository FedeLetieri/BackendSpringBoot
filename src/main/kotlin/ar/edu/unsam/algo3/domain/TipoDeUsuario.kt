package ar.edu.unsam.algo3.domain

interface TipoDeUsuario{
    fun puedeRegalar(usuario: Usuario, figurita: Figurita): Boolean
}

object Par : TipoDeUsuario {
    override fun puedeRegalar(usuario: Usuario, figurita: Figurita): Boolean = !(figurita.esPar() || figurita.jugadorCamisetaPar() || figurita.jugadorConSeleccionConCoparPar())
}

class Nacionalista(var seleccionesFavoritas:MutableSet<Seleccion>) : TipoDeUsuario {
    override fun puedeRegalar(usuario: Usuario, figurita: Figurita): Boolean = figurita.seleccion() !in seleccionesFavoritas

    fun agregarSeleccionFavorita(seleccion: Seleccion) = seleccionesFavoritas.add(seleccion)
    fun eliminarSeleccionFavorita(seleccion: Seleccion) = seleccionesFavoritas.remove(seleccion)
}

object Conservador : TipoDeUsuario {
    override fun puedeRegalar(usuario: Usuario, figurita: Figurita): Boolean = figurita.esImpresionALTA() && usuario.tieneAlbumCompleto()
}

class Fanatico(var jugadorFavorito: Jugador) : TipoDeUsuario {
    override fun puedeRegalar(usuario: Usuario, figurita: Figurita): Boolean = !(this.jugadorFavorito(figurita) || figurita.tieneJugadorLeyenda())
    fun jugadorFavorito(figurita: Figurita): Boolean = figurita.jugador == jugadorFavorito

}

object Desprendido : TipoDeUsuario {
    override fun puedeRegalar(usuario: Usuario, figurita: Figurita): Boolean = true
}

object Apostador : TipoDeUsuario {
    override fun puedeRegalar(usuario: Usuario, figurita: Figurita): Boolean = !(figurita.onFire && figurita.tieneJugadorPromesa())
}

object Interesado : TipoDeUsuario {
    override fun puedeRegalar(usuario: Usuario, figurita: Figurita): Boolean = figurita !in usuario.topValoracion()
}

object Cambiante : TipoDeUsuario {
    override fun puedeRegalar(usuario: Usuario, figurita: Figurita): Boolean = nuevoTipo(usuario).puedeRegalar(usuario, figurita)
    fun nuevoTipo(usuario: Usuario): TipoDeUsuario = if (usuario.edad() <= 25) Desprendido else Conservador
}