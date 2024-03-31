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
        Log.d("t", "on broooooooooooooooooooooood cast")
        repository = Repository.getInstance(context)

        val receivedAlert = intent.getParcelableExtra<Alert>("A")


        Log.d(
            "t",
            "on brooooooo ${receivedAlert} and test ${intent.getStringExtra("test")} and another way ${
                intent.getExtras()?.getString("test")
            }"
        )

        if (receivedAlert != null) {
            requestTheWeather(context, receivedAlert)
            Log.d("t", "on broooooooooooooooooooooood cast alert not null")
            typeOfAlarm = receivedAlert?.typeOfAlarm.toString()
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun requestTheWeather(context: Context, alert: Alert) {
        var alertlongitude = alert.alertlongitude
        var alertlatitude = alert.alertlatitude

        if (alertlongitude != 0.0 && alertlatitude != 0.0) {
            val coroutineScope = CoroutineScope(Dispatchers.Main)
            coroutineScope.launch {
                withContext(Dispatchers.IO) {
                    repository.deleteAlert(alert)
                    val result =
                        repository.getWeather(alertlongitude!!, alertlatitude!!, null, null)
                    result.collectLatest {
                        Log.d(
                            "null", "result length is $result " +
                                    "000${it.city.name} and ${it.list.get(0).weather.get(0).description}"
                        )
                        showResult(context, it.city.name, it.list.get(0).weather.get(0).description)
                    }
                }
            }
        } else {
            Log.d("null", "00000")
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