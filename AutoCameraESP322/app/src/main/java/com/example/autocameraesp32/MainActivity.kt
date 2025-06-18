package com.example.autocameraesp32

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.telecom.Call
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.IOException

class MainActivity : ComponentActivity() {

    @Volatile
    private var puedeTomarFoto = true

    private lateinit var bluetoothService: BluetoothService
    private val nombreDispositivoBluetooth = "ESP32" // Cambia si tu ESP32 tiene otro nombre

    // Lanzador para pedir múltiples permisos
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permisos ->
        val todosConcedidos = permisos.all { it.value == true }

        if (todosConcedidos) {
            iniciarBluetooth()
        } else {
            Toast.makeText(this, "Permisos requeridos no concedidos", Toast.LENGTH_LONG).show()
        }
    }

    // Lanzador para abrir la cámara
    private val lanzadorCamara = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { resultado ->
        puedeTomarFoto = true // restablecer bandera

        if (resultado.resultCode == RESULT_OK && fotoUri != null) {
            val fileDescriptor = contentResolver.openFileDescriptor(fotoUri!!, "r")
            if (fileDescriptor != null) {
                Log.d("MainActivity", "Imagen tomada y guardada en: $fotoUri")
                fileDescriptor.close()
                enviarFotoAlServidor(fotoUri!!)
            } else {
                Toast.makeText(this, "Error: No se pudo abrir la imagen", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Captura cancelada o fallida", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        solicitarPermisos()
    }

    private fun solicitarPermisos() {
        val permisos = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN
        )

        val permisosFaltantes = permisos.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permisosFaltantes.isNotEmpty()) {
            permissionLauncher.launch(permisosFaltantes.toTypedArray())
        } else {
            iniciarBluetooth()
        }
    }

    private fun iniciarBluetooth() {
        bluetoothService = BluetoothService(nombreDispositivoBluetooth) { mensaje ->
            runOnUiThread {
                Toast.makeText(this, "Recibido: $mensaje", Toast.LENGTH_SHORT).show()

                if (mensaje == "1" && puedeTomarFoto) {
                    puedeTomarFoto = false
                    runOnUiThread {
                        tomarFoto()
                    }
                }
            }
        }
        bluetoothService.connect()
    }

    private var fotoUri: Uri? = null

    private fun tomarFoto() {
        val archivo = "foto_${System.currentTimeMillis()}"

        val valores = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, archivo)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/MyApp")
        }

        fotoUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, valores)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null && fotoUri != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri)
            lanzadorCamara.launch(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::bluetoothService.isInitialized) {
            bluetoothService.disconnect()
        }

    }

    private fun enviarFotoAlServidor(uri: Uri) {
        val inputStream = contentResolver.openInputStream(uri)
        val imageBytes = inputStream?.readBytes()
        inputStream?.close()

        if (imageBytes != null) {
            val client = OkHttpClient()

            val cuerpoArchivo = RequestBody.create(
                "image/jpeg".toMediaTypeOrNull(),
                imageBytes
            )

            val cuerpoMultipart = MultipartBody.Part.createFormData(
                "imagen", "foto.jpg", cuerpoArchivo
            )

            val request = Request.Builder()
                .url("https://programador.arequipa.site/nodered//upload/image") // ⚠️ Cambia por tu URL real
                .post(cuerpoMultipart.body)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Error al subir imagen", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponse(call: okhttp3.Call, response: Response) {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Imagen subida correctamente", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }
}

