package ar.edu.unsam.algo3.services

import ar.edu.unsam.algo3.repository.Repositorio
import ar.edu.unsam.algo3.domain.PuntoDeVenta
import ar.edu.unsam.algo3.dto.PuntoDeVentaAdminDto
import ar.edu.unsam.algo3.dto.factoryTipoDePuntoDeVenta
import ar.edu.unsam.algo3.errors.parametroOrdenadorInvalido
import ar.edu.unsam.algo3.repository.RepositorioPuntoVenta


import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired


@Service
class SobresServices {

    var distanciaMaxima: Double = 5000.0

    @Autowired
    lateinit var sobresRepository : RepositorioPuntoVenta

    @Autowired
    lateinit var usuarioService: UsuarioService


    //val coleccionSobres = sobresRepository.coleccion
    //val usuario = usuarioService.getUsuario()



    fun obtenerSobres(tipoOrdenamiento: String,idUsuario: Int,criterio: String):List<PuntoDeVenta> {
        var sobresFiltradosPorOrdenamiento:List<PuntoDeVenta> = when(tipoOrdenamiento){
            "cercanos" -> ordenarPorcercanos(idUsuario)
            "baratos" ->  ordenarPorprecio(idUsuario)
            "cantidad" -> ordenarPorstock()
            "masCercanos" -> filtroMasCercanos(idUsuario)
            else -> throw parametroOrdenadorInvalido()
        }
        return if (validarFiltroPorBuscador(criterio))
             filtrarPorCriterio(sobresFiltradosPorOrdenamiento,criterio)
        else sobresFiltradosPorOrdenamiento
    }

    fun eliminarSobre(idSobre: Int):String {
        sobresRepository.delete(sobresRepository.getById(idSobre))
        return "Sobre eliminado correctamente"
    }

    fun ordenarPorcercanos(idUsuario:Int):List<PuntoDeVenta> = sobresRepository.coleccion.sortedBy { sobre -> sobre.distanciaEnvio(usuarioService.getUsuarioById(idUsuario)) }
    fun ordenarPorprecio(idUsuario:Int):List<PuntoDeVenta> = sobresRepository.coleccion.sortedBy { sobre -> sobre.costoEnvio(usuarioService.getUsuarioById(idUsuario)) }

    fun ordenarPorstock():List<PuntoDeVenta> = sobresRepository.coleccion.sortedByDescending { sobre -> sobre.stockSobres }

    fun filtroMasCercanos(idUsuario:Int):List<PuntoDeVenta> = sobresRepository.filtrarPorDistancia(usuarioService.getUsuarioById(idUsuario),distanciaMaxima)

    fun filtrarPorCriterio(listaDeSobres:List<PuntoDeVenta>, criterio:String):List<PuntoDeVenta> = listaDeSobres.filter {sobre -> sobre.cumpleCriterioBusqueda(criterio)}

    fun validarFiltroPorBuscador(criterio:String):Boolean = criterio.length > 0

    fun obtenerSobrePorId(idSobre:Int):PuntoDeVenta = sobresRepository.getById(idSobre)

    fun agregarSobre(puntoDeVentaAdminDto: PuntoDeVentaAdminDto){
        val puntoDeVenta = puntoDeVentaAdminDto.factoryTipoDePuntoDeVenta(puntoDeVentaAdminDto.tipo, puntoDeVentaAdminDto)
        sobresRepository.create(puntoDeVenta)
    }

    fun editarSobre(puntoDeVentaAdminDto: PuntoDeVentaAdminDto){
        val puntoDeVenta = puntoDeVentaAdminDto.factoryTipoDePuntoDeVenta(puntoDeVentaAdminDto.tipo, puntoDeVentaAdminDto)
        puntoDeVenta.id = puntoDeVentaAdminDto.id
        sobresRepository.update(puntoDeVenta)
    }
}