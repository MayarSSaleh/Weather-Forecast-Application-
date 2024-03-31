package application.alerts.view

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import application.model.Alert
import application.model.Repository
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AlarmBroadcastReceiver : BroadcastReceiver() {

    private var typeOfAlarm = ""
    lateinit var repository: Repository


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onReceive(context: Context, intent: Intent) {
        repository = Repository.getInstance(context)
        val alertUri = intent.data
        val alertJson = alertUri?.getQueryParameter("alert_data")
        if (alertJson != null) {
            // Deserialize JSON string into Alert object
            val alert = Gson().fromJson(alertJson, Alert::class.java)
            // Now you have the Alert object
            requestTheWeather(context, alert)
            typeOfAlarm = alert?.typeOfAlarm.toString()
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun requestTheWeather(context: Context, alert: Alert) {
           val coroutineScope = CoroutineScope(Dispatchers.Main)
            coroutineScope.launch {
                withContext(Dispatchers.IO) {
                    repository.deleteAlert(alert)
                    val result =
                        repository.getWeather( alert.alertlatitude, alert.alertlongitude,null, null)
                    result.collectLatest {
                        Log.d(
                            "null", "result length is ${it.list.get(0).weather.get(0).description}"
                        )
                        Log.d(
                            "null", " nammmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmme ${it.city.name}"
                        )

                        showResult(context, it.city.name, it.list.get(0).weather.get(0).description)
                    }
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun showResult(context: Context, cityName: String, description: String) {
        if (typeOfAlarm == "Notification") {
            val notificationManagerCompat = NotificationManagerCompat.from(context)
            val notificationBuilder = createNotification(context, cityName, description)
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notificationManagerCompat.notify(NOTIFICATION_PERM, notificationBuilder.build())
        } else {
            if (typeOfAlarm == "Alarm" && Settings.canDrawOverlays(context)) {
                val intent = Intent(context, AlarmActivity::class.java)
                intent.putExtra("cityName", cityName)
                intent.putExtra("description", description)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
        }
    }
}