package ar.edu.unsam.algo2.worldcapp
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class PedidoSpec: DescribeSpec({
    describe("Test Pedido") {
        it("setter & getters cantidadSobres") {
            val pedido = Pedido(10, LocalDate.now().plusDays(1))
            pedido.cantidadSobres = 20
            pedido.cantidadSobres shouldBe 20
        }

        it("setter & getters fechaEntrega") {
            val pedido = Pedido(10, LocalDate.now().plusDays(1))
            val nuevaFecha = LocalDate.now().plusDays(2)
            pedido.fechaEntrega = nuevaFecha
            pedido.fechaEntrega shouldBe nuevaFecha
        }
    }
})