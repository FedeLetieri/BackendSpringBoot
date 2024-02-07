package ar.edu.unsam.algo3.controller
import ar.edu.unsam.algo3.domain.PuntoDeVenta
import ar.edu.unsam.algo3.dto.*
import ar.edu.unsam.algo3.services.SobresServices
import ar.edu.unsam.algo3.services.UsuarioService
import org.springframework.web.bind.annotation.*
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired

@RestController
@CrossOrigin("*")
class SobresController {

    @Autowired
    lateinit var sobresServices: SobresServices

    @Autowired
    lateinit var usuarioService: UsuarioService


    @GetMapping("/ordenarSobres")
    @Operation(summary = "Devuelve los puntos de sobres mas cercanos para el usuario")
    fun sobresCercanos(@RequestParam tipoOrdenamiento: String,@RequestParam idUsuario:Int,
                       @RequestParam(required = false) criterioBusqueda: String): List<PuntoDeVentaDto> {
        return sobresServices.obtenerSobres(tipoOrdenamiento,idUsuario,criterioBusqueda).map { sobre -> sobre.toDTO(usuarioService,idUsuario) }
    }

    @GetMapping("/obtenerSobres")
    @Operation(summary = "Devuelve todos los sobres")
    fun obtenerSobres(): List<PuntoDeVentaAdminDto> {
        return sobresServices.sobresRepository.coleccion.map { sobre -> sobre.toAdminDTO() }
    }

    @GetMapping("/obtenerSobrePorId")
    @Operation(summary = "Devuelve un sobre por id")
    fun obtenerSobrePorId(@RequestParam idSobre:Int): PuntoDeVentaAdminDto {
        return sobresServices.obtenerSobrePorId(idSobre).toAdminDTO()
    }

    @PostMapping("/agregarSobre")
    @Operation(summary = "Agrega un nuevo sobre")
    fun agregarSobre(@RequestBody puntoDeVenta: PuntoDeVentaAdminDto): String {
        sobresServices.agregarSobre(puntoDeVenta)
        return "Sobre agregado exitosamente"
    }

    @PutMapping("/editarSobre")
    @Operation(summary = "Edita un sobre por id")
    fun editarSobre(@RequestBody puntoDeVenta: PuntoDeVentaAdminDto): String {
        sobresServices.editarSobre(puntoDeVenta)
        return "Sobre editado exitosamente"
    }

    @DeleteMapping("/eliminarSobre")
    @Operation(summary = "Elimina un sobre del sistema")
    fun eliminarSobre(@RequestParam idSobre:Int): List<PuntoDeVentaAdminDto> {
        sobresServices.eliminarSobre(idSobre)
        return sobresServices.sobresRepository.coleccion.map { sobre -> sobre.toAdminDTO() }
    }
    
}