package weather.application.localDataBase

import android.content.Context
import kotlinx.coroutines.flow.Flow
import weather.application.model.FavLocation
import weather.application.model.WeatherResponse

class LocalDataSource(var context: Context) {

    private val locationsDao: LocationsDao by lazy {
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

    fun getWeathearToday(): Flow<WeatherResponse> {
        return WeatherDao.getAll()
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