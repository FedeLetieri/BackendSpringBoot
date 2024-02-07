package ar.edu.unsam.algo3.generic

import java.time.LocalDate
import java.time.Period
import java.time.temporal.ChronoUnit
import java.time.format.DateTimeFormatter

fun Int.esPar(): Boolean = this % 2 == 0
fun LocalDate.aniosTranscurridos(): Int = Period.between(this, LocalDate.now()).getYears()

fun LocalDate.diasTranscurridos() = ChronoUnit.DAYS.between(LocalDate.now(), this)

fun LocalDate.fechaAnteriorAHoy() = this.isBefore(LocalDate.now())

fun String.coincideParcialemente(value: String): Boolean = this.contains(value, ignoreCase = true)

fun String.esExactamente(value: String): Boolean = this.equals(value, ignoreCase = true)

fun Int.esIgual(value: String): Boolean = this.toString() == value

data class RespuestaPersonalizada(val mensaje: String)

fun String.toLocalDate(): LocalDate {
    val formato = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return LocalDate.parse(this, formato)
}