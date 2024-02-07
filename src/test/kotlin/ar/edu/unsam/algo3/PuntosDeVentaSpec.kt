package ar.edu.unsam.algo2.worldcapp

import ar.edu.unsam.algo3.domain.*
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.uqbar.geodds.Point
import java.time.DayOfWeek
import java.time.LocalDate

class PuntoDeVentaSpec : DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest

    describe("Tests Puntos de venta") {

        val pedidoQueNOIngresaPronto = Pedido(10, LocalDate.now().plusDays(15))
        val pedidoQueIngresaPronto = Pedido(10, LocalDate.now().plusDays(10))

        val direccion1 = Direccion("Buenos Aires", "San Martin", "Calle Real", 123, Point(-34.575177, -58.540963))
        val direccion2 = Direccion("Buenos Aires", "Hurlingham", "Calle Falsa", 456, Point(-34.575621, -58.641412))
        val direccion3 = Direccion("Buenos Aires", "Tapiales", "Calle Otra", 789, Point(-34.700217, -58.505156))
        val usuario1 = Usuario(
            "Juan",
            "Pérez",
            "juanperez",
            LocalDate.now().minusYears(24),
            "juanperez@gmail.com",
            direccion2,
            10.0,
            "asd.png",
            "hola"
        )
        val usuario2 = Usuario(
            "Juana",
            "Pérez",
            "juanaperez",
            LocalDate.now().minusYears(24),
            "juanaperez@gmail.com",
            direccion3,
            10.0,
            "asd.png",
            "hola"
        )

        val mockJuevesConDescuento = MockJueves(DayOfWeek.THURSDAY.value)
        //val juevesConDescuento = Jueves()

        val mockPrimeros10DiasMes = MockPrimeros10DiasMes(5)
        //val primeros10DiasMes = Primeros10DiasMes()

        describe("Metodos") {
            val puntoDeVenta = object : PuntoDeVenta(
                nombre = "Carrefour",
                direccion = direccion1,
                stockSobres = 1000,
                pedidosPendientes = mutableListOf()

            ) {
                override fun factorCambiante(cantidadSobres: Int): Double = 1.0
            }

            it("Remover un PedidoPendiente") {
                val pedido = Pedido(cantidadSobres = 1, fechaEntrega = LocalDate.now())
                puntoDeVenta.agregarPedidoPendiente(pedido)
                puntoDeVenta.removerPedidoPendiente(pedido)
                //puntoDeVenta.pedidosPendientes shouldBe beEmpty()
            }

            it("CumpleCriterioBusqueda") {
                puntoDeVenta.cumpleCriterioBusqueda("Carrefour").shouldBeTrue()
            }

            it("hayDisponibilidad de Stock") {
                puntoDeVenta.hayDisponibilidad().shouldBeTrue()
                puntoDeVenta.stockSobres = 0
                puntoDeVenta.hayDisponibilidad().shouldBeFalse()
            }
        }

        describe("getters y setters") {
            val puntoDeVenta = object : PuntoDeVenta(
                nombre = "Carrefour",
                direccion = direccion1,
                stockSobres = 1000,
                pedidosPendientes = mutableListOf()

            ) {
                override fun factorCambiante(cantidadSobres: Int): Double = 1.0
            }
            it("Set Nombre") {
                puntoDeVenta.nombre = "Walmart"
                puntoDeVenta.nombre shouldBe "Walmart"
            }

            it("Set Direccion") {
                val nuevaDireccion = Direccion("Buenos Aires", "San Martin", "Calle Falsa", 456, Point(0.55, 2.3))
                puntoDeVenta.direccion = nuevaDireccion
                puntoDeVenta.direccion shouldBe nuevaDireccion
            }

            it("Setear pedidos Pendientes") {
                val nuevosPedidos = mutableListOf(
                    Pedido(cantidadSobres = 1, fechaEntrega = LocalDate.now()),
                    Pedido(cantidadSobres = 2, fechaEntrega = LocalDate.now().plusDays(1))
                )
                puntoDeVenta.pedidosPendientes = nuevosPedidos
                puntoDeVenta.pedidosPendientes shouldBe nuevosPedidos
            }

            it("Setear stockSobres") {
                puntoDeVenta.stockSobres = 500
                puntoDeVenta.stockSobres shouldBe 500
            }

            it("Setear Id") {
                puntoDeVenta.id = 1
                puntoDeVenta.id shouldBe 1
            }

            it("costoMinimoFigurita (getter)") {
                PuntoDeVenta.COSTO_MINIMO_FIGURITA shouldBe 170.0
            }

            it("kmMinimo (getter)") {
                PuntoDeVenta.KM_MINIMO shouldBe 10.0
            }

            it("costoMinimo10Km (getter)") {
                PuntoDeVenta.COSTO_MINIMO_10_KM shouldBe 1000.0
            }

            it("costoAdicionalPorKm (getter)") {
                PuntoDeVenta.COSTO_ADICIONAL_POR_KM shouldBe 100.0
            }
        }

        describe("Kiosco") {
            describe("Es atendido por sus dueños") {
                it("Distancia mayor a 10km") {
                    val kioscoAtendidoPorSusDuenios = Kiosco("Kiosco", direccion1, 120, mutableListOf(), false

                    )
                    kioscoAtendidoPorSusDuenios.importeACobrar(usuario2, 10) shouldBe 3520.0.plusOrMinus(0.1)
                }
                it("Distancia menor o igual a 10km") {
                    val kioscoAtendidoPorSusDuenios = Kiosco("Kiosco", direccion1, 120, mutableListOf(), false)
                    kioscoAtendidoPorSusDuenios.importeACobrar(usuario1, 10) shouldBe 2970.0.plusOrMinus(0.1)
                }
            }
            describe("Tiene empleados") {
                it("Distancia mayor a 10km") {
                    val kioscoConEmpleados = Kiosco("Kiosco", direccion1, 120, mutableListOf(), true)
                    kioscoConEmpleados.importeACobrar(usuario2, 10) shouldBe 4000.0
                }
                it("Distancia menor o igual a 10km") {
                    val kioscoConEmpleados = Kiosco("Kiosco", direccion1, 120, mutableListOf(), true)
                    kioscoConEmpleados.importeACobrar(usuario1, 10) shouldBe 3375.0
                }
            }
        }

        describe("Tests Libreria") {
            val libreria = Libreria("PepeLibreria", direccion1, 20, mutableListOf(pedidoQueNOIngresaPronto))

            describe("Importe a cobrar") {
                it("Importe a cobrar si no tienen pedidos a fábrica pendientes pronto") {
                    libreria.importeACobrar(usuario1, 5) shouldBe 1942.5
                }

                it("Importe a cobrar SI tienen pedidos a fábrica pendientes pronto") {
                    libreria.agregarPedidoPendiente(pedidoQueIngresaPronto)

                    libreria.importeACobrar(usuario1, 5) shouldBe 2035.0.plusOrMinus(0.1)
                }
            }
        }

        describe("Supermercado") {
            describe("Tipo Jueves") {
                val supermercadoJueves = Supermercado("Supermercado", direccion1, 120, mutableListOf(), mockJuevesConDescuento)
                it("Distancia mayor a 10km") {
                    supermercadoJueves.importeACobrar(usuario2, 10) shouldBe 2880.0
                }
                it("Distancia menor o igual a 10km") {
                    supermercadoJueves.importeACobrar(usuario1, 10) shouldBe 2430.0
                }
            }
            describe("Tipo primeros 10 días del mes") {
                val supermercado10Dias = Supermercado("Supermercado", direccion1, 120, mutableListOf(), mockPrimeros10DiasMes)
                it("Distancia mayor a 10km") {
                    supermercado10Dias.importeACobrar(usuario2, 10) shouldBe 3040.0
                }
                it("Distancia menor o igual a 10km") {
                    supermercado10Dias.importeACobrar(usuario1, 10) shouldBe 2565.0
                }
            }
            describe("Tipo compra mayor a 200 paquetes") {
                val supermercadoCompraMayor =
                    Supermercado("Supermercado", direccion1, 300, mutableListOf(), CompraMayor200Sobres)
                it("Distancia mayor a 10km") {
                    supermercadoCompraMayor.importeACobrar(usuario2, 201) shouldBe 19618.5.plusOrMinus(0.1)
                }
                it("Distancia menor o igual a 10km") {
                    supermercadoCompraMayor.importeACobrar(usuario1, 201) shouldBe 19343.5.plusOrMinus(0.1)
                }
            }
            describe("Tipo sin descuento") {
                val supermercadoSinDescuento =
                    Supermercado("Supermercado", direccion1, 300, mutableListOf(), SinDescuento)
                it("Distancia mayor a 10km") {
                    supermercadoSinDescuento.importeACobrar(usuario2, 10) shouldBe 3200.0
                }
                it("Distancia menor o igual a 10km") {
                    supermercadoSinDescuento.importeACobrar(usuario1, 10) shouldBe 2700.0
                }
            }

            describe("Tipo combinatoria") {
                val todos = Combinatoria(mutableSetOf(SinDescuento, mockPrimeros10DiasMes, CompraMayor200Sobres, mockJuevesConDescuento))
                val supermercadoCombinatoria = Supermercado("Supermercado", direccion1, 300, mutableListOf(), todos)

                it("Distancia mayor a 10km") {
                    supermercadoCombinatoria.importeACobrar(usuario2, 201) shouldBe 17835.0
                }
                it("Distancia menor o igual a 10km") {
                    supermercadoCombinatoria.importeACobrar(usuario1, 201) shouldBe 17585.0
                }
            }
        }
    }
})

class TipoDeDescuentoSpec : DescribeSpec({

    val mockJuevesConDescuento = MockJueves(DayOfWeek.THURSDAY.value)
    val mockJuevesSinDescuento = MockJueves(DayOfWeek.WEDNESDAY.value)
    //val juevesConDescuento = Jueves()
    //val juevesSinDescuento = Jueves()

    val mockPrimeros10DiasMes = MockPrimeros10DiasMes(5)
    val mockNoPrimeros10DiasMes = MockPrimeros10DiasMes(15)
    //val primeros10DiasMes = Primeros10DiasMes()
    //val noPrimeros10DiasMes = Primeros10DiasMes()

    describe("Jueves") {
        it("Es jueves") {
            mockJuevesConDescuento.descuento() shouldBe 10.0
        }
        it("No es jueves") {
            mockJuevesSinDescuento.descuento() shouldBe 0.0
        }
    }

    describe("Primeros 10 días del mes") {
        it("Es primeros 10 días del mes") {
            mockPrimeros10DiasMes.descuento() shouldBe 5.0
        }
        it("No es primeros 10 días del mes") {
            mockNoPrimeros10DiasMes.descuento() shouldBe 0.0
        }
    }

    describe("Compra mayor a 200 paquetes") {
        it("Es compra mayor a 200 paquetes") {
            CompraMayor200Sobres.descuento(201) shouldBe 45.0
        }
        it("No es compra mayor a 200 paquetes") {
            CompraMayor200Sobres.descuento(200) shouldBe 0.0
        }
    }

    describe("Sin descuento") {
        it("Único caso") {
            SinDescuento.descuento() shouldBe 0.0
        }
    }

    describe("Combinatoria (solo casos en que se cumplan todos los descuentos)") {
        it("Solo Jueves") {
            val soloJueves = Combinatoria(mutableSetOf(mockJuevesConDescuento))
            soloJueves.descuento() shouldBe 10.0
        }
        it("Solo Primeros10DiasMes") {
            val soloPrimeros10DiasMes = Combinatoria(mutableSetOf(mockPrimeros10DiasMes))
            soloPrimeros10DiasMes.descuento() shouldBe 5.0
        }
        it("Solo PedidoMayor200Sobres") {
            val soloCompraMayor200Paquetes = Combinatoria(mutableSetOf(CompraMayor200Sobres))
            soloCompraMayor200Paquetes.descuento(201) shouldBe 45.0
        }
        it("Solo SinDescuento") {
            val soloSinDescuento = Combinatoria(mutableSetOf(SinDescuento))
            soloSinDescuento.descuento() shouldBe 0.0
        }
        it("Jueves y Primeros10DiasMes") {
            val combinatoria = Combinatoria(mutableSetOf(mockJuevesConDescuento, mockPrimeros10DiasMes))
            combinatoria.descuento() shouldBe 15.0
        }

        it("Jueves y CompraMayor200Sobres") {
            val combinatoria = Combinatoria(mutableSetOf(mockJuevesConDescuento, CompraMayor200Sobres))
            combinatoria.descuento(201) shouldBe 50.0
        }
        it("Jueves y SinDescuento") {
            val combinatoria = Combinatoria(mutableSetOf(mockJuevesConDescuento, SinDescuento))
            combinatoria.descuento() shouldBe 10.0
        }
        it("CompraMayor200Sobres y Primeros10DiasMes") {
            val combinatoria = Combinatoria(mutableSetOf(CompraMayor200Sobres, mockPrimeros10DiasMes))
            combinatoria.descuento(201) shouldBe 50.0
        }
        it("CompraMayor200Sobres y SinDescuento") {
            val combinatoria = Combinatoria(mutableSetOf(CompraMayor200Sobres, SinDescuento))
            combinatoria.descuento(201) shouldBe 45.0
        }
        it("Jueves, Primeros10DiasMes y PedidoMayor200Sobres") {
            val combinatoria = Combinatoria(mutableSetOf(CompraMayor200Sobres, mockJuevesConDescuento, mockPrimeros10DiasMes))
            combinatoria.descuento(201) shouldBe 50.0
        }
        it("Jueves, Primeros10DiasMes y SinDescuento") {
            val combinatoria = Combinatoria(mutableSetOf(SinDescuento, mockJuevesConDescuento, mockPrimeros10DiasMes))
            combinatoria.descuento() shouldBe 15.0
        }
        it("Jueves, SinDescuento y PedidoMayor200Sobres") {
            val combinatoria = Combinatoria(mutableSetOf(SinDescuento, mockJuevesConDescuento, CompraMayor200Sobres))
            combinatoria.descuento(201) shouldBe 50.0
        }
        it("Primeros10DiasMes, SinDescuento y PedidoMayor200Sobres") {
            val combinatoria = Combinatoria(mutableSetOf(SinDescuento, mockPrimeros10DiasMes, CompraMayor200Sobres))
            combinatoria.descuento(201) shouldBe 50.0
        }
        it("Todos") {
            val todos = Combinatoria(mutableSetOf(SinDescuento, mockPrimeros10DiasMes, CompraMayor200Sobres, mockJuevesConDescuento))
            todos.descuento(201) shouldBe 50.0
        }
    }

    describe("Combinatoria (casos en los que algunas condiciones se cumplan") {
        it("Se cumple solo Jueves") {
            val combinatoria = Combinatoria(mutableSetOf(mockJuevesConDescuento, mockNoPrimeros10DiasMes, CompraMayor200Sobres, SinDescuento))
            combinatoria.descuento() shouldBe 10.0
        }
        it("Se cumple solo Primeros10DiasMes") {
            val combinatoria = Combinatoria(mutableSetOf(mockJuevesSinDescuento, mockPrimeros10DiasMes, CompraMayor200Sobres, SinDescuento))
            combinatoria.descuento() shouldBe 5.0
        }
        it("Se cumple solo PedidoMayor200Sobres") {
            val combinatoria = Combinatoria(mutableSetOf(mockJuevesSinDescuento, mockNoPrimeros10DiasMes, CompraMayor200Sobres, SinDescuento))
            combinatoria.descuento(201) shouldBe 45.0
        }
        it("Se cumple Jueves y PedidoMayor200Sobres") {
            val combinatoria = Combinatoria(mutableSetOf(mockJuevesConDescuento, mockNoPrimeros10DiasMes, CompraMayor200Sobres, SinDescuento))
            combinatoria.descuento(201) shouldBe 50.0
        }
        it("Se cumple Jueves y Primeros10DiasMes") {
            val combinatoria = Combinatoria(mutableSetOf(mockJuevesConDescuento, mockPrimeros10DiasMes, CompraMayor200Sobres, SinDescuento))
            combinatoria.descuento() shouldBe 15.0
        }
        it("Se cumple Primeros10DiasMes y PedidoMayor200Sobres") {
            val combinatoria = Combinatoria(mutableSetOf(mockJuevesSinDescuento, mockPrimeros10DiasMes, CompraMayor200Sobres, SinDescuento))
            combinatoria.descuento(201) shouldBe 50.0
        }
    }
})

fun MockPrimeros10DiasMes(fecha: Int): Primeros10DiasMes {
    val descuento10Dias = mockk<Primeros10DiasMes>(relaxUnitFun = true)
    every { descuento10Dias.condicionDescuento(any()) } answers { fecha <= 10 }
    every { descuento10Dias.cantidadDescuento(any()) } returns 5.0
    every { descuento10Dias.descuento(any()) } answers { if(descuento10Dias.condicionDescuento(0)) descuento10Dias.cantidadDescuento(0) else 0.0 }
    return descuento10Dias
}

fun MockJueves(fecha: Int): Jueves {
    val descuentoJueves = mockk<Jueves>(relaxUnitFun = true)
    every { descuentoJueves.condicionDescuento(any()) } answers { fecha == DayOfWeek.THURSDAY.value }
    every { descuentoJueves.cantidadDescuento(any()) } returns 10.0
    every { descuentoJueves.descuento(any()) } answers { if(descuentoJueves.condicionDescuento(0)) descuentoJueves.cantidadDescuento(0) else 0.0 }
    return descuentoJueves
}