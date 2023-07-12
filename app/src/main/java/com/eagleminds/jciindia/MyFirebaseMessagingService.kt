package com.eagleminds.jciindia

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val NOTIFICATION_ID = 1
    private val CHANNEL_ID = "notification_channel"
    private val CHANNEL_NAME = "Jobs7WebView"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {
            val url = remoteMessage.data["urls"]
            val imageUrl = remoteMessage.data["imageUrl"]
            val title = remoteMessage.data["title"]
            val message = remoteMessage.data["message"]
            generateNotification(title, message, imageUrl, url)
        }
    }

    private fun generateNotification(title: String?, message: String?, imageUrl: String?, urls: String?) {
        val intent = Intent(this, NotificationWebviewActivity::class.java)
        intent.putExtra("urls", urls)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationLayout = RemoteViews(packageName, R.layout.notification)
        notificationLayout.setTextViewText(R.id.text, message)

        val imageBitmap = if (imageUrl != null) {
            try {
                val url = URL(imageUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input: InputStream = connection.inputStream
                BitmapFactory.decodeStream(input)
            } catch (e: IOException) {
                e.printStackTrace()
                BitmapFactory.decodeResource(resources, R.drawable.jobs7no)
            }
        } else {
            BitmapFactory.decodeResource(resources, R.drawable.jobs7no)
        }
        notificationLayout.setImageViewBitmap(R.id.image, imageBitmap)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.jobs7no)
            .setContentTitle(title)
            .setContentText(message)
            .setCustomContentView(notificationLayout)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, builder.build())
    }
}
