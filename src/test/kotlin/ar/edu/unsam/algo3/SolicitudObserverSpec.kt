package ar.edu.unsam.algo2.worldcapp

import ar.edu.unsam.algo3.domain.*
import ar.edu.unsam.algo3.errors.FiguritaEsFaltante
import ar.edu.unsam.algo3.observers.*
import ar.edu.unsam.algo3.services.Mail
import ar.edu.unsam.algo3.services.MailSender
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import org.uqbar.geodds.Point
import java.time.LocalDate
import io.mockk.mockk
import io.mockk.verify

class AccionSpec : DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest

    describe("Tests Acciones Observers") {
        val portugal = Seleccion("Portugal", Confederacion.UEFA, 0, 2)
        val argentina = Seleccion("Argentina", Confederacion.CONMEBOL, 3, 15)
        val delantero = Delantero
        val jugadorNormal = Jugador(
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

        val jugadorFavorito = Jugador(
            "un",
            "jugador",
            LocalDate.now().minusYears(20),
            9,
            portugal,
            delantero,
            LocalDate.now().minusYears(2),
            1.78,
            70.0,
            false,
            100_000_000.0
        )

        val jugadorArgentino = Jugador(
            "a",
            "b",
            LocalDate.now().minusYears(35),
            10,
            argentina,
            delantero,
            LocalDate.now().minusYears(2),
            1.78,
            75.0,
            true,
            100_000_000.0
        )


        val figuritaPortuguesa1 = Figurita(2, false, NivelImpresion.MEDIA, jugadorNormal)
        val figuritaPortuguesa2 = Figurita(3, true, NivelImpresion.MEDIA, jugadorNormal)
        val figuritaPortuguesa3 = Figurita(4, false, NivelImpresion.MEDIA, jugadorNormal)
        val figuritaPortuguesa4 = Figurita(5, false, NivelImpresion.MEDIA, jugadorNormal)
        val figuritaPortuguesa5 = Figurita(6, false, NivelImpresion.MEDIA, jugadorNormal)

        val figuritaArgetina = Figurita(10, true, NivelImpresion.MEDIA, jugadorArgentino)


        val usuario1 = Usuario("Pepita",
            "Golondrina",
            "pepa3000",
            LocalDate.now().minusYears(20),
            "pgolondrina@gmail.com",
            Direccion("Buenos Aires", "San Martin","Calle Real", 123, Point(0.55,2.3)),
            250.0, "asd.png", "hola")

        val usuarioQueTieneFiguritas = Usuario(
            "Pedro",
            "Golondrina",
            "pepa3000",
            LocalDate.now().minusYears(20),
            "pgolondrina@gmail.com",
            Direccion("Buenos Aires", "San Martin", "Calle Real", 123, Point(0.55, 2.3)),
            250.0, "asd.png", "hola"
        ).apply {
            registrarRepetida(figuritaPortuguesa1)
            registrarRepetida(figuritaPortuguesa2)
            registrarRepetida(figuritaPortuguesa3)
            registrarRepetida(figuritaPortuguesa4)
            registrarRepetida(figuritaArgetina)

        }

        var usuarioUltimasSolicitudes = Usuario(
            "Pepita",
            "Golondrina",
            "pepa3000",
            LocalDate.now().minusYears(20),
            "pgolondrina@gmail.com",
            Direccion("Buenos Aires", "San Martin", "Calle Real", 123, Point(0.55, 2.3)),
            250.0, "asd.png", "hola"
        ).apply {
            registrarFaltante(figuritaPortuguesa1)
            registrarFaltante(figuritaPortuguesa2)
            registrarFaltante(figuritaPortuguesa3)
            registrarFaltante(figuritaPortuguesa4)
        }

        var usuarioUltimaSolicitud = Usuario(
            "Pepita",
            "Golondrina",
            "pepa3000",
            LocalDate.now().minusYears(20),
            "pgolondrina@gmail.com",
            Direccion("Buenos Aires", "San Martin", "Calle Real", 123, Point(0.55, 2.3)),
            250.0, "asd.png", "hola"
        ).apply {
            registrarFaltante(figuritaPortuguesa1)
            registrarFaltante(figuritaPortuguesa2)
        }

        describe("Tests RegistraRepetidas Observer") {
            val repetidasObserver = RegistraRepetidasObserver()
            repetidasObserver.apply{
                agregarFigurita(usuario1, figuritaPortuguesa3)
                agregarFigurita(usuario1, figuritaPortuguesa4)
            }

            usuario1.agregarSolicitudObserver(repetidasObserver)
            usuario1.registrarFaltante(figuritaPortuguesa1)

            describe("Usuario sin Figuritas Repetidas") {
                it("Se registra menos valiosa regalable cuando la valoracion de la solicitada es mayor que alguna") {
                    usuario1.solicitar(usuarioQueTieneFiguritas, figuritaPortuguesa1)
                    usuario1.figuritasRepetidas.shouldContain(figuritaPortuguesa4)
                }

                it("No se registra menos valiosa regalable cuando la valoracion de la solicitada no es mayor que alguna") {
                    repetidasObserver.figuritasRepetidasReservadas = mutableListOf(figuritaPortuguesa2)
                    usuario1.solicitar(usuarioQueTieneFiguritas, figuritaPortuguesa1)

                    usuario1.figuritasRepetidas.size.shouldBe(0)
                }

                it("Si la figurita de menor valoracion es faltante, recibe la siguiente de menor valoracion"){
                    usuario1.registrarFaltante(figuritaPortuguesa4)
                    usuario1.solicitar(usuarioQueTieneFiguritas, figuritaPortuguesa1)
                    usuario1.figuritasRepetidas.shouldContain(figuritaPortuguesa3)
                }
            }

            describe("Usuario con Figuritas Repetidas") {
                it("No pasa nada") {
                    usuario1.registrarRepetida(figuritaPortuguesa5)
                    usuario1.solicitar(usuarioQueTieneFiguritas, figuritaPortuguesa1)
                    usuario1.figuritasRepetidas.size.shouldBe(1)
                }
            }

            describe("La lista está vacía") {
                repetidasObserver.figuritasRepetidasReservadas.clear()
                it("No se lanza una excepción") {
                    shouldNotThrow<RuntimeException> {
                        usuario1.solicitar(usuarioQueTieneFiguritas, figuritaPortuguesa1)
                    }
                }
            }

            describe("Registrar figurita faltante en RepetidasReservadas"){
                usuario1.registrarFaltante(figuritaArgetina)
                it("Tira una excepción"){
                    shouldThrow<FiguritaEsFaltante> { repetidasObserver.agregarFigurita(usuario1, figuritaArgetina) }
                }
            }
        }

        describe("Tests UltimasSolicitudesObserver") {
            usuarioUltimasSolicitudes.cambiarTipoDeUsuario(Desprendido)
            usuarioUltimasSolicitudes.agregarSolicitudObserver(UltimasSolicitudesObserver())



            it("Se cumple con la cantidad de Solicitudes, y el usuario pasa a ser Nacionalista") {
                listOf(figuritaPortuguesa1, figuritaPortuguesa2, figuritaPortuguesa3).forEach({usuarioUltimasSolicitudes.solicitar(usuarioQueTieneFiguritas, it)})
                usuarioUltimasSolicitudes.registrarRepetida(figuritaPortuguesa5)
                (usuarioUltimasSolicitudes.tipoDeUsuario is Nacionalista).shouldBeTrue()
                usuarioUltimasSolicitudes.puedeRegalar(figuritaPortuguesa5).shouldBeFalse()

            }


            it("Se cumple con los requerimientos de Solicitudes pero el album esta lleno"){
                usuarioUltimasSolicitudes.eliminarFaltante(figuritaPortuguesa4)

                listOf(figuritaPortuguesa1, figuritaPortuguesa2, figuritaPortuguesa3).forEach({usuarioUltimasSolicitudes.solicitar(usuarioQueTieneFiguritas, it)})

                (usuarioUltimasSolicitudes.tipoDeUsuario is Nacionalista).shouldBeFalse()
            }


            it("No se cumple con el requerimiento de 3 solicitudes del mismo pais seguidas"){
                usuarioUltimasSolicitudes.registrarFaltante(figuritaArgetina)
                listOf(figuritaPortuguesa1,figuritaArgetina ,figuritaPortuguesa2, figuritaPortuguesa3).forEach({usuarioUltimasSolicitudes.solicitar(usuarioQueTieneFiguritas, it)})
                (usuarioUltimasSolicitudes.tipoDeUsuario is Nacionalista).shouldBeFalse()
            }


            it("Al solicitar figurita por cuarta vez seguida, no cambia el tipo"){
                listOf(figuritaPortuguesa1, figuritaPortuguesa2, figuritaPortuguesa3).forEach({usuarioUltimasSolicitudes.solicitar(usuarioQueTieneFiguritas, it)})
                usuarioUltimasSolicitudes.cambiarTipoDeUsuario(Desprendido)
                usuarioUltimasSolicitudes.solicitar(usuarioQueTieneFiguritas, figuritaPortuguesa4)
                (usuarioUltimasSolicitudes.tipoDeUsuario is Nacionalista).shouldBeFalse()
            }
        }

        describe("Tests SeConvierteADesprendidoObserver"){
            usuarioUltimaSolicitud.cambiarTipoDeUsuario(Fanatico(jugadorFavorito))
            usuarioUltimaSolicitud.agregarSolicitudObserver(SeConvierteADesprendidoObserver(2))

            it("No Cumple condicion de ultima fig, No supera numero limite a regalar y el usuario No cambia de tipo de usuario a Desprendido"){
                usuarioUltimaSolicitud.solicitar(usuarioQueTieneFiguritas, figuritaPortuguesa1)
                usuarioUltimaSolicitud.registrarFaltante(figuritaPortuguesa4)
                (usuarioUltimaSolicitud.tipoDeUsuario is Desprendido).shouldBeFalse()
            }


            it("No Cumple condicion de ultima fig, supera numero limite a regalar y el usuario No cambia de tipo de usuario a Desprendido"){
                usuarioUltimaSolicitud.solicitar(usuarioQueTieneFiguritas, figuritaPortuguesa1)
                usuarioUltimaSolicitud.registrarFaltante(figuritaPortuguesa4)
                usuarioUltimaSolicitud.registrarRepetida(figuritaPortuguesa3)
                usuarioUltimaSolicitud.registrarRepetida(figuritaPortuguesa5)
                (usuarioUltimaSolicitud.tipoDeUsuario is Desprendido).shouldBeFalse()
            }

            it("Cumple condicion de ultima fig, No supera numero limite a regalar y el usuario No cambia de tipo de usuario a Desprendido"){
                usuarioUltimaSolicitud.solicitar(usuarioQueTieneFiguritas, figuritaPortuguesa1)
                (usuarioUltimaSolicitud.tipoDeUsuario is Desprendido).shouldBeFalse()
            }

            it("Cumple condicion de ultima fig, supera numero limite a regalar y el usuario cambia de tipo de usuario a Desprendido"){
                usuarioUltimaSolicitud.registrarRepetida(figuritaPortuguesa3)
                usuarioUltimaSolicitud.registrarRepetida(figuritaPortuguesa4)
                usuarioUltimaSolicitud.registrarRepetida(figuritaPortuguesa5)
                usuarioUltimaSolicitud.solicitar(usuarioQueTieneFiguritas, figuritaPortuguesa1)
                usuarioUltimaSolicitud.solicitar(usuarioQueTieneFiguritas, figuritaPortuguesa2)
                (usuarioUltimaSolicitud.tipoDeUsuario is Desprendido).shouldBeTrue()
            }


        }

        describe("Tests LlenoAlbumObserver") {
            val mockedMailSender = mockk<MailSender>(relaxUnitFun = true)
            usuarioUltimaSolicitud.agregarSolicitudObserver(LlenoAlbumObserver(mockedMailSender))

            it("Si lleno el album con la ultima solicitud, se le envia Mail"){
                // Act
                usuarioUltimaSolicitud.solicitar(usuarioQueTieneFiguritas, figuritaPortuguesa2)
                usuarioUltimaSolicitud.solicitar(usuarioQueTieneFiguritas, figuritaPortuguesa1)

                // Assert
                verify(exactly = 1) {
                    mockedMailSender.sendMail(
                        Mail(
                            from = "info@worldcapp.com.ar",
                            to = usuarioUltimaSolicitud.email,
                            subject = "¡Felicitaciones por completar el álbum!",
                            content = "Hola, ${usuarioUltimaSolicitud.nombre} ${usuarioUltimaSolicitud.apellido}, completo el album con la figurita nro:${figuritaPortuguesa1.numero} de valoracion:${figuritaPortuguesa1.valoracion()} de seleccion: ${figuritaPortuguesa1.seleccion()}"
                        )
                    )
                }
            }
            it("Si no se lleno el album con la ultima solicitud, no se le envia Mail") {
                usuarioUltimaSolicitud.solicitar(usuarioQueTieneFiguritas, figuritaPortuguesa1)

                verify(exactly = 0) {
                    mockedMailSender.sendMail(
                        Mail(
                            from = "info@worldcapp.com.ar",
                            to = usuarioUltimaSolicitud.email,
                            subject = "¡Felicitaciones por completar el álbum!",
                            content = "Hola, ${usuarioUltimaSolicitud.nombre} ${usuarioUltimaSolicitud.apellido}, completo el album con la figurita nro:${figuritaPortuguesa1.numero} de valoracion:${figuritaPortuguesa1.valoracion()} de seleccion: ${figuritaPortuguesa1.seleccion()}"
                        )
                    )
                }
            }
        }

        describe("triplicaDistanciaObserver") {
            it("La distancia inicial se triplica") {
                usuarioUltimasSolicitudes.distanciaDeBusqueda shouldBe 250.0
                usuarioUltimasSolicitudes.agregarSolicitudObserver(TriplicaDistanciaObserver)
                usuarioUltimasSolicitudes.solicitar(usuarioQueTieneFiguritas, figuritaPortuguesa1)
                usuarioUltimasSolicitudes.distanciaDeBusqueda shouldBe 750.0
            }
            it("Si se triplicó no se vuelve a triplicar") {
                usuarioUltimasSolicitudes.distanciaDeBusqueda shouldBe 250.0
                usuarioUltimasSolicitudes.agregarSolicitudObserver(TriplicaDistanciaObserver)

                usuarioUltimasSolicitudes.solicitar(usuarioQueTieneFiguritas, figuritaPortuguesa1)
                usuarioUltimasSolicitudes.distanciaDeBusqueda shouldBe 750.0
                usuarioUltimasSolicitudes.solicitar(usuarioQueTieneFiguritas, figuritaPortuguesa2)
                usuarioUltimasSolicitudes.distanciaDeBusqueda shouldBe 750.0
            }
            it("Si se deshabilita y vuelve a habilitar sí triplica nuevamente") {
                usuarioUltimasSolicitudes.distanciaDeBusqueda shouldBe 250.0
                usuarioUltimasSolicitudes.agregarSolicitudObserver(TriplicaDistanciaObserver)
                usuarioUltimasSolicitudes.solicitar(usuarioQueTieneFiguritas, figuritaPortuguesa1)
                usuarioUltimasSolicitudes.distanciaDeBusqueda shouldBe 750.0
                usuarioUltimasSolicitudes.agregarSolicitudObserver(TriplicaDistanciaObserver)
                usuarioUltimasSolicitudes.solicitar(usuarioQueTieneFiguritas, figuritaPortuguesa2)
                usuarioUltimasSolicitudes.distanciaDeBusqueda shouldBe 2250.0
            }
        }
    }
})