package application.data.localDataBase

import android.util.Log
import application.model.FavLocation
import application.model.Alert
import application.model.WeatherResponse
import kotlinx.coroutines.flow.Flow


class LocalDataSource(
    private val weatherDAO: WeatherDAO,
    private val favLocationsDao: FavLocationsDao,
    private val alertDao: AlertDao
) : InterfaceLocalDataSource {

    override suspend fun insertWeather(weatherResponse: WeatherResponse) {
        weatherDAO.insert(weatherResponse)
    }

    override suspend fun deleteWeather() {
        weatherDAO.delete()
    }


    override fun getAllFavLocations(): Flow<List<FavLocation>> {
        return favLocationsDao.getAll()
    }

    override suspend fun insertAlert(favLocation: FavLocation) {
        favLocationsDao.insert(favLocation)
    }

    override suspend fun deleteFavLocation(favLocation: FavLocation) {
        favLocationsDao.delete(favLocation)
    }


    override suspend fun getLestWeathear(): Flow<WeatherResponse> {
        return weatherDAO.getTodayWeather()
    }


    override fun getAllAlerts(): Flow<List<Alert>> {
        return alertDao.getAllAlerts()
    }

    override suspend fun deleteALLNotification() {
        Log.d("t", "deleteALLNotification ")

        alertDao.deleteALLNotification()
    }

    override suspend fun deleteALLAlarms() {
        Log.d("t", "deeeeeeeeeeeeelet alermsss alarm ")

        alertDao.deleteALLAlarms()
    }

    override suspend fun deleteAlert(alert: Alert) {

        alertDao.deleteAlert(alert)
    }

    override suspend fun insertAlert(alert: Alert) {
        alertDao.insert(alert)
    }


}