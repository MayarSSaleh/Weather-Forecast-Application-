package application.alerts.view

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AlarmBroadcastReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onReceive(context: Context, intent: Intent) {

// thread
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
//               val result=viewModel
            }
        }
        val alertType = intent.getStringExtra("type")

        if (alertType == "Notification") {
            Log.d("notif", "in notification ${intent.getStringExtra("cityName")}")
//
            val notificationManagerCompat = NotificationManagerCompat.from(context)
            val notificationBuilder =  createNotification(context, "test", "test")
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notificationManagerCompat.notify(NOTIFICATION_PERM, notificationBuilder.build())

        } else if (alertType == "Alarm") {
            createAlarm()
        }
    }

    private fun createAlarm() {
        TODO("Not yet implemented")
    }

}