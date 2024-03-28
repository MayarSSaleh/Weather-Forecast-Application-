package application.alerts.view


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.weather.application.R


private const val CHANNEL_ID: String = "CHANNEL_ID"
const val NOTIFICATION_PERM = 123

@RequiresApi(Build.VERSION_CODES.S)
fun createNotification(context: Context, cityName: String, descrption: String): NotificationCompat.Builder {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Notification Channel"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance)
        val notificationManager =
            context.getSystemService(NotificationManager::class.java) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        Log.d("notif", "inside channel")

    }

    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("The weather")
        .setContentText("The weather in ${cityName} is ${descrption} ")
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
    return builder
}