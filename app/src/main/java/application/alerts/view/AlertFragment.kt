package application.alerts.view

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import application.MapFragment
import application.alerts.viewModel.AlertViewModel
import application.alerts.viewModel.AlertViewModelFactory
import application.model.LocalStateAlerts
import application.model.Repository
import com.weather.application.R
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class AlertFragment : Fragment() {
    private lateinit var add: ImageView
    private lateinit var stopAlarms: ImageView

    private lateinit var alertRecycler: RecyclerView
    private lateinit var alertAdaptor: AlertAdaptor
    private lateinit var alertFactory: AlertViewModelFactory
    private lateinit var viewModel: AlertViewModel
    private lateinit var layoutManager: LinearLayoutManager
    lateinit var loading_view: ProgressBar
    lateinit var stopNotifcation: ImageView
    private val CHANNEL_ID: String = "CHANNEL_ID"
    private lateinit var alarmManager: AlarmManager


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_alert, container, false)
        Ui(view)
        setOnClickLisenter()
        alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val notificationManager =
                requireContext().getSystemService(NotificationManager::class.java) as NotificationManager
            if (notificationManager.areNotificationsEnabled()) {
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERM
                )
            }
        }
        lifecycleScope.launch {
            viewModel.alertsList.collectLatest {
                when (it) {
                    is LocalStateAlerts.LoadingLocaAlertl -> {
                        loading_view.visibility = View.VISIBLE
                    }
                    is LocalStateAlerts.SuccessLocalAlert -> {
                        alertAdaptor.submitList(it.data)
                        loading_view.visibility = View.GONE
                    }
                    else -> {
                        loading_view.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.sorry_can_not_get_them),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        return view
    }

    private fun Ui(view: View) {
        add = view.findViewById(R.id.addAlert)
        alertFactory = AlertViewModelFactory(Repository.getInstance(requireContext()))
        viewModel = ViewModelProvider(this, alertFactory).get(AlertViewModel::class.java)
        alertRecycler = view.findViewById(R.id.alert_recycler)
        loading_view = view.findViewById(R.id.alert_loading_view)
        loading_view.visibility = View.GONE
        stopAlarms = view.findViewById(R.id.img_stop)
        stopNotifcation = view.findViewById(R.id.btn_stopNotification)
        alertAdaptor = AlertAdaptor()

        layoutManager = LinearLayoutManager(requireContext())
        alertRecycler.adapter = alertAdaptor
        alertRecycler.layoutManager = layoutManager
    }


    fun setOnClickLisenter() {

        add.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragment_container, MapFragment("", 5))
            transaction?.addToBackStack(null)
            transaction?.commit()
        }

        stopAlarms.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(getString(R.string.conformation))
                .setMessage(getString(R.string.are_your_stop_the_alarms))
                .setPositiveButton(getString(R.string.ok)) { dialog, id ->
                    viewModel.deleteALLAlarms()
                    upDataAlarm()
                }
            val dialog = builder.create()
            dialog.show()
        }

        stopNotifcation.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(getString(R.string.conformation))
                .setMessage(getString(R.string.are_your_stop_the_notifications))
                .setPositiveButton(getString(R.string.ok)) { dialog, id ->
                    viewModel.deleteALLNotification()
                    upDataAlarm()
                }
            val dialog = builder.create()
            dialog.show()
        }
    }

    fun upDataAlarm() {
        var requestCodeCounter = 0
        viewModel.getAlerts()
        lifecycleScope.launch {
            viewModel.alertsList.collectLatest {
                when (it) {
                    is LocalStateAlerts.SuccessLocalAlert -> {
                        for (alert in it.data) {
                            //get from room
                            val scheduledTimeMillis: Long = convertDateTimeToMillis(alert.day, alert.time)
                            val intent = Intent(requireContext(), AlarmBroadcastReceiver::class.java)
                            intent.putExtra("Alert", alert)
                            val pendingIntent = PendingIntent.getBroadcast(
                                requireContext(),
                                requestCodeCounter,
                                intent,
                                PendingIntent.FLAG_IMMUTABLE
                            )
                            alarmManager?.setAndAllowWhileIdle(
                                AlarmManager.RTC_WAKEUP,
                                scheduledTimeMillis,
                                pendingIntent
                            )
                            requestCodeCounter++
                        }
                    }
                    else -> {
                    }
                }
            }
        }
    }

    private fun convertDateTimeToMillis(day: String, time: String): Long {
        Log.d("notif", "in method  ${day}and time is ${time}")
        val dateTimeString = "$day $time"
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val date = format.parse(dateTimeString)
        val timeInMillis = date?.time ?: -1
        return if (timeInMillis != -1L) {
            // Subtract 60 seconds (60,000 milliseconds) from the calculated time
            timeInMillis - 18000
        } else {
            -1
        }
    }
}