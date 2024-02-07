package ar.edu.unsam.algo2.worldcapp
import ar.edu.unsam.algo3.domain.*
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.core.spec.IsolationMode
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldNotContain
import org.uqbar.geodds.Point
import java.time.LocalDate

class UsuarioSpec: DescribeSpec ({
    isolationMode = IsolationMode.InstancePerTest

    var portugal: Seleccion = Seleccion("Portugal", Confederacion.CONMEBOL, 0, 2)

    var usuario1 = Usuario("Pepita", "Golondrina", "pepa3000", LocalDate.now().minusYears(20), "pgolondrina@gmail.com", Direccion("Buenos Aires", "San Martin","Calle Real", 123, Point(0.55,2.3)), 250.0, "asd.png", "hola123")
    var usuario2: Usuario = Usuario("Pepe", "Picaflor", "pepe3000", LocalDate.now().minusYears(30), "ppicaflor@gmail.com", Direccion("Buenos Aires", "Garin","Calle Falsa", 321, Point(-1.4,3.14)), 200.0, "asd.png", "aaa")

    var jugador = Jugador("El", "Bicho", LocalDate.now().minusYears(20), 7, portugal, Delantero, LocalDate.now().minusYears(26), 1.98, 75.0, true, 100_000_000.0)
    var jugador2 = Jugador("El", "Bicho", LocalDate.now().minusYears(20), 7, portugal, Defensa, LocalDate.now().minusYears(26), 1.98, 75.0, true, 1_000_000_000_000.0)
    var jugador3 = Jugador("El", "Bicho", LocalDate.now().minusYears(20), 7, portugal, Arquero, LocalDate.now().minusYears(26), 1.98, 75.0, true, 1_000_000_000_000.0)
    var jugador4 = Jugador("El", "Bicho", LocalDate.now().minusYears(20), 7, portugal, MedioCampo, LocalDate.now().minusYears(26), 1.98, 75.0, true, 1_000_000_000_000.0)
    var jugador5 = Jugador("El", "Bicho", LocalDate.now().minusYears(20), 7, portugal, Delantero, LocalDate.now().minusYears(26), 1.98, 75.0, true, 1_000_000_000_000.0)
    var jugador6 = Jugador("El", "Bicho", LocalDate.now().minusYears(20), 7, portugal, Defensa, LocalDate.now().minusYears(26), 1.98, 75.0, true, 1_000_000_000_000.0)

    var figurita1 = Figurita(1,true, NivelImpresion.MEDIA,jugador)
    var figurita2 = Figurita(2,false, NivelImpresion.ALTA,jugador2)
    var figurita3 = Figurita(50,true, NivelImpresion.MEDIA,jugador3)
    var figurita4 = Figurita(7,false, NivelImpresion.MEDIA,jugador4)
    var figurita5 = Figurita(4,false, NivelImpresion.BAJA,jugador5)
    var figurita6 = Figurita(3,true, NivelImpresion.BAJA,jugador6)

    describe("Tests de Usuario"){
        describe("Edad"){
            it("Se corrobora que el calculo de la edad sea correcto"){
                usuario1.edad() shouldBe 20
            }
        }

        describe("Figuritas de Usuario"){
            describe("Figurita Faltante"){
                usuario1.registrarFaltante(figurita1)
                it("Se registra correctamente"){
                    usuario1.esFaltante(figurita1).shouldBeTrue()
                }
                it("No puede ser Repetida"){
                    shouldThrow<RuntimeException>{
                        usuario1.registrarRepetida(figurita1)
                    }
                }
            }
            describe("Figurita Repetida"){
                usuario1.registrarRepetida(figurita1)
                it("Se registra correctamente"){
                    usuario1.esRepetida(figurita1).shouldBeTrue()
                }
                it("No puede ser Faltante"){
                    shouldThrow<RuntimeException> {
                        usuario1.registrarFaltante(figurita1)
                    }
                }
            }
        }

        describe("Distancias"){
            describe("Distancia entre dos usuarios"){
                it("El cánculo  de la distancia se realiza correctamente"){
                    usuario1.direccion.distanciaEntre(usuario2.direccion) shouldBe 236.32.plusOrMinus(0.99)
                }
            }
            describe("Cercanía entre dos usuarios"){
                it("Un usuario considera cercano al otro"){
                    usuario1.esCercano(usuario2).shouldBeTrue()
                }
                it("Un usuario no considera cercano al otro"){
                    usuario2.esCercano(usuario1).shouldBeFalse()
                }
            }
        }

        describe("Tipos de Usuario"){
            describe("Par"){
                usuario1.tipoDeUsuario = Par
                describe("Puede regalar figurita"){
                    it("Nro Camiseta, Nro de Figurita y Cant de Copas de Selección impar"){
                        usuario1.registrarRepetida(figurita1)
                        figurita1.jugador.seleccion.cantidadCopasMundo = 1

                        usuario1.figuritasARegalar() shouldContain figurita1
                    }
                }

                describe("No puede regalar figurita"){
                    it("Cantidad de Copas de Selección par, resto impar"){
                        usuario1.figuritasARegalar() shouldNotContain figurita1
                    }

                    figurita1.jugador.seleccion.cantidadCopasMundo = 1

                    it("Numero de Camiseta par, resto impar"){
                        figurita1.jugador.nroCamiseta = 2
                        usuario1.figuritasARegalar() shouldNotContain figurita1
                    }
                    it("Numero de Figurita par, resto impar"){
                        figurita1.numero = 4
                        usuario1.figuritasARegalar() shouldNotContain figurita1
                    }
                    it("Nro Camiseta par, Nro de Figurita par y Cant de Copas de Selección impar"){
                        usuario1.registrarRepetida(figurita2)
                        figurita1.jugador.nroCamiseta = 2
                        figurita1.jugador.seleccion.cantidadCopasMundo = 1

                        usuario1.figuritasARegalar() shouldNotContain figurita1
                    }
                    it("Nro Camiseta par, Nro de Figurita impar y Cant de Copas de Selección par"){
                        usuario1.registrarRepetida(figurita1)
                        figurita1.jugador.nroCamiseta = 2
                        figurita1.jugador.seleccion.cantidadCopasMundo = 2

                        usuario1.figuritasARegalar() shouldNotContain figurita1
                    }
                    it("Nro Camiseta impar, Nro de Figurita par y Cant de Copas de Selección par"){
                        usuario1.registrarRepetida(figurita2)
                        figurita1.jugador.nroCamiseta = 1
                        figurita1.jugador.seleccion.cantidadCopasMundo = 2

                        usuario1.figuritasARegalar() shouldNotContain figurita1
                    }
                }
            }

            describe("Nacionalista"){
                usuario1.tipoDeUsuario = Nacionalista(mutableSetOf())
                usuario1.registrarRepetida(figurita1)

                describe("Puede regalar figurita"){
                    it("Jugador no es de Seleccion favorita"){
                        usuario1.figuritasARegalar() shouldContain figurita1
                    }
                }

                usuario1.tipoDeUsuario = Nacionalista(mutableSetOf(portugal))

                describe("No puede regalar figurita"){
                    it("Jugador no es de Seleccion favorita"){
                        usuario1.figuritasARegalar() shouldNotContain figurita1
                    }
                }
            }

            describe("Conservador"){
                usuario1.tipoDeUsuario = Conservador
                usuario1.registrarRepetida(figurita2)

                describe("Puede regalar figurita") {
                    it("Impresión Alta y Album Completo") {
                        usuario1.figuritasARegalar() shouldContain figurita2
                    }
                }

                describe("No puede regalar figurita"){
                    it("Impresión Alta, Album Incompleto"){
                        usuario1.registrarFaltante(figurita1)
                        usuario1.figuritasARegalar() shouldNotContain figurita2
                    }

                    figurita2.nivelImpresion = NivelImpresion.BAJA

                    it("Impresión No Alta, Album Completo"){
                        usuario1.figuritasARegalar() shouldNotContain figurita2
                    }
                    it("Impresión No Alta, Album Incompleto"){
                        usuario1.registrarFaltante(figurita1)
                        usuario1.figuritasARegalar() shouldNotContain figurita2
                    }
                }
            }

            describe("Fanatico"){
                usuario1.tipoDeUsuario = Fanatico(jugador2)
                usuario1.registrarRepetida(figurita1)

                figurita1.jugador.nroCamiseta = 4
                figurita1.jugador.cotizacion = 300000.0
                figurita1.jugador.anioDebutSeleccion = LocalDate.now().minusYears(5)

                describe("Puede regalar figurita"){
                    it("Jugador no es Favorito ni Leyenda"){
                        usuario1.figuritasARegalar() shouldContain figurita1
                    }
                }

                usuario1.tipoDeUsuario = Fanatico(jugador)

                describe("No puede regalar figurita"){
                    it("Jugador es Favorito y no Leyenda"){
                        usuario1.figuritasARegalar() shouldNotContain figurita1
                    }

                    figurita1.jugador.anioDebutSeleccion = LocalDate.now().minusYears(15)
                    figurita1.jugador.cotizacion = 30_000_000.0

                    it("Jugador no es Favorito y sí Leyenda"){

                        usuario1.tipoDeUsuario = Fanatico(jugador2)
                        usuario1.figuritasARegalar().contains(figurita1).shouldBeFalse()
                    }

                    it("Jugador es Favorito y Leyenda"){
                        usuario1.tipoDeUsuario = Fanatico(jugador)
                        usuario1.figuritasARegalar().contains(figurita1).shouldBeFalse()

                    }
                }
            }

            describe("Desprendido"){
                usuario1.tipoDeUsuario = Desprendido
                usuario1.registrarRepetida(figurita1)

                it("Puede regalar figurita"){
                    usuario1.figuritasARegalar() shouldContain figurita1
                }
            }

            describe("Apostador"){
                usuario1.tipoDeUsuario = Apostador
                usuario1.registrarRepetida(figurita2)

                val condicionPromesa = { -> figurita2.jugador.cotizacion = 300000.0
                    figurita2.jugador.anioDebutSeleccion = LocalDate.now().minusYears(1)
                    figurita2.jugador.fechaNacimiento = LocalDate.of(2003,2,21)
                }

                describe("Puede regalar figurita"){
                    it("Figurita está On Fire y Jugador no es Promesa"){
                        figurita2.onFire = true
                        usuario1.figuritasARegalar() shouldContain figurita2
                    }
                    it("Figurita no está On Fire y Jugador no es Promesa"){
                        usuario1.figuritasARegalar() shouldContain figurita2
                    }
                    it("Figurita no está On Fire y Jugador es Promesa"){
                        condicionPromesa()
                        usuario1.figuritasARegalar() shouldContain figurita2
                    }

                }

                describe("No puede regalar figurita"){
                    it("Figuerita está On Fire y Jugador es Promesa"){
                        condicionPromesa()
                        figurita2.onFire = true
                        usuario1.figuritasARegalar() shouldNotContain figurita2
                    }
                }
            }

            describe("Interesado"){
                usuario1.tipoDeUsuario = Interesado

                //Guardamos las figuritas en distintos orden por eso 2 repeats
                repeat(2){
                    usuario1.registrarRepetida(figurita4)
                    usuario1.registrarRepetida(figurita1)
                    usuario1.registrarRepetida(figurita2)
                    usuario1.registrarRepetida(figurita3)
                    usuario1.registrarRepetida(figurita5)
                    usuario1.registrarRepetida(figurita6)
                }
                repeat(4){
                    usuario1.registrarRepetida(figurita3)
                    usuario1.registrarRepetida(figurita5)
                    usuario1.registrarRepetida(figurita2)
                    usuario1.registrarRepetida(figurita4)
                    usuario1.registrarRepetida(figurita6)
                    usuario1.registrarRepetida(figurita1)



                }

                describe("Puede regalar figurita"){
                    it("Figurita fuera de Top 5 Valoración"){
                        usuario1.figuritasARegalar() shouldNotContain figurita5
                    }
                }

                describe("No puede regalar figurita"){
                    it("Figurita dentro de Top 5 Valoración"){

                        usuario1.figuritasARegalar() shouldContain figurita4

                        //usuario1.figuritasARegalar().contains(figurita4).shouldBeTrue()

                    }
                }
            }

            describe("Cambiante"){
                usuario1.tipoDeUsuario = Cambiante
                usuario1.registrarRepetida(figurita5)

                describe("Puede regalar figurita"){
                    it("Se comporta como Desprendido"){
                        usuario1.figuritasARegalar() shouldContain figurita5
                    }
                }

                usuario1.fechaNacimiento = LocalDate.now().minusYears(26)
                describe("No puede regalar figurita"){
                    it("Se comporta como Conservador"){
                        usuario1.figuritasARegalar() shouldNotContain figurita5
                    }
                }
            }
        }
        
        describe("Solicitar y Proveer Figuritas"){
            usuario2.tipoDeUsuario = Par

            usuario1.distanciaDeBusqueda = 100000.0
            usuario2.distanciaDeBusqueda = 100000.0

            describe("Usuario A puede solicitar Figurita"){
                usuario1.registrarFaltante(figurita1)

                it("Usuario B puede proveer"){
                    usuario2.registrarRepetida(figurita1)
                    figurita1.jugador.seleccion.cantidadCopasMundo = 1

                    usuario1.solicitar(usuario2, figurita1)
                }

                it("Usuario B no puede proveer"){
                    shouldThrow<RuntimeException>{
                        usuario1.solicitar(usuario2, figurita1)
                    }
                }
            }
            it("Usuario A no puede solicitar Figurita"){
                shouldThrow<RuntimeException> {
                    usuario1.solicitar(usuario2, figurita1)
                }
            }
        }
        
        describe("Getters y Setters de la clase Usuario") {

            it("Nombre del usuario") {
                usuario1.nombre = "pedro"
                usuario1.nombre shouldBe "pedro"
            }

            it("Apellido del usuario") {
                usuario1.apellido = "gonzales"
                usuario1.apellido shouldBe "gonzales"
            }

            it("Username del usuario") {
                usuario1.username = "user777"
                usuario1.username shouldBe "user777"
            }

            it("Fecha de nacimiento del usuario") {
                val nuevaFecha = LocalDate.of(1995, 2, 10)
                usuario1.fechaNacimiento = nuevaFecha
                usuario1.fechaNacimiento shouldBe nuevaFecha
            }

            it("Email del usuario") {
                usuario1.email = "example@gmail.com"
                usuario1.email shouldBe "example@gmail.com"
            }

            it("Dirección del usuario") {
                val nuevaDireccion = Direccion("Buenos Aires", "San Martin","Calle Real", 123, Point(0.55,2.3))
                usuario1.direccion = nuevaDireccion
                usuario1.direccion shouldBe nuevaDireccion
            }

            it("Distancia de búsqueda del usuario") {
                usuario1.distanciaDeBusqueda = 20.0
                usuario1.distanciaDeBusqueda shouldBe 20.0
            }

        }

    }
})