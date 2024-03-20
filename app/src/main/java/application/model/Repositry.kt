package application.model

import android.content.Context
import android.util.Log
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import application.localDataBase.LocalDataSource
import application.network.RemoteDataSource

class Repositry private constructor(
    private val productRemoteDataSource: RemoteDataSource,
    private val productLocalDataSource: LocalDataSource
) {

    companion object {
        @Volatile
        private var INSTANCE: Repositry? = null
        fun getInstance(context: Context): Repositry {
            return INSTANCE ?: synchronized(this) {
                if (INSTANCE == null) {
                    val remoteDataSource = RemoteDataSource()
                    val localDataSource = LocalDataSource(context)
                    INSTANCE = Repositry(remoteDataSource, localDataSource)
                }
                INSTANCE!!
            }
        }
    }

    suspend fun getWeather(latitude: Double,longitude: Double, units: String?,lang: String?): Response<WeatherResponse> {
        return productRemoteDataSource.getWeather(latitude, longitude, units, lang)
    }

    fun getAllLocalLocation(): Flow<List<FavLocation>> {
        return productLocalDataSource.getAllFavLocations()
    }

    suspend fun deleteFavLocation(favLocation: FavLocation) {
        return productLocalDataSource.deleteFavLocation(favLocation)
    }
    suspend fun insert(favLocation: FavLocation) {
        return productLocalDataSource.insertFavLocation(favLocation)
    }

    suspend fun getWeathearToday(): WeatherResponse {
        Log.d("weather","in side get reeeeeeeeeeep")

        return productLocalDataSource.getWeathearToday()
    }

    suspend fun deleteLocation() {
        return productLocalDataSource.deleteWeather()
    }
    suspend fun insertWeather(weatherResponse: WeatherResponse) {
        return productLocalDataSource.insertWeather(weatherResponse)
    }



}

