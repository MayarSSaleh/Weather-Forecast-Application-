package weather.application.setting

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import weather.application.MyConstant
import weather.application.MyConstant.SHARED_PREFS
import weather.application.R
import weather.application.databinding.FragmentSettingBinding
import weather.application.home.viewModel.HomeViewModel
import weather.application.model.Repositry

class SettingFragment : Fragment() {

    private lateinit var homeviewModel: HomeViewModel
    private lateinit var binding: FragmentSettingBinding
    private lateinit var location: String
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor


    companion object {
        private const val KEY_LOCATION_RADIO_BUTTON_ID = "locationRadioButtonId"
        private const val KEY_WIND_RADIO_BUTTON_ID = "windRadioButtonId"
        private const val KEY_TEMPERATURE_RADIO_BUTTON_ID = "temperatureRadioButtonId"
        private const val KEY_LANGUAGE_RADIO_BUTTON_ID = "languageRadioButtonId"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeviewModel = HomeViewModel(Repositry.getInstance(requireContext()))
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        sharedPreferences = context?.getSharedPreferences(SHARED_PREFS, 0)!!
        editor = sharedPreferences.edit()

        binding.root.findViewById<RadioButton>(
            sharedPreferences.getInt(
                KEY_LOCATION_RADIO_BUTTON_ID,
                0
            )
        )?.isChecked = true
        binding.root.findViewById<RadioButton>(
            sharedPreferences.getInt(
                KEY_TEMPERATURE_RADIO_BUTTON_ID,
                0
            )
        )?.isChecked = true
        binding.root.findViewById<RadioButton>(
            sharedPreferences.getInt(
                KEY_LANGUAGE_RADIO_BUTTON_ID,
                0
            )
        )?.isChecked = true
        binding.root.findViewById<RadioButton>(
            sharedPreferences.getInt(
                KEY_WIND_RADIO_BUTTON_ID,
                0
            )
        )?.isChecked = true

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.radioGroupLocation.setOnCheckedChangeListener { group, checkedId ->
            location = when (checkedId) {
                R.id.map_button -> "Map"
                R.id.gpsButton -> "Gps"
                else -> "Gps"
            }
            when (location) {
                "Map" -> editor.putString(MyConstant.location, "Map")
                "Gps" -> editor.putString(MyConstant.location, "Gps")
            }
            editor.putInt(KEY_LOCATION_RADIO_BUTTON_ID, checkedId)
            editor.apply()
        }

        binding.radioGroupLanguage.setOnCheckedChangeListener { group, checkedId ->
            val language: String = when (checkedId) {
                R.id.btn_Arabic -> "Arabic"
                R.id.btn_English -> "English"
                else -> "English"
            }
            when (language) {
                "Arabic" -> {
                    editor.putString(MyConstant.lan, "Arabic")
                    homeviewModel.setLocale("ar",requireContext())
                }
                "English" -> {
                    editor.putString(MyConstant.lan, "English")
                    homeviewModel.setLocale("en",requireContext())
                }
            }
            editor.putInt(KEY_LANGUAGE_RADIO_BUTTON_ID, checkedId)
            editor.apply()
        }
        binding.radioGroupWind.setOnCheckedChangeListener { group, checkedId ->
            val windUnit: String = when (checkedId) {
                R.id.btn_meter_sec -> "meter_sec"
                R.id.btn_miles_hour -> "miles_hour"
                else -> "meter_sec"
            }
            when (windUnit) {
                "meter_sec" -> editor.putString(MyConstant.wind_unit, "meter_sec")
                "miles_hour" -> editor.putString(MyConstant.wind_unit, "miles_hour")
            }
            editor.putInt(KEY_WIND_RADIO_BUTTON_ID, checkedId)
            editor.apply()
        }

        binding.radioGroupTemperature.setOnCheckedChangeListener { group, checkedId ->
            val temperature: String = when (checkedId) {
                R.id.btn_Kelvin -> "Kelvin"
                R.id.btn_Celsius -> "Celsius"
                R.id.btn_Fahrenheit -> "Fahrenheit"
                else -> "Kelvin"
            }
            when (temperature) {
                "Celsius" -> editor.putString(MyConstant.temp_unit, "Celsius")
                "Fahrenheit" -> editor.putString(MyConstant.temp_unit, "Fahrenheit")
                "Kelvin" -> editor.putString(MyConstant.temp_unit, "Kelvin")
            }
            editor.putInt(KEY_TEMPERATURE_RADIO_BUTTON_ID, checkedId)
            editor.apply()
        }
    }
}