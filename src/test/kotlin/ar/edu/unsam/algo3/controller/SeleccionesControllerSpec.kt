package ar.edu.unsam.algo3.controller

import ar.edu.unsam.algo3.domain.*
import ar.edu.unsam.algo3.repository.repositorioSeleccion
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.uqbar.geodds.Point
import java.time.LocalDate

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Controller de Selecciones")
class SeleccionesControllerSpec(@Autowired val mockMVC: MockMvc) {

    @Autowired
    lateinit var seleccionRepository : repositorioSeleccion

    @BeforeEach
    fun init() {
        seleccionRepository.clear()
        //Inicializo el valor del id en 0, si no lo hago se seguira aumentando el valor por cada usuario que ingrese al repositorio
        //sin importar si el usuario es eliminado o no.
        seleccionRepository.nextID = 0


        seleccionRepository.apply {
            create(Seleccion("Portugal", Confederacion.CONMEBOL, 0, 2))
            create(Seleccion("España", Confederacion.CONMEBOL, 1, 3))
        }

    }

    @Test
    fun `Se obtienen todas las selecciones disponibles`(){
        mockMVC.perform(MockMvcRequestBuilders.get("/selecciones"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))

    }

}