package weather.application.model


data class WeatherResponse(
    val list: List<WeatherItem>,
    val city: City
)

data class WeatherItem(
    val dt: String,
    val main: Main,
    val weather: List<weather>,
    val clouds: Clouds,
    val wind: Wind,
    val dt_txt: String,
)

data class weather(
    val description: String,
    val icon: String
)

data class Clouds(val all: Int)

data class Wind(val speed: Double)


data class City(private val name: String)

data class Main(
    val temp: Double,
    val pressure: Int,
    val humidity: Int,
)

/*The current temperature
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
*/