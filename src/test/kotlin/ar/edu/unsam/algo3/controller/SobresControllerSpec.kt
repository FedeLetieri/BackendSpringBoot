package ar.edu.unsam.algo3.controller

import ar.edu.unsam.algo3.domain.*
import ar.edu.unsam.algo3.repository.RepositorioPuntoVenta
import ar.edu.unsam.algo3.repository.repositorioUsuario
import ar.edu.unsam.algo3.services.SobresServices
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.uqbar.geodds.Point
import java.time.LocalDate

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Dado un controller de sobres")
class SobresControllerTest(@Autowired val mockMvc: MockMvc) {

    @Autowired
    lateinit var sobresServices: SobresServices

    @Autowired
    lateinit var sobresRepository:  RepositorioPuntoVenta
    @Autowired
    lateinit var usuarioRepository :  repositorioUsuario

    @BeforeEach
    fun init() {
        usuarioRepository.clear()
        sobresRepository.clear()

        val fechaNacimiento = LocalDate.now()
        val direccionUsuario = Direccion("Buenos Aires","Chascomus","Avenida Siempre Viva",5555,Point(23,-1))

        usuarioRepository.apply {
            create(Usuario("Juan","Barrios","ju",fechaNacimiento,"@", direccionUsuario,20.0, "ju.png", "hola"))
        }
        val direccionSuperChumbo = Direccion("Buenos Aires","Chascomus","Avenida Siempre Viva",5555, Point(-10,58))
        val direccionLibreriaJorge = Direccion("Buenos Aires","Chascomus","Avenida Siempre Viva",5555, Point(100,18))
        val direccionKioscoLoPibe = Direccion("Buenos Aires","Chascomus","Avenida Siempre Viva",5555, Point(10,28))
        val direccionLibreriaDani = Direccion("Buenos Aires","Chascomus","Avenida Siempre Viva",5555, Point(0,1))

        sobresRepository.apply {
            create(Supermercado("Supermercado Chumbo", direccionSuperChumbo, 100, mutableListOf(), Jueves))
            create(Libreria("Libreria Jorge", direccionLibreriaJorge, 50, mutableListOf()))
            create(Kiosco("Kiosko Lo Pibe!", direccionKioscoLoPibe, 0, mutableListOf(),true))
            create(Libreria("Libreria Daniel", direccionLibreriaDani, 10, mutableListOf()))
        }



    }


//    @Test
//    fun `puedo mockear una llamada al endpoint via get y me responde correctamente`() {
//        mockMvc
//            .perform(MockMvcRequestBuilders.get("/ordenarSobres")
//                .param("tipoOrdenamiento","cercanos"))
//            .andExpect(MockMvcResultMatchers.status().isOk)
//            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(4))
//    }
//    @Test
//    fun ` El endpoint de mas cercanos ordena bien los sobres mas cercanos para el usuario`(){
//        mockMvc
//            .perform(MockMvcRequestBuilders.get("/ordenarSobres")
//                .param("tipoOrdenamiento","cercanos"))
//            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].distancia").value(2566.611071529733))
//            .andExpect(MockMvcResultMatchers.jsonPath("$.[3].distancia").value( 8505.00828272837))
//            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].nombre").value("Libreria Daniel"))
//
//    }
//    @Test
//    fun ` El endpoint ordena de manera correcta los sobres por cantidad de dinero`(){
//        mockMvc
//            .perform(MockMvcRequestBuilders.get("/ordenarSobres")
//                .param("tipoOrdenamiento","baratos"))
//            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].precio").value(256700.0))
//            .andExpect(MockMvcResultMatchers.jsonPath("$.[3].precio").value(850600.0))
//    }
//
//
//
//    @Test
//    fun `El punto de venta con mayor stock de sobres es correcto `(){
//        mockMvc
//            .perform(MockMvcRequestBuilders.get("/ordenarSobres")
//                .param("tipoOrdenamiento","cantidad"))
//            .andExpect(MockMvcResultMatchers.jsonPath("$.[0].nombre").value("Supermercado Chumbo"))
//    }
//    @Test
//    fun ` el endpoint que filtra solo los puntos de ventas mas cercanos es correcto`(){
//        sobresServices.distanciaMaxima = 5000.0
//        mockMvc
//            .perform(MockMvcRequestBuilders.get("/ordenarSobres")
//                .param("tipoOrdenamiento","masCercanos"))
//
//            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
//    }
}
