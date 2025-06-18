package com.example.camarax_aplicativo.MQTT

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

object MqttClientManager {
    private var mqttClient: MqttClient? = null

    fun connect(
        serverUri: String,
        username: String,
        password: String,
        onConnected: () -> Unit = {},
        onMessageReceived: (String) -> Unit
    ) {
        try {
            if (mqttClient?.isConnected == true) return

            mqttClient = MqttClient(serverUri, MqttClient.generateClientId(), MemoryPersistence())
            val options = MqttConnectOptions().apply {
                isAutomaticReconnect = true
                isCleanSession = true
                this.userName = username
                this.password = password.toCharArray()
            }

            mqttClient?.apply {
                connect(options)
                setCallback(object : MqttCallback {
                    override fun connectionLost(cause: Throwable?) {}

                    override fun messageArrived(topic: String?, message: MqttMessage?) {
                        val payload = message?.payload ?: return
                        val received = String(payload, Charsets.UTF_8)
                        onMessageReceived(received)
                    }

                    override fun deliveryComplete(token: IMqttDeliveryToken?) {}
                })
                onConnected()
            }

        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun subscribe(topic: String) {
        try {
            mqttClient?.subscribe(topic, 0)
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun publish(topic: String, message: String) {
        try {
            mqttClient?.publish(topic, MqttMessage(message.toByteArray()))
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun disconnect() {
        try {
            mqttClient?.disconnect()
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }
}
