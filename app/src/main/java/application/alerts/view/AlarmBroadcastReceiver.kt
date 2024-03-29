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
import androidx.lifecycle.ViewModelProvider
import application.ShowWeatherDeailrsViewModel.WeatherShowModelFactory
import application.ShowWeatherDeailrsViewModel.WeatherShowViewModel
import application.model.APiStateOrLocalStateFromLastWeather
import application.model.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AlarmBroadcastReceiver : BroadcastReceiver() {

    private var typeOfAlarm = ""
    private var alertlongitude = 0.0
    private var alertlatitude = 0.0

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onReceive(context: Context, intent: Intent) {

        alertlongitude = intent.getDoubleExtra("alert-longitude", 0.0)
        alertlatitude = intent.getDoubleExtra("alert-latitude", 0.0)
        typeOfAlarm = intent.getStringExtra("typeOfAlarm").toString()
        Log.d(
            "null", "broad long    ${intent.getStringExtra("alert-longitude")} " +
                    "lat ${intent.getDoubleExtra("alert-latitude", 0.0)}" +
                    " and type ${intent.getDoubleExtra("typeOfAlarm", 0.0)}  "
        )

        showResult(context, "eeee", "ttttttt")
//        if (alertlongitude != 0.0 && alertlatitude != 0.0) {
//        val
//            lifecycleScope.launch {
//                withContext(Dispatchers.IO) {
//                    homeViewModelFactory =
//                        WeatherShowModelFactory(Repository.getInstance(context))
//                 var   homeViewModel = ViewModelProvider(this, homeViewModelFactory).get( WeatherShowViewModel::class.java )
//                  var  homeViewModel.getWeather(context, alertlongitude, alertlatitude)
//
//                    homeViewModel.weatherResponseLiveData.collectLatest {
//                        when (it) {
//                            is APiStateOrLocalStateFromLastWeather.Success -> {
//                                showResult(context, it.data.city.name, it.data.list.get(0).weather.get(0).description)
//                            }
//                            else -> {}
//                        }
//                    }
//                }
//            }
//        }
//        else {
//            Log.d("null", "0.00000000000000000000000")
//        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun showResult(context: Context, cityName: String, descrpstion: String) {
        if (typeOfAlarm == "Notification") {
            val notificationManagerCompat = NotificationManagerCompat.from(context)
            val notificationBuilder = createNotification(context, cityName, descrpstion)
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
                intent.putExtra("description", descrpstion)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
        }
    }
}