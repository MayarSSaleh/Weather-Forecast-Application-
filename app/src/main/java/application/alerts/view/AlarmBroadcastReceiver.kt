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
import application.ShowWeatherDeailrsViewModel.WeatherShowModelFactory
import application.ShowWeatherDeailrsViewModel.WeatherShowViewModel


class AlarmBroadcastReceiver : BroadcastReceiver() {

    private lateinit var homeViewModel: WeatherShowViewModel
    private lateinit var homeViewModelFactory: WeatherShowModelFactory
    private var typeOfAlarm = ""
    private  var alertlongitude = ""
    private  var alertlatitude = ""

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onReceive(context: Context, intent: Intent) {

        alertlongitude = intent.getStringExtra("alert-longitude").toString()
        alertlatitude = intent.getStringExtra("alert-latitude").toString()
        typeOfAlarm = intent.getStringExtra("typeOfAlarm").toString()

        Log.d("null", "broad long    ${intent.getStringExtra("alert-longitude").toString() } " +
                "lat ${intent.getStringExtra("alert-latitude").toString()}" +
                " and type ${intent.getStringExtra("typeOfAlarm").toString()}  ")

        showResult(context, "eeee", "ttttttt")

        /* thread
        //        GlobalScope.launch {
        //            withContext(Dispatchers.IO) {
        //                homeViewModelFactory =
        //                    WeatherShowModelFactory(Repository.getInstance(context))
        //                homeViewModel =
        //                    ViewModelProvider(this, homeViewModelFactory).get(
        //                        WeatherShowViewModel::class.java
        //                    )
        //                if (alert != null) {
        //                    Log.d("notify","is nnnnnnnnnnnnnnnnnnnnnul")
        //                    homeViewModel.getWeather(
        //                        context,
        //                        alert.alertlongitude,
        //                        alert.alertlatitude
        //                    )
        //                }
        //                homeViewModel.weatherResponseLiveData.collectLatest {
        //                    when (it) {
        //                        is APiStateOrLocalStateFromLastWeather.Success -> {
        //                            cityName = it.data.city.name
        //                            descrpstion = it.data.list.get(0).weather.get(0).description
        //                        }
        //                        else -> {
        //                        }
        //                    }
        //                }
        //                showResult(context,cityName,descrpstion)
        //            }
        //       }*/
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
            createAlarm(context)
        }
    }

    private fun createAlarm(context: Context) {

        if (Settings.canDrawOverlays(context)) {
            Log.d("null","alaaaaaaaaaaaaaaaaaaa")
            val intent = Intent(context, AlarmActivity::class.java)
            // intent.putExtra("msg",msg)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK /*or Intent.FLAG_INCLUDE_STOPPED_PACKAGES or Intent.FLAG_ACTIVITY_CLEAR_TASK*/)
            context.startActivity(intent)

        }
    }
}