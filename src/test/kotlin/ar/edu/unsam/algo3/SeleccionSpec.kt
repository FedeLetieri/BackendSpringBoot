package ar.edu.unsam.algo2.worldcapp

import ar.edu.unsam.algo3.domain.Confederacion
import ar.edu.unsam.algo3.domain.Seleccion
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class SeleccionSpec: DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest

    var argentina = Seleccion("Argentina", Confederacion.CONMEBOL, 3, 15)
    var guatemala = Seleccion("Guatemala", Confederacion.CONMEBOL, 0, 0)
    var uruguay = Seleccion("Uruguay", Confederacion.CONMEBOL, 2, 7)

    describe("Test de Seleccion"){
        describe("Es campeon"){
            it("Campeona del mundo"){
                argentina.esCampeonDelMundo() shouldBe true
            }
            it("No campeona del mundo"){
                guatemala.esCampeonDelMundo() shouldBe false
            }
        }
        describe("Su cantidad de copas es correcta"){
            it("cantidad de copas correctas"){
                uruguay.totalDeCopas() shouldBe 9
            }
        }

        it("CumpleCriterio ") {
            argentina.cumpleCriterioBusqueda("Argentina") shouldBe true
        }

        it("setPais") {
            argentina.pais = "Brasil"
            argentina.pais shouldBe "Brasil"
        }

        it("setConfederacion") {
            argentina.confederacion = Confederacion.UEFA
            argentina.confederacion shouldBe Confederacion.UEFA
        }

        it("setCantidadCopasMundo") {
            argentina.cantidadCopasMundo = 3
            argentina.cantidadCopasMundo shouldBe 3
        }

        it("setCantidadCopasConfederacion") {
            argentina.cantidadCopasConfederacion = 2
            argentina.cantidadCopasConfederacion shouldBe 2
        }

        it("setId ") {
            argentina.id = 1
            argentina.id shouldBe 1
        }



    }

})