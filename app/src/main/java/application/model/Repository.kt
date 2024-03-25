 package application.model

import android.content.Context
import application.data.localDataBase.InterfaceLocalDataSource
import kotlinx.coroutines.flow.Flow
import application.data.localDataBase.LocalDataSource
import application.data.network.InterfaceRemoteDataSource
import application.data.network.RemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

 class Repository(
    private var productRemoteDataSource: InterfaceRemoteDataSource,
    private var productLocalDataSource: InterfaceLocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : InterfaceRepository {

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

    override suspend fun getWeather(
        latitude: Double,
        longitude: Double,
        units: String?,
        lang: String?
    ): Flow<WeatherResponse> {
        return productRemoteDataSource.getWeather(latitude, longitude, units, lang)
    }

    override fun getAllFavLocation(): Flow<List<FavLocation>> {
        return productLocalDataSource.getAllFavLocations()
    }

    // the following will be reflected directly to UI as i call get all fav after it so no need to handle the return
    override suspend fun deleteFavLocation(favLocation: FavLocation) {
        return productLocalDataSource.deleteFavLocation(favLocation)
    }

    override suspend fun insert(favLocation: FavLocation) {
        return productLocalDataSource.insertFavLocation(favLocation)
    }

    override suspend fun getLastWeather(): Flow<WeatherResponse> {
        return productLocalDataSource.getLestWeathear()
    }

    // the following in back process not get the return to user
    override suspend fun deleteLocation() {
        return productLocalDataSource.deleteWeather()
    }

    override suspend fun insertWeather(weatherResponse: WeatherResponse) {
        return productLocalDataSource.insertWeather(weatherResponse)
    }

}

