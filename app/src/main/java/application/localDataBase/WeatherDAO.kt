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
    //So, the insert method returns the row ID(s) of the inserted item(s)
    // or -1 if an error occurred during the insertion process.
    @Query("DELETE FROM today_weather_table")
    suspend fun delete(): Int
    //If the deletion is successful, it returns the number of rows deleted from the database table.
    //If no rows are deleted (e.g., because there were no matching rows in the table), it returns 0.
    //If an error occurs during the deletion operation, it might return an error code or throw an exception.
}