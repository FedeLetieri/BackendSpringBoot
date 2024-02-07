package ar.edu.unsam.algo2.worldcapp

import ar.edu.unsam.algo3.domain.Direccion
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.uqbar.geodds.Point

class DireccionSpec : DescribeSpec({

    describe("Test Direccion") {
        var ubicacion = Point(10, 20)
        val direccion = Direccion(
            provincia = "Buenos Aires",
            localidad = "La plata",
            calle = "Calle Falsa 123",
            numero = 123,
            ubicacion = ubicacion
        )

        it("setNumero") {
            direccion.numero = 566
            direccion.numero shouldBe 566
        }

        it("setProvincia") {
            direccion.provincia = "Mendoza"
            direccion.provincia shouldBe "Mendoza"
        }

        it("setLocalidad") {
            direccion.localidad = "Godoy Cruz"
            direccion.localidad shouldBe "Godoy Cruz"
        }

        it("setCalle") {
            direccion.calle = "Otra Calle Falsa 321"
            direccion.calle shouldBe "Otra Calle Falsa 321"
        }

        it("setUbicacion") {
            val nuevaUbicacion = Point(30, 40)
            direccion.ubicacion = nuevaUbicacion
            direccion.ubicacion shouldBe nuevaUbicacion
        }
    }
})