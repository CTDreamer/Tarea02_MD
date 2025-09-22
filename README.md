# Tarea 02 - Sistema de Gestión de Cuentas Bancarias

## Información del Curso
**Curso:** Desarrollo de Sistemas Móviles  
**Ciclo:** VIII  

## Integrantes del Equipo
- **Cristhian Anthony Torres Tineo** - Código: 22200050
- **José Manuel Sernaque Cobeñas** - Código: 22200182

## Descripción del Proyecto

Este proyecto implementa un sistema de gestión de cuentas bancarias en Kotlin, diseñado como parte de los fundamentos para el desarrollo de aplicaciones móviles. El sistema modela diferentes tipos de cuentas bancarias utilizando los principios de programación orientada a objetos, incluyendo herencia, polimorfismo y encapsulamiento.

## Arquitectura del Sistema

### Clase Base: Cuenta (Abstracta)

La clase `Cuenta` es una clase abstracta que define la estructura base para todas las cuentas bancarias del sistema. Sus características principales incluyen:

**Propiedades protegidas:**
- `saldo`: Saldo actual de la cuenta
- `tasaAnual`: Tasa de interés anual aplicable
- `numConsignaciones`: Contador de depósitos realizados en el mes
- `numRetiros`: Contador de retiros realizados en el mes
- `comisionMensual`: Comisión acumulada en el mes

**Métodos principales:**
- `consignar(cantidad)`: Método virtual que permite depositar dinero en la cuenta
- `retirar(cantidad)`: Método virtual que permite retirar dinero, validando saldo disponible
- `calcularInteresMensual()`: Calcula y aplica el interés mensual basado en la tasa anual
- `extractoMensual()`: Procesa el cierre mensual aplicando comisiones e intereses
- `imprimir()`: Muestra el estado actual de la cuenta

### Clase CuentaAhorros

Extiende la clase `Cuenta` e implementa las reglas específicas para cuentas de ahorro:

**Características especiales:**
- **Estado de activación**: La cuenta se considera activa solo si el saldo es mayor o igual a 10,000
- **Restricciones de operación**: Solo permite retiros cuando la cuenta está activa
- **Penalidad por retiros excesivos**: Aplica una comisión adicional si se realizan más de 4 retiros en el mes
- **Reactivación automática**: La cuenta se reactiva automáticamente al recibir depósitos que eleven el saldo por encima del mínimo

**Funcionalidades implementadas:**
- Validación de estado antes de permitir retiros
- Cálculo automático de penalidades por exceso de retiros
- Gestión dinámica del estado activo/inactivo de la cuenta
- Procesamiento especializado del extracto mensual

### Clase CuentaCorriente

Extiende la clase `Cuenta` e implementa el sistema de sobregiro característico de las cuentas corrientes:

**Características especiales:**
- **Sistema de sobregiro**: Permite retiros que excedan el saldo disponible hasta un límite establecido
- **Gestión de deuda**: Maneja el sobregiro como una deuda separada con su propia tasa de interés
- **Priorización de pagos**: Los depósitos se aplican primero al pago del sobregiro antes de incrementar el saldo
- **Comisiones e intereses del sobregiro**: Aplica tasas e intereses específicos al monto del sobregiro

**Funcionalidades implementadas:**
- Cálculo dinámico del dinero disponible (saldo + sobregiro disponible)
- Gestión automática del sobregiro en retiros
- Aplicación prioritaria de depósitos al pago de sobregiros
- Cálculo de intereses y comisiones específicas para el sobregiro

## Funcionalidad del Programa Principal

El método `main()` implementa casos de prueba que demuestran todas las funcionalidades del sistema:

### Caso de Prueba 1: Cuenta de Ahorros
1. **Inicialización**: Crea una cuenta con saldo inicial de 12,000 (cuenta activa)
2. **Prueba de retiros múltiples**: Realiza 6 retiros de 1,000 cada uno para demostrar la penalidad por exceso
3. **Procesamiento mensual**: Genera el extracto mensual aplicando penalidades e intereses
4. **Prueba de desactivación**: Retira una cantidad que deja la cuenta inactiva
5. **Reactivación**: Deposita dinero suficiente para reactivar la cuenta

### Caso de Prueba 2: Cuenta Corriente
1. **Inicialización**: Crea una cuenta con saldo inicial de 2,000
2. **Uso de sobregiro**: Retira 4,000 para demostrar el funcionamiento del sobregiro
3. **Procesamiento mensual**: Aplica intereses y comisiones al sobregiro usado
4. **Pago de sobregiro**: Deposita dinero para cubrir la deuda del sobregiro

## Tecnologías Utilizadas

- **Lenguaje**: Kotlin
- **Paradigma**: Programación Orientada a Objetos
- **Conceptos aplicados**: 
  - Herencia y clases abstractas
  - Polimorfismo y sobrescritura de métodos
  - Encapsulamiento de datos
  - Validación de reglas de negocio

## Ejecución del Programa

Para ejecutar el programa:

1. Asegúrate de tener Kotlin instalado en tu sistema
2. Compila el archivo: `kotlinc src/Main.kt -include-runtime -d Main.jar`
3. Ejecuta el programa: `java -jar Main.jar`

El programa mostrará la ejecución de todos los casos de prueba con salidas detalladas que permiten verificar el correcto funcionamiento de cada funcionalidad implementada.

## Objetivo Académico

Este proyecto sirve como base para comprender los principios de la programación orientada a objetos que posteriormente se aplicarán en el desarrollo de aplicaciones móviles, proporcionando una base sólida en el manejo de estructuras de datos complejas y lógica de negocio.