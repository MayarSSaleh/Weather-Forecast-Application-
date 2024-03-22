package application.localDataBase

import application.model.FavLocation
import android.content.Context
import application.model.WeatherResponse
import kotlinx.coroutines.flow.Flow


class LocalDataSource(var context: Context) {

    private val locationsDao: FavLocationsDao by lazy {
        AppDataBase.getInstance(context).getLocationDao()
    }

    private val WeatherDao: WeatherDAO by lazy {
        AppDataBase.getInstance(context).getWeatherDao()
    }

    suspend fun insertWeather(weatherResponse: WeatherResponse) {
        WeatherDao.insert(weatherResponse)
    }

    suspend fun deleteWeather() {
        WeatherDao.delete()
    }

    suspend fun getWeathearToday():  Flow<WeatherResponse> {

        return WeatherDao.getTodayWeather()
    }

    suspend fun insertFavLocation(favLocation: FavLocation) {
        locationsDao.insert(favLocation)
    }

    suspend fun deleteFavLocation(favLocation: FavLocation) {
        locationsDao.delete(favLocation)
    }

    fun getAllFavLocations(): Flow<List<FavLocation>> {
        return locationsDao.getAll()
    }
}