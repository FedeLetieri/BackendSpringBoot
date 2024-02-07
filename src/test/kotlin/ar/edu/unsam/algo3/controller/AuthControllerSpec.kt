package ar.edu.unsam.algo3.controller

import ar.edu.unsam.algo3.domain.*
import ar.edu.unsam.algo3.dto.AuthDTO
import ar.edu.unsam.algo3.repository.repositorioUsuario
import ar.edu.unsam.algo3.services.AuthService
import com.fasterxml.jackson.databind.ObjectMapper
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
@DisplayName("Dado un controller de Auth")
class AuthControllerSpec(@Autowired val mockMvc: MockMvc) {

    @Autowired
    lateinit var authRepository: repositorioUsuario

    @Autowired
    lateinit var authService: AuthService

    @BeforeEach
    fun init() {
        authRepository.clear()

        val fechaNacimiento = LocalDate.now()
        val direccionUsuario = Direccion("Buenos Aires", "Chascomus", "Avenida Siempre Viva", 5555, Point(23, -1))
        authRepository.apply {
            create(
                Usuario(
                    "Juan", "Barrios", "JBa", fechaNacimiento, "@", direccionUsuario, 20.0, "imagen.png", "hola123"
                )
            )
            create(
                Usuario(
                    "Germán", "Palacios", "GerPa", fechaNacimiento, "@", direccionUsuario, 20.0, "imagen.png", "chau456"
                )
            )
            create(
                Usuario(
                    "María", "Dolores", "MaDo", fechaNacimiento, "@", direccionUsuario, 20.0, "imagen.png", "quetal789"
                )
            )
            create(
                Usuario(
                    "Agustina", "Pereyra", "AgusP", fechaNacimiento, "@", direccionUsuario, 20.0, "imagen.png", "aaa000"
                )
            )
        }
    }

    @Test
    fun `login exitoso con username y password correctos`() {
        val authDTO = AuthDTO("JBa", "hola123")
        mockMvc.perform(
            MockMvcRequestBuilders.post("/login").contentType("application/json")
                .content(ObjectMapper().writeValueAsString(authDTO))
        ).andExpect(MockMvcResultMatchers.status().isOk)
            // .andExpect(MockMvcResultMatchers.content().string("1"))
    }

    @Test
    fun `login no existoso por username no existente`() {
        val authDTO = AuthDTO("alalala", "hola123")
        mockMvc.perform(
            MockMvcRequestBuilders.post("/login").contentType("application/json")
                .content(ObjectMapper().writeValueAsString(authDTO))
        ).andExpect(MockMvcResultMatchers.status().isUnauthorized())
            .andExpect(MockMvcResultMatchers.content().string("Usuario y/o contraseña incorrectos."))
    }

    @Test
    fun `login no existoso por password incorrecta`() {
        val authDTO = AuthDTO("GerPa", "hola123")
        mockMvc.perform(
            MockMvcRequestBuilders.post("/login").contentType("application/json")
                .content(ObjectMapper().writeValueAsString(authDTO))
        ).andExpect(MockMvcResultMatchers.status().isUnauthorized())
            .andExpect(MockMvcResultMatchers.content().string("Usuario y/o contraseña incorrectos."))
    }

    @Test
    fun `logout exitoso`() {
        mockMvc.perform(MockMvcRequestBuilders.post("/logout")).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().string("true"))
    }
}
