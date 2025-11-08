package com.example.OfferApp.data.firebase
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.example.OfferApp.R
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
class Notifications: FirebaseMessagingService() {
    private val CHANNEL_ID = "post_notifications"
    private val CHANNEL_NAME = "Notificaciones de Posts"

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM_TOKEN", "Nuevo Token: $token")

    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        remoteMessage.data.isNotEmpty().let {
            val title = remoteMessage.data["title"] ?: "Nuevo Post"
            val body = remoteMessage.data["body"] ?: "Alguien que sigues ha publicado una oferta."


            sendNotification(title, body)
        }
    }

    private fun sendNotification(title: String, body: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Canal para notificaciones de nuevos posts."
            }
            notificationManager.createNotificationChannel(channel)
        }


        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.offerapplogo) // **¡Reemplaza 'ic_notification' con el ícono de tu app!**
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)


        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }
}