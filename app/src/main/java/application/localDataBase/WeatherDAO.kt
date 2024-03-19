package application.localDataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import application.model.WeatherResponse

@Dao
interface WeatherDAO {
    @Query("SELECT * FROM today_weather_table")
    fun getTodayWeather(): WeatherResponse

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(weatherResponse: WeatherResponse): Long

    @Query("DELETE FROM today_weather_table")
    suspend fun delete(): Int
}