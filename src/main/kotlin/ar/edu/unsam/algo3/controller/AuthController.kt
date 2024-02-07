package ar.edu.unsam.algo3.controller
import ar.edu.unsam.algo3.dto.AuthDTO
import ar.edu.unsam.algo3.services.AuthService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin("*")
class AuthController {

    @Autowired
    lateinit var authService: AuthService

    @PostMapping("/login")
    @Operation(summary = "Validación del login")
    fun login(@RequestBody loginRequest: AuthDTO): ResponseEntity<String> {
        val userID = authService.validarLogin(loginRequest)
        return if (userID != null) {
            ResponseEntity.ok(userID.toString())
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario y/o contraseña incorrectos.")
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "Aplicar el logout")
    fun logout() = authService.cerrarSesion()
}