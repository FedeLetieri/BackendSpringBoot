package ar.edu.unsam.algo3.controller

import ar.edu.unsam.algo3.dto.toFiguritaDTO
import ar.edu.unsam.algo3.services.FiguritasService

import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.*
import org.springframework.beans.factory.annotation.Autowired
import ar.edu.unsam.algo3.dto.ExceptionDTO
import ar.edu.unsam.algo3.dto.FiguritaDTO
import ar.edu.unsam.algo3.dto.FiguritaFormDTO
import ar.edu.unsam.algo3.generic.RespuestaPersonalizada
import org.springframework.http.ResponseEntity

@RestController
@CrossOrigin("*")
class FiguritasController {
    @Autowired
    private lateinit var figuritasService: FiguritasService

    @GetMapping("/busqueda-figuritas/figuritas")
    @Operation(summary = "Busca todas las figuritas de busqueda o las que responden a los filtros llamados")
    fun figuritasBusqueda(@RequestParam filterCalls: Map<String, String>) = figuritasService.figuritasBusqueda(filterCalls)

    @GetMapping("/busqueda-figuritas-repetidas/figuritas")
    @Operation(summary = "Busca todas las figuritas de busqueda para agregar a repetidas o las que responden a los filtros llamados")
    fun figuritasBusquedaRepetidas(@RequestParam filterCalls: Map<String, String>) = figuritasService.figuritasBusquedaRepetidas(filterCalls)

    @GetMapping("/busqueda-figuritas-faltantes/figuritas")
    @Operation(summary = "Busca todas las figuritas de busqueda para agregar a faltantes o las que responden a los filtros llamados")
    fun figuritasBusquedaFaltantes(@RequestParam filterCalls: Map<String, String>) = figuritasService.figuritasBusquedaFaltantes(filterCalls)

    @GetMapping("/figurita")
    @Operation(summary = "Devuelve figurita por ID")
    fun figuritaByID(@RequestParam("usuario", required = false) usuarioId: String?, @RequestParam("figurita") figuritaId: String) = if(usuarioId != null) figuritasService.getFiguritaByIdFrom(usuarioId, figuritaId) else figuritasService.getFiguritaById(figuritaId)

    @GetMapping("/solicitudFigurita")
    @Operation(summary = "Realiza la solicitud de la figurita")
    fun solicitudFigurita(@RequestParam("emisor") emisorId: String, @RequestParam("receptor") receptorId: String, @RequestParam("figurita") figuritaId: String): ExceptionDTO{
        return figuritasService.solicitudFigurita(emisorId, receptorId, figuritaId)
    }

    @DeleteMapping("/eliminarFigurita")
    @Operation(summary = "Elimina una figurita del sistema")
    fun eliminarFigurita(@RequestParam idFigurita:Int): ResponseEntity<RespuestaPersonalizada> {
        figuritasService.eliminarFigurita(idFigurita)
          return ResponseEntity.ok(RespuestaPersonalizada("Figurita eliminada"))
    }

    @GetMapping("/figuritas")
    @Operation(summary = "Envia las figuritas que contengan el filtro, mandado por parametro")
    fun obtenerFiguritaByFiltro(@RequestParam(required = false) filtro:String?):List<FiguritaDTO>{
        return figuritasService.figuritasByFiltro(filtro).map { it.toFiguritaDTO() }
    }

    @PutMapping("/editarFigurita")
    @Operation(summary = "Envia una figuritaDTO con  los campos que quiere actualizar y modifica la figurita")
    fun editarFigurita(@RequestBody figuritaActualizada:FiguritaFormDTO): ResponseEntity<RespuestaPersonalizada> {
        figuritasService.actualizarFigurita(figuritaActualizada)

        return ResponseEntity.ok(RespuestaPersonalizada("Figurita Editada Correctamente"))
    }

    @PostMapping("/figuritas")
    @Operation(summary = "Envia una FiguritaDTO nueva para al repositorio de figuritas")
    fun crearFigurita(@RequestBody figuritaNueva:FiguritaFormDTO): ResponseEntity<RespuestaPersonalizada> {

        figuritasService.crearFigurita(figuritaNueva)

        return ResponseEntity.ok(RespuestaPersonalizada("Figurita Creada Correctamente"))

    }

}