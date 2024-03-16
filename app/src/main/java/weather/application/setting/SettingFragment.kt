package weather.application.setting

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import weather.application.R
import weather.application.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {
    lateinit var binding: FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("TAG", "Setting OnCheckedChangeListener")

        binding.radioGroupGender.setOnCheckedChangeListener { group, checkedId ->
            val location: String = when (checkedId) {
                R.id.map_button -> "Map"
                R.id.gpsButton -> "Gps"
                else -> "default"
            }
            when (location) {
                "Map" -> {
                    Log.d("TAG", "Selected location: Map")
                }
                "Gps"-> {
                    Log.d("TAG", "Selected location: GPS")
                }
            }
        }



    }
}
