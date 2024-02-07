package ar.edu.unsam.algo2.worldcapp


import ar.edu.unsam.algo3.domain.*
import ar.edu.unsam.algo3.repository.Repositorio
import io.kotest.assertions.throwables.shouldThrowMessage
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Assertions.assertEquals
import org.uqbar.geodds.Point
import java.time.LocalDate

class RepositorioSpec : DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest

    describe("Repositorio Test") {
        val direccion = Direccion("Buenos Aires", "San Martin", "Calle Real", 123, Point(0.55, 2.3))
        val usuario = Usuario(
            nombre = "willy",
            apellido = "bardales",
            username = "willybc",
            email = "willy@gmail.com",
            direccion = direccion,
            fechaNacimiento = LocalDate.now().minusYears(26),
            distanciaDeBusqueda = 250.0,
            imagen = "asd.png",
            password = "hola"
        )

        val usuario2 = Usuario(
            nombre = "willy",
            apellido = "bardales",
            username = "willybc",
            email = "willy@gmail.com",
            direccion = direccion,
            fechaNacimiento = LocalDate.now().minusYears(26),
            distanciaDeBusqueda = 250.0,
            imagen = "asd.png",
            password = "hola"
        )

        val usuario3 = Usuario(
            nombre = "willy",
            apellido = "bardales",
            username = "willybc",
            email = "willy@gmail.com",
            direccion = direccion,
            fechaNacimiento = LocalDate.now().minusYears(26),
            distanciaDeBusqueda = 250.0,
            imagen = "asd.png",
            password = "hola"
        )
        var portugal = Seleccion("Portugal", Confederacion.CONMEBOL, 0, 2)
        var jugador = Jugador(
            "El",
            "Bicho",
            LocalDate.now().minusYears(20),
            7,
            portugal,
            Delantero,
            LocalDate.now().minusYears(26),
            1.98,
            75.0,
            true,
            100_000_000.0
        )
        var jugador2 = Jugador(
            "Lio",
            "Messi",
            LocalDate.now().minusYears(20),
            7,
            portugal,
            Defensa,
            LocalDate.now().minusYears(26),
            1.98,
            75.0,
            true,
            1_000_000_000_000.0
        )

        var figurita1 = Figurita(1, true, NivelImpresion.MEDIA, jugador)
        var figurita2 = Figurita(2, false, NivelImpresion.ALTA, jugador2)
        var figurita3 = Figurita(1, true, NivelImpresion.MEDIA, jugador)

        val repoUsuarios = Repositorio<Usuario>()
        val repoFiguritas = Repositorio<Figurita>()

        describe("Test Usuarios Repositorio") {
            describe("funcion create en varios objetos") {


                repoUsuarios.create(usuario)
                repoUsuarios.create(usuario2)
                repoUsuarios.create(usuario3)
                repoFiguritas.create(figurita1)
                repoFiguritas.create(figurita2)

                it("valido primer usuario") {
                    usuario.id shouldBe 1
                    repoUsuarios.coleccion.contains(usuario).shouldBeTrue()
                }


                it("valido segundo usuario") {
                    usuario2.id shouldBe 2
                    repoUsuarios.coleccion.contains(usuario2).shouldBeTrue()
                }


                it("valido segundo usuario") {
                    usuario3.id shouldBe 3
                    repoUsuarios.coleccion.contains(usuario3).shouldBeTrue()
                }


                it("valido primera figurita") {
                    figurita1.id shouldBe 1
                    repoFiguritas.coleccion.contains(figurita1).shouldBeTrue()
                }


                it("valido segunda figurita") {
                    figurita2.id shouldBe 2
                    repoFiguritas.coleccion.contains(figurita2).shouldBeTrue()
                }

                it("Crear un objeto con id, me deberia tirar error"){
                    usuario.id = 1
                    val excepcion = {repoUsuarios.create(usuario)}

                    shouldThrowMessage("El objeto ya esta creado, error al crear",excepcion)
                }

            }

            describe("funcion delete") {
                repoUsuarios.create(usuario)
                repoUsuarios.create(usuario2)

                repoFiguritas.create(figurita1)
                repoFiguritas.create(figurita2)

                it("Si existe en el repo lo elimina") {
                    repoUsuarios.delete(usuario)
                    repoFiguritas.delete(figurita2)

                    repoUsuarios.coleccion.contains(usuario).shouldBeFalse()
                    repoFiguritas.coleccion.contains(figurita2).shouldBeFalse()
                }

                //it("Si no existe en el repo, tira error") {
                //    val excepcion = shouldThrow<ObjetoNoEncontrado> {
                //        repoUsuarios.delete(usuario3)
                //   }
                //    excepcion.message shouldBe "Usuario no existe en el repositorio."
                //}


            }

            describe("funcion getById") {
                repoUsuarios.create(usuario)
                repoUsuarios.create(usuario2)

                it("Debería encontrar el usuario") {
                    repoUsuarios.getById(1) shouldBe usuario
                }

                it("Debería encontrar el usuario 2") {
                    repoUsuarios.getById(2) shouldBe usuario2
                }
            }

            describe("funcion esNuevoObjeto") {
                it("Si es NuevoObjeto") {
                    usuario.esNuevo().shouldBeTrue()
                }

                it("Si no es NuevoObjeto") {
                    repoUsuarios.create(usuario)
                    usuario.esNuevo().shouldBeFalse()
                }
            }

        """
            it("Getter & Setter de nextID") {
                val nuevoValor = 5
                repoUsuarios.nextID = nuevoValor
                assertEquals(nuevoValor, repoUsuarios.nextID)
            }
        """
            it("Setter de coleccion") {
                val repo = Repositorio<Usuario>()
                val nuevaColeccion: MutableSet<Usuario> = mutableSetOf(
                    Usuario(
                        "NombreFalso",
                        "ApellidoFalso",
                        "unknow777",
                        LocalDate.now().minusYears(26),
                        "emailFaso@gmail.com",
                        direccion,
                        250.0,
                        "asd.png",
                        "hola"
                    ),
                    Usuario(
                        "NombreFalso",
                        "ApellidoFalso",
                        "unknow777",
                        LocalDate.now().minusYears(26),
                        "emailFaso@gmail.com",
                        direccion,
                        250.0,
                        "asd.png",
                        "hola"
                    )
                )
                repo.coleccion = nuevaColeccion
                assertEquals(nuevaColeccion, repo.coleccion)
            }
        }
    }

    describe("Test Update para todos los repos") {
        val repoUsuarios = Repositorio<Usuario>()
        val repoFiguritas = Repositorio<Figurita>()
        val repoJugadores = Repositorio<Jugador>()
        val repoSelecciones = Repositorio<Seleccion>()
        val repoPuntoDeVentas = Repositorio<PuntoDeVenta>()

        var portugal = Seleccion("Portugal", Confederacion.CONMEBOL, 0, 2)
        var argentina = Seleccion("Argentina", Confederacion.CONMEBOL, 3, 6)
        val delantero = Delantero

        val direccion = Direccion("Buenos Aires", "San Martin", "Calle Real", 123, Point(0.55, 2.3))
        val usuario = Usuario("willy", "bardales", "willybc", LocalDate.now().minusYears(26), "willy@gmail.com", direccion, 250.0, "asd.png", "hola")
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
        var jugador2 = Jugador("Lionel", "Messi", LocalDate.now().minusYears(26), 10, portugal, delantero, LocalDate.now().minusYears(2), 1.98, 75.0, true, 100_000_000.0)
        var figurita = Figurita(1, true, NivelImpresion.MEDIA, jugador)
        var figurita2 = Figurita(2, false, NivelImpresion.ALTA, jugador)
        var puntoVenta = Kiosco("Carrefour", direccion, 5, mutableListOf(), true)

        it("Update() para repo Usuarios") {
            // Agregamos el usuario al repositorio
            repoUsuarios.create(usuario)


            repoUsuarios.getById(1).nombre.shouldBe("willy")

            //Modificamos algunos datos del usuario
            val usuarioModificado = Usuario(
                "NombreModificado",
                "ApellidoModificado",
                "willybc",
                LocalDate.now().minusYears(26),
                "emailModificado@gmail.com",
                direccion,
                250.0,
                "asd.png",
                "hola"
            ).apply {
                id = 1
            }
            // Actualizamos el usuario
            repoUsuarios.update(usuarioModificado)


            repoUsuarios.getById(1).nombre.shouldBe("NombreModificado")

            repoUsuarios.getById(1).email.shouldBe("emailModificado@gmail.com")

            repoUsuarios.getById(1).apellido.shouldBe("ApellidoModificado")


        }

        it("Upddate() para repo Figuritas ") {
            repoFiguritas.create(figurita)
            repoFiguritas.create(figurita2)

            repoFiguritas.getById(1).numero.shouldBe(1)
            repoFiguritas.getById(1).nivelImpresion.shouldBe(NivelImpresion.MEDIA)
            repoFiguritas.getById(1).jugador.nombre.shouldBe("El")
            repoFiguritas.getById(1).jugador.apellido.shouldBe("Bicho")


            val figuritaModificada = Figurita(
                10,
                true,
                NivelImpresion.ALTA,
                jugador2
            ).apply {
                id = 1
            }

            repoFiguritas.update(figuritaModificada)

            repoFiguritas.getById(1).numero.shouldBe(10)
            repoFiguritas.getById(1).nivelImpresion.shouldBe(NivelImpresion.ALTA)
            repoFiguritas.getById(1).jugador.nombre.shouldBe("Lionel")
            repoFiguritas.getById(1).jugador.apellido.shouldBe("Messi")
        }

        it("Update() para repo Jugadores ") {
            repoJugadores.create(jugador)

            repoJugadores.getById(1).nombre.shouldBe("El")
            repoJugadores.getById(1).apellido.shouldBe("Bicho")
            repoJugadores.getById(1).seleccion.shouldBe(portugal)
            repoJugadores.getById(1).seleccion.cantidadCopasMundo.shouldBe(0)

            val jugadorModificado = Jugador(
                "Lionel",
                "Messi",
                LocalDate.now().minusYears(33),
                10,
                argentina,
                delantero,
                LocalDate.now().minusYears(2),
                1.70,
                75.0,
                true,
                100_000_000.0
            ).apply {
                id = 1
            }

            repoJugadores.update(jugadorModificado)

            repoJugadores.getById(1).nombre.shouldBe("Lionel")
            repoJugadores.getById(1).apellido.shouldBe("Messi")
            repoJugadores.getById(1).seleccion.shouldBe(argentina)
            repoJugadores.getById(1).seleccion.cantidadCopasMundo.shouldBe(3)
        }

        it("Update() para repo Selecciones ") {
            repoSelecciones.create(portugal)

            repoSelecciones.getById(1).pais.shouldBe("Portugal")
            repoSelecciones.getById(1).cantidadCopasMundo.shouldBe(0)

            val seleccionModificada = Seleccion(
                "Argentina",
                Confederacion.CONMEBOL,
                3,
                6
            ).apply {
                id = 1
            }

            repoSelecciones.update(seleccionModificada)

            repoSelecciones.getById(1).pais.shouldBe("Argentina")
            repoSelecciones.getById(1).cantidadCopasMundo.shouldBe(3)

        }

        it("Update() para repo PuntoDeVenta") {
            repoPuntoDeVentas.create(puntoVenta)

            repoPuntoDeVentas.getById(1).nombre.shouldBe("Carrefour")
            repoPuntoDeVentas.getById(1).stockSobres.shouldBe(5)
            val puntoVentaModificada = Libreria(
                "Ateneo",
                direccion,
                250,
                mutableListOf()

            ).apply {
                id = 1
            }

            repoPuntoDeVentas.update(puntoVentaModificada)
            repoPuntoDeVentas.getById(1).nombre.shouldBe("Ateneo")
            repoPuntoDeVentas.getById(1).stockSobres.shouldBe(250)

        }

    }

    describe("Test Search para todos los repos") {
        val repoUsuarios = Repositorio<Usuario>()
        val repoFiguritas = Repositorio<Figurita>()
        val repoJugadores = Repositorio<Jugador>()
        val repoSelecciones = Repositorio<Seleccion>()
        val repoPuntoDeVentas = Repositorio<PuntoDeVenta>()

        var portugal = Seleccion("Portugal", Confederacion.CONMEBOL, 0, 2)
        val delantero = Delantero

        val direccion = Direccion("Buenos Aires", "San Martin", "Calle Real", 123, Point(0.55, 2.3))
        val usuario =
            Usuario("willy", "bardales", "willybc", LocalDate.now().minusYears(26), "willy@gmail.com", direccion, 250.0, "asd.png", "hola")
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
        var jugador2 = Jugador(
            "Neymar",
            "Jr",
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
        var figurita = Figurita(1, true, NivelImpresion.MEDIA, jugador)
        var figurita2 = Figurita(1, true, NivelImpresion.MEDIA, jugador2)
        var figurita3 = Figurita(1, true, NivelImpresion.MEDIA, jugador)
        var puntoVenta = Kiosco("Carrefour", direccion, 5, mutableListOf(), true)

        describe("Search() para Usuarios") {
            repoUsuarios.create(usuario)
            val resultado = repoUsuarios.search("willy")
            resultado.contains(usuario) shouldBe true
        }

        describe("Search() para Figuritas") {
            repoFiguritas.create(figurita)
            repoFiguritas.create(figurita2)
            repoFiguritas.create(figurita3)
            val resultado = repoFiguritas.search("Bicho")
            resultado.contains(figurita).shouldBeTrue()
            resultado.contains(figurita2).shouldBeFalse()
            resultado.contains(figurita3).shouldBeTrue()
            resultado[0] shouldBe figurita
            resultado[1] shouldBe figurita3
        }

        describe("Search() para Jugadores") {
            repoJugadores.create(jugador)
            val resultado = repoJugadores.search("Bicho")
            resultado.contains(jugador).shouldBeTrue()
        }

        describe("Search() para Selecciones") {
            repoSelecciones.create(portugal)
            val resultado = repoSelecciones.search("Portugal")
            resultado.contains(portugal).shouldBeTrue()
        }

        describe("Search() para PuntoDeVenta") {
            repoPuntoDeVentas.create(puntoVenta)
            val resultado = repoPuntoDeVentas.search("Carrefour")
            resultado.contains(puntoVenta).shouldBeTrue()
        }
    }


})
