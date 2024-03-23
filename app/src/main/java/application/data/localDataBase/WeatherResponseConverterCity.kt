package application.data.localDataBase


import androidx.room.TypeConverter
import application.model.City
import application.model.WeatherItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class WeatherResponseConverterCity {
    @TypeConverter
    fun fromJson(json: String): City {
        val type = object : TypeToken<City>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toJson(city: City): String {
        return Gson().toJson(city)
    }
}

class WeatherResponseConverterList {
    @TypeConverter
    fun fromJson(json: String): List<WeatherItem> {
        val type = object : TypeToken<List<WeatherItem>>() {}.type
        return Gson().fromJson(json, type)
    }

    @TypeConverter
    fun toJson(list: List<WeatherItem>): String {
        return Gson().toJson(list)
    }
}

