package com.example.rayzarduino

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var estadoTextView: TextView // TextView para mostrar el estado
    private lateinit var imageView: ImageView // ImageView para mostrar la imagen del LED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Configuración de Firebase
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("sensor_data/status") // Ruta donde se encuentra el estado del LED

        // Inicializa los componentes de la interfaz
        estadoTextView = findViewById(R.id.ledStatusText) // Asegúrate de tener un TextView con ese ID en tu layout
        imageView = findViewById(R.id.imageView) // Asegúrate de tener un ImageView con ese ID en tu layout

        // Ajustar padding para el diseño
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Escuchar cambios en Firebase
        listenFirebaseChanges()
    }

    private fun listenFirebaseChanges() {
        // Escuchar los cambios en tiempo real en la base de datos (solo lee el estado del LED)
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Accede a los datos de estado del LED
                val ledState = snapshot.child("led_state").getValue(String::class.java)
                val pirState = snapshot.child("pir_state").getValue(String::class.java)

                if (ledState != null) {
                    Log.d("Firebase", "Estado del LED: $ledState, Estado PIR: $pirState")
                    updateEstado(ledState)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error al escuchar los cambios", error.toException())
            }
        })
    }

    // Actualizar el estado en la interfaz de usuario
    private fun updateEstado(estado: String) {
        when (estado) {
            "ON" -> {
                estadoTextView.text = "La luz está ENCENDIDA"
                imageView.setImageResource(R.drawable.encendida) // Cambiar imagen a encendida
            }
            "OFF" -> {
                estadoTextView.text = "La luz está APAGADA"
                imageView.setImageResource(R.drawable.bombillaapagada) // Cambiar imagen a apagada
            }
            else -> {
                estadoTextView.text = "Estado desconocido"
                imageView.setImageResource(R.drawable.bombillaapagada) // Imagen por defecto
            }
        }
    }
}
