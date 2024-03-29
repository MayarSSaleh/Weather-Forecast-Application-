package application.data.localDataBase

import application.model.Alert
import application.model.FavLocation
import application.model.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface InterfaceLocalDataSource {
    suspend fun insertWeather(weatherResponse: WeatherResponse)

    suspend fun deleteWeather()

    suspend fun getLestWeathear(): Flow<WeatherResponse>

    suspend fun insertAlert(favLocation: FavLocation)

    suspend fun deleteFavLocation(favLocation: FavLocation)
    fun getAllFavLocations(): Flow<List<FavLocation>>

    fun getAllAlerts(): Flow<List<Alert>>

    suspend fun insertAlert(alert: Alert)
    suspend fun deleteALLNotification()
    suspend fun deleteALLAlarms()

}