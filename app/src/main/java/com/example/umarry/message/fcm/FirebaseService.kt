package com.example.umarry.message.fcm



//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.content.Context
//import android.os.Build
//import android.util.Log
//import androidx.core.app.NotificationCompat
//import androidx.core.app.NotificationManagerCompat
//import com.example.umarry.R
//import com.google.firebase.messaging.FirebaseMessagingService
//import com.google.firebase.messaging.RemoteMessage
//
//class FirebaseService:FirebaseMessagingService() {
//    override fun onNewToken(token: String) {
//        super.onNewToken(token)
//    }
//
//    override fun onMessageReceived(message: RemoteMessage) {
//        super.onMessageReceived(message)
//
////        val title = message.notification?.title.toString()
////        val body = message.notification?.body.toString()
//
//        val title = message.data["title"].toString()
//        val body = message.data["content"].toString()
//        Log.d("mtmtmt",title)
//        Log.d("mdmdmd",body)
//
//        createNotificationChannel()
//        sendNotification(title, body)
//    }
//
//    private fun createNotificationChannel() {
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val name = "name"
//            val descriptionText = "description"
//            val importance = NotificationManager.IMPORTANCE_DEFAULT
//
//            //channel id 작성
//            val channel = NotificationChannel("Test", name, importance).apply {
//                description = descriptionText
//            }
//            // Register the channel with the system
//            val notificationManager: NotificationManager =
//                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//        }
//    }
//
//    private fun sendNotification(title:String, body:String){
//        var builder = NotificationCompat.Builder(this, "Test") //이전에 작성한 channel id를 사용
//            .setSmallIcon(R.drawable.near_m_icon)
//            .setContentTitle(title)
//            .setContentText(body)
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//        with(NotificationManagerCompat.from(this)){
//            notify(123,builder.build())
//        }
//    }
//}