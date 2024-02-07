package ar.edu.unsam.algo3.controller

import ar.edu.unsam.algo3.domain.*
import ar.edu.unsam.algo3.repository.repositorioJugador
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
@DisplayName("Controller de Jugadores")
class JugadoresControllerSpec(@Autowired val mockMVC: MockMvc) {

    @Autowired
    lateinit var seleccionRepository : repositorioSeleccion

    @Autowired
    lateinit var jugadorRepository : repositorioJugador

    @BeforeEach
    fun init() {

        jugadorRepository.clear()
        //Inicializo el valor del id en 0, si no lo hago se seguira aumentando el valor por cada usuario que ingrese al repositorio
        //sin importar si el usuario es eliminado o no.
        jugadorRepository.nextID = 0

        seleccionRepository.apply {
            create(Seleccion("Portugal", Confederacion.CONMEBOL, 0, 2))
        }

        val portugal = seleccionRepository.getById(1)
        val delantero = Delantero

        jugadorRepository.apply {
            create(
                Jugador("El", "Bicho", LocalDate.now().minusYears(26), 7, portugal, delantero, LocalDate.now().minusYears(2), 1.98,
                75.0,
                true,
                100_000_000.0
            )
            )
        }

        val jugadorFavorito = jugadorRepository.getById(1)

    }

    @Test
    fun `Se obtienen todos los jugadores disponibles`(){
        mockMVC.perform(MockMvcRequestBuilders.get("/jugadores"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))

    }
}