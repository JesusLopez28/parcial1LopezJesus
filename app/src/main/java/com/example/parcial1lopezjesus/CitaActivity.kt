package com.example.parcial1lopezjesus

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CitaActivity : AppCompatActivity() {

    private lateinit var editTextFecha: EditText
    private lateinit var editTextHora: EditText
    private lateinit var btnAgendar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cita)

        editTextFecha = findViewById(R.id.editTextFecha)
        editTextHora = findViewById(R.id.editTextHora)
        btnAgendar = findViewById(R.id.btnAgendar)

        btnAgendar.setOnClickListener {
            val fecha = editTextFecha.text.toString().trim()
            val hora = editTextHora.text.toString().trim()

            if (fecha.isEmpty() || hora.isEmpty()) {
                Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "Cita agendada", Toast.LENGTH_SHORT).show()
        }
    }
}