package com.example.parcial1lopezjesus

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var editTextCurp: EditText
    private lateinit var editTextNombre: EditText
    private lateinit var editTextApellidos: EditText
    private lateinit var editTextDomicilio: EditText
    private lateinit var spinnerPrestamo: Spinner
    private lateinit var editTextIngreso: EditText
    private lateinit var btnValidar: Button
    private lateinit var btnLimpiar: Button
    private lateinit var solicitud: Solicitud

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        createNotificationChannel()

        editTextCurp = findViewById(R.id.editTextCurp)
        editTextNombre = findViewById(R.id.editTextNombre)
        editTextApellidos = findViewById(R.id.editTextApellidos)
        editTextDomicilio = findViewById(R.id.editTextDomicilio)
        spinnerPrestamo = findViewById(R.id.spinnerPrestamo)
        editTextIngreso = findViewById(R.id.editTextIngreso)
        btnValidar = findViewById(R.id.btnValidar)
        btnLimpiar = findViewById(R.id.btnLimpiar)

        val tiposPrestamo = arrayOf("Personal", "Negocio", "Vivienda")
        val adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tiposPrestamo)
        spinnerPrestamo.adapter = adapter

        btnLimpiar.setOnClickListener {
            clearFields()
        }

        btnValidar.setOnClickListener {
            if (validateFields()) {
                solicitudProcess()
            } else {
                Toast.makeText(this, "Llena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (solicitud.validarIngreso()) {
                sendNotification()
            } else {
                Toast.makeText(this, "Rechazado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateFields(): Boolean {
        return editTextCurp.text.isNotEmpty() &&
                editTextNombre.text.isNotEmpty() &&
                editTextApellidos.text.isNotEmpty() &&
                editTextDomicilio.text.isNotEmpty() &&
                editTextIngreso.text.isNotEmpty()
    }

    private fun solicitudProcess() {
        val curp = editTextCurp.text.toString()
        val nombre = editTextNombre.text.toString()
        val apellidos = editTextApellidos.text.toString()
        val domicilio = editTextDomicilio.text.toString()
        val cantidadIngreso = editTextIngreso.text.toString().toDouble()
        val tipoPrestamo = spinnerPrestamo.selectedItem.toString()

        solicitud = Solicitud(curp, nombre, apellidos, domicilio, cantidadIngreso, tipoPrestamo)
    }


    private fun clearFields() {
        editTextCurp.text.clear()
        editTextNombre.text.clear()
        editTextApellidos.text.clear()
        editTextDomicilio.text.clear()
        spinnerPrestamo.setSelection(0)
        editTextIngreso.text.clear()
    }

    private fun createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val name = "Solicitud de préstamo"
            val descriptionText = "Notificaciones de solicitud de préstamo"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("solicitud_prestamo", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("MissingPermission")
    private fun sendNotification() {
        val intentCita = Intent(this, CitaActivity::class.java)
        val pendingIntentCita: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            intentCita,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val intentPrestamo = Intent(this, PrestamoActivity::class.java)
        val pendingIntentPrestamo: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            intentPrestamo,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, "solicitud_prestamo")
            .setSmallIcon(R.drawable.data_icon)
            .setContentTitle("Solicitud de prestamo")
            .setContentText("¿Qué desea hacer?")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntentCita)
            .addAction(R.drawable.date_icon, "Cita", pendingIntentCita)
            .addAction(R.drawable.money_icon, "Préstamo", pendingIntentPrestamo)
            .setAutoCancel(true)
            .build()

        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1
                )
                return
            }
            notify(1001, notification)
        }
    }
}