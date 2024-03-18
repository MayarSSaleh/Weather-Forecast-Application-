package weather.application.localDataBase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import weather.application.model.WeatherResponse

@Dao
interface WeatherDAO {
    @Query("SELECT * FROM today_weather_table")
    fun getAll(): Flow<WeatherResponse>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(weatherResponse: WeatherResponse): Long

    @Delete
    suspend fun delete(): Int
}