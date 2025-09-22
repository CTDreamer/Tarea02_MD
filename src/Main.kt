abstract class Cuenta(protected var saldo: Float, protected val tasaAnual: Float) {
    protected var numConsignaciones = 0
    protected var numRetiros = 0
    protected var comisionMensual = 0f

    // Método para consignar dinero en la cuenta
    open fun consignar(cantidad: Float) {
        saldo += cantidad
        numConsignaciones++
    }

    // Método para retirar dinero de la cuenta
    open fun retirar(cantidad: Float) {
        if (cantidad <= saldo) {
            saldo -= cantidad
            numRetiros++
        } else {
            println("Saldo insuficiente para realizar el retiro.")
        }
    }

    // Método para calcular el interés mensual y actualizar el saldo
    protected fun calcularInteresMensual() {
        val interesMensual = saldo * (tasaAnual / 100) / 12
        saldo += interesMensual
    }

    // Método para generar el extracto mensual
    open fun extractoMensual() {
        saldo -= comisionMensual
        calcularInteresMensual()
        comisionMensual = 0f // Reinicio al final de mes
        numConsignaciones = 0            // reinicio mensual
        numRetiros = 0
    }

    // Imprimir los valores de la cuenta
    open fun imprimir() {
        println("Saldo: %.2f".format(saldo))
        println("Consignaciones: $numConsignaciones")
        println("Retiros: $numRetiros")
        println("Comisión mensual: %.2f".format(comisionMensual))
    }
}

class CuentaAhorros(saldoInicial: Float, tasaAnual: Float, private val penalidadPorRetiroExtra: Float = 1000f ) : Cuenta(saldoInicial, tasaAnual) {
    private var activa: Boolean = saldoInicial >= 10000f

    // Consignar dinero solo si la cuenta está activa
    override fun consignar(cantidad: Float) {
        super.consignar(cantidad)   // siempre se consigna
        if (saldo >= 10000f) activa = true  // si cumple, se reactiva
    }

    // Retirar dinero solo si la cuenta está activa
    override fun retirar(cantidad: Float) {
        if (activa) {
            super.retirar(cantidad)
        } else {
            println("La cuenta está inactiva. No se puede realizar el retiro.")
        }
    }

    // Generar el extracto mensual considerando el número de retiros
    override fun extractoMensual() {
        if (numRetiros > 4) {
            comisionMensual += (numRetiros - 4) * penalidadPorRetiroExtra
        }
        super.extractoMensual()

        // Verificar si la cuenta se vuelve inactiva
        if (saldo < 10000f) activa = false

    }

    // Imprimir los detalles de la cuenta de ahorros, incluyendo su estado
    override fun imprimir() {
        super.imprimir()
        println("Estado de la cuenta: ${if (activa) "Activa" else "Inactiva"}")
    }
}

class CuentaCorriente(saldoInicial: Float, tasaAnual: Float, private val limiteSobregiro: Float = 5000f, private val tasaSobregiro: Float = 30f, private val comisionSobregiro: Float = 200f) : Cuenta(saldoInicial, tasaAnual) {
    private var sobregiro: Float = 0f

    override fun retirar(cantidad: Float) {
        val disponible = saldo + (limiteSobregiro - sobregiro)
        if (cantidad <= disponible) {
            saldo -= cantidad
            if (saldo < 0) {
                sobregiro += -saldo
                saldo = 0f
            }
            numRetiros++
        } else {
            println("Saldo insuficiente incluso con sobregiro.")
        }
    }

    override fun consignar(cantidad: Float) {
        if (sobregiro > 0) {
            if (cantidad >= sobregiro) {
                val restante = cantidad - sobregiro
                sobregiro = 0f
                println("Sobregiro pagado completamente.")
                super.consignar(restante)
            } else {
                sobregiro -= cantidad
            }
        } else {
            super.consignar(cantidad)
        }
    }

    override fun extractoMensual() {
        super.extractoMensual()
        if (sobregiro > 0) {
            val interesSobregiro = sobregiro * (tasaSobregiro / 100) / 12
            sobregiro += interesSobregiro + comisionSobregiro
        }
    }

    override fun imprimir() {
        super.imprimir()
        println("Sobregiro usado: %.2f".format(sobregiro))
    }
}

fun main() {
    // Caso 1: Cuenta de Ahorros
    val cuentaAhorros = CuentaAhorros(12000f, 10f, penalidadPorRetiroExtra = 500f)
    println("=== EPÍTOME: Cuenta de Ahorros ===")
    cuentaAhorros.imprimir()

    println("\n[Acción] Realizar 6 retiros de 1000 (los 2 últimos generan penalidad):")
    repeat(6) { cuentaAhorros.retirar(1000f) }
    cuentaAhorros.imprimir()

    println("\n[Acción] Generar extracto mensual (aplica penalidad y calcula intereses):")
    cuentaAhorros.extractoMensual()
    cuentaAhorros.imprimir()

    println("\n[Acción] Retirar 7000:")
    cuentaAhorros.retirar(7000f)
    cuentaAhorros.imprimir()

    println("\n[Acción] Consignar 10000 para reactivar la cuenta de ahorros:")
    cuentaAhorros.consignar(10000f)
    cuentaAhorros.imprimir()

    println("------------------------------------------------")

    // Caso 2: Cuenta Corriente
    val cuentaCorriente = CuentaCorriente(2000f, 12f, limiteSobregiro = 3000f, tasaSobregiro = 25f, comisionSobregiro = 150f)
    println("\n=== EPÍTOME: Cuenta Corriente ===")
    cuentaCorriente.imprimir()

    println("\n[Acción] Retirar 4000 (saldo insuficiente, se usa sobregiro):")
    cuentaCorriente.retirar(4000f)
    cuentaCorriente.imprimir()

    println("\n[Acción] Generar extracto mensual (aplica interés y comisión al sobregiro):")
    cuentaCorriente.extractoMensual()
    cuentaCorriente.imprimir()

    println("\n[Acción] Consignar 3000 para cubrir sobregiro:")
    cuentaCorriente.consignar(3000f)
    cuentaCorriente.imprimir()
}
