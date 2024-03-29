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
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import application.alerts.viewModel.AlertViewModel
import application.alerts.viewModel.AlertViewModelFactory
import application.model.Alert
import application.model.LocalStateAlerts
import application.model.Repository
import com.weather.application.R
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                val typeOfAlarm =
                    dialogView.findViewById<RadioButton>(alarmType.checkedRadioButtonId).text.toString()
                newAlert.typeOfAlarm = typeOfAlarm
                if (newAlert.day == "" || newAlert.time == "") {
                    Toast.makeText(context, "Please Enter valid data", Toast.LENGTH_SHORT).show()
                } else {
                    viewModelAlert.insertALert(newAlert)
                    upDataAlarm()
                }
            }
            .setNegativeButton(getString(R.string.cancel), null)
        return builder.create()
    }

    fun upDataAlarm() {
        var requestCodeCounter = 0
        viewModelAlert.getAlerts()
        lifecycleScope.launch {
            viewModelAlert.alertsList.collectLatest {
                when (it) {
                    is LocalStateAlerts.SuccessLocalAlert -> {
                        for (alert in it.data) {

                            Log.d(
                                "null",
                                "long    ${alert.alertlongitude} lat ${alert.alertlatitude} and type ${alert.typeOfAlarm}  "
                            )
                            Log.d("null", "date ${it.data.size}  ")

                            //get from room
                            val scheduledTimeMillis: Long =
                                convertDateTimeToMillis(alert.day, alert.time)
                            val intent =
                                Intent(requireContext(), AlarmBroadcastReceiver::class.java)
                            intent.putExtra("alert-longitude", alert.alertlongitude)
                            intent.putExtra("alert-latitude", alert.alertlatitude)
                            intent.putExtra("typeOfAlarm", alert.typeOfAlarm)

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