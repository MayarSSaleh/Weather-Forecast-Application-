package application.data.localDataBase

import application.model.FavLocation
import android.content.Context
import application.model.WeatherResponse
import kotlinx.coroutines.flow.Flow


class LocalDataSource(var context: Context) : InterfaceLocalDataSource {

    private val locationsDao: FavLocationsDao by lazy {
        AppDataBase.getInstance(context).getLocationDao()
    }

    private val WeatherDao: WeatherDAO by lazy {
        AppDataBase.getInstance(context).getWeatherDao()
    }

    override suspend fun insertWeather(weatherResponse: WeatherResponse) {
        WeatherDao.insert(weatherResponse)
    }

    override suspend fun deleteWeather() {
        WeatherDao.delete()
    }



    override suspend fun insertFavLocation(favLocation: FavLocation) {
        locationsDao.insert(favLocation)
    }

    override suspend fun deleteFavLocation(favLocation: FavLocation) {
        locationsDao.delete(favLocation)
    }

    override fun getAllFavLocations(): Flow<List<FavLocation>> {
        return locationsDao.getAll()
    }

    override suspend fun getLestWeathear(): Flow<WeatherResponse> {
        return WeatherDao.getTodayWeather()
    }
}