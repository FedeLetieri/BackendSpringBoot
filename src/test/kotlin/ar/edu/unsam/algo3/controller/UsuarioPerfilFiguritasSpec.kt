package ar.edu.unsam.algo3.controller

import ar.edu.unsam.algo3.domain.*
import ar.edu.unsam.algo3.repository.RepositorioFiguritasBusqueda
import ar.edu.unsam.algo3.repository.repositorioUsuario
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
@DisplayName("Controller de Figuritas del perfil de Usuario")
class UsuarioPerfilFiguritasSpec(@Autowired val mockMVC: MockMvc){
    @Autowired
    lateinit var usuarioRepository : repositorioUsuario

    @Autowired
    lateinit var figuritasRepository : RepositorioFiguritasBusqueda

    @BeforeEach
    fun init(){
        usuarioRepository.clear()
        figuritasRepository.clear()

        usuarioRepository.nextID = 0
        figuritasRepository.nextID = 0

        val fechaNacimiento = LocalDate.now()
        val direccionUsuario = Direccion("Buenos Aires","Chascomus","Avenida Siempre Viva",5555, Point(23,-1))
        var seleccion = Seleccion("Portugal", Confederacion.CONMEBOL, 0, 2)
        var posicion = Delantero
        var jugador = Jugador("El", "Bicho", LocalDate.now().minusYears(20), 7, seleccion, posicion, LocalDate.now().minusYears(26), 1.98, 75.0, true, 100_000_000.0)

        usuarioRepository.apply {
            create(Usuario("Juan","Barrios","ju",fechaNacimiento,"@", direccionUsuario,20.0, "ju.png", "hola"))
        }

        figuritasRepository.apply {
            create(Figurita(2, false, NivelImpresion.MEDIA, jugador))
            create(Figurita(11, true, NivelImpresion.ALTA, jugador))
            create(Figurita(5, true, NivelImpresion.BAJA, jugador))
        }

    }

//    @Test
//    fun `Se obtienen las figuritas faltantes`(){
//        mockMVC.perform(MockMvcRequestBuilders.get("/figuritasFaltantes"))
//            .andExpect(MockMvcResultMatchers.status().isOk)
//            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(0))
//    }
//
//    @Test
//    fun `agregamos una figurita faltante y obtenemos devuelta con esa figurita agregada a la lista`(){
//        mockMVC.perform(MockMvcRequestBuilders.put("/figuritasFaltantes")
//            .param("idFiguritaFaltante","1"))
//            .andExpect(MockMvcResultMatchers.status().isOk)
//
//        mockMVC.perform(MockMvcRequestBuilders.get("/figuritasFaltantes"))
//            .andExpect(MockMvcResultMatchers.status().isOk)
//            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
//    }
//
//    @Test
//    fun `Se elimina una figurita faltante`(){
//        mockMVC.perform(MockMvcRequestBuilders.put("/figuritasFaltantes")
//            .param("idFiguritaFaltante","1"))
//            .andExpect(MockMvcResultMatchers.status().isOk)
//
//        mockMVC.perform(MockMvcRequestBuilders.get("/figuritasFaltantes"))
//            .andExpect(MockMvcResultMatchers.status().isOk)
//            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
//
//        mockMVC.perform(MockMvcRequestBuilders.delete("/figuritasFaltantes")
//            .param("idUsuario","1")
//            .param("idFiguritaFaltante","1"))
//            .andExpect(MockMvcResultMatchers.status().isOk)
//
//        mockMVC.perform(MockMvcRequestBuilders.get("/figuritasFaltantes"))
//            .andExpect(MockMvcResultMatchers.status().isOk)
//            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(0))
//    }

}