package com.example.parcial1lopezjesus

class Solicitud {
    var curp: String = ""
    var nombre: String = ""
    var apellidos: String = ""
    var domicilio: String = ""
    var cantidadIngreso: Double = 0.0
    var tipoPrestamo: String = ""

    constructor(
        curp: String,
        nombre: String,
        apellidos: String,
        domicilio: String,
        cantidadIngreso: Double,
        tipoPrestamo: String
    ) {
        this.curp = curp
        this.nombre = nombre
        this.apellidos = apellidos
        this.domicilio = domicilio
        this.cantidadIngreso = cantidadIngreso
        this.tipoPrestamo = tipoPrestamo
    }

    fun validarIngreso(): Boolean {
        return when (tipoPrestamo) {
            "Personal" -> cantidadIngreso in 20000.0..40000.0
            "Negocio" -> cantidadIngreso in 40001.0..60000.0
            "Vivienda" -> cantidadIngreso in 15000.0..35000.0
            else -> false
        }
    }

}