package ar.edu.unsam.algo3.controller

import ar.edu.unsam.algo3.services.CountDataService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin("*")
class CountDataController {
    @Autowired
    private lateinit var countDataService: CountDataService

    @GetMapping("/countData")
    @Operation(summary = "Busca todos los datos estadisticos")
    fun countData(@RequestParam userId: String) = this.countDataService.countData(userId.toInt())
}