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

package com.example.android.eggtimernotifications.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.android.eggtimernotifications.R
import com.example.android.eggtimernotifications.databinding.FragmentEggTimerBinding
import com.google.firebase.messaging.FirebaseMessaging

class EggTimerFragment : Fragment() {

    private val TOPIC = "breakfast"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentEggTimerBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_egg_timer, container, false
        )

        val viewModel = ViewModelProviders.of(this).get(EggTimerViewModel::class.java)

        binding.eggTimerViewModel = viewModel
        binding.lifecycleOwner = this.viewLifecycleOwner

        // TODO: Step 1.7 call create channel
        //Criacção do canal de notificação passando como paramentro o id e o nome a atribuir ao canal
        createChannel(getString(R.string.egg_notification_channel_id), getString(R.string.egg_notification_channel_name))

        // TODO: Step 3.1 create a new channel for FCM
        createChannel(getString(R.string.breakfast_notification_channel_id),getString(R.string.breakfast_notification_channel_name))

        // TODO: Step 3.4 call subscribe topics on start
        subscribeTopic()

        return binding.root
    }

    //Metodo recebe o id do canal e o nome do canal que é criado logo no onCreateView
    private fun createChannel(channelId: String, channelName: String) {
        //Channels estão disponiveis apartir da API 26 para cima
        // TODO: Step 1.6 START create a channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                    channelId,
                    channelName, //Channel name: o que o utilizador vê no ecra de settings
                    // TODO: Step 2.4 change importance
                    NotificationManager.IMPORTANCE_HIGH
            )
            // TODO: Step 2.6 disable badges for this channel
                    .apply { setShowBadge(false) }

            notificationChannel.enableLights(true) //ativas as luzes do dispositivo sempre que a notificação (com este ID_Channel) é desparada
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Time for breakfast"

            //Obter novamente uma instancia do notification manager atraves do sistema
            val notificationManager = requireActivity().getSystemService(
                    NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
            //Fim da criação do canal de notificação
        }
    }

    // TODO: Step 3.3 subscribe to breakfast topic
    private fun subscribeTopic() {
        // [START subscribe_topics]
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
                .addOnCompleteListener { task ->
                    var msg = getString(R.string.message_subscribed)
                    if (!task.isSuccessful) {
                        msg = getString(R.string.message_subscribe_failed)
                    }
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                }
        // [END subscribe_topics]
    }

    companion object {
        fun newInstance() = EggTimerFragment()
    }
}

