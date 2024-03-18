package weather.application.localDataBase

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import weather.application.model.WeatherResponse


class WeatherResponseConverter {
    @TypeConverter
    fun fromJson(json: String): WeatherResponse {
        val type = object : TypeToken<WeatherResponse>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toJson(weatherResponse: WeatherResponse): String {
        return Gson().toJson(weatherResponse)
    }
}

