package ar.edu.unsam.algo3.controller

import ar.edu.unsam.algo3.domain.Seleccion
import ar.edu.unsam.algo3.dto.SeleccionFromDTO
import ar.edu.unsam.algo3.services.SeleccionService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin("*")
class SeleccionesController {
    @Autowired
    lateinit var seleccionService: SeleccionService

    @GetMapping("/selecciones")
    @Operation(summary = "Todas las selecciones")
    fun getSelecciones(@RequestParam(value = "search", required = false) search: String?) = seleccionService.getSelecciones(search)

    @PostMapping("/nuevaSeleccion")
    @Operation(summary = "Crea nueva seleccion")
    fun createSeleccion(@RequestBody seleccion: SeleccionFromDTO) { seleccionService.createSeleccion(seleccion) }

    @PutMapping("/editarSeleccion")
    @Operation(summary = "Modifica seleccion existente")
    fun updateSeleccion(@RequestBody seleccion: SeleccionFromDTO) { seleccionService.updateSeleccion(seleccion) }

    @DeleteMapping("/eliminarSeleccion")
    @Operation(summary = "Devuelve todas selecciones disponibles")
    fun deleteSeleccion(@RequestParam id: String) = seleccionService.deleteSeleccion(id)

    @GetMapping("/confederaciones")
    @Operation(summary = "Todas las confederaciones")
    fun getSelecciones() = seleccionService.getConfederaciones()
}