package weather.application.home.view

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import weather.application.MyConstant
import weather.application.R
import weather.application.home.viewModel.HomeViewModel
import weather.application.home.viewModel.HomeViewModelFactory
import weather.application.model.Repositry
import weather.application.model.WeatherItem
import weather.application.model.WeatherResponse
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.TextStyle
import java.util.Locale

class HomeFragment : Fragment() {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeViewModelFactory: HomeViewModelFactory
    private lateinit var tv_Weather_description: TextView
    private lateinit var tv_cityName: TextView
    private lateinit var tv_dt: TextView
    private lateinit var icon_image: ImageView
    private lateinit var tv_wind: TextView
    private lateinit var tv_temp: TextView
    private lateinit var tv_cloud: TextView
    private lateinit var tv_pressure: TextView
    private lateinit var tv_humidity: TextView
    private lateinit var tv_temp_unit: TextView
    private lateinit var tv_wind_speed: TextView
    private lateinit var hoursAdapterList: HoursAdapterList
    private lateinit var recyclerView_hours: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var daylayoutManager: LinearLayoutManager
    private lateinit var recyclerViewDays: RecyclerView
    private lateinit var daysAdaptor: DaysAdaptor
    private lateinit var sharedPreferences: SharedPreferences

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        homeViewModelFactory = HomeViewModelFactory(Repositry.getInstance(requireContext()))
        homeViewModel = ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel::class.java)
        initUI(view)
        hoursAdapterList = HoursAdapterList()
        daysAdaptor = DaysAdaptor()
        homeViewModel.getWeather(requireContext())
        sharedPreferences = context?.getSharedPreferences(MyConstant.SHARED_PREFS, 0)!!
        homeViewModel.weatherResponseLiveData.observe(viewLifecycleOwner) { weatherResponse ->
            setCurrentWeather(weatherResponse)
            submitTOHoursAdapterList(weatherResponse.list)
            submitToDaysAdapterList(weatherResponse.list)
        }
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setCurrentWeather(weatherResponse: WeatherResponse) {
        var weatherItem = weatherResponse.list.get(0)
        tv_Weather_description.text =
            weatherItem.weather.get(0).description
        tv_cityName.text = weatherResponse.city.name
        tv_dt.text = weatherItem.dt_txt
        var url =
            "https://openweathermap.org/img/wn/" + weatherItem.weather.get(0).icon + "@2x.png"
        Picasso.get().load(url).into(icon_image)
        tv_temp.text = weatherItem.main.temp.toString()
        tv_pressure.text = weatherItem.main.pressure.toString()
        tv_cloud.text = weatherItem.clouds.all.toString()
        tv_humidity.text = weatherItem.main.humidity.toString()
        tv_temp_unit.text = sharedPreferences.getString(MyConstant.temp_unit, " Kelvin")
        when (sharedPreferences.getString(MyConstant.temp_unit, " Kelvin")) {
            "Kelvin", "Celsius" -> {                // Meter unit
                if (sharedPreferences.getString(MyConstant.wind_unit, "meter_sec") == "miles_hour") {
                    tv_wind_speed.text = "miles/hour"
                    tv_wind.text = convertMetersPerSecondToMilesPerHour(weatherItem.wind.speed)
                } else {
                    tv_wind_speed.text = "meter/sec"
                    tv_wind.text = weatherItem.wind.speed.toString()
                }
            }
            "Fahrenheit" -> {                // Miles/hour
                if (sharedPreferences.getString(MyConstant.wind_unit, "meter_sec") == "meter_sec") {
                    tv_wind_speed.text = "meter/sec"
                    tv_wind.text = convertMilesPerHourToMetersPerSecond(weatherItem.wind.speed)
                } else {
                    tv_wind_speed.text = "miles/hour"
                    tv_wind.text = weatherItem.wind.speed.toString()
                }
            }
        }

    }
    private fun convertMetersPerSecondToMilesPerHour(metersPerSecond: Double): String {
        val metersToMiles = 0.000621371
        val secondsToHours = 3600.0
        val result=metersPerSecond * metersToMiles * secondsToHours
        return String.format("%.2f", result)
    }
    private fun convertMilesPerHourToMetersPerSecond(milesPerHour: Double): String {
        val milesToMeters = 1609.34
        val hoursToSeconds = 3600.0
        val result = milesPerHour * milesToMeters / hoursToSeconds
        return String.format("%.2f", result)
    }
    private fun initUI(view: View) {
        tv_Weather_description = view.findViewById(R.id.tv_Weather_description)
        tv_cityName = view.findViewById(R.id.city_name)
        tv_dt = view.findViewById(R.id.time_data)
        icon_image = view.findViewById(R.id.icon_image)
        tv_wind = view.findViewById(R.id.tv_wind_speed)
        tv_temp = view.findViewById(R.id.tv_temp)
        tv_pressure = view.findViewById(R.id.tv_pressure)
        tv_cloud = view.findViewById(R.id.tv_cloud)
        tv_humidity = view.findViewById(R.id.tv_humidity)
        recyclerView_hours = view.findViewById(R.id.recyclerView_hours)
        recyclerViewDays = view.findViewById(R.id.recyclerView_days)
        tv_temp_unit = view.findViewById(R.id.tv_temp_unit)
        tv_wind_speed = view.findViewById(R.id.tv_wind_unit)
    }
    private fun submitTOHoursAdapterList(weatherItemList: List<WeatherItem>) {
        var hoursList = mutableListOf<WeatherItem>()
        for (x in 1 until 8) {
            hoursList.add(weatherItemList[x])
        }
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView_hours.adapter = hoursAdapterList
        recyclerView_hours.layoutManager = layoutManager
        hoursAdapterList.submitList(hoursList)
    }
    private fun submitToDaysAdapterList(list: List<WeatherItem>) {
        var dayList = mutableListOf<WeatherItem>()
        for (i in 8..32 step 8) {        //  dayList.add(list.get(8), list.get(16), list.get(24), list.get(32))
            dayList.add(list[i])
        }
        daylayoutManager = LinearLayoutManager(requireContext())
        recyclerViewDays.adapter = daysAdaptor
        recyclerViewDays.layoutManager = daylayoutManager
        daysAdaptor.submitList(dayList)
    }

}