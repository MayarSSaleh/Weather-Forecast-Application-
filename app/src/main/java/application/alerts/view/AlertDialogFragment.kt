package application.alerts.view


import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.DialogFragment
import com.weather.application.R
import java.util.Calendar

class AlertDialogFragment : DialogFragment() {
    private lateinit var btn_day: Button
    private lateinit var btn_time: Button
    val calendar = Calendar.getInstance()
    private var day = ""
    private var time = ""

    interface DialogListener {
        fun onPositiveButtonClick(
            alarmType: String,
            selectedDay: String,
            selectedTime: String
        )
        fun onNegativeButtonClick()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = LayoutInflater.from(requireContext())
        val dialogView = inflater.inflate(R.layout.dialog, null)
        val alarmType = dialogView.findViewById<RadioGroup>(R.id.radioGroup1)
        btn_time = dialogView.findViewById(R.id.btn_start_time)
        btn_day = dialogView.findViewById(R.id.btn_start_day)

        btn_day.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, monthOfYear, dayOfMonth ->
                    val selectedDate = "$dayOfMonth/${monthOfYear + 1}/$year"
                    btn_day.text = selectedDate
                    day = selectedDate
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
                    time = "$hourOfDay:$minute"
                    btn_time.text = time
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
            )
            timePickerDialog.show()
        }

        builder.setView(dialogView)
            .setPositiveButton("OK") { _, _ ->
                val selectedOption1 =
                    dialogView.findViewById<RadioButton>(alarmType.checkedRadioButtonId).text.toString()
                val selectedDay = btn_day.text.toString()
                val selectedTime = btn_time.text.toString()

                val listener = targetFragment as DialogListener?
                listener?.onPositiveButtonClick(selectedOption1, selectedDay, selectedTime)
            }
            .setNegativeButton("Cancel") { _, _ ->
                val listener = targetFragment as DialogListener?
                listener?.onNegativeButtonClick()
            }

        return builder.create()
    }
}
