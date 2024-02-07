package ar.edu.unsam.algo3.bootstrap
import ar.edu.unsam.algo3.domain.*
import ar.edu.unsam.algo3.repository.*
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.uqbar.geodds.Point
import java.time.LocalDate

@Service
class WorldcappBootstrap: InitializingBean {

    @Autowired
    lateinit var  sobresRepository : RepositorioPuntoVenta

    @Autowired
    lateinit var  usuarioRepository : repositorioUsuario

    @Autowired
    lateinit var provinciaRepository: repositorioProvincia

    @Autowired
    lateinit var jugadorRepository: repositorioJugador

    @Autowired
    lateinit var seleccionRepository: repositorioSeleccion

    @Autowired
    lateinit var repositorioFiguritasBusqueda: RepositorioFiguritasBusqueda

    @Autowired
    lateinit var repositorioPosicion: repositorioPosicion

    // POSICIONES
    val delantero = Delantero
    val mediocampo = MedioCampo
    val defensa = Defensa
    val arquero = Arquero

    // SELECCIONES
    val seleccionARG = Seleccion("ARG", Confederacion.CONMEBOL, 3, 10)
    val seleccionFRA = Seleccion("FRA", Confederacion.UEFA, 2, 12)
    val seleccionBRA = Seleccion("BRA", Confederacion. CONMEBOL, 5, 10)
    val seleccionNED = Seleccion("NED", Confederacion. UEFA, 0, 12)
    val seleccionMAR = Seleccion("MAR", Confederacion. CAF, 0, 0)
    val seleccionCRO = Seleccion("CRO", Confederacion. UEFA, 0, 12)
    val seleccionENG= Seleccion("ENG", Confederacion. UEFA, 1, 12)
    val seleccionPOL= Seleccion("POL", Confederacion. UEFA, 0, 12)
    val seleccionBEL= Seleccion("BEL", Confederacion. UEFA, 0, 12)
    val seleccionJAP= Seleccion("JAP", Confederacion. AFC, 0, 0)
    val seleccionPOR= Seleccion("POR", Confederacion. UEFA, 0, 12)

    // JUGADORES
    val martinez = Jugador("Emiliano", "Martinez", LocalDate.of(1992, 9, 2), 23, seleccionARG, arquero, LocalDate.of(2012, 5, 5), 1.98, 88.0, false, 75000000.0)
    val molina = Jugador("Nahuel", "Molina", LocalDate.of(1998, 4, 6), 26, seleccionARG, defensa, LocalDate.of(2016, 8, 8), 1.75, 70.0, false, 50000000.0)
    val messi = Jugador("Lionel", "Messi", LocalDate.of(1987, 6, 24), 10, seleccionARG, delantero, LocalDate.of(2004, 10, 16), 1.70, 72.0, true, 100000000.0)
    val diMaria = Jugador("Angel", "DiMaria", LocalDate.of(1988, 2, 14), 11, seleccionARG, delantero, LocalDate.of(2005, 12, 14), 1.80, 75.0, false, 100000000.0)
    val lloris = Jugador("Hugo", "Lloris", LocalDate.of(1986, 12, 12), 1, seleccionFRA, arquero, LocalDate.of(2005, 5, 5), 1.88, 80.0, true, 60000000.0)
    val varane = Jugador("Raphael", "Varane", LocalDate.of(1993, 4, 25), 4, seleccionFRA, defensa, LocalDate.of(2010, 11, 8), 1.91, 81.0, false, 55000000.0)
    val mbappe = Jugador("Kylian", "Mbappe", LocalDate.of(1998, 12, 20), 10, seleccionFRA, delantero, LocalDate.of(2015, 12, 2), 1.78, 73.0, false, 100000000.0)
    val becker = Jugador("Alisson", "Becker", LocalDate.of(1992, 10, 2), 1, seleccionBRA, arquero, LocalDate.of(2013, 5, 5), 1.93, 90.0, false, 70000000.0)
    val neymar = Jugador("Neymar", "Junior", LocalDate.of(1992, 2, 5), 10, seleccionBRA, delantero, LocalDate.of(2009, 3, 7), 1.75, 68.0, true, 100000000.0)
    val vanDijk= Jugador("Virgil", "VanDijk", LocalDate.of(1997, 7, 8), 4, seleccionNED, defensa, LocalDate.of(2011, 12, 2), 1.95, 92.0, true, 80000000.0)
    val deJong = Jugador("Frenkie", "DeJong", LocalDate.of(1997, 5, 12), 21, seleccionNED, mediocampo, LocalDate.of(2014, 12, 2), 1.81, 74.0, false, 70000000.0)
    val bounou = Jugador("Yassine", "Bounou", LocalDate.of(1991, 4, 5), 1, seleccionMAR, arquero, LocalDate.of(2010, 12, 2), 1.95, 78.0, false, 40000000.0)
    val amrabat = Jugador("Sofyan", "Amrabat", LocalDate.of(1996, 8, 21), 4, seleccionMAR, mediocampo, LocalDate.of(2015, 12, 2), 1.83, 70.0, false, 50000000.0)
    val gvardiol = Jugador("Josko", "Gvardiol", LocalDate.of(2002, 1, 23), 20, seleccionCRO, defensa, LocalDate.of(2019, 12, 2), 1.85, 82.0, false, 55000000.0)
    val modric = Jugador("Luka", "Modric", LocalDate.of(1985, 9, 9), 10, seleccionCRO, mediocampo, LocalDate.of(2003, 8, 3), 1.72, 66.0, true, 100000000.0)
    val arnold = Jugador("Trent", "Arnold", LocalDate.of(1998, 10, 9), 18, seleccionENG, defensa, LocalDate.of(2016, 8, 3), 1.75, 72.0, false, 45000000.0)
    val bellingham = Jugador("Jude", "Bellingham", LocalDate.of(2003, 6, 29), 22, seleccionENG, mediocampo, LocalDate.of(2023, 8, 3), 1.86, 75.0, false, 10000000.0)
    val szczesny = Jugador("Wojciech", "Szczesny", LocalDate.of(1990, 4, 18), 1, seleccionPOL, arquero, LocalDate.of(2019, 8, 3), 1.96, 84.0, true, 45000000.0)
    val courtois = Jugador("Thibaut", "Courtois", LocalDate.of(1992, 5, 11), 1, seleccionBEL, arquero, LocalDate.of(2009, 4, 17), 2.00, 96.0, true, 80000000.0)
    val minamino = Jugador("Takumi", "Minamino", LocalDate.of(1995, 1, 16), 10, seleccionJAP, delantero, LocalDate.of(2012, 4, 17), 1.72, 68.0, false, 50000000.0)
    val ronaldo = Jugador("Cristiano", "Ronaldo", LocalDate.of(1985, 2, 5), 7, seleccionPOR, delantero, LocalDate.of(2002, 8, 14), 1.87, 83.0, true, 100000000.0)

    // FIGURITAS
    val figuritaMartinez = Figurita(82, true, NivelImpresion.ALTA, martinez)
    val figuritaMolina = Figurita(70, false, NivelImpresion.BAJA, molina)
    val figuritaMessi = Figurita(10, true, NivelImpresion.ALTA, messi)
    val figuritaDiMaria = Figurita(11, true, NivelImpresion.ALTA, diMaria)
    val figuritaLloris = Figurita(69, false, NivelImpresion.MEDIA, lloris)
    val figuritaVarane = Figurita(49, false, NivelImpresion.MEDIA, varane)
    val figuritaMbappe = Figurita(15, true, NivelImpresion.ALTA, mbappe)
    val figuritaBecker = Figurita(99, false, NivelImpresion.MEDIA, becker)
    val figuritaNeymar = Figurita(53, true, NivelImpresion.ALTA, neymar)
    val figuritaVanDijk = Figurita(3, false, NivelImpresion.MEDIA, vanDijk)
    val figuritaDeJong = Figurita(21, true, NivelImpresion.MEDIA, deJong)
    val figuritaBounou = Figurita(31, false, NivelImpresion.BAJA, bounou)
    val figuritaAmrabat = Figurita(26, false, NivelImpresion.BAJA, amrabat)
    val figuritaGvardiol = Figurita(32, false, NivelImpresion.BAJA, gvardiol)
    val figuritaModric = Figurita(29, true, NivelImpresion.ALTA, modric)
    val figuritaArnold = Figurita(73, false, NivelImpresion.BAJA, arnold)
    val figuritaBellingham = Figurita(84, true, NivelImpresion.MEDIA, bellingham)
    val figuritaSzczesny = Figurita(29, false, NivelImpresion.BAJA, szczesny)
    val figuritaCourtois = Figurita(2, true, NivelImpresion.ALTA, courtois)
    val figuritaMinamino = Figurita(31, false, NivelImpresion.BAJA, minamino)
    val figuritaRonaldo = Figurita(7, true, NivelImpresion.ALTA, ronaldo)

    fun crearPuntosDeVentas(){

        val direccionSuperChumbo = Direccion("Buenos Aires","Chascomus","Avenida Siempre Viva",5555,Point(-10,58))
        val direccionLibreriaJorge = Direccion("Buenos Aires","Chascomus","Avenida Siempre Viva",5555,Point(100,18))
        val direccionKioscoLoPibe = Direccion("Buenos Aires","Chascomus","Avenida Siempre Viva",5555,Point(10,28))
        val direccionLibreriaDani = Direccion("Buenos Aires","Chascomus","Avenida Siempre Viva",5555,Point(0,1))

        sobresRepository.apply {
            create(Supermercado("Supermercado Chumbo", direccionSuperChumbo, 100, mutableListOf(),Jueves))
            create(Libreria("Libreria Jorge", direccionLibreriaJorge, 50, mutableListOf()))
            create(Kiosco("Kiosko Lo Pibe!", direccionKioscoLoPibe, 0, mutableListOf(),true))
            create(Libreria("Libreria Daniel", direccionLibreriaDani, 10, mutableListOf()))
        }
    }

    fun crearUsuario(){
        val fechaNacimiento = LocalDate.now().minusYears(20)
        val portugal = seleccionRepository.getById(1)
        val españa = seleccionRepository.getById(2)
        val jugadorFavorito = jugadorRepository.getById(1)
        usuarioRepository.apply {
            val direccionUsuarioCercana = Direccion("Buenos Aires","Chascomus","Avenida Siempre Viva",5555,Point(23,-1))

            val direccionUsuario2Lejana = Direccion("Buenos Aires","Chascomus","Avenida Siempre Viva",5555,Point(100,-10))
            create(Usuario("Juan", "Barrios", "JBa", fechaNacimiento, "jba@gmail.com", direccionUsuarioCercana, 20.0, "assets/img/bilardo.jpeg" ,"hola123"))
            create(Usuario("Germán", "Palacios", "GerPa", fechaNacimiento, "gerpa@gmail.com", direccionUsuarioCercana, 20.0, "assets/img/bilardo.jpeg","chau456"))
            create(Usuario("María", "Dolores", "MaDo", fechaNacimiento, "mado@gmail.com", direccionUsuario2Lejana, 20.0, "assets/img/bilardo.jpeg","quetal789"))
            create(Usuario("Agustina", "Pereyra", "AgusP", fechaNacimiento, "agusp@gmail.com", direccionUsuario2Lejana, 20.0, "assets/img/bilardo.jpeg","aaa000"))
        }
        usuarioRepository.getById(2).cambiarTipoDeUsuario(Nacionalista(seleccionesFavoritas = mutableSetOf(portugal,españa)))
        usuarioRepository.getById(3).cambiarTipoDeUsuario(Fanatico(jugadorFavorito = jugadorFavorito))
        usuarioRepository.getById(1).apply {
            cambiarTipoDeUsuario(Desprendido)
            registrarRepetida(figuritaDeJong)
            registrarRepetida(figuritaBounou)
            registrarRepetida(figuritaAmrabat)
            registrarRepetida(figuritaGvardiol)
            registrarRepetida(figuritaModric)
            registrarRepetida(figuritaArnold)
            registrarRepetida(figuritaBellingham)
            registrarRepetida(figuritaSzczesny)
            registrarRepetida(figuritaCourtois)
            registrarRepetida(figuritaMinamino)
            registrarRepetida(figuritaRonaldo)
        }
        usuarioRepository.getById(4).apply {
            cambiarTipoDeUsuario(Desprendido)
            registrarRepetida(figuritaMartinez)
            registrarRepetida(figuritaMolina)
            registrarRepetida(figuritaMessi)    
            registrarRepetida(figuritaDiMaria)
            registrarRepetida(figuritaLloris)
            registrarRepetida(figuritaVarane)
            registrarRepetida(figuritaMbappe)
            registrarRepetida(figuritaBecker)
            registrarRepetida(figuritaNeymar)
            registrarRepetida(figuritaVanDijk)
        }
    }

    fun crearProvincias(){
        provinciaRepository.apply {
            create(Provincia("Buenos Aires", mutableSetOf("Ciudad Autonoma de Buenos Aires", "La Plata", "Mar del Plata", "Chascomus")))
            create(Provincia("Catamarca", mutableSetOf("San Fernando del Valle de Catamarca", "Santa María", "Andalgalá", "Tinogasta")))
            create(Provincia("Chaco", mutableSetOf("Resistencia", "Barranqueras", "Presidencia Roque Sáenz Peña", "Charata")))
            create(Provincia("Chubut", mutableSetOf("Rawson", "Comodoro Rivadavia", "Trelew", "Puerto Madryn")))
            create(Provincia("Córdoba", mutableSetOf("Córdoba", "Villa María", "Río Cuarto", "Jesús María")))
            create(Provincia("Corrientes", mutableSetOf("Corrientes", "Goya", "Mercedes", "Curuzú Cuatiá")))
            create(Provincia("Entre Ríos", mutableSetOf("Paraná", "Concordia", "Gualeguaychú", "Victoria")))
            create(Provincia("Formosa", mutableSetOf("Formosa", "Pirané", "Clorinda", "Las Lomitas")))
            create(Provincia("Jujuy", mutableSetOf("San Salvador de Jujuy", "Palpalá", "San Pedro", "La Quiaca")))
            create(Provincia("La Pampa", mutableSetOf("Santa Rosa", "General Pico", "Toay", "Realicó")))
            create(Provincia("La Rioja", mutableSetOf("La Rioja", "Chilecito", "Aimogasta", "Chamical")))
            create(Provincia("Mendoza", mutableSetOf("Mendoza", "San Rafael", "Godoy Cruz", "Luján de Cuyo")))
            create(Provincia("Misiones", mutableSetOf("Posadas", "Puerto Iguazú", "Oberá", "Eldorado")))
            create(Provincia("Neuquén", mutableSetOf("Neuquén", "San Martín de los Andes", "Centenario", "Plottier")))
            create(Provincia("Río Negro", mutableSetOf("Viedma", "San Carlos de Bariloche", "General Roca", "Cipolletti")))
            create(Provincia("Salta", mutableSetOf("Salta", "San Ramón de la Nueva Orán", "Tartagal", "Cafayate")))
            create(Provincia("San Juan", mutableSetOf("San Juan", "Rivadavia", "Santa Lucía", "Pocito")))
            create(Provincia("San Luis", mutableSetOf("San Luis", "Villa Mercedes", "La Toma", "Justo Daract")))
            create(Provincia("Santa Cruz", mutableSetOf("Río Gallegos", "El Calafate", "Caleta Olivia", "Puerto Deseado")))
            create(Provincia("Santa Fe", mutableSetOf("Santa Fe", "Rosario", "Santo Tomé", "Villa Constitución")))
            create(Provincia("Santiago del Estero", mutableSetOf("Santiago del Estero", "La Banda", "Termas de Río Hondo", "Fernández")))
            create(Provincia("Tierra del Fuego", mutableSetOf("Ushuaia", "Río Grande", "Tolhuin", "San Sebastián")))
            create(Provincia("Tucumán", mutableSetOf("San Miguel de Tucumán", "Yerba Buena", "Tafí Viejo", "Banda del Río Salí")))
        }
    }


    fun crearJugadores(){
        jugadorRepository.apply {
            create(martinez)
            create(molina)
            create(messi)
            create(diMaria)
            create(lloris)
            create(varane)
            create(mbappe)
            create(becker)
            create(neymar)
            create(vanDijk)
            create(deJong)
            create(bounou)
            create(amrabat)
            create(gvardiol)
            create(modric)
            create(arnold)
            create(bellingham)
            create(szczesny)
            create(courtois)
            create(minamino)
            create(ronaldo)
        }
    }

    fun crearSelecciones(){
        seleccionRepository.apply {
            create(seleccionARG)
            create(seleccionFRA)
            create(seleccionBRA)
            create(seleccionNED)
            create(seleccionMAR)
            create(seleccionCRO)
            create(seleccionENG)
            create(seleccionPOL)
            create(seleccionBEL)
            create(seleccionJAP)
            create(seleccionPOR)
        }
    }

    fun crearFiguritasBusqueda(){
        repositorioFiguritasBusqueda.create(figuritaMartinez)
        repositorioFiguritasBusqueda.create(figuritaMolina)
        repositorioFiguritasBusqueda.create(figuritaMessi)
        repositorioFiguritasBusqueda.create(figuritaDiMaria)
        repositorioFiguritasBusqueda.create(figuritaLloris)
        repositorioFiguritasBusqueda.create(figuritaVarane)
        repositorioFiguritasBusqueda.create(figuritaMbappe)
        repositorioFiguritasBusqueda.create(figuritaBecker)
        repositorioFiguritasBusqueda.create(figuritaNeymar)
        repositorioFiguritasBusqueda.create(figuritaVanDijk)
        repositorioFiguritasBusqueda.create(figuritaDeJong)
        repositorioFiguritasBusqueda.create(figuritaBounou)
        repositorioFiguritasBusqueda.create(figuritaAmrabat)
        repositorioFiguritasBusqueda.create(figuritaGvardiol)
        repositorioFiguritasBusqueda.create(figuritaModric)
        repositorioFiguritasBusqueda.create(figuritaArnold)
        repositorioFiguritasBusqueda.create(figuritaBellingham)
        repositorioFiguritasBusqueda.create(figuritaSzczesny)
        repositorioFiguritasBusqueda.create(figuritaCourtois)
        repositorioFiguritasBusqueda.create(figuritaMinamino)
        repositorioFiguritasBusqueda.create(figuritaRonaldo)
    }

    fun crearPosiciones() {
        repositorioPosicion.create(arquero)
        repositorioPosicion.create(defensa)
        repositorioPosicion.create(mediocampo)
        repositorioPosicion.create(delantero)
    }

    override fun afterPropertiesSet() {
        crearJugadores()
        crearSelecciones()
        crearProvincias()
        crearPuntosDeVentas()
        crearUsuario()
        crearFiguritasBusqueda()
        crearPosiciones()
    }
}