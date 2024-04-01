package application.data.localDataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import application.model.Alert
import application.model.FavLocation
import application.model.WeatherResponse


@Database(entities = [FavLocation::class, WeatherResponse::class,Alert::class], version = 4)
@TypeConverters(WeatherResponseConverterCity::class, WeatherResponseConverterList::class)

abstract class AppDataBase : RoomDatabase() {
    abstract fun getLocationDao(): FavLocationsDao
    abstract fun getWeatherDao(): WeatherDAO
    abstract fun getAlertsDao(): AlertDao

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null
        fun getInstance(ctx: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    ctx.applicationContext, AppDataBase::class.java, "weather_database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}


