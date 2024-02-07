package ar.edu.unsam.algo3.errors

import ar.edu.unsam.algo3.domain.TipoDeDescuento
import ar.edu.unsam.algo3.domain.Usuario
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.time.LocalDate

interface ErrorHandler{
    fun validar()

    fun stringVacio(string: String, nombreCampo: String){
        if(string.isBlank()) throw StringVacio(nombreCampo)
    }

    fun nroNegativo(num: Number, nombreCampo: String){
        if(num.toDouble() < 0) throw NroNegativo(nombreCampo)
    }

    fun fechaPosterior(fecha: LocalDate, nombreCampo: String){
        if(fecha > LocalDate.now()) throw FechaPosterior(nombreCampo)
    }

    fun fechaAnterior(fecha: LocalDate, nombreCampo: String){
        if(fecha < LocalDate.now()) throw FechaAnterior(nombreCampo)
    }

    fun nroEntre(num: Number, inicio: Int, fin: Int, nombreCampo: String){
        val nro = num.toDouble()
        if(!(nro >= inicio && nro <= fin)) throw NroFueraDeRango(nombreCampo)
    }

    fun campoNulo(campo: Any, nombreCampo: String) {
        if(campo == null) throw CampoNulo(nombreCampo)
    }

    fun setVacio(set: MutableSet<TipoDeDescuento>, nombreCampo: String) {
        if(set.isEmpty()) throw SetVacio(nombreCampo)
    }
}

class StringVacio(val campo: String = ""): RuntimeException("El campo $campo de texto no debe estar vacío")
class NroFueraDeRango(val campo: String = ""): RuntimeException("El campo $campo numérico se encuentra fuera del rango requerido")
class NroNegativo(val campo: String = ""): RuntimeException("El campo $campo numérico no  debe ser negativo")
class CampoNulo(val campo: String = ""): RuntimeException("El campo $campo no debe ser nulo.")
class FechaPosterior(val campo: String = ""): RuntimeException("El campo $campo no puede ser posterior a la del dia")
class FechaAnterior(val campo: String = ""): RuntimeException("El campo $campo no puede ser anterior a la del dia")
@ResponseStatus(HttpStatus.NOT_FOUND)
class ObjetoNoEncontrado(val campo: String = ""): RuntimeException("$campo no existe en el repositorio.")

class StockNoSuficiente(): RuntimeException("no hay stock suficiente para cantidad solicitada ")
class ObjetoExistente(): RuntimeException("El objeto ya tiene un id, por lo tanto existe en el repositorio")
class FiguritaEsFaltante(): RuntimeException("Una figurita faltante no puede ser repetida")
class FiguritaEsRepetida(): RuntimeException("Una figurita repetida no puede ser faltante")
@ResponseStatus(HttpStatus.BAD_REQUEST)
class figuritaYaEstaEnFaltantes(): RuntimeException("La figurita ya se encuentra en la lista de faltantes")
@ResponseStatus(HttpStatus.NOT_FOUND)
class parametroOrdenadorInvalido(): RuntimeException("El tipo de ordenamiento no existe")
@ResponseStatus(HttpStatus.NOT_FOUND)
class IdNoEncontrado(val id: Int): RuntimeException("El ID $id no fue encontrado en el repositorio")
class ObjetoEstaCreado(): RuntimeException("El objeto ya esta creado, error al crear")
class ObjetoEsNuevo(): RuntimeException("El objeto es nuevo, error al actualizar")
@ResponseStatus(HttpStatus.BAD_REQUEST)
class FiguritaNoFaltante(): RuntimeException("La Figurita no es Faltante")
@ResponseStatus(HttpStatus.BAD_REQUEST)
class UsuarioNoCercano(): RuntimeException("El Usuario no es cercano")
class FiguritaRepetida(): RuntimeException("La Figurita es Repetida")
class FiguritaNoRegalable(): RuntimeException("El Tipo de Usuario no puede regalar la Figurita")
class SetVacio(val campo: String = ""): RuntimeException("El set $campo no puede estar vacío.")

@ResponseStatus(HttpStatus.BAD_REQUEST)
class figuritaEsFaltanteEnAlgunUsuario(usuario: Usuario): RuntimeException("La figurita es faltante en el usuario: ${usuario.nombre}")
@ResponseStatus(HttpStatus.BAD_REQUEST)
class figuritaEsRepetidaEnAlgunUsuario(usuario:Usuario): RuntimeException("La figurita es repetida en el usuario: ${usuario.nombre}")

@ResponseStatus(HttpStatus.BAD_REQUEST)
class numeroRepetido(numeroFigurita:Int):RuntimeException("El numero de figurita $numeroFigurita ya esta en uso")
class InvalidPathParamValue(val pathVar: String, val pathVarVal: String): RuntimeException("La variable [ $pathVar = $pathVarVal ] es inválida. Pista: Verificar los tipos de los datos")
class NullPathParamValue(val pathVar: String): RuntimeException("La variable [ $pathVar ] está vacía. Pista: Verificar que se están enviando los datos")