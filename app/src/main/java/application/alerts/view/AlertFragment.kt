package application.alerts.view

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
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
import application.MyConstant
import application.MyConstant.alarmNumbers
import application.alerts.viewModel.AlertViewModel
import application.alerts.viewModel.AlertViewModelFactory
import application.model.Alert
import application.model.LocalStateAlerts
import application.model.Repository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.weather.application.R
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class AlertFragment : Fragment() {
    // alerts add here so it mush back here after add

//    private lateinit var add: ImageView
//    private lateinit var stopAlarms: ImageView
//    lateinit var stopNotifcation: ImageView

    private lateinit var alertRecycler: RecyclerView
    private lateinit var alertAdaptor: AlertAdaptor
    private lateinit var alertFactory: AlertViewModelFactory
    private lateinit var viewModel: AlertViewModel
    private lateinit var layoutManager: LinearLayoutManager
    lateinit var loading_view: ProgressBar
    private val CHANNEL_ID: String = "CHANNEL_ID"
    private val PERMISSION_REQUEST_CODE = 1001
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var alarmManager: AlarmManager

    private lateinit var alert_fab: FloatingActionButton

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_alert, container, false)
        Ui(view)
        setOnClickListener()
        sharedPreferences = context?.getSharedPreferences(MyConstant.SHARED_PREFS, 0)!!
        editor = sharedPreferences.edit()
        alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        getPermissions()
        var isAlarmDataHandled = false
        lifecycleScope.launch {
            viewModel.alertsList.collectLatest {
                when (it) {
                    is LocalStateAlerts.LoadingLocaAlertl -> {
                        loading_view.visibility = View.VISIBLE
                    }

                    is LocalStateAlerts.SuccessLocalAlert -> {
                        alertAdaptor.submitList(it.data)
                        loading_view.visibility = View.GONE
                        //to stop  subsequent calls.
                        if (!isAlarmDataHandled) {
                            upDataAlarm(it.data)
                            isAlarmDataHandled = true
                        }
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

    private fun getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(
                requireContext()
            )
        ) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + requireActivity().packageName)
            )
            startActivityForResult(intent, PERMISSION_REQUEST_CODE)
        }
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

    }


    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun observeOnAlertsList() {
        var isAlarmDataHandled = false
        lifecycleScope.launch {
            viewModel.alertsList.collectLatest {
                when (it) {
                    is LocalStateAlerts.LoadingLocaAlertl -> {
                        loading_view.visibility = View.VISIBLE
                    }

                    is LocalStateAlerts.SuccessLocalAlert -> {
                        alertAdaptor.submitList(it.data)
                        loading_view.visibility = View.GONE
                        //to stop  subsequent calls.
                        if (!isAlarmDataHandled) {
                            upDataAlarm(it.data)
                            isAlarmDataHandled = true
                        }
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
    }


    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun upDataAlarm(data: List<Alert>) {

        // Remove previous alarms
        val requestCodeListSize = sharedPreferences.getInt(alarmNumbers, 0)
        Log.d("t", "Previous alarm count: $requestCodeListSize")

        for (requestCode in 1..requestCodeListSize) {
            Log.d("t", "Previous alarm count in for loop: $requestCodeListSize")
            val alertIntent = Intent(requireContext(), AlarmBroadcastReceiver::class.java)
            val alertPendingIntent = PendingIntent.getBroadcast(
                requireContext(),
                requestCode,
                alertIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
            alarmManager.cancel(alertPendingIntent)
        }

        // Set new alarms
        var requestCodeCounter = 0
        for (alert in data) {
            Log.d("t", "data: ${data.size}")

            requestCodeCounter++
            // Serialize Alert object into JSON string
            val alertJson = Gson().toJson(alert)
            val alertUri = Uri.parse("custom_scheme://alert").buildUpon()
                .appendQueryParameter("alert_data", alertJson)
                .build()

            val intent = Intent(requireContext(), AlarmBroadcastReceiver::class.java)
            intent.data = alertUri

// i can replace requestCodeCounter with id which  final int id = (int) System.currentTimeMillis();
            val pendingIntent = PendingIntent.getBroadcast(
                requireContext(),
                requestCodeCounter,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
//If you try to set an alarm for a time that has already passed,the AlarmManager will trigger the alarm immediately.
// It will essentially treat the past time as if it were in the future and execute the alarm as soon as possible.
// This behavior is consistent with the AlarmManager's purpose,which is to schedule tasks at specific times,
// regardless of whether those times have already occurred.
            val scheduledTimeMillis = convertDateTimeToMillis(alert.day, alert.time)
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                scheduledTimeMillis,
                pendingIntent
            )
        }
//        // Update the stored alarm count
        editor.putInt(alarmNumbers, requestCodeCounter)
        editor.apply()
    }


    private fun convertDateTimeToMillis(day: String, time: String): Long {
        val dateTimeString = "$day $time"
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val date = format.parse(dateTimeString)
        val timeInMillis = date?.time ?: -1
        return if (timeInMillis != -1L) {
            timeInMillis - 18000
        } else {
            -1
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(
                    requireContext()
                )
            ) {
            }
        }
    }

    private fun Ui(view: View) {
//        add = view.findViewById(R.id.addAlert)
//        stopAlarms = view.findViewById(R.id.img_stop)
//        stopNotifcation = view.findViewById(R.id.btn_stopNotification)
        alert_fab = view.findViewById(R.id.alert_fab)

        alertFactory = AlertViewModelFactory(Repository.getInstance(requireContext()))
        viewModel = ViewModelProvider(this, alertFactory).get(AlertViewModel::class.java)
        alertRecycler = view.findViewById(R.id.alert_recycler)
        loading_view = view.findViewById(R.id.alert_loading_view)
        loading_view.visibility = View.GONE

        alertAdaptor = AlertAdaptor()
        layoutManager = LinearLayoutManager(requireContext())
        alertRecycler.adapter = alertAdaptor
        alertRecycler.layoutManager = layoutManager
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun setOnClickListener() {
        alert_fab.setOnClickListener(View.OnClickListener { showPopupMenu() })
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun showPopupMenu() {
        val popupMenu = PopupMenu(requireContext(), alert_fab)
        popupMenu.menuInflater.inflate(R.menu.alert_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_stop -> {
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle(getString(R.string.conformation))
                        .setMessage(getString(R.string.are_your_stop_the_alarms))
                        .setPositiveButton(getString(R.string.ok)) { dialog, id ->
                            viewModel.deleteALLAlarms()
                            observeOnAlertsList()
                        }
                    val dialog = builder.create()
                    dialog.show()
                    true
                }

                R.id.menu_stop_notification -> {
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle(getString(R.string.conformation))
                        .setMessage(getString(R.string.are_your_stop_the_notifications))
                        .setPositiveButton(getString(R.string.ok)) { dialog, id ->
                            viewModel.deleteALLNotification()
                            observeOnAlertsList()
                        }
                    val dialog = builder.create()
                    dialog.show()
                    true
                }
                R.id.menu_add_alert -> {
                    val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragment_container, MapFragment("", 5))
                    transaction?.addToBackStack(null)
                    transaction?.commit()
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }
}






//        add.setOnClickListener {
//            val transaction = activity?.supportFragmentManager?.beginTransaction()
//            transaction?.replace(R.id.fragment_container, MapFragment("", 5))
//            transaction?.addToBackStack(null)
//            transaction?.commit()
//        }
//
//        stopAlarms.setOnClickListener {
//            val builder = AlertDialog.Builder(requireContext())
//            builder.setTitle(getString(R.string.conformation))
//                .setMessage(getString(R.string.are_your_stop_the_alarms))
//                .setPositiveButton(getString(R.string.ok)) { dialog, id ->
//                    viewModel.deleteALLAlarms()
//                    observeOnAlertsList()
//                }
//            val dialog = builder.create()
//            dialog.show()
//        }
//
//        stopNotifcation.setOnClickListener {
//            val builder = AlertDialog.Builder(requireContext())
//            builder.setTitle(getString(R.string.conformation))
//                .setMessage(getString(R.string.are_your_stop_the_notifications))
//                .setPositiveButton(getString(R.string.ok)) { dialog, id ->
//                    viewModel.deleteALLNotification()
//                    observeOnAlertsList()
//                }
//            val dialog = builder.create()
//            dialog.show()
//        }
//    }
