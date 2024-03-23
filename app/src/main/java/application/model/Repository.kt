package application.model

import android.content.Context
import application.data.localDataBase.InterfaceLocalDataSource
import kotlinx.coroutines.flow.Flow
import application.data.localDataBase.LocalDataSource
import application.data.network.InterfaceRemoteDataSource
import application.data.network.RemoteDataSource

class Repository(
    private var productRemoteDataSource: InterfaceRemoteDataSource,
    private var productLocalDataSource: InterfaceLocalDataSource
) {

    companion object {
        @Volatile
        private var INSTANCE: Repository? = null
        fun getInstance(context: Context): Repository {
            return INSTANCE ?: synchronized(this) {
                if (INSTANCE == null) {
                    val remoteDataSource = RemoteDataSource()
                    val localDataSource = LocalDataSource(context)
                    INSTANCE = Repository(remoteDataSource, localDataSource)
                }
                INSTANCE!!
            }
        }
    }

    suspend fun getWeather(
        latitude: Double,
        longitude: Double,
        units: String?,
        lang: String?
    ): Flow<WeatherResponse> {
        return productRemoteDataSource.getWeather(latitude, longitude, units, lang)
    }

    fun getAllLocalLocation(): Flow<List<FavLocation>> {
        return productLocalDataSource.getAllFavLocations()
    }

    // the following will be reflected directly to UI as i call get all fav after it so no need to handle the return
    suspend fun deleteFavLocation(favLocation: FavLocation) {
        return productLocalDataSource.deleteFavLocation(favLocation)
    }

    suspend fun insert(favLocation: FavLocation) {
        return productLocalDataSource.insertFavLocation(favLocation)
    }

    suspend fun getLastWeather(): Flow<WeatherResponse> {
        return productLocalDataSource.getLestWeathear()
    }

    // the following in back process not get the return to user
    suspend fun deleteLocation() {
        return productLocalDataSource.deleteWeather()
    }

    suspend fun insertWeather(weatherResponse: WeatherResponse) {
        return productLocalDataSource.insertWeather(weatherResponse)
    }

}

