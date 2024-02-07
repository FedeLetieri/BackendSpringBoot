    package ar.edu.unsam.algo3.domain

    import ar.edu.unsam.algo3.generic.coincideParcialemente
    import ar.edu.unsam.algo3.generic.esExactamente
    import ar.edu.unsam.algo3.generic.esIgual

    abstract class Posicion(): Entidad {
        abstract var nombre: String
        abstract var puntajeInicial: Double
        override var id: Int = Entidad.ID_INICIAL

        open fun valoracionJugador(jugador: Jugador): Double = puntajeInicial + factorPlus(jugador)

        open fun factorPlus(jugador: Jugador): Double = if(condicionPlusJugador(jugador)) plusJugador(jugador) else 0.0

        abstract fun condicionPlusJugador(jugador: Jugador): Boolean

        abstract fun plusJugador(jugador: Jugador): Double

        override fun cumpleCriterioBusqueda(value: String): Boolean = nombre.coincideParcialemente(value)
    }

    object Arquero : Posicion() {
        override var nombre: String = "Arquero"
        override var puntajeInicial = 100.0

        // Calcula el puntaje inicial por la altura(Si la altura es mayor a cierto valor, se suma sino no varia el factor altura)
        override fun valoracionJugador(jugador: Jugador) = puntajeInicial * factorPlus(jugador)
        override fun factorPlus(jugador: Jugador): Double = if(condicionPlusJugador(jugador)) plusJugador(jugador) else 1.0
        override fun condicionPlusJugador(jugador: Jugador): Boolean = jugador.alturaMayor()
        override fun plusJugador(jugador: Jugador) = jugador.altura
    }

    object Defensa : Posicion() {
        override var nombre: String = "Defensa"
        override var puntajeInicial: Double = 50.0

        //Calcula el puntaje inicial, y le suma cierto valor si es lider
        // El valor sumado es 10 por los anios jugados en su seleccion
        override fun condicionPlusJugador(jugador: Jugador): Boolean = jugador.esLider
        override fun plusJugador(jugador: Jugador) = 10.0 * jugador.antiguedad()

    }

    object MedioCampo : Posicion() {
        override var nombre: String = "MedioCampo"
        override var puntajeInicial: Double = 150.0
        override fun condicionPlusJugador(jugador: Jugador): Boolean = jugador.esLigero()
        override fun plusJugador(jugador: Jugador) = jugador.peso

    }

    object Delantero : Posicion() {
        override var nombre: String = "Delantero"
        override var puntajeInicial: Double = 200.0
        override fun condicionPlusJugador(jugador: Jugador): Boolean = jugador.esCampeonDelMundo()
        override fun plusJugador(jugador: Jugador) = jugador.nroCamiseta * 10.0


    }

    class Polivalente(val posiciones: MutableSet<Posicion>) : Posicion() {
        override var nombre: String = "Polivalente"
        override var puntajeInicial: Double = posiciones.sumOf { it.puntajeInicial } / cantidadDePosicionesQueJuega()

        fun agregarPosicion(posicion: Posicion) = posiciones.add(posicion)
        fun eliminarPosicion(posicion: Posicion) = posiciones.remove(posicion)
        fun cantidadDePosicionesQueJuega() = posiciones.count()
        override fun condicionPlusJugador(jugador: Jugador): Boolean = jugador.esLeyenda() || jugador.esPromesaDeFutbol()

        fun promedioValoracionDePosiciones(jugador: Jugador) = sumaDeValoracionesDePosiciones(jugador) / cantidadDePosicionesQueJuega()
        override fun plusJugador(jugador: Jugador) =  promedioValoracionDePosiciones(jugador) - jugador.edad()
        fun sumaDeValoracionesDePosiciones(jugador: Jugador) = posiciones.sumOf { posicion -> posicion.valoracionJugador(jugador) }
    }