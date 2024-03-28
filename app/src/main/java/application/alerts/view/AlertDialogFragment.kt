package application.alerts.view


import android.app.AlarmManager
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import application.alerts.viewModel.AlertViewModel
import application.alerts.viewModel.AlertViewModelFactory
import application.model.Alert
import application.model.LocalStateFavouirteLocations
import application.model.Repository
import com.weather.application.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AlertDialogFragment(val addresssName: String, val longitude: Double, val latitude: Double) :
    DialogFragment() {

    private lateinit var btn_day: Button
    private lateinit var btn_time: Button
    val calendar = Calendar.getInstance()
    private lateinit var viewModelAlert: AlertViewModel
    private lateinit var alertFactory: AlertViewModelFactory
    private val newAlert = Alert(addresssName, longitude, latitude, "", "", "")
    private lateinit var alarmManager: AlarmManager

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = LayoutInflater.from(requireContext())
        val dialogView = inflater.inflate(R.layout.dialog, null)

        alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val alarmType = dialogView.findViewById<RadioGroup>(R.id.radioGroup1)
        btn_time = dialogView.findViewById(R.id.btn_start_time)
        btn_day = dialogView.findViewById(R.id.btn_start_day)
        alertFactory = AlertViewModelFactory(Repository.getInstance(requireContext()))
        viewModelAlert = ViewModelProvider(this, alertFactory).get(AlertViewModel::class.java)

        btn_day.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, monthOfYear, dayOfMonth ->
                    val selectedDate = "$dayOfMonth/${monthOfYear + 1}/$year"
                    btn_day.text = selectedDate
                    newAlert.day = selectedDate
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        btn_time.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                requireContext(),
                { _, hourOfDay, minute ->
                    var time = "$hourOfDay:$minute"
                    btn_time.text = time
                    newAlert.time = time
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            )
            timePickerDialog.show()
        }

        builder.setView(dialogView)
            .setPositiveButton("OK") { _, _ ->
                val typeOfAlarm =
                    dialogView.findViewById<RadioButton>(alarmType.checkedRadioButtonId).text.toString()
                newAlert.typeOfAlarm = typeOfAlarm
                viewModelAlert.insertALert(newAlert)
                setAlerts(newAlert)

            }
            .setNegativeButton("Cancel", null)

        return builder.create()
    }

    private fun setAlerts(alert: Alert) {
        val scheduledTimeMillis: Long = convertDateTimeToMillis(alert.day, alert.time)
        Log.d("notif", "scheduledTimeMillis ${alert.day}and time is ${alert.time}")
        Log.d("notif", "scheduledTimeMillis $scheduledTimeMillis")

        val intent = Intent(requireContext(), AlarmBroadcastReceiver::class.java)
        intent.putExtra("long", alert.alertlongitude)
        intent.putExtra("type", alert.typeOfAlarm)
        intent.putExtra("lat", alert.alertlatitude)
        intent.putExtra("cityName", alert.alertlocationName)
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            1,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager?.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            scheduledTimeMillis,
            pendingIntent
        )
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