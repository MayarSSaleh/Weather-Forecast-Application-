package application.setting.view

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import application.MapFragment
import application.MyConstant
import application.MyConstant.SHARED_PREFS

import application.sharedBetweenScrens.WeatherShowViewModel
import application.model.Repository
import com.weather.application.R
import com.weather.application.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {

    private lateinit var homeviewModel: WeatherShowViewModel
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
        homeviewModel = WeatherShowViewModel(Repository.getInstance(requireContext()))
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        sharedPreferences = context?.getSharedPreferences(SHARED_PREFS, 0)!!
        editor = sharedPreferences.edit()

        binding.root.findViewById<RadioButton>(
            sharedPreferences.getInt(KEY_LOCATION_RADIO_BUTTON_ID, 0)
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
                "Map" -> {
                    val transaction = activity?.supportFragmentManager?.beginTransaction()
                    transaction?.replace(
                        R.id.fragment_container, MapFragment(
                            KEY_LOCATION_RADIO_BUTTON_ID, checkedId
                        )
                    )
                    transaction?.addToBackStack(null)
                    transaction?.commit()
                }

                "Gps" -> {
                    editor.putString(MyConstant.location, "Gps")
                    editor.putInt(KEY_LOCATION_RADIO_BUTTON_ID, checkedId)
                    editor.apply()
                }
            }
        }

        binding.radioGroupLanguage.setOnCheckedChangeListener { group, checkedId ->
            val language: String = when (checkedId) {
                R.id.btn_Arabic -> "Arabic"
                R.id.btn_English -> "English"
                else -> "English"
            }
            when (language) {
                "Arabic" -> {
                    editor.putString(MyConstant.lan, "ar")
                    homeviewModel.mySetLocale("ar", requireContext(), requireActivity())
                }

                "English" -> {
                    editor.putString(MyConstant.lan, "en")
                    homeviewModel.mySetLocale("en", requireContext(), requireActivity())
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