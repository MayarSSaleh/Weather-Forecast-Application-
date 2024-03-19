package application.model

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import application.localDataBase.WeatherResponseConverterCity
import application.localDataBase.WeatherResponseConverterList
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "locations_table")
@Parcelize
data class FavLocation(
    @PrimaryKey
    @NonNull
    val locationName: String,
    @NonNull
    val longitude: Double,
    @NonNull
    val latitude: Double
) : Parcelable

@Entity(tableName = "today_weather_table")
@TypeConverters(WeatherResponseConverterCity::class, WeatherResponseConverterList::class)
data class WeatherResponse(
    @PrimaryKey
    @NonNull
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
data class Main(
    val temp: Double,
    val pressure: Int,
    val humidity: Int,
)
data class weather(
    val description: String,
    val icon: String
)
data class Clouds(val all: Int)
data class Wind(val speed: Double)
data class City(val name: String)