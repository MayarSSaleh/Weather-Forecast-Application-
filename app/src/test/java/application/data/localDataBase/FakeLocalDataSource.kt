package application.data.localDataBase

import application.model.Alert
import application.model.FavLocation
import application.model.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

// these parameter will be my dataBase i will make the function deal with it, it available for all class,
// sent as parameter when create object to be different according the object and how create it and use it

class FakeLocalDataSource(
    private var weatherResponsList: MutableList<WeatherResponse>? = mutableListOf(),
    private var favLocations: MutableList<FavLocation>? = mutableListOf()
) : InterfaceLocalDataSource {

    override suspend fun deleteWeather() {
        weatherResponsList?.clear()
    }


    override suspend fun insertWeather(weatherResponse: WeatherResponse) {
        weatherResponsList?.add(weatherResponse)
    }

    override suspend fun getLestWeathear(): Flow<WeatherResponse> {
        return flow {
            // Create a copy of the list to avoid exposing the mutable reference
            val weatherListCopy = weatherResponsList?.toList() ?: emptyList()
            emitAll(weatherListCopy.asFlow())
        }
    }

    override suspend fun insertAlert(favLocation: FavLocation) {
        favLocations?.add(favLocation)
    }

    override suspend fun insertAlert(alert: Alert) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFavLocation(favLocation: FavLocation) {
        favLocations?.remove(favLocation)
    }

    override fun getAllFavLocations(): Flow<List<FavLocation>> {
        return flow {
            emit(favLocations ?: emptyList())
        }
    }

    override fun getAllAlerts(): Flow<List<Alert>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteALLNotification() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteALLAlarms() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlert(alert: Alert) {
        TODO("Not yet implemented")
    }
}