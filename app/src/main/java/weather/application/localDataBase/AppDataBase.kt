package weather.application.localDataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import weather.application.model.FavLocation
import weather.application.model.WeatherResponse


@Database(entities = [FavLocation::class, WeatherResponse::class], version = 1)
@TypeConverters(WeatherResponseConverter::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun getLocationDao(): LocationsDao
    abstract fun getWeatherDao(): WeatherDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null
        fun getInstance(ctx: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(ctx.applicationContext, AppDataBase::class.java, "mydatabase")
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}