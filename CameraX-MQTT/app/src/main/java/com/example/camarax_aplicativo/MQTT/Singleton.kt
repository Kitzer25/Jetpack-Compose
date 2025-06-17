package com.example.camarax_aplicativo.MQTT

import android.content.Context

import android.widget.Toast

import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage

object MQTTmanager{

    private lateinit var mqttClient: MqttAndroidClient

    fun connect(
        context: Context,
        serverUri: String,
        username: String,
        password: String,
        clientId: String = MqttClient.generateClientId(),
        onConnected: () -> Unit){

        mqttClient = MqttAndroidClient(context, serverUri, clientId)
        val options = MqttConnectOptions().apply {
            isAutomaticReconnect = true
            isCleanSession = true
            this.userName = username
            this.password = password.toCharArray()
        }

        mqttClient.connect(options, null, object : IMqttActionListener{
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                onConnected()
                Toast.makeText(context,"Conexión Exitosa", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Toast.makeText(
                    context,
                    "Conexión MQTT Fallida: ${exception?.message}",
                    Toast.LENGTH_SHORT).show()
            }
        } )
    }

    fun subcribe(topic: String, callback: (String) -> Unit){
        mqttClient.subscribe(topic, 1){ _, message ->
            val payload = String(message.payload)
            callback(payload)
        }
    }

    fun publish(topic: String, message: String){
        mqttClient.publish(topic, MqttMessage(message.toByteArray()))
    }

    fun isConnected(): Boolean = mqttClient.isConnected
}