package ar.edu.unsam.algo3.generic

import ar.edu.unsam.algo3.domain.Figurita
import ar.edu.unsam.algo3.errors.InvalidPathParamValue
import ar.edu.unsam.algo3.errors.NullPathParamValue

interface FiltroFiguritas{
    fun filter(filterCalls: Map<String, String>, collection: MutableSet<Figurita>): MutableSet<Figurita> {
        var figuritas: MutableSet<Figurita>
        if(enableFilter(filterCalls)){
            validateFilter(filterCalls)
            figuritas = filterProcess(filterCalls, collection)
        } else figuritas = collection

        return figuritas
    }
    fun enableFilter(filterCalls: Map<String, String>): Boolean = filterCalls.containsKey(filterCall())
    fun filterProcess(filterCalls: Map<String, String>, collection: MutableSet<Figurita>): MutableSet<Figurita>
    fun filterCall(): String
    fun filterPathVar(filterCalls: Map<String, String>) = filterCalls[filterCall()] ?: ""
    fun isUndefined(value: String) = value.lowercase() == "undefined"
    fun isBoolean(value: String) = value == "true" || value == "false"
    fun isNumber(value: String): Boolean{
        var esNumero = true
        try{value.toDouble()}catch(e: Error){ esNumero = false }
        return esNumero
    }
    fun validateFilter(filterCalls: Map<String, String>){
        val filterCall: String = filterPathVar(filterCalls)
        if(filterCall == "") throw NullPathParamValue(filterCall()) else if(!validationCondition(filterCall)) throw InvalidPathParamValue(FiltroOnFire.filterCall(), filterCall)
    }
    fun validationCondition(filterCall: String): Boolean
}

object FiltroPromesa: FiltroFiguritas {
    override fun filterProcess(filterCalls: Map<String, String>, collection: MutableSet<Figurita>): MutableSet<Figurita> {
        val filterCall: String = filterPathVar(filterCalls)
        return  if(filterCall.toBoolean()) collection.filter({ figurita -> figurita.tieneJugadorPromesa() }).toMutableSet() else collection
    }
    override fun filterCall(): String = "promesa"
    override fun validationCondition(filterCall: String) = isBoolean(filterCall)
}

object FiltroOnFire: FiltroFiguritas {
    override fun filterProcess(filterCalls: Map<String, String>, collection: MutableSet<Figurita>): MutableSet<Figurita> {
        val filterCall: String = filterPathVar(filterCalls)
        return  if(filterCall.toBoolean()) collection.filter({ figurita -> figurita.onFire }).toMutableSet() else collection
    }
    override fun filterCall(): String = "onFire"
    override fun validationCondition(filterCall: String) = isBoolean(filterCall)
}

object FiltroJugador: FiltroFiguritas {
    override fun filterProcess(filterCalls: Map<String, String>, collection: MutableSet<Figurita>): MutableSet<Figurita> = collection.filter({ figurita -> figurita.cumpleCriterioBusqueda(filterCalls[filterCall()]!!)}).toMutableSet()
    override fun filterCall(): String = "search"
    override fun validationCondition(filterCall: String) = true
}

open class FiltroFiguritasCompuesto(val filtros: MutableSet<FiltroFiguritas>): FiltroFiguritas {
    override fun filterProcess(filterCalls: Map<String, String>, collection: MutableSet<Figurita>): MutableSet<Figurita> {
        var currentList = collection.toMutableSet()
        for (filtro in filtros) {
            currentList = filtro.filter(filterCalls, currentList)
        }
        return currentList
    }

    override fun enableFilter(filterCalls: Map<String, String>): Boolean = true
    override fun filterCall(): String = ""
    override fun validationCondition(filterCall: String) = true
    override fun validateFilter(filterCalls: Map<String, String>) {}
}

object FiltroMinValoracion: FiltroFiguritas{
    override fun filterProcess(filterCalls: Map<String, String>, collection: MutableSet<Figurita>): MutableSet<Figurita> = collection.filter({ figurita -> isHigherOrEqual( figurita.valoracion(), filterCalls[filterCall()]!!) }).toMutableSet()
    override fun filterCall(): String = "min_valoracion"
    fun isHigherOrEqual(numA: Double, numB: String) = isUndefined(numB) || numA >= numB.toDouble()
    override fun validationCondition(filterCall: String) = isNumber(filterCall)
}

object FiltroMaxValoracion: FiltroFiguritas{
    override fun filterProcess(filterCalls: Map<String, String>, collection: MutableSet<Figurita>): MutableSet<Figurita> = collection.filter({ figurita -> isLowerOrEqual( figurita.valoracion(), filterCalls[filterCall()]!!) }).toMutableSet()
    override fun filterCall(): String = "max_valoracion"
    fun isLowerOrEqual(numA: Double, numB: String) = isUndefined(numB) || numA <= numB.toDouble()
    override fun validationCondition(filterCall: String) = isNumber(filterCall)
}

object FiltroRangoValoracion: FiltroFiguritasCompuesto(mutableSetOf(FiltroMinValoracion, FiltroMaxValoracion))