package application.home.view


import android.Manifest
import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.squareup.picasso.Picasso
import application.MyConstant
import application.ShowWeatherDeailrsViewModel.WeatherShowViewModel
import application.ShowWeatherDeailrsViewModel.WeatherShowModelFactory
import application.model.APiStateOrLocalStateFromLastWeather
import application.model.FavLocation

import application.model.Repository
import application.model.WeatherItem
import application.model.WeatherResponse
import com.weather.application.R
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {
    private lateinit var homeViewModel: WeatherShowViewModel
    private lateinit var homeViewModelFactory: WeatherShowModelFactory
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
    private lateinit var geocoder: Geocoder
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationProviderClient: FusedLocationProviderClient
    lateinit var loading_view: ProgressBar
    var requestrefused = 0


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        homeViewModelFactory = WeatherShowModelFactory(Repository.getInstance(requireContext()))
        homeViewModel = ViewModelProvider(this, homeViewModelFactory).get(WeatherShowViewModel::class.java)
        initUI(view)
        loading_view.visibility = View.GONE
        hoursAdapterList = HoursAdapterList()
        daysAdaptor = DaysAdaptor(requireContext())
        geocoder = Geocoder(requireContext())
        sharedPreferences = context?.getSharedPreferences(MyConstant.SHARED_PREFS, 0)!!
        getGpsLocationPermision()
        val favLocation: FavLocation? = arguments?.getParcelable("favLocation")
        if (favLocation != null) {
            homeViewModel.getWeather(requireContext(), favLocation.latitude, favLocation.longitude)
        } else {
            if (homeViewModel.isNetworkAvailable(context)) {

                var openLocationFrom=sharedPreferences.getString(MyConstant.User_CURRENT_LOCATION, "isGps")

                if ( openLocationFrom == "isMap") {
                    homeViewModel.getWeather(
                        requireContext(),
                        sharedPreferences.getString(MyConstant.location_lat, "0.0")!!
                            .toDouble(),
                        sharedPreferences.getString(MyConstant.location_lon, "0.0")!!.toDouble()
                    )
                } else {
                    getFreshLocation()
                }
            } else {
                homeViewModel.getLastWeather()
                responseOfGetWeatherFromApi()
            }
        }
        responseOfGetWeatherFromApi()
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun responseOfGetWeatherFromApi() {
        lifecycleScope.launch {
            homeViewModel.weatherResponseLiveData.collectLatest {
                when (it) {
                    is APiStateOrLocalStateFromLastWeather.Loading -> {
                        loading_view.visibility = View.VISIBLE
                    }
                    is APiStateOrLocalStateFromLastWeather.Success -> {
                        setCurrentWeather(it.data)
                        submitTOHoursAdapterList(it.data.list)
                        submitToDaysAdapterList(it.data.list)
                        loading_view.visibility = View.GONE
                    }
                    else -> {
                        loading_view.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            "Sorry Can not get the weather}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
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

        if (sharedPreferences.getString(MyConstant.temp_unit, "Kelvin") != "Kelvin") {
            tv_temp_unit.text = sharedPreferences.getString(MyConstant.temp_unit, " Kelvin")
        }
        var savedTmepUnit = sharedPreferences.getString(MyConstant.temp_unit, "Kelvin")
        when (savedTmepUnit) {
            "Kelvin" -> {       // Meter unit
                if (sharedPreferences.getString(MyConstant.wind_unit,"meter_sec") == "miles_hour") {
                    tv_wind_speed.text = getString(R.string.miles_hour)
                    var value = convertMetersPerSecondToMilesPerHour(weatherItem.wind.speed)
                    tv_wind.text = value
                } else {
                    tv_wind_speed.text = getString(R.string.meter_sec)
                    tv_wind.text = weatherItem.wind.speed.toString()
                }
            }

            "Celsius" -> {
                // Meter unit
                if (sharedPreferences.getString(
                        MyConstant.wind_unit,
                        "meter_sec"
                    ) == "miles_hour"
                ) {
                    tv_wind_speed.text = getString(R.string.miles_hour)
                    var value = convertMetersPerSecondToMilesPerHour(weatherItem.wind.speed)
                    tv_wind.text = value
                } else {
                    tv_wind_speed.text = getString(R.string.meter_sec)
                    tv_wind.text = weatherItem.wind.speed.toString()
                }
            }

            "Fahrenheit" -> {                // Miles/hour
                if (sharedPreferences.getString(MyConstant.wind_unit, "meter_sec") == "meter_sec") {
                    tv_wind_speed.text = getString(R.string.meter_sec)
                    var value = convertMilesPerHourToMetersPerSecond(weatherItem.wind.speed)
                    tv_wind.text = value
                } else {
                    tv_wind_speed.text = getString(R.string.miles_hour)
                    tv_wind.text = weatherItem.wind.speed.toString()
                }
            }
        }
    }

    private fun convertMetersPerSecondToMilesPerHour(metersPerSecond: Double): String {
        val metersToMiles = 0.000621371
        val secondsToHours = 3600.0
        val result = metersPerSecond * metersToMiles * secondsToHours
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
        loading_view = view.findViewById(R.id.loading_view)

    }

    private fun submitTOHoursAdapterList(weatherItemList: List<WeatherItem>) {
        var hoursList = mutableListOf<WeatherItem>()
        for (x in 1 until 8) {
            hoursList.add(weatherItemList[x])
        }
        layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
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

    private fun getGpsLocationPermision() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {      // get permission for what i need
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), 5
            )
            return
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // sure at some point we stop request the permission
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestrefused++
            if (requestrefused < 2) {
                getGpsLocationPermision()
            } else {
                Toast.makeText(
                    requireContext(),
                    "sorry the app will can not work probably without location permission ",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(
                requireContext(),
                "Be sure you open your location",
                Toast.LENGTH_SHORT
            ).show()
            //check interent an get according
            getFreshLocation()
        }
    }

    @SuppressLint("MissingPermission")
    fun getFreshLocation() {
        locationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        val locationRequest: LocationRequest = LocationRequest.Builder(1000).apply {
            setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
        }.build()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult.lastLocation ?: return
                var   longitudinalValue = location.longitude
                var  latitudeValue = location.latitude
                if (isAdded) {
                    homeViewModel.getWeather(requireContext(), latitudeValue, longitudinalValue)
                    locationProviderClient.removeLocationUpdates(this)
                }
            }
        }
        locationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
    }

}
