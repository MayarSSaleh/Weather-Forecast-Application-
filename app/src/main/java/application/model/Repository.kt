package application.model

import application.data.localDataBase.InterfaceLocalDataSource
import kotlinx.coroutines.flow.Flow
import application.data.network.InterfaceRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class Repository private constructor(
    private var productRemoteDataSource: InterfaceRemoteDataSource,
    private var productLocalDataSource: InterfaceLocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : InterfaceRepository {


//    companion object {
//        @Volatile
//        private var INSTANCE: Repository? = null
//        fun getInstance(context: Context): Repository {
//            return INSTANCE ?: synchronized(this) {
//                if (INSTANCE == null) {
//                    val remoteDataSource = RemoteDataSource(
//                        RetrofitHelper.retrofit.create(WeatherService::class.java)
//                    )
//                    val localDataSource = LocalDataSource(
//                        AppDataBase.getInstance(context).getWeatherDao(),
//                        AppDataBase.getInstance(context).getLocationDao(),
//                        AppDataBase.getInstance(context).getAlertsDao()
//                    )
//                    INSTANCE = Repository(remoteDataSource, localDataSource)
//                }
//                INSTANCE!!
//            }
//        }
//    }

companion object {
    @Volatile
    private var INSTANCE: Repository? = null
    fun getInstance(
         productRemoteDataSource: InterfaceRemoteDataSource,
        productLocalDataSource: InterfaceLocalDataSource,
        ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    ): Repository {
        return INSTANCE ?: synchronized(this) {
            if (INSTANCE == null) {
                INSTANCE = Repository(productRemoteDataSource, productLocalDataSource)
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
        return productLocalDataSource.insertAlert(favLocation)
    }

    override suspend fun getLastWeather(): Flow<WeatherResponse> {
        return productLocalDataSource.getLestWeather()
    }

    // the following in back process not get the return to user
    override suspend fun deleteLocation() {
        return productLocalDataSource.deleteWeather()
    }

    override suspend fun insertWeather(weatherResponse: WeatherResponse) {
        return productLocalDataSource.insertWeather(weatherResponse)
    }


    override fun getAllAlerts(): Flow<List<Alert>> {
        return productLocalDataSource.getAllAlerts()
    }

    override suspend fun insertAlert(alert: Alert) {
        productLocalDataSource.insertAlert(alert)
    }

    override suspend fun deleteALLNotification() {
        productLocalDataSource.deleteALLNotification()
    }

    override suspend fun deleteALLAlarms() {
        productLocalDataSource.deleteALLAlarms()
    }

    override suspend fun deleteAlert(alert: Alert) {
        productLocalDataSource.deleteAlert(alert)
    }
}

