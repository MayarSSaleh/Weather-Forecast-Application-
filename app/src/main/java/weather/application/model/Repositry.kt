package weather.application.model

import android.content.Context
import retrofit2.Response
import weather.application.localDataBase.LocalDataSource
import weather.application.network.RemoteDataSource

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

    suspend fun getWeather(latitude:Double , longitude:Double,units:String?,lang: String?) : Response<WeatherResponse> {
        return productRemoteDataSource.getWeather(latitude,longitude,units, lang)
    }

}

