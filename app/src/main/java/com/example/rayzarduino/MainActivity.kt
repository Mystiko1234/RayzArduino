package com.example.rayzarduino

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.SeekBar
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Configuración de Firebase
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("control")

        // Ajustar padding para el diseño
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configurar funciones del menú
        setupMenuFunctions()

        // Escuchar cambios en Firebase
        listenFirebaseChanges()
    }

    private fun setupMenuFunctions() {
        // Botón para encender
        val btnEncender: Button = findViewById(R.id.buttonEncender)
        btnEncender.setOnClickListener {
            sendToFirebase("ENCENDER")
        }

        // Botón para apagar
        val btnApagar: Button = findViewById(R.id.buttonApagar)
        btnApagar.setOnClickListener {
            sendToFirebase("APAGAR")
        }

        // Control de brillo
        val seekBarBrillo: SeekBar = findViewById(R.id.btnBrillo)
        seekBarBrillo.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                sendToFirebase("BRILLO_$progress")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Configurar vistas de color
        val colorRojo: View = findViewById(R.id.color_rojo)
        colorRojo.setOnClickListener {
            sendToFirebase("CAMBIAR_COLOR_ROJO")
        }

        val colorVerde: View = findViewById(R.id.color_verde)
        colorVerde.setOnClickListener {
            sendToFirebase("CAMBIAR_COLOR_VERDE")
        }

        val colorAzul: View = findViewById(R.id.color_azul)
        colorAzul.setOnClickListener {
            sendToFirebase("CAMBIAR_COLOR_AZUL")
        }

        val colorAmarillo: View = findViewById(R.id.color_amarillo)
        colorAmarillo.setOnClickListener {
            sendToFirebase("CAMBIAR_COLOR_AMARILLO")
        }

        val colorCian: View = findViewById(R.id.color_cian)
        colorCian.setOnClickListener {
            sendToFirebase("CAMBIAR_COLOR_CIAN")
        }
    }

    private fun sendToFirebase(command: String) {
        databaseReference.setValue(command)
            .addOnSuccessListener {
                Log.d("Firebase", "Comando enviado correctamente: $command")
            }
            .addOnFailureListener {
                Log.e("Firebase", "Error al enviar comando: $command", it)
            }
    }

    private fun listenFirebaseChanges() {
        // Escuchar los cambios en tiempo real en la base de datos
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val command = snapshot.getValue(String::class.java)
                if (command != null) {
                    Log.d("Firebase", "Comando recibido: $command")
                    // Aquí puedes manejar los comandos recibidos
                    handleFirebaseCommand(command)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error al escuchar los cambios", error.toException())
            }
        })
    }

    private fun handleFirebaseCommand(command: String) {
        // Manejar los comandos recibidos desde Firebase
        when (command) {
            "ENCENDER" -> {
                Log.d("Comando", "Encender el dispositivo")
                // Realiza la acción para encender el dispositivo
            }
            "APAGAR" -> {
                Log.d("Comando", "Apagar el dispositivo")
                // Realiza la acción para apagar el dispositivo
            }
            "CAMBIAR_COLOR_ROJO" -> {
                Log.d("Comando", "Cambiar color a Rojo")
                // Realiza la acción para cambiar el color a Rojo
            }
            "CAMBIAR_COLOR_VERDE" -> {
                Log.d("Comando", "Cambiar color a Verde")
                // Realiza la acción para cambiar el color a Verde
            }
            "CAMBIAR_COLOR_AZUL" -> {
                Log.d("Comando", "Cambiar color a Azul")
                // Realiza la acción para cambiar el color a Azul
            }
            "CAMBIAR_COLOR_AMARILLO" -> {
                Log.d("Comando", "Cambiar color a Amarillo")
                // Realiza la acción para cambiar el color a Amarillo
            }
            "CAMBIAR_COLOR_CIAN" -> {
                Log.d("Comando", "Cambiar color a Cian")
                // Realiza la acción para cambiar el color a Cian
            }
            "APLICAR_COLOR" -> {
                Log.d("Comando", "Aplicar color")
                // Realiza la acción para aplicar el color
            }
        }
    }
}
