package ar.edu.unsam.algo2.worldcapp

import ar.edu.unsam.algo3.services.Mail
import ar.edu.unsam.algo3.services.MailSender
import ar.edu.unsam.algo3.domain.*
import ar.edu.unsam.algo3.repository.Repositorio
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

import io.mockk.verify
import org.uqbar.geodds.Point
import java.time.LocalDate

class ProcesoSpec : DescribeSpec({

    var usuario1 = Usuario(
        "Pepita",
        "Golondrina",
        "pepa3000",
        LocalDate.now().minusYears(20),
        "pgolondrina@gmail.com",
        Direccion("Buenos Aires", "San Martin", "Calle Real", 123, Point(0.55, 2.3)),
        250.0,
        "asd.png",
        "hola"
    )

    val delantero = Delantero
    var portugal: Seleccion = Seleccion("Portugal", Confederacion.CONMEBOL, 0, 2)
    var jugador = Jugador(
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

    var figurita = Figurita(10, false, NivelImpresion.MEDIA, jugador)
    var figurita2 = Figurita(20, false, NivelImpresion.ALTA, jugador)
    var figurita3 = Figurita(30, false, NivelImpresion.ALTA, jugador)


    var jugador6 = Jugador("El", "Bicho", LocalDate.now().minusYears(20), 7, portugal, Defensa, LocalDate.now().minusYears(26), 1.98, 75.0, true, 1_000_000_000_000.0)
    var figurita6 = Figurita(3,true, NivelImpresion.BAJA,jugador6)


    val mockedMailSender = mockk<MailSender>(relaxUnitFun = true)


    val mail = Mail(
        from = "admin@worldcapp.com.ar",
        to = "admin@worldcapp.com.ar",
        subject = "",
        content = ""
    )

    val listadoDeFiguritas: List<String>  = listOf("10", "20", "30")

    val repositorioFiguritas= Repositorio<Figurita>()

    val procesoActualizarOnFire = ActualizarOnFire(
        tipoProceso = "Se actualizaron a OnFire las figuritas seleccionadas",
        mockedMailSender,
        repoFigurita = repositorioFiguritas,
        nroDeFiguritas = listadoDeFiguritas
    )

    val repositorioUsuarios = Repositorio<Usuario>()
    val procesoEliminaUsuariosInactivos = BorrarUsuariosInactivos(
        "Se eliminaron usuarios inactivos",
        mockedMailSender,
        repositorioUsuarios)


    val repositorioDePuntosDeVenta = Repositorio<PuntoDeVenta>()

    val procesoBorrarPuntosDeVentasInactivos = BorrarPuntosDeVentasInactivos(
        tipoProceso = "Se elimino Puntos De Venta Inactivos",
        mockedMailSender,
        repoPuntosDeVentas = repositorioDePuntosDeVenta
    )

    isolationMode = IsolationMode.InstancePerTest
    val pedidoQueNOIngresaPronto = Pedido(10, LocalDate.now().plusDays(100))
    val pedidoQueIngresaPronto = Pedido(10, LocalDate.now().plusDays(90))
    val pedidoDeFabrica = Pedido(10, LocalDate.now().plusDays(90))
    val pedidoACumplir = Pedido(10, LocalDate.now().plusDays(9))

    val direccion1 =
        Direccion("Buenos Aires", "San Martin", "Calle Real", 123, Point(-34.575177, -58.540963))
    val puntoDeVentaConCaracteristicasParaSerInactivo = Libreria(
        nombre = "CarrefourInactivo",
        direccion = direccion1,
        stockSobres = 0,
        pedidosPendientes = mutableListOf()
       
    )
    val puntoDeVentaActivo = Libreria(
        nombre = "CarrefourActivo",
        direccion = direccion1,
        stockSobres = 1000,
        pedidosPendientes = mutableListOf()
       )


    val puntoDeVentaActivo2 = Kiosco("Kiosco", direccion1, 10, mutableListOf(), false)


    describe("Test metodo realizar") {
        repositorioFiguritas.create(figurita)
        repositorioFiguritas.create(figurita2)

        describe("Proceso de Actualizacion de figuritas a OnFire") {

            it("Las figuritas no estan OnFire") {
                figurita.onFire.shouldBeFalse()
                figurita2.onFire.shouldBeFalse()
            }

            it("Las figuritas pasan a estar OnFire") {
                procesoActualizarOnFire.realizar()
                figurita.onFire.shouldBeTrue()
                figurita2.onFire.shouldBeTrue()
            }

            it("Las figuritas del listado pasan a estar OnFire y las que no estan dentro del listado no son modificadas") {
                procesoActualizarOnFire.realizar()
                figurita.onFire.shouldBeTrue()
                figurita2.onFire.shouldBeTrue()
                figurita3.onFire.shouldBeFalse()
            }
        }


        describe("Test metodo realizar") {

            describe("Proceso Borrar Punto de Venta Inactivo") {

                it("Solo tienen disponibilidad") {
                    puntoDeVentaConCaracteristicasParaSerInactivo.stockSobres = 1
                    puntoDeVentaConCaracteristicasParaSerInactivo.esActivo(90).shouldBeTrue()
                }

                it("Solo tiene pedidos a fabrica sin procesar") {
                    puntoDeVentaConCaracteristicasParaSerInactivo.agregarPedidoPendiente(pedidoDeFabrica)
                    puntoDeVentaConCaracteristicasParaSerInactivo.esActivo(90).shouldBeTrue()
                }

                it("Solo tiene pedidos a fabrica pronto") {
                    puntoDeVentaConCaracteristicasParaSerInactivo.agregarPedidoPendiente(pedidoQueIngresaPronto)
                    puntoDeVentaConCaracteristicasParaSerInactivo.esActivo(90).shouldBeTrue()
                }

                it("Tiene todas las condiciones para ser un Punto de Venta activo") {
                    puntoDeVentaConCaracteristicasParaSerInactivo.stockSobres = 1
                    puntoDeVentaConCaracteristicasParaSerInactivo.agregarPedidoPendiente(pedidoDeFabrica)
                    puntoDeVentaConCaracteristicasParaSerInactivo.agregarPedidoPendiente(pedidoQueIngresaPronto)

                    puntoDeVentaConCaracteristicasParaSerInactivo.esActivo(90).shouldBeTrue()
                }

                it("No cumple con ninguna condicion para ser un Punto de Venta Activo") {
                    puntoDeVentaConCaracteristicasParaSerInactivo.esActivo(90).shouldBeFalse()
                }

                it("El proceso debe borrar los Puntos de venta Inactivos del Repositorio") {
                    repositorioDePuntosDeVenta.create(puntoDeVentaConCaracteristicasParaSerInactivo)
                    repositorioDePuntosDeVenta.create(puntoDeVentaActivo)

                    procesoBorrarPuntosDeVentasInactivos.realizar()

                    repositorioDePuntosDeVenta.coleccion.size.shouldBe(1)
                    repositorioDePuntosDeVenta.search("CarrefourActivo").shouldNotBeEmpty()
                    repositorioDePuntosDeVenta.search("CarrefourInactivo").shouldBeEmpty()
                }
            }
        }

        describe("Test ActualizarSelecciones") {
            isolationMode = IsolationMode.InstancePerTest

            val mockedMailSender = mockk<MailSender>(relaxUnitFun = true)
            val repoSelecciones = Repositorio<Seleccion>()

            var argentina = Seleccion("Argentina", Confederacion.CONMEBOL, 2, 12)
            var brasil = Seleccion("Brasil", Confederacion.CONMEBOL, 5, 9)
            var mexico = Seleccion("Mexico", Confederacion.CONCACAF, 0, 1)

            repoSelecciones.create(argentina)
            repoSelecciones.create(brasil)
            repoSelecciones.create(mexico)

            val stubServiceSelecciones = StubServiceSelecciones(seleccionesJson)
            val actualizadorStub = ActualizadorSelecciones(repoSelecciones, stubServiceSelecciones)

            val actualizarSelecciones = ActualizarSelecciones(
                tipoProceso = "Actualización de Selecciones",
                mockedMailSender,
                actualizadorStub
            )

            it("Realizo el proceso ActualizarSelecciones, verifico que todas las selecciones se actualizaron correctamente") {
                actualizarSelecciones.realizar()
                repoSelecciones.getById(1).pais.shouldBe("Argentina")
                repoSelecciones.getById(2).pais.shouldBe("Brasil")
                repoSelecciones.getById(3).pais.shouldBe("Mexico")
                repoSelecciones.getById(4).pais.shouldBe("Alemania")
                repoSelecciones.getById(4).cantidadCopasMundo.shouldBe(4)
            }

            it("debe enviar un correo al finalizar la actualización") {
                actualizarSelecciones.ejecutar()

                verify(exactly = 1) {
                    mockedMailSender.sendMail(
                        Mail(
                            from = "admin@worldcapp.com.ar",
                            to = "admin@worldcapp.com.ar",
                            subject = "Se realizó el proceso: Actualización de Selecciones",
                            content = "Se realizó el proceso: Actualización de Selecciones"
                        )
                    )
                }
            }
        }

        describe("Proceso Borrar Usuarios Inactivos"){
            repositorioUsuarios.create(usuario1)

            describe("Se elimina usuario"){
                it("Sin figuritas repetivas, album completo"){
                    procesoEliminaUsuariosInactivos.ejecutar()
                    repositorioUsuarios.search("pepa3000").contains(usuario1).shouldBeFalse()
                }
            }

            describe("No se elimina usuario"){
                it("Figuritas repetidas, album completo"){
                    usuario1.registrarRepetida(figurita6)
                    procesoEliminaUsuariosInactivos.ejecutar()
                    repositorioUsuarios.search("pepa3000").contains(usuario1).shouldBeTrue()
                }
                it("Sin figuritas repetidas, album incompleto"){
                    usuario1.registrarFaltante(figurita6)
                    procesoEliminaUsuariosInactivos.ejecutar()
                    repositorioUsuarios.search("pepa3000").contains(usuario1).shouldBeTrue()
                }
            }

        }


    }

    describe("Actualizar stocks de puntos de venta") {
        it("Se actualiza el stock") {
            val pedidoCumplido = mockPedidoSinProcesar(10)
            val pedidoCumplido2 = mockPedidoSinProcesar(5)
            repositorioDePuntosDeVenta.create(puntoDeVentaActivo)
            repositorioDePuntosDeVenta.create(puntoDeVentaActivo2)
            puntoDeVentaActivo.agregarPedidoPendiente(pedidoCumplido)
            puntoDeVentaActivo.agregarPedidoPendiente(pedidoCumplido2)
            puntoDeVentaActivo2.agregarPedidoPendiente(pedidoCumplido)
            puntoDeVentaActivo2.agregarPedidoPendiente(pedidoCumplido2)
            puntoDeVentaActivo2.agregarPedidoPendiente(pedidoACumplir)
            val actualizador = ActualizarStockSobresPuntosDeVenta("Se actualizó el stock de los pedidos cumplidos", mockedMailSender, repositorioDePuntosDeVenta)
            puntoDeVentaActivo.stockSobres shouldBe 1000
            puntoDeVentaActivo2.stockSobres shouldBe 10
            actualizador.realizar()
            puntoDeVentaActivo.stockSobres shouldBe 1015
            puntoDeVentaActivo2.stockSobres shouldBe 25
        }
        it("No se actualiza si no hay pedidos sin procesar") {
            repositorioDePuntosDeVenta.create(puntoDeVentaActivo)
            puntoDeVentaActivo.agregarPedidoPendiente(pedidoACumplir)
            val actualizador = ActualizarStockSobresPuntosDeVenta("Se actualizó el stock de los pedidos cumplidos", mockedMailSender, repositorioDePuntosDeVenta)
            puntoDeVentaActivo.stockSobres shouldBe 1000
            actualizador.realizar()
            puntoDeVentaActivo.stockSobres shouldBe 1000
        }
    }
})

fun mockPedidoSinProcesar(cantidadSobres: Int): Pedido {
    val pedido = mockk<Pedido>(relaxUnitFun = true)
    every { pedido.cantidadSobres } returns cantidadSobres
    every { pedido.sinProcesar() } answers { true }
    return pedido
}

