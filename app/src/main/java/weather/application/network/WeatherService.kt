package weather.application.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import weather.application.model.WeatherResponse

interface WeatherService {

    val APIKey: String
        get() = "7a6155686334997733d302ec7ac0544b"

//    api.openweathermap.org/data/2.5/forecast?lat={lat}&lon={lon}&appid={API key}

    @GET("forecast?")
    suspend fun getWeatherDetails(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Response<WeatherResponse>

    @GET("lat={lat}&lon={lon}&appid=APIKey")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Response<WeatherResponse>


//    @Query("lat") lat: Double,
//    @Query("lon") lon: Double,
//    @Query("units") units: String,
//    @Query("exclude") exclude: String="minutely",
//    @Query("lang") lang: String

}
