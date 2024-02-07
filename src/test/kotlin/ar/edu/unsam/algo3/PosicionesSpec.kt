package ar.edu.unsam.algo2.worldcapp

import ar.edu.unsam.algo3.domain.*
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class PosicionesSpec : DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest

    describe("Tests de las posiciones del jugador") {

        var seleccion = Seleccion("Argentina", Confederacion.CONMEBOL, 3, 15)
        var argentina = Seleccion("Argentin", Confederacion.CONMEBOL, 1, 2)
        var posicion = Arquero
        var jugador = Jugador(
            "Lionel",
            "Messi",
            LocalDate.now().minusYears(35),
            10,
            seleccion,
            posicion,
            LocalDate.now().minusYears(18),
            1.80,
            70.0,
            true,
            100_000_000.0
        )

        describe("Test arquero") {
            it("el arquero supera la altura") {
                jugador.valoracionJugador() shouldBe 180
            }
            it("el arquero NO supera la altura") {
                jugador.altura = 1.70
                jugador.valoracionJugador() shouldBe 100
            }

        }

        describe("Test Defensa") {
            jugador.posicion = Defensa
            it("el Defensor es lider") {
                jugador.valoracionJugador() shouldBe 230
            }

            it("el Defensor NO es lider") {
                jugador.esLider = false
                jugador.valoracionJugador() shouldBe 50
            }
        }


        describe("Tests Mediocampo") {
            jugador.posicion = MedioCampo
            it("El mediocampo es Ligero") {
                jugador.valoracionJugador() shouldBe 220
            }
            it("El mediocampo NO es ligero") {
                jugador.peso = 80.0
                jugador.valoracionJugador() shouldBe 150
            }
        }

        describe("Tests Delantero") {
            jugador.posicion = Delantero

            it("Su seleccion es campeona del mundo") {
                jugador.valoracionJugador() shouldBe 300
            }

            it("Su seleccion NO es campeona del mundo") {
                seleccion.cantidadCopasMundo = 0
                jugador.valoracionJugador() shouldBe 200
            }
        }

        describe("Posicion Polivalente") {
            var Polivalente1posicion = Polivalente(mutableSetOf(MedioCampo))
            var Polivalente2posicion = Polivalente(mutableSetOf(MedioCampo, Delantero))

            var jugadorPolivalente = Jugador(
                "Lionel",
                "Messi",
                LocalDate.now().minusYears(30),
                10,
                argentina,
                posicion,
                LocalDate.now().minusYears(5),
                1.80,
                70.0,
                true,
                10_000_000.0
            )

            describe("Valor para 1 posicion (Mediocampo)") {

                it("Valor para 1 posicion (Mediocampo)") {
                    jugadorPolivalente.posicion = Polivalente1posicion
                    jugadorPolivalente.valoracionJugador() shouldBe 150
                }
            }
            describe("Valor para 2 posicion (Mediocampo y Delantero)") {
                var Polivalente2posicion = Polivalente(mutableSetOf(MedioCampo, Delantero))
                jugadorPolivalente.posicion = Polivalente2posicion

                it("Valor para 2 posicion (Mediocampo y Delantero") {

                    jugadorPolivalente.valoracionJugador() shouldBe 175
                }

                it("es leyenda pero no promesa") {

                    jugadorPolivalente.anioDebutSeleccion = LocalDate.now().minusYears(13)
                    jugadorPolivalente.valoracionJugador() shouldBe 405
                }

                it("No es leyenda pero es promesa") {

                    jugadorPolivalente.fechaNacimiento = LocalDate.now().minusYears(22)
                    jugadorPolivalente.anioDebutSeleccion = LocalDate.now().minusYears(1)

                    jugadorPolivalente.valoracionJugador() shouldBe 413
                }
                it("No es leyenda y tampoco promesa") {

                    jugadorPolivalente.cotizacion = 30_000_000.0

                    jugadorPolivalente.valoracionJugador() shouldBe 175
                }


            }

        }
    }
})