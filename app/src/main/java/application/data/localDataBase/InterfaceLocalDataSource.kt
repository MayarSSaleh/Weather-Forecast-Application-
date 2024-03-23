package application.data.localDataBase

import application.model.FavLocation
import application.model.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface InterfaceLocalDataSource {
    suspend fun insertWeather(weatherResponse: WeatherResponse)

    suspend fun deleteWeather()

    suspend fun getLestWeathear(): Flow<WeatherResponse>

    suspend fun insertFavLocation(favLocation: FavLocation)

    suspend fun deleteFavLocation(favLocation: FavLocation)
    fun getAllFavLocations(): Flow<List<FavLocation>>
}