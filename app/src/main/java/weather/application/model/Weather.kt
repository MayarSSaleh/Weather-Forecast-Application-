package weather.application.model

import java.io.Serializable

data class Weather(
    private var dataAndTime: Long,
    private var main: Main,
    private var dataTime: Long
    private var dataTime: Long
    private var dataTime: Long
    private var dataTime: Long
    private var dataTime: Long
) : Serializable

data class Main(
    var temp: Double,
    var pressure: Int,
    var seaLevel: Int,
    var humidity: Int,
)
The current temperature
● Current date
● Current time
● Humidity
● Wind speed
● Pressure
● Clouds
● Icon (suitable to the weather status)
● Weather description (clear sky, light rain ... etc.)

● All the past hourly for the current date
● All past features for 5 days
}

class WeatherResponse {
    lateinit var city: String

    var list = listOf<Weather>()

}

/*
*  class WeatherResponse{
     var meals: ArrayList<Weather>? = null

 }
 * */
