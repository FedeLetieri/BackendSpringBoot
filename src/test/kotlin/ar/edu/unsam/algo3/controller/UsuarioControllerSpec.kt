package ar.edu.unsam.algo3.controller

import ar.edu.unsam.algo3.domain.*
import ar.edu.unsam.algo3.dto.PerfilFormDTO
import ar.edu.unsam.algo3.dto.PerfilUsuarioDTO
import org.springframework.http.MediaType
import ar.edu.unsam.algo3.repository.repositorioJugador
import ar.edu.unsam.algo3.repository.repositorioProvincia
import ar.edu.unsam.algo3.repository.repositorioSeleccion
import ar.edu.unsam.algo3.repository.repositorioUsuario
import ar.edu.unsam.algo3.services.UsuarioService
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
@DisplayName("Controller de Usuario")
class UsuarioControllerSpec (@Autowired val mockMVC: MockMvc){

    @Autowired
    lateinit var usuarioRepository : repositorioUsuario

    @Autowired
    lateinit var provinciaRepository : repositorioProvincia

    @Autowired
    lateinit var seleccionRepository : repositorioSeleccion

    @Autowired
    lateinit var jugadorRepository : repositorioJugador

    @BeforeEach
    fun init() {
        usuarioRepository.clear()
        provinciaRepository.clear()
        seleccionRepository.clear()
        jugadorRepository.clear()
        //Inicializo el valor del id en 0, si no lo hago se seguira aumentando el valor por cada usuario que ingrese al repositorio
        //sin importar si el usuario es eliminado o no.
        usuarioRepository.nextID = 0
        provinciaRepository.nextID = 0
        seleccionRepository.nextID = 0
        jugadorRepository.nextID = 0

        val fechaNacimiento = LocalDate.now()
        val direccionUsuario = Direccion("Buenos Aires","Chascomus","Avenida Siempre Viva",5555, Point(23,-1))


        seleccionRepository.apply {
            create(Seleccion("Portugal", Confederacion.CONMEBOL, 0, 2))
            create(Seleccion("España", Confederacion.CONMEBOL, 1, 3))
        }

        val portugal = seleccionRepository.getById(1)
        val españa = seleccionRepository.getById(2)
        val delantero = Delantero

        jugadorRepository.apply {
            create(Jugador("El", "Bicho", LocalDate.now().minusYears(26), 7, portugal, delantero, LocalDate.now().minusYears(2), 1.98,
                75.0,
                true,
                100_000_000.0
            ))
        }

        val jugadorFavorito = jugadorRepository.getById(1)

        usuarioRepository.apply {
            create(Usuario("Juan","Barrios","ju",fechaNacimiento,"@", direccionUsuario,20.0, "ju.png", "hola"))
            create(Usuario("Carlos","Alvarez","Carlangas",fechaNacimiento,"elCarlangas24@gmail.com", direccionUsuario,20.0, "elCarlangas.png", "hola"))
            create(Usuario("Juana","Perez","Juanita",fechaNacimiento,"juanita11@gmail.com", direccionUsuario,20.0, "juani.png", "hola"))
        }


        usuarioRepository.getById(2).cambiarTipoDeUsuario(Nacionalista(seleccionesFavoritas = mutableSetOf(portugal,españa)))
        usuarioRepository.getById(3).cambiarTipoDeUsuario(Fanatico(jugadorFavorito = jugadorFavorito))

        provinciaRepository.apply {
            create(Provincia("Buenos Aires", mutableSetOf("Buenos Aires", "La Plata", "Mar del Plata", "Quilmes")))

        }
    }
    @Test
    fun `Se obtiene toda la informacion del usuario a partir del ID`(){
        mockMVC.perform(MockMvcRequestBuilders.get("/infoUsuario")
            .param("id", "1"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Juan"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.tipoDeUsuario").value("Desprendido"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.listadoNacionalista").value(null))
            .andExpect(MockMvcResultMatchers.jsonPath("$.jugadorFanatico").value(null))
            .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("ju"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.imagen").value("ju.png"))

    }
    @Test
    fun `Se obtiene informacion del perfil de usuario a partir del ID del usuario`(){
        mockMVC.perform(MockMvcRequestBuilders.get("/perfilUsuario")
            .param("id", "1"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("ju"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.imagen").value("ju.png"))

    }
    @Test
    fun `Se ingresa un ID de usuario inexistente`(){
        mockMVC.perform(MockMvcRequestBuilders.get("/perfilUsuario")
            .param("id", "40"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `Se modifica con PUT informacion del perfil de usuario a partir del ID del usuario`(){
        val idUsuario = 1
        val perfilUsuarioDTO = PerfilUsuarioDTO(username = "NuevoUsername", imagen = "nueva_imagen.jpg")

        mockMVC.perform(MockMvcRequestBuilders.get("/perfilUsuario")
            .param("id", idUsuario.toString()))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("ju"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.imagen").value("ju.png"))

        mockMVC.perform(
            MockMvcRequestBuilders.put("/perfilUsuario")
                .param("id", idUsuario.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(perfilUsuarioDTO))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("NuevoUsername"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.imagen").value("nueva_imagen.jpg"))
    }
    @Test
    fun `Se obtiene informacion general de perfil de usuario Desprendido para modificar el formulario a partir del ID del usuario`(){
        mockMVC.perform(MockMvcRequestBuilders.get("/perfilInfoGeneralUsuario")
            .param("id", "1"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Juan"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.tipoDeUsuario").value("Desprendido"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.listadoNacionalista").value(null))
            .andExpect(MockMvcResultMatchers.jsonPath("$.jugadorFanatico").value(null))

    }
    @Test
    fun `Se obtiene informacion general de perfil de usuario Nacionalista para modificar el formulario a partir del ID del usuario`(){
        mockMVC.perform(MockMvcRequestBuilders.get("/perfilInfoGeneralUsuario")
            .param("id", "2"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Carlos"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.tipoDeUsuario").value("Nacionalista"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.listadoNacionalista[1].pais").value("España"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.jugadorFanatico").value(null))

    }
    @Test
    fun `Se obtiene informacion general de perfil de usuario Fanatico para modificar el formulario a partir del ID del usuario`(){
        mockMVC.perform(MockMvcRequestBuilders.get("/perfilInfoGeneralUsuario")
            .param("id", "3"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Juana"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.tipoDeUsuario").value("Fanatico"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.listadoNacionalista").value(null))
            .andExpect(MockMvcResultMatchers.jsonPath("$.jugadorFanatico.nombre").value("El"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.jugadorFanatico.apellido").value("Bicho"))

    }

    @Test
    fun `Se cambia la informacion general del usuario de perfil`(){
        val idUsuario = 3
        val perfilFormDTO = "{\"nombre\":\"NuevoNombre\",\"apellido\":\"Alvarez\",\"fechaDeNacimiento\":\"2023-10-16\",\"email\":\"elCarlangas24@gmail.com\",\"direccion\":{\"provincia\":\"Buenos Aires\",\"localidad\":\"Chascomus\",\"calle\":\"Avenida Siempre Viva\",\"numero\":5555,\"ubicacion\":{\"x\":23.0,\"y\":-1.0}},\"distanciaDeCercania\":20.0,\"tipoDeUsuario\":\"Nacionalista\",\"listadoNacionalista\":[{\"pais\":\"Portugal\",\"confederacion\":\"CONMEBOL\",\"copasDelMundo\":0,\"copasConfederacion\":2,\"id\":1},{\"pais\":\"España\",\"confederacion\":\"CONMEBOL\",\"copasDelMundo\":1,\"copasConfederacion\":3,\"id\":2}],\"jugadorFanatico\":null}\n"

        mockMVC.perform(MockMvcRequestBuilders.get("/perfilInfoGeneralUsuario")
            .param("id", idUsuario.toString()))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Juana"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.tipoDeUsuario").value("Fanatico"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.listadoNacionalista").value(null))
            .andExpect(MockMvcResultMatchers.jsonPath("$.jugadorFanatico.nombre").value("El"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.jugadorFanatico.apellido").value("Bicho"))

        mockMVC.perform(
            MockMvcRequestBuilders.put("/perfilInfoGeneralUsuario")
                .param("id", idUsuario.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(perfilFormDTO)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("NuevoNombre"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.tipoDeUsuario").value("Nacionalista"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.listadoNacionalista.length()").value(2))
            .andExpect(MockMvcResultMatchers.jsonPath("$.jugadorFanatico").value(null))

    }

    @Test
    fun `Se obtienen todas las provincias disponibles`(){
        mockMVC.perform(MockMvcRequestBuilders.get("/provincias"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))

    }
    @Test
    fun `Se obtienen las localidades de una provincia a partir del nombre de la provincia`(){
        mockMVC.perform(MockMvcRequestBuilders.get("/localidades")
            .param("provincia", "Buenos Aires"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(4))

    }
    @Test
    fun `Se buscan las localidades de una provincia que no esta ingresada y emite error 404`(){
        mockMVC.perform(MockMvcRequestBuilders.get("/localidades")
            .param("provincia", "Catamarca"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }
}