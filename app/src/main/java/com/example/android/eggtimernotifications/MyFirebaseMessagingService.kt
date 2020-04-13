package com.example.android.eggtimernotifications

import android.app.NotificationManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.android.eggtimernotifications.util.sendNotification
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("First_OnMessageRc", "From: ${remoteMessage?.from}")

        // TODO: Step 3.5 check messages for data
        // Check if message contains a data payload.
        //Data payload esta armazenada na propriedade data
        //Validação se a propriedade data é null ou não
        remoteMessage?.data?.let {
            Log.d("Second_OnMessageRc", "Message data payload: " + remoteMessage.data)
        }

        // TODO: Step 3.6 check messages for notification and call sendNotification
        // Check if message contains a notification payload.
        remoteMessage?.notification?.let {
            Log.d("third_OnMessageRc", "Message Notification Body: ${it.body}")
            sendNotification(it.body!!)
        }

    }

    private fun sendRegistrationToServer(token: String?){
        //Implementar este metodo para enviar o token para o servidor
    }

    private fun sendNotification(messageBody: String){
        val notificationManager = ContextCompat.getSystemService(applicationContext, NotificationManager::class.java) as NotificationManager
        notificationManager.sendNotification(messageBody, applicationContext)
    }

    override fun onNewToken(token: String?) {
        Log.d("onNewToken", "Refreshed token: $token")

        sendRegistrationToServer(token)
    }
}