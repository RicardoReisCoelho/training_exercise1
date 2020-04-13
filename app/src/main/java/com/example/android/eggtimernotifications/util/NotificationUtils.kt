/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.eggtimernotifications.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.example.android.eggtimernotifications.MainActivity
import com.example.android.eggtimernotifications.R
import com.example.android.eggtimernotifications.receiver.SnoozeReceiver

// Notification ID.
private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0

// TODO: Step 1.1 extension function to send messages (GIVEN)
/**
 * Builds and delivers the notification.
 *
 * @param context, activity context.
 */
//Extension method para enviar notificações
fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {
    // Create the content intent for the notification, which launches this activity
    // TODO: Step 1.11 create intent
    //Passado o contexto da aplicação e a activity que deve ser lançada
    val contentIntent = Intent(applicationContext, MainActivity::class.java)

    // TODO: Step 1.12 create PendingIntent - Diferente do intent normal, indica interações futuras que das notificações com a aplicação atual
    val contentPendingIntent = PendingIntent.getActivity(
            applicationContext,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT //Flag que indica que para não estarmos a criar um nova notificação apenas atualizamos a existente
    )

    // TODO: Step 2.0 add style
    //Carregar imagem dos resources usando BitmapFactory
    val eggImage = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.cooked_egg)

    //Cria um BigPicture e adicionar a imagem carregada acima
    val bigPicStyle = NotificationCompat.BigPictureStyle()
            .bigPicture(eggImage)
            .bigLargeIcon(null) //Definir o bigLargeIcon como null provoca que o icon despareça quando se expande a notificação

    // TODO: Step 2.2 add snooze action
    val snoozeIntent = Intent(applicationContext, SnoozeReceiver::class.java)
    val snoozePendingIntent: PendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            REQUEST_CODE,
            snoozeIntent, //Intent da activity que ir ser lançada
            FLAGS
    )

    // TODO: Step 1.2 get an instance of NotificationCompat.Builder
    // Build the notification
    //To support devices to running older versions - instance of notification builder
    val builder = NotificationCompat.Builder(
            applicationContext, //App context
            applicationContext.getString(R.string.egg_notification_channel_id) //Channel ID from string resources
    )

    // TODO: Step 1.8 use the new 'breakfast' notification channel

    // TODO: Step 1.3 set title, text and icon to builder
            .setSmallIcon(R.drawable.cooked_egg) //Set icon from drawable icons
            .setContentTitle(applicationContext
                    .getString(R.string.notification_title)) //Set notification title from resources
            .setContentText(messageBody)

    // TODO: Step 1.13 set content intent - Define o intent na construção da notificação
            .setContentIntent(contentPendingIntent)
            .setAutoCancel(true)

        // TODO: Step 2.1 add style to builder
            .setStyle(bigPicStyle)
            .setLargeIcon(eggImage)

        // TODO: Step 2.3 add snooze action
            .addAction(
                    R.drawable.egg_icon,
                    applicationContext.getString(R.string.snooze),
                    snoozePendingIntent
            )

        // TODO: Step 2.5 set priority
            .setPriority(NotificationCompat.PRIORITY_HIGH)

    // TODO: Step 1.4 call notify
    notify(NOTIFICATION_ID, builder.build());
    //Este ID representa o id da notificação atual
    //Podemos usar sempre o mesmo id se a nossa aplicação tiver sempre a mesma notificação, ou seja, declarando uma constante como neste exemplo
}

// TODO: Step 1.14 Cancel all notifications
//Extension method para cancelar todas as notificações presentes
fun NotificationManager.cancelNotifications() {
    cancelAll()
}