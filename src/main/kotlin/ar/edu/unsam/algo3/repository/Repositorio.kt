package ar.edu.unsam.algo3.repository


import ar.edu.unsam.algo3.domain.*
import ar.edu.unsam.algo3.dto.FiguritaConDuegnoDTO
import ar.edu.unsam.algo3.dto.toFiguritaConDuegnoDTO
import ar.edu.unsam.algo3.errors.IdNoEncontrado
import ar.edu.unsam.algo3.errors.ObjetoEsNuevo
import ar.edu.unsam.algo3.errors.ObjetoEstaCreado
import ar.edu.unsam.algo3.errors.numeroRepetido
import ar.edu.unsam.algo3.generic.*
import org.springframework.stereotype.Repository


@Repository
open class Repositorio<T : Entidad> {
    var coleccion: MutableSet<T> = mutableSetOf()

    var nextID = Entidad.ID_INICIAL

    fun create(objeto: T) {
        validarCreacion(objeto)
        getNewId(objeto)
        agregarObjeto(objeto)
    }

    private fun agregarObjeto(objeto: T) {
        coleccion.add(objeto)
    }

    fun getNewId(objeto: T) {
        nextID++
        objeto.id = nextID
    }

    fun validarCreacion(objeto: T) {
        if (!objeto.esNuevo()) throw ObjetoEstaCreado()
    }

    fun validarEsNuevo(objeto: T) {
        if (objeto.esNuevo()) throw ObjetoEsNuevo()
    }

    fun delete(objeto: T) {
        coleccion.remove(getById(objeto.id))
    }

    fun search(value: String): List<T> = coleccion.filter { objeto -> objeto.cumpleCriterioBusqueda(value) }

    fun update(objeto: T) {
        validarEsNuevo(objeto)
        delete(objeto)
        agregarObjeto(objeto)
    }

    fun getById(id: Int): T = coleccion.find { it.id == id } ?: throw IdNoEncontrado(id)

    fun clear() {
        coleccion.clear()
    }
}


@Repository
class RepositorioPuntoVenta : Repositorio<PuntoDeVenta>() {
    fun filtrarPorDistancia(usuario: Usuario, distanciaMaxima: Double): List<PuntoDeVenta> =
        coleccion.filter { sobre -> (sobre.distanciaEnvio(usuario) < distanciaMaxima) }

}

@Repository
class repositorioUsuario : Repositorio<Usuario>() {
    fun figuritasRepetidasDeTodos(filterCalls: Map<String, String>): List<FiguritaConDuegnoDTO> =
        coleccion.flatMap { usuario ->
            filtrarFiguritasUsuario(
                filterCalls,
                usuario.figuritasARegalar().toMutableSet()
            ).toMutableSet().map { it.toFiguritaConDuegnoDTO(usuario) }
        }

    private fun filtrarFiguritasUsuario(
        filterCalls: Map<String, String>,
        figuritas: MutableSet<Figurita>
    ): MutableSet<Figurita> {
        val repoFiguritas = RepositorioFiguritasBusqueda()
        repoFiguritas.coleccion = figuritas
        return repoFiguritas.figuritas(filterCalls)
    }
}


@Repository
class repositorioAuth : Repositorio<Usuario>()

@Repository
class RepositorioFiguritasBusqueda : Repositorio<Figurita>() {
    val filter: FiltroFiguritas =
        FiltroFiguritasCompuesto(mutableSetOf(FiltroRangoValoracion, FiltroOnFire, FiltroPromesa, FiltroJugador))

    fun figuritas(filterCalls: Map<String, String>): MutableSet<Figurita> {
        return filter.filter(filterCalls, coleccion)
    }

    fun validarNumeroRepetido(numeroFigurita: Int) {
        if (numeroEsRepetido(numeroFigurita)) {
            throw numeroRepetido(numeroFigurita)
        }
    }

    fun numeroEsRepetido(numeroFigurita: Int): Boolean =
        coleccion.any { figurita -> figurita.numeroIgual(numeroFigurita) }

}

@Repository
class repositorioProvincia : Repositorio<Provincia>()

@Repository
class repositorioJugador : Repositorio<Jugador>(

)

@Repository
class repositorioSeleccion : Repositorio<Seleccion>() {
    fun find(seleccion: String) = coleccion.find { it.pais == seleccion }
    fun getSelecciones() = coleccion.sortedBy { it.pais }
    fun getSelecciones(search: String) = this.search(search).sortedBy { it.pais }
}

@Repository
class repositorioPosicion : Repositorio<Posicion>() {
    fun find(posicion: String) = coleccion.find { it.nombre == posicion }
}