package application.alerts.view


import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import application.model.SetAlert
import com.weather.application.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import java.util.Calendar

class AlertDialogFragment(val addresssName:String,val longitude: Double, val latitude: Double) : DialogFragment() {

    private lateinit var btn_day: Button
    private lateinit var btn_time: Button
    val calendar = Calendar.getInstance()
    private lateinit var viewModelAlert: AlertViewModel
    private lateinit var alertFactory: AlertViewModelFactory
    private val newAlert = Alert(addresssName, longitude, latitude, "", "", "")

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = LayoutInflater.from(requireContext())
        val dialogView = inflater.inflate(R.layout.dialog, null)
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
                false
            )
            timePickerDialog.show()
        }

        builder.setView(dialogView)
            .setPositiveButton("OK") { _, _ ->
                val typeOfAlarm =
                    dialogView.findViewById<RadioButton>(alarmType.checkedRadioButtonId).text.toString()
                newAlert.typeOfAlarm = typeOfAlarm
                viewModelAlert.insertALert(newAlert)
            }
            .setNegativeButton("Cancel", null)

        return builder.create()
    }
}
