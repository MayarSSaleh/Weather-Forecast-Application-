package application.model

import kotlinx.coroutines.flow.Flow

interface InterfaceRepository {
    suspend fun getWeather(
        latitude: Double,
        longitude: Double,
        units: String?,
        lang: String?
    ): Flow<WeatherResponse>

    fun getAllFavLocation(): Flow<List<FavLocation>>
     suspend fun deleteAlert(alert: Alert)

    // the following will be reflected directly to UI as i call get all fav after it so no need to handle the return
    suspend fun deleteFavLocation(favLocation: FavLocation)

    suspend fun insert(favLocation: FavLocation)

    suspend fun getLastWeather(): Flow<WeatherResponse>

    // the following in back process not get the return to user
    suspend fun deleteLocation()

    suspend fun insertWeather(weatherResponse: WeatherResponse)

    fun getAllAlerts(): Flow<List<Alert>>
    suspend fun insertAlert(alert: Alert)
    suspend fun deleteALLNotification()
    suspend fun deleteALLAlarms()
}