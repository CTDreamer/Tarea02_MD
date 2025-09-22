open class Cuenta(protected var saldo: Float, protected val tasaAnual: Float) {
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
        calcularInteresMensual()
        saldo -= comisionMensual
    }

    // Imprimir los valores de la cuenta
    open fun imprimir() {
        println("Saldo: %.2f".format(saldo))
        println("Consignaciones: $numConsignaciones")
        println("Retiros: $numRetiros")
        println("Comisión mensual: %.2f".format(comisionMensual))
    }
}

class CuentaAhorros(saldoInicial: Float, tasaAnual: Float) : Cuenta(saldoInicial, tasaAnual) {
    private var activa: Boolean = saldoInicial >= 10000f

    // Consignar dinero solo si la cuenta está activa
    override fun consignar(cantidad: Float) {
        if (activa) {
            super.consignar(cantidad)
        } else {
            println("La cuenta está inactiva. No se puede realizar la consignación.")
        }
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
            comisionMensual += (numRetiros - 4) * 1000f
        }
        super.extractoMensual()

        // Verificar si la cuenta se vuelve inactiva
        if (saldo < 10000f) {
            activa = false
        }
    }

    // Imprimir los detalles de la cuenta de ahorros, incluyendo su estado
    override fun imprimir() {
        super.imprimir()
        println("Estado de la cuenta: ${if (activa) "Activa" else "Inactiva"}")
    }
}

class CuentaCorriente(saldoInicial: Float, tasaAnual: Float) : Cuenta(saldoInicial, tasaAnual) {
    private var sobregiro: Float = 0f

    // Retirar dinero permitiendo el sobregiro
    override fun retirar(cantidad: Float) {
        if (cantidad <= saldo + sobregiro) {
            saldo -= cantidad
            if (saldo < 0) {
                sobregiro = -saldo
                saldo = 0f
            }
            numRetiros++
        } else {
            println("Saldo insuficiente para realizar el retiro.")
        }
    }

    // Consignar dinero a la cuenta y reducir el sobregiro si es necesario
    override fun consignar(cantidad: Float) {
        if (sobregiro > 0) {
            if (cantidad >= sobregiro) {
                val restante = cantidad - sobregiro
                sobregiro = 0f
                super.consignar(restante)
            } else {
                sobregiro -= cantidad
            }
        }else{
            super.consignar(cantidad)
        }
    }

    // Generar el extracto mensual
    override fun extractoMensual() {
        super.extractoMensual()
    }

    // Imprimir los detalles de la cuenta corriente, incluyendo el sobregiro
    override fun imprimir() {
        super.imprimir()
        println("Sobregiro: %.2f".format(sobregiro))
    }
}

fun main() {
    // Crear una cuenta de ahorros
    val cuentaAhorros = CuentaAhorros(15000f, 12f)
    println("Cuenta de Ahorros Inicial:")
    cuentaAhorros.imprimir()

    println("\nConsignar $5000 en cuenta de ahorros:")
    cuentaAhorros.consignar(5000f)
    cuentaAhorros.imprimir()

    println("\nRealizar retiro de $3000 de cuenta de ahorros:")
    cuentaAhorros.retirar(3000f)
    cuentaAhorros.imprimir()

    println("\nGenerar extracto mensual de cuenta de ahorros:")
    cuentaAhorros.extractoMensual()
    cuentaAhorros.imprimir()

    // Crear una cuenta corriente
    val cuentaCorriente = CuentaCorriente(5000f, 15f)
    println("\nCuenta Corriente Inicial:")
    cuentaCorriente.imprimir()

    println("\nConsignar $3000 en cuenta corriente:")
    cuentaCorriente.consignar(3000f)
    cuentaCorriente.imprimir()

    println("\nRealizar retiro de $7000 de cuenta corriente (creando sobregiro):")
    cuentaCorriente.retirar(7000f)
    cuentaCorriente.imprimir()

    println("\nGenerar extracto mensual de cuenta corriente:")
    cuentaCorriente.extractoMensual()
    cuentaCorriente.imprimir()
}