package ar.edu.unsam.algo2.worldcapp

import ar.edu.unsam.algo3.domain.*
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import java.time.LocalDate

class JugadorSpec : DescribeSpec({

    var portugal = Seleccion("Portugal", Confederacion.CONMEBOL, 0, 2)
    var argentina = Seleccion("Argentin", Confederacion.CONMEBOL, 1, 2)
    val delantero = Delantero
    val arquero = Arquero
    val mediocampista = MedioCampo
    val defensa = Defensa


    describe("Tests Jugador Normal") {

        var jugadorNormal = Jugador(
            "El",
            "Bicho",
            LocalDate.now().minusYears(26),
            7,
            portugal,
            delantero,
            LocalDate.now().minusYears(2),
            1.98,
            75.0,
            true,
            100_000_000.0
        )

        it("Pais del jugador (solo 3 LETRAS)") {
            jugadorNormal.pais() shouldBe "POR"
        }

        it("Edad del jugador") {
            jugadorNormal.edad() shouldBe 26
        }

        describe("Valoracion del jugador") {
            describe("Si es Defensa") {
                jugadorNormal.posicion = defensa
                it("Es Lider con 2 años de antiguedad") {
                    jugadorNormal.esLider = true
                    jugadorNormal.antiguedad() shouldBe 2
                    jugadorNormal.valoracionJugador() shouldBe 70
                }
                it("No es Lider") {
                    jugadorNormal.esLider = false
                    jugadorNormal.valoracionJugador() shouldBe 50
                }
            }
            describe("Si es Arquero") {
                jugadorNormal.posicion = arquero
                it("Con Plus Por Altura") {
                    jugadorNormal.valoracionJugador() shouldBe 198
                }
                it("Sin Plus Por Altura") {
                    jugadorNormal.altura = 1.79
                    jugadorNormal.valoracionJugador() shouldBe 100
                }
            }
            describe("Si es MedioCampista") {
                jugadorNormal.posicion = mediocampista
                it("No es Ligero") {
                    jugadorNormal.valoracionJugador() shouldBe 150.0
                }
                it("Es Ligero") {
                    jugadorNormal.peso = 68.0
                    jugadorNormal.valoracionJugador() shouldBe 218.0
                }
            }
            describe("Si es Delantero") {
                jugadorNormal.posicion = delantero
                it("No es Campeon del Mundo") {
                    jugadorNormal.valoracionJugador() shouldBe 200.0
                }
                it("Es Campeon del Mundo con nroCamiseta 7") {
                    jugadorNormal.pais() shouldBe "POR"
                    portugal.cantidadCopasMundo = 1
                    jugadorNormal.valoracionJugador() shouldBe 270
                }
            }
        }

        describe("Jugador es Campeon del Mundo?") {
            it("No es Campeon del Mundo") {
                portugal.cantidadCopasMundo = 0
                jugadorNormal.seleccion.esCampeonDelMundo().shouldBeFalse()
            }
            it("Es Campeon del Mundo") {
                portugal.cantidadCopasMundo = 1
                jugadorNormal.seleccion.esCampeonDelMundo().shouldBeTrue()
            }
        }

        describe("Jugador Es Ligero?") {
            it("No es ligero") {
                jugadorNormal.peso = 77.0
                jugadorNormal.esLigero().shouldBeFalse()
            }
            it("Es ligero") {
                jugadorNormal.peso = 67.0
                jugadorNormal.esLigero().shouldBeTrue()
            }
        }

        describe("Jugador Camiseta Par?") {
            it("No es Par") {
                jugadorNormal.nroCamiseta = 7
                jugadorNormal.esParCamiseta().shouldBeFalse()
            }
            it("Es Par") {
                jugadorNormal.nroCamiseta = 10
                jugadorNormal.esParCamiseta().shouldBeTrue()
            }
        }

        describe("Funcion Numero de Camiseta Entre") {
            context("El número de camiseta está dentro del rango") {
                it("debería retornar verdadero") {
                    jugadorNormal.nroCamiseta = 5
                    jugadorNormal.nroCamisetaEntre(5, 10).shouldBeTrue()
                }
            }
            context("El número de camiseta es menor que el inicio del rango") {
                it("debería retornar falso") {
                    jugadorNormal.nroCamiseta = 4
                    jugadorNormal.nroCamisetaEntre(5, 10).shouldBeFalse()
                }
            }
            context("El número de camiseta es mayor que el final del rango") {
                it("debería retornar falso") {
                    jugadorNormal.nroCamiseta = 11
                    jugadorNormal.nroCamisetaEntre(5, 10).shouldBeFalse()
                }
            }
        }

        describe("Función esLigero") {
            context("El peso está dentro del rango de peso ligero") {
                it("debería retornar verdadero") {
                    jugadorNormal.peso = 67.0
                    jugadorNormal.esLigero().shouldBeTrue()
                }
            }
            context("El peso es igual al límite inferior del rango de peso ligero") {
                it("debería retornar verdadero") {
                    jugadorNormal.peso = 65.0
                    jugadorNormal.esLigero().shouldBeTrue()
                }
            }
            context("El peso es igual al límite superior del rango de peso ligero") {
                it("debería retornar verdadero") {
                    jugadorNormal.peso = 70.0
                    jugadorNormal.esLigero().shouldBeTrue()
                }
            }
            context("El peso es menor que el límite inferior del rango de peso ligero") {
                it("debería retornar falso") {
                    jugadorNormal.peso = 64.0
                    jugadorNormal.esLigero().shouldBeFalse()
                }
            }
            context("El peso es mayor que el límite superior del rango de peso ligero") {
                it("debería retornar falso") {
                    jugadorNormal.peso = 71.0
                    jugadorNormal.esLigero().shouldBeFalse()
                }
            }
        }

        describe("Jugador es Promesa de Futbol?") {
            it("es Promesa de Fútbol con edad 22, cotización 20M y antigüedad 2 años") {
                jugadorNormal.fechaNacimiento = LocalDate.now().minusYears(22)
                jugadorNormal.cotizacion = 20_000_000.00
                jugadorNormal.anioDebutSeleccion = LocalDate.now().minusYears(2)
                jugadorNormal.esPromesaDeFutbol().shouldBeTrue()
            }

            it("no es Promesa de Fútbol con edad 23, cotización 20M y antigüedad 2 años") {
                jugadorNormal.fechaNacimiento = LocalDate.now().minusYears(23)
                jugadorNormal.cotizacion = 20_000_001.00
                jugadorNormal.anioDebutSeleccion = LocalDate.now().minusYears(2)
                jugadorNormal.esPromesaDeFutbol().shouldBeFalse()
            }

            it("no es Promesa de Fútbol con edad 22, cotización 20M y antigüedad 2 años") {
                jugadorNormal.fechaNacimiento = LocalDate.now().minusYears(22)
                jugadorNormal.cotizacion = 20_000_001.00
                jugadorNormal.anioDebutSeleccion = LocalDate.now().minusYears(2)
                jugadorNormal.esPromesaDeFutbol().shouldBeFalse()
            }

            it("no es Promesa de Fútbol con edad 22, cotización 20M y antigüedad 3 años") {
                jugadorNormal.fechaNacimiento = LocalDate.now().minusYears(22)
                jugadorNormal.cotizacion = 20_000_000.00
                jugadorNormal.anioDebutSeleccion = LocalDate.now().minusYears(3)
                jugadorNormal.esPromesaDeFutbol().shouldBeFalse()
            }
        }

        describe("Jugador es Leyenda?") {
            jugadorNormal.cotizacion = 100_000.0
            jugadorNormal.anioDebutSeleccion = LocalDate.now()
            jugadorNormal.nroCamiseta = 1

            describe("Factor Antiguedad True, Factor Cotizacion True, Factor nroCamiseta False") {
                jugadorNormal.cotizacion = 100_000_000.0
                jugadorNormal.anioDebutSeleccion = LocalDate.now().minusYears(13)
                it("Es Leyenda") {
                    jugadorNormal.esLeyenda().shouldBeTrue()
                }
            }

            describe("Factor Antiguedad False, Factor Cotizacion True, Factor nroCamiseta False") {
                jugadorNormal.cotizacion =  100_000_000.0
                jugadorNormal.anioDebutSeleccion = LocalDate.now()
                it("No es Leyenda") {
                    jugadorNormal.esLeyenda().shouldBeFalse()
                }
            }

            describe("Factor Antiguedad True, Factor Cotizacion False, Factor nroCamiseta False") {
                jugadorNormal.cotizacion = 100_000.0
                jugadorNormal.anioDebutSeleccion = LocalDate.now().minusYears(13)
                it("No es Leyenda") {
                    jugadorNormal.esLeyenda() shouldBe false
                }
            }

            describe("Factor Antiguedad False, Factor Cotizacion False, Factor nroCamiseta False") {
                jugadorNormal.cotizacion = 100_000.0
                jugadorNormal.anioDebutSeleccion = LocalDate.now()
                it("No es leyenda") {
                    jugadorNormal.esLeyenda().shouldBeFalse()
                }
            }


            describe("Factor Antiguedad True, Factor Cotizacion True, Factor nroCamiseta True") {
                jugadorNormal.cotizacion = 100_000_000.0
                jugadorNormal.anioDebutSeleccion = LocalDate.now().minusYears(13)
                jugadorNormal.nroCamiseta = 7
                describe("Es Lider") {
                    jugadorNormal.esLider = true
                    it("Es Leyenda") {
                        jugadorNormal.esLeyenda().shouldBeTrue()
                    }
                }
                describe("No es Lider") {
                    jugadorNormal.esLider = false
                    it("Es Leyenda") {
                        jugadorNormal.esLeyenda() shouldBe true
                    }
                }
            }

            describe("Factor Antiguedad False, Factor Cotizacion True, Factor nroCamiseta True") {
                jugadorNormal.cotizacion =  100_000_000.0
                jugadorNormal.anioDebutSeleccion = LocalDate.now()
                jugadorNormal.nroCamiseta = 7
                describe("Es Lider") {
                    jugadorNormal.esLider = true
                    it("No es Leyenda") {
                        jugadorNormal.esLeyenda() shouldBe false
                    }
                }
                describe("No es Lider") {
                    jugadorNormal.esLider = false
                    it("No es Leyenda") {
                        jugadorNormal.esLeyenda().shouldBeFalse()
                    }
                }
            }

            describe("Factor Antiguedad True, Factor Cotizacion False, Factor nroCamiseta True") {
                jugadorNormal.cotizacion = 100_000.0
                jugadorNormal.anioDebutSeleccion = LocalDate.now().minusYears(13)
                jugadorNormal.nroCamiseta = 7
                describe("Es Lider") {
                    jugadorNormal.esLider = true
                    it("Es Leyenda") {
                        jugadorNormal.esLeyenda() shouldBe true
                    }
                }
                describe("No es Lider") {
                    jugadorNormal.esLider = false
                    it("No es Leyenda") {
                        jugadorNormal.esLeyenda() shouldBe false
                    }
                }
            }

            describe("Factor Antiguedad False, Factor Cotizacion False, Factor nroCamiseta True") {
                jugadorNormal.cotizacion = 100_000.0
                jugadorNormal.anioDebutSeleccion = LocalDate.now()
                jugadorNormal.nroCamiseta = 7
                describe("Es Lider") {
                    jugadorNormal.esLider = true
                    it("No es Leyenda") {
                        jugadorNormal.esLeyenda() shouldBe false
                    }
                }
                describe("No es Lider") {
                    jugadorNormal.esLider = false
                    it("No es Leyenda") {
                        jugadorNormal.esLeyenda() shouldBe false
                    }
                }
            }
        }

        describe("Funcion Seleccion Con Copas Par") {
            it("Si es Par") {
                portugal.cantidadCopasMundo = 2
                jugadorNormal.seleccionConCopasPar().shouldBeTrue()
            }
            it("Si es Impar") {
                portugal.cantidadCopasMundo = 1
                jugadorNormal.seleccionConCopasPar().shouldBeFalse()
            }
        }

        describe("getters y setters") {

            it("Nombre") {
                jugadorNormal.nombre = "El"
                jugadorNormal.nombre shouldBe "El"
            }

            it("Apellido") {
                jugadorNormal.apellido = "Bicho"
                jugadorNormal.apellido shouldBe "Bicho"
            }

            it("fechaNacimiento") {
                val nuevaFecha = LocalDate.now().minusYears(26)
                jugadorNormal.fechaNacimiento = LocalDate.now().minusYears(26)
                jugadorNormal.fechaNacimiento shouldBe nuevaFecha
            }

            it("Numero") {
                jugadorNormal.nroCamiseta = 10
                jugadorNormal.nroCamiseta shouldBe 10
            }

            it("Seleccion") {
                var nuevaSeleccion = Seleccion("Argentina", Confederacion.CONMEBOL, 2, 1)
                jugadorNormal.seleccion = nuevaSeleccion
                jugadorNormal.seleccion shouldBe nuevaSeleccion
            }

            it("Posicion") {
                jugadorNormal.posicion = arquero
                jugadorNormal.posicion shouldBe arquero
            }

            it("anioDebutSeleccion") {
                val nuevaFecha = LocalDate.of(2005, 8, 17)
                jugadorNormal.anioDebutSeleccion = nuevaFecha
                jugadorNormal.anioDebutSeleccion shouldBe nuevaFecha
            }

            it("Altura") {
                jugadorNormal.altura = 1.7
                jugadorNormal.altura shouldBe 1.7
            }

            it("Peso") {
                jugadorNormal.peso = 70.0
                jugadorNormal.peso shouldBe 70.0
            }

            it("esLider") {
                jugadorNormal.esLider = false
                jugadorNormal.esLider shouldBe false
            }

            it("Cotizacion") {
                jugadorNormal.cotizacion = 200_000_000.0
                jugadorNormal.cotizacion shouldBe 200_000_000.0
            }
        }
    }

   })