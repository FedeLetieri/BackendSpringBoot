package ar.edu.unsam.algo3.services

import ar.edu.unsam.algo3.repository.RepositorioFiguritasBusqueda
import ar.edu.unsam.algo3.domain.Figurita
import ar.edu.unsam.algo3.dto.*
import ar.edu.unsam.algo3.repository.repositorioJugador
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy

@Service
class FiguritasService {
    @Autowired
    @Lazy
    lateinit var usuarioService: UsuarioService

    @Autowired
    private lateinit var repositorioFiguritasBusqueda: RepositorioFiguritasBusqueda

    @Autowired
    private lateinit var repositorioJugador: repositorioJugador

    fun figuritasBusqueda(filterCalls: Map<String, String>): List<FiguritaConDuegnoDTO> =
        usuarioService.obtenerFiguritasRepetidasTodos(filterCalls)

    fun figuritasBusquedaRepetidas(filterCalls: Map<String, String>): List<FiguritaDTO> {
        val usuario = usuarioService.getUsuarioById(filterCalls["usuario"]!!.toInt())
        return toFiguritaDTO(
            repositorioFiguritasBusqueda.figuritas(filterCalls).filter { figurita -> !usuario.esFaltante(figurita) })
    }

    fun figuritasBusquedaFaltantes(filterCalls: Map<String, String>): List<FiguritaDTO> {
        val usuario = usuarioService.getUsuarioById(filterCalls["usuario"]!!.toInt())
        return toFiguritaDTO(
            repositorioFiguritasBusqueda.figuritas(filterCalls)
                .filter { figurita -> !usuario.esRepetida(figurita) && !usuario.esFaltante(figurita) })
    }

    fun obtenerFiguritaPorId(idFigurita: Int) = repositorioFiguritasBusqueda.getById(idFigurita)
    fun toFiguritaDTO(figuritas: List<Figurita>) = figuritas.map { it.toFiguritaDTO() }

    fun getFiguritaById(figuritaID: String) = repositorioFiguritasBusqueda.getById(figuritaID.toInt()).toFiguritaDTO()
    fun getFiguritaByIdFrom(usuarioId: String, figuritaId: String): FiguritaConDuegnoDTO {
        val usuario = usuarioService.getUsuarioById(usuarioId.toInt())
        return usuario.getFiguritaById(figuritaId.toInt())!!.toFiguritaConDuegnoDTO(usuario)
    }

    fun solicitudFigurita(emisorId: String, receptorId: String, figuritaId: String): ExceptionDTO {
        val emisor = usuarioService.getUsuarioById(emisorId.toInt())
        val receptor = usuarioService.getUsuarioById(receptorId.toInt())
        val figurita = repositorioFiguritasBusqueda.getById(figuritaId.toInt())
        var errDTO: ExceptionDTO = ExceptionDTO("")
        try {
            emisor.solicitar(receptor, figurita)
        } catch (error: Exception) {
            errDTO = error.toExceptionDTO()
        }
        return errDTO
    }

    fun eliminarFigurita(idFigurita: Int) {
        val figuritaAEliminar = repositorioFiguritasBusqueda.getById(idFigurita)
        usuarioService.validarFigurita(figuritaAEliminar)
        repositorioFiguritasBusqueda.delete(figuritaAEliminar)
    }

    fun figuritasByFiltro(filtro: String?): Collection<Figurita> {
        return if (filtro.isNullOrEmpty()) {
            repositorioFiguritasBusqueda.coleccion
        } else {
            repositorioFiguritasBusqueda.coleccion.filter { figurita ->
                figurita.cumpleCriterioBusqueda(filtro)
            }
        }
    }

    fun actualizarFigurita(figuritaActualizada: FiguritaFormDTO) {
        val nuevoJugador = repositorioJugador.getById(figuritaActualizada.idJugador)
        val figuritaNueva = repositorioFiguritasBusqueda.getById(figuritaActualizada.id)
        validarNumeroRepetido(figuritaNueva, figuritaActualizada)
        figuritaNueva.numero = figuritaActualizada.numero
        figuritaNueva.onFire = figuritaActualizada.onFire
        figuritaNueva.nivelImpresion =
            figuritaActualizada.factoryNivelDeImpresionFromString(figuritaActualizada.nivelImpresion)
        figuritaNueva.jugador = nuevoJugador

        repositorioFiguritasBusqueda.update(figuritaNueva)
    }

    private fun validarNumeroRepetido(figuritaNueva: Figurita, figuritaActualizada: FiguritaFormDTO) {
        if (!validarMismoNumero(figuritaNueva.numero, figuritaActualizada.numero)) {
            repositorioFiguritasBusqueda.validarNumeroRepetido(figuritaActualizada.numero)
        }
    }

    fun validarMismoNumero(numeroViejo: Int, numeroNuevo: Int): Boolean = numeroViejo == numeroNuevo

    fun crearFigurita(figuritaNueva: FiguritaFormDTO) {

        val jugador = repositorioJugador.getById(figuritaNueva.idJugador)

        repositorioFiguritasBusqueda.validarNumeroRepetido(figuritaNueva.numero)
        repositorioFiguritasBusqueda.create(figuritaNueva.toFigurita(jugador))

    }

}