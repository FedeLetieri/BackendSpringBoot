package ar.edu.unsam.algo3.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDate
import ar.edu.unsam.algo3.domain.*
import ar.edu.unsam.algo3.repository.Repositorio
import ar.edu.unsam.algo3.repository.RepositorioFiguritasBusqueda

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Controlador de Figuritas")
class FiguritasControllerSpec(@Autowired val mockMvc: MockMvc) {
    @Autowired
    lateinit var repoFiguritas: RepositorioFiguritasBusqueda
    var seleccion = Seleccion("Portugal", Confederacion.CONMEBOL, 0, 2)
    var posicion = Delantero
    var jugador = Jugador("El", "Bicho", LocalDate.now().minusYears(20), 7, seleccion, posicion, LocalDate.now().minusYears(26), 1.98, 75.0, true, 100_000_000.0)

    @BeforeEach
    fun init(){
        repoFiguritas.coleccion = mutableSetOf(
            Figurita(2, false, NivelImpresion.MEDIA, jugador),
            Figurita(11, true, NivelImpresion.ALTA, jugador),
            Figurita(5, true, NivelImpresion.BAJA, jugador)
        )
    }

//    @Test
//    fun `Se obtienen todas las figuritas`(){
//        mockMvc
//            .perform(MockMvcRequestBuilders.get("/busqueda-figuritas/figuritas"))
//            .andExpect(status().isOk)
//            .andExpect(content().contentType("application/json"))
//            .andExpect(jsonPath("$.length()").value(3))
//    }
//
//    @Test
//    fun `Se obtienen todas las figuritas repetidas`(){
//        mockMvc
//            .perform(MockMvcRequestBuilders.get("/busqueda-figuritas-repetidas/figuritas"))
//            .andExpect(status().isOk)
//            .andExpect(content().contentType("application/json"))
//            .andExpect(jsonPath("$.length()").value(3))
//    }
//
//    @Test
//    fun `Se obtienen todas las figuritas faltantes`(){
//        mockMvc
//            .perform(MockMvcRequestBuilders.get("/busqueda-figuritas-faltantes/figuritas"))
//            .andExpect(status().isOk)
//            .andExpect(content().contentType("application/json"))
//            .andExpect(jsonPath("$.length()").value(3))
//    }

}