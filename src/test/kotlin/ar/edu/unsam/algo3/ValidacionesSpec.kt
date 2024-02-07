package ar.edu.unsam.algo2.worldcapp

import ar.edu.unsam.algo3.domain.*
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrowMessage
import io.kotest.core.spec.style.DescribeSpec
import org.uqbar.geodds.Point
import java.time.LocalDate

internal class ValidacionesSpec: DescribeSpec({
    val delantero = Delantero
    val portugal = Seleccion(
        pais = "Portugal",
        confederacion = Confederacion.CONMEBOL,
        cantidadCopasMundo = 0,
        cantidadCopasConfederacion = 2
    )
    var jugador = Jugador(
        nombre = "El",
        apellido = "Bicho",
        fechaNacimiento = LocalDate.now().minusYears(20),
        nroCamiseta = 7,
        seleccion = portugal,
        posicion = delantero,
        anioDebutSeleccion = LocalDate.now().minusYears(26),
        altura = 1.98,
        peso = 75.0,
        esLider = true,
        cotizacion = 100_000_000.0
    )

    describe("Validaciones Usuario"){
        it("Carga de Usuario exitosa") {
            shouldNotThrowAny {
                Usuario(
                    nombre = "Pepita",
                    apellido = "Golondrina",
                    username = "pepita777",
                    fechaNacimiento = LocalDate.now().minusYears(20),
                    email = "pgolondrina@gmail.com",
                    direccion = Direccion("Buenos Aires", "San Martin","Calle Real", 123, Point(0.55,2.3)),
                    distanciaDeBusqueda = 250.0,
                    imagen = "asd.png",
                    password = "hola"
                )
            }
        }
        it("Usuario, falla en carga de fechaNacimiento posterior a la del dia actual") {
            val excepcion = {
                Usuario(
                    nombre = "Pepita",
                    apellido = "Golondrina",
                    username = "pepita777",
                    fechaNacimiento = LocalDate.now().plusYears(1),
                    email = "pgolondrina@gmail.com",
                    direccion = Direccion("Buenos Aires", "San Martin","Calle Real", 123, Point(0.55,2.3)),
                    distanciaDeBusqueda = 250.0,
                    imagen = "asd.png",
                    password = "hola"
                )
            }
            shouldThrowMessage("El campo FECHA DE NACIMIENTO no puede ser posterior a la del dia", excepcion)
        }
        it("Usuario, falla en carga de nombre vacio "){
            val excepcion = {
                Usuario(
                    nombre = "",
                    apellido = "Golondrina",
                    username = "pepita777",
                    fechaNacimiento = LocalDate.now().minusYears(20),
                    email = "pgolondrina@gmail.com",
                    direccion = Direccion("Buenos Aires", "San Martin","Calle Real", 123, Point(0.55,2.3)),
                    distanciaDeBusqueda = 250.0,
                    imagen = "asd.png",
                    password = "hola"
                )
            }
            shouldThrowMessage("El campo NOMBRE de texto no debe estar vacío", excepcion)
        }
        it("Usuario, falla en carga de apellido vacio "){
            val excepcion = {
                Usuario(
                    nombre = "Pepita",
                    apellido = "",
                    username = "pepita777",
                    fechaNacimiento = LocalDate.now().minusYears(20),
                    email = "pgolondrina@gmail.com",
                    direccion = Direccion("Buenos Aires", "San Martin","Calle Real", 123, Point(0.55,2.3)),
                    distanciaDeBusqueda = 250.0,
                    imagen = "asd.png",
                    password = "hola"
                )
            }
            shouldThrowMessage("El campo APELLIDO de texto no debe estar vacío", excepcion)
        }
        it("Usuario, falla en carga de username vacio "){
            val excepcion = {
                Usuario(
                    nombre = "Peita",
                    apellido = "Golondrina",
                    username = "",
                    fechaNacimiento = LocalDate.now().minusYears(20),
                    email = "pgolondrina@gmail.com",
                    direccion = Direccion("Buenos Aires", "San Martin","Calle Real", 123, Point(0.55,2.3)),
                    distanciaDeBusqueda = 250.0,
                    imagen = "asd.png",
                    password = "hola"
                )
            }
            shouldThrowMessage("El campo NOMBRE DE USUARIO de texto no debe estar vacío", excepcion)
        }
        it("Usuario, falla en carga de email vacio "){
            val excepcion = {
                Usuario(
                    nombre = "Pepita",
                    apellido = "Golondrina",
                    username = "pepita777",
                    fechaNacimiento = LocalDate.now().minusYears(20),
                    email = "",
                    direccion = Direccion("Buenos Aires", "San Martin","Calle Real", 123, Point(0.55,2.3)),
                    distanciaDeBusqueda = 250.0,
                    imagen = "asd.png",
                    password = "hola"
                )
            }
            shouldThrowMessage("El campo E-MAIL de texto no debe estar vacío", excepcion)
        }

    }

    describe("Validaciones Jugador") {
        it("Carga de Jugador exitosa") {
            shouldNotThrowAny {
                Jugador(
                    nombre = "Lionel",
                    apellido = "Messi",
                    fechaNacimiento = LocalDate.now(),
                    nroCamiseta = 1,
                    seleccion = portugal,
                    posicion = delantero,
                    anioDebutSeleccion = LocalDate.now().minusYears(26),
                    altura = 190.0,
                    peso = 70.0,
                    esLider = true,
                    cotizacion = 100_000_000.0,
                )
            }
        }
        it("Jugador, falla en carga de fechaNacimiento posterior a la del dia actual") {
            val excepcion = {
                Jugador(
                    nombre = "Cristiano",
                    apellido = "Ronaldo",
                    fechaNacimiento = LocalDate.now().plusDays(5),
                    nroCamiseta = 7,
                    seleccion = portugal,
                    posicion = delantero,
                    anioDebutSeleccion = LocalDate.now().minusYears(20),
                    altura = 187.0,
                    peso = 83.0,
                    esLider = true,
                    cotizacion = 150_000_000.0
                )
            }
            shouldThrowMessage("El campo FECHA DE NACIMIENTO no puede ser posterior a la del dia", excepcion)
        }
        it("Jugador falla en carga de fechaNacimiento posterior a la del dia actual") {
            val excepcion = {
                Jugador(
                    nombre = "Cristiano",
                    apellido = "Ronaldo",
                    fechaNacimiento = LocalDate.now(),
                    nroCamiseta = 7,
                    seleccion = portugal,
                    posicion = delantero,
                    anioDebutSeleccion = LocalDate.now().plusYears(1),
                    altura = 187.0,
                    peso = 83.0,
                    esLider = true,
                    cotizacion = 150_000_000.0
                )
            }
            shouldThrowMessage("El campo AÑO DEBUT DE SELECCIÓN no puede ser posterior a la del dia", excepcion)
        }
    }

    describe("Validaciones Figurita") {
        it("Carga de Figurita exitosa") {
            shouldNotThrowAny {
                Figurita(2, false, NivelImpresion.BAJA, jugador)
            }
        }
        it("Figurita falla en carga de numero por ser negativo "){
            val excepcion = {
                Figurita(
                    numero = -1,
                    onFire = false,
                    nivelImpresion = NivelImpresion.BAJA,
                    jugador = jugador
                )
            }
            shouldThrowMessage("El campo NUMERO numérico no  debe ser negativo", excepcion)
        }
    }

    describe("Validaciones Seleccion") {
        it("Carga de Seleccion exitosa") {
            shouldNotThrowAny {
                Seleccion(
                    pais = "Argentina",
                    confederacion = Confederacion.CONMEBOL,
                    cantidadCopasMundo = 3,
                    cantidadCopasConfederacion = 3,
                )
            }
        }
    }

    describe("Validaciones Pedido") {
        it("Carga Pedido exitosa") {
            shouldNotThrowAny {
                Pedido(10, LocalDate.now().plusDays(1))
            }
        }

        it("Pedido falla por cantidadSobres negativa") {
            val excepcion = {
                Pedido(
                    cantidadSobres = -1,
                    fechaEntrega = LocalDate.now().plusDays(1)
                )
            }
            shouldThrowMessage("El campo CANTIDAD SOBRES numérico no  debe ser negativo", excepcion)
        }

        it("Pedido falla por fechaEntrega vencida") {
            val excepcion = {
                Pedido(
                    cantidadSobres = 1,
                    fechaEntrega = LocalDate.now().minusDays(1)
                )
            }
            shouldThrowMessage("El campo FECHA DE ENTREGA no puede ser anterior a la del dia", excepcion)
        }
    }

    describe("Validaciones PuntoDeVenta") {
        val listaPedidos = mutableListOf<Pedido>()
        listaPedidos.add(Pedido(cantidadSobres = 2, fechaEntrega = LocalDate.now().plusDays(5)))
        listaPedidos.add(Pedido(cantidadSobres = 3, fechaEntrega = LocalDate.now().plusDays(7)))

        it("Pedido falla por stockSobres negativo") {
            val excepcion = {
                Libreria(
                    nombre = "Ateneo",
                    direccion = Direccion("Buenos Aires", "San Martin","Calle Real", 123, Point(0.55,2.3)),
                    stockSobres = -1,
                    pedidosPendientes = listaPedidos
                )
            }
            shouldThrowMessage("El campo STOCK SOBRES numérico no  debe ser negativo", excepcion)
        }

        it("Pedido falla por stockSobres negativo") {
            val excepcion = {
                Libreria(
                    nombre = "",
                    direccion = Direccion("Buenos Aires", "San Martin","Calle Real", 123, Point(0.55,2.3)),
                    stockSobres = 1000,
                    pedidosPendientes = listaPedidos
                )
            }
            shouldThrowMessage("El campo NOMBRE de texto no debe estar vacío", excepcion)
        }

        it("TiposDeDescuentos no puede estar vacío") {
            val excepcion = { Combinatoria(mutableSetOf()) }
            shouldThrowMessage("El set TIPOS DE DESCUENTO no puede estar vacío.", excepcion)
        }

    }

})