package com.example.autocameraesp32

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.InputStream
import java.util.*
import kotlin.concurrent.thread

class BluetoothService(
    private val deviceName: String,  // Ej: "ESP32"
    private val onMessageReceived: (String) -> Unit
) {
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var bluetoothSocket: BluetoothSocket? = null
    private var inputStream: InputStream? = null

    private val uuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb") // UUID Serial

    fun connect() {
        val device = bluetoothAdapter?.bondedDevices?.find {
            it.name == deviceName
        }

        if (device == null) {
            Log.e("BluetoothService", "Dispositivo '$deviceName' no encontrado")
            return
        }

        try {
            bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid)
            bluetoothSocket?.connect()
            inputStream = bluetoothSocket?.inputStream

            Log.d("BluetoothService", "Conectado a $deviceName")

            startListening()
        } catch (e: Exception) {
            Log.e("BluetoothService", "Error al conectar: ${e.message}")
        }
    }

    private fun startListening() {
        thread {
            try {
                val buffer = ByteArray(1024)
                var bytes: Int

                while (true) {
                    bytes = inputStream?.read(buffer) ?: break
                    val message = String(buffer, 0, bytes).trim()
                    Log.d("BluetoothService", "Recibido: $message")
                    onMessageReceived(message)
                }
            } catch (e: Exception) {
                Log.e("BluetoothService", "Error de lectura: ${e.message}")
            }
        }
    }

    fun disconnect() {
        try {
            inputStream?.close()
            bluetoothSocket?.close()
        } catch (e: Exception) {
            Log.e("BluetoothService", "Error al desconectar: ${e.message}")
        }
    }
}
