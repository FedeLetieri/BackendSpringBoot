package ar.edu.unsam.algo2.worldcapp


import ar.edu.unsam.algo3.domain.*
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class FiguritaSpec : DescribeSpec({
    var seleccion = Seleccion("Portugal", Confederacion.CONMEBOL, 0, 2)
    var posicion = Delantero
    var jugador = Jugador("El", "Bicho", LocalDate.now().minusYears(20), 7, seleccion, posicion, LocalDate.now().minusYears(26), 1.98, 75.0, true, 100_000_000.0)

    describe("Valoracion Base") {
        describe("Para Nivel no nivelImpresion BAJO") {
            val figuritaNivelMedio = Figurita(2, false, NivelImpresion.MEDIA, jugador)

            describe("Con onFire True") {
                figuritaNivelMedio.onFire = true
                it("Con numero impar") {
                    figuritaNivelMedio.numero = 1
                    figuritaNivelMedio.valoracionBase() shouldBe 102.0.plusOrMinus(0.001)
                }

                it("Con numero par") {
                    figuritaNivelMedio.numero = 2
                    figuritaNivelMedio.valoracionBase() shouldBe 112.20.plusOrMinus(0.001)
                }
            }

            describe("Con onFire False") {
                figuritaNivelMedio.onFire = false
                it("Con numero impar") {
                    figuritaNivelMedio.numero = 1
                    figuritaNivelMedio.valoracionBase() shouldBe 85.0.plusOrMinus(0.001)
                }

                it("Con numero par") {
                    figuritaNivelMedio.numero = 2
                    figuritaNivelMedio.valoracionBase() shouldBe 93.5.plusOrMinus(0.001)
                }
            }
        }

        describe("Para nivelImpresion BAJO") {
            val figuritaNivelBaja = Figurita(2, false, NivelImpresion.BAJA, jugador)

            describe("Con onFire True") {
                figuritaNivelBaja.onFire = true
                it("Con numero par") {
                    figuritaNivelBaja.valoracionBase() shouldBe 132.0.plusOrMinus(0.001)
                }

                it("Con numero impar"){
                    figuritaNivelBaja.numero = 1
                    figuritaNivelBaja.valoracionBase() shouldBe 120.0.plusOrMinus(0.001)
                }

            }

            describe("Con onFire False") {
                figuritaNivelBaja.onFire = false

                it("Con numero par") {
                    figuritaNivelBaja.numero = 2
                    figuritaNivelBaja.valoracionBase() shouldBe 110.0.plusOrMinus(0.001)
                }

                it("Con numero impar") {
                    figuritaNivelBaja.numero = 1
                    figuritaNivelBaja.valoracionBase() shouldBe 100.0.plusOrMinus(0.001)
                }
            }

        }
    }

    describe("Valoración") {
        describe("Para un arquero") {
            val seleccion = Seleccion("Portugal", Confederacion.CONMEBOL, 0, 2)
            val posicion = Arquero
            val arquero = Jugador("Juan", "Pérez", LocalDate.now().minusYears(20), 12, seleccion, posicion, LocalDate.now().minusYears(14), 1.79, 70.0, false, 12_000.0)
            val figuritaNivelMedio = Figurita(2, false, NivelImpresion.MEDIA, arquero)

            it("Mide menos que la altura límite") {
                arquero.altura -=   1
                figuritaNivelMedio.valoracion() shouldBe 193.5
            }
        }
    }

    describe("Es par") {
        it("Si es par es verdadero") {
            val figuritaPar = Figurita(2, false, NivelImpresion.BAJA, jugador)
            figuritaPar.esPar().shouldBeTrue()
        }
        it("Si es impar debe ser falso") {
            val figuritaImpar = Figurita(3, false, NivelImpresion.BAJA, jugador)
            figuritaImpar.esPar().shouldBeFalse()
        }
    }

    describe("Es nivel") {
        it("Es alta") {
            val figuritaNivelImpresionAlta = Figurita(2, false, NivelImpresion.ALTA, jugador)
            figuritaNivelImpresionAlta.esNivel(NivelImpresion.ALTA).shouldBeTrue()
        }
        it("Es media") {
            val figuritaNivelImpresionMedia = Figurita(2, false, NivelImpresion.MEDIA, jugador)
            figuritaNivelImpresionMedia.esNivel(NivelImpresion.MEDIA).shouldBeTrue()
        }
        it("Es baja") {
            val figuritaNivelImpresionMedia = Figurita(2, false, NivelImpresion.BAJA, jugador)
            figuritaNivelImpresionMedia.esNivel(NivelImpresion.BAJA).shouldBeTrue()
        }
    }

    describe("Jugador Camiseta Par") {
        it("Si es par es verdadero") {
            jugador.nroCamiseta = 2
            val figurita = Figurita(2, false, NivelImpresion.BAJA, jugador)
            figurita.jugadorCamisetaPar().shouldBeTrue()
        }
        it("Si es impar debe ser falso") {
            jugador.nroCamiseta = 3
            val figurita = Figurita(3, false, NivelImpresion.BAJA, jugador)
            figurita.jugadorCamisetaPar().shouldBeFalse()
        }
    }

    describe("Selección con copas par") {
        it("Si es par es verdadero") {
            seleccion.cantidadCopasMundo = 2
            val figurita = Figurita(2, false, NivelImpresion.BAJA, jugador)
            figurita.jugadorConSeleccionConCoparPar().shouldBeTrue()
        }
        it("Si es impar debe ser falso") {
            seleccion.cantidadCopasMundo = 3
            val figurita = Figurita(3, false, NivelImpresion.BAJA, jugador)
            figurita.jugadorConSeleccionConCoparPar().shouldBeFalse()
        }
    }

    describe("Selección") {
        it("Debe ser la selección establecida") {
            val figurita = Figurita(3, false, NivelImpresion.BAJA, jugador)
            figurita.seleccion() shouldBe seleccion
        }
    }

    describe("Tiene Jugador Leyenda") {
        it("Si tiene leyenda debe ser verdadero") {
            var jugador = Jugador("El", "Bicho", LocalDate.now().minusYears(20), 7, seleccion, posicion, LocalDate.now().minusYears(26), 1.98, 75.0, true, 100_000_000.0)
            val figurita = Figurita(2, false, NivelImpresion.BAJA, jugador)
            figurita.tieneJugadorLeyenda().shouldBeTrue()
        }
        it("Si no tiene leyenda debe ser falso") {
            var jugador = Jugador("El", "Bicho", LocalDate.now().minusYears(20), 7, seleccion, posicion, LocalDate.now().minusYears(5), 1.98, 75.0, false, 2_000.0)
            val figurita = Figurita(3, false, NivelImpresion.BAJA, jugador)
            figurita.tieneJugadorLeyenda().shouldBeFalse()
        }
    }

    describe("Tiene Jugador Promesa") {
        it("Si tiene promesa debe ser verdadero") {
            var jugador = Jugador("El", "Bicho", LocalDate.now().minusYears(20), 7, seleccion, posicion, LocalDate.now().minusYears(1), 1.98, 75.0, true, 2_000.0)
            val figurita = Figurita(2, false, NivelImpresion.BAJA, jugador)
            figurita.tieneJugadorPromesa().shouldBeTrue()
        }
        it("Si no tiene promesa debe ser falso") {
            var jugador = Jugador("El", "Bicho", LocalDate.now().minusYears(25), 7, seleccion, posicion, LocalDate.now().minusYears(26), 1.98, 75.0, true, 100_000_000.0)
            val figurita = Figurita(3, false, NivelImpresion.BAJA, jugador)
            figurita.tieneJugadorPromesa().shouldBeFalse()
        }
    }

    describe("Getters y Setters") {
        val figurita = Figurita(2, false, NivelImpresion.BAJA, jugador)
        it("Numero") {
            figurita.numero shouldBe 2
        }
        it("onFire") {
            figurita.onFire shouldBe false
        }
        it("Nivel Impresión") {
            figurita.nivelImpresion shouldBe NivelImpresion.BAJA
        }
        it("Jugador") {
            figurita.jugador shouldBe jugador
        }
    }
})