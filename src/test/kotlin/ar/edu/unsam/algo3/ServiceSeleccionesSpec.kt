package ar.edu.unsam.algo2.worldcapp

import ar.edu.unsam.algo3.domain.Confederacion
import ar.edu.unsam.algo3.domain.Seleccion
import ar.edu.unsam.algo3.repository.Repositorio
import io.kotest.assertions.throwables.shouldThrowMessage
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class ServiceSeleccionesSpec : DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest

    // Instancio repoSelecciones para pasarle a ActualizadorSelecciones
    val repoSelecciones = Repositorio<Seleccion>()

    // Instancio el mockServiceSelecciones que hereda la interface ServiceSelecciones
    // con el que previamente ya piso al metodo getSelecciones
    val mockedServiceSelecciones = mockServiceSelecciones(seleccionesJson)
    val actualizadorMock = ActualizadorSelecciones(repoSelecciones, mockedServiceSelecciones)

    //Metodo Stub
    val stubServiceSelecciones = StubServiceSelecciones(seleccionesJson)
    val actualizadorStub = ActualizadorSelecciones(repoSelecciones, stubServiceSelecciones)

    describe("Al actualizar el repositorio, se hace una llamada al servicio, Stub") {
            var argentina = Seleccion("Argentina", Confederacion.CONMEBOL,2,12)
            var brasil = Seleccion("Brasil", Confederacion.CONMEBOL,5,9)
            var mexico = Seleccion("Mexico", Confederacion.CONCACAF,0,1)

            repoSelecciones.create(argentina)
            repoSelecciones.create(brasil)
            repoSelecciones.create(mexico)

            it(" modifico la seleccion creada, con el servicio actualizar") {

            repoSelecciones.getById(1).cantidadCopasMundo.shouldBe(2)
            repoSelecciones.getById(1).cantidadCopasConfederacion.shouldBe(12)

            actualizadorStub.actualizarSelecciones()

            repoSelecciones.getById(1).cantidadCopasMundo.shouldBe(3)
            repoSelecciones.getById(1).cantidadCopasConfederacion.shouldBe(15)

        }

        it("Actualiza Selecciones que no estaban en el repositorio ,creandolas"){
            actualizadorStub.actualizarSelecciones()
            repoSelecciones.getById(4).pais.shouldBe("Alemania")
        }


        it("Verifico que todas las selecciones se actualizaron correctamente") {

            actualizadorStub.actualizarSelecciones()
            repoSelecciones.getById(1).pais.shouldBe("Argentina")
            repoSelecciones.getById(2).pais.shouldBe("Brasil")
            repoSelecciones.getById(3).pais.shouldBe("Mexico")
            repoSelecciones.getById(4).pais.shouldBe("Alemania")
            repoSelecciones.getById(4).cantidadCopasMundo.shouldBe(4)
        }

        it("Si una Seleccion con ID no esta en el repositorio para actualizar, tira ERROR"){
            repoSelecciones.delete(brasil)
            val excepcion = {actualizadorStub.actualizarSelecciones()}


            shouldThrowMessage("El ID 2 no fue encontrado en el repositorio",excepcion)
        }
            }

})

val seleccionesJson =
    """
    
   [
  {
    "id": 1,
    "pais": "Argentina",
    "confederacion": "CONMEBOL",
    "copasDelMundo": 3,
    "copasConfederacion": 15
  },
  {
    "id": 2,
    "pais": "Brasil",
    "confederacion": "CONMEBOL",
    "copasDelMundo": 5,
    "copasConfederacion": 9
  },
  {
    "pais": "Alemania",
    "confederacion": "UEFA",
    "copasDelMundo": 4,
    "copasConfederacion": 3
  },
  {
    "id": 3,
    "pais": "Mexico",
    "confederacion": "CONCACAF",
    "copasDelMundo": 0,
    "copasConfederacion": 1
  }

]
""".trimIndent()
/*For testing:
   {
    "id": 20,
    "pais": "Holanda",
    "confederacion": "UEFA",
    "copasDelMundo": 0,
    "copasConfederacion": 3
  }
**/

class StubServiceSelecciones(private val selecciones: String) : ServiceSelecciones {
    override fun getSelecciones(): String {
        return selecciones
    }
}


fun mockServiceSelecciones(selecciones: String): ServiceSelecciones {
    val service = mockk<ServiceSelecciones>()
    every { service.getSelecciones() } answers { selecciones }
    return service
}