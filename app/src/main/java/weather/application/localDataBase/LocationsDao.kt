package weather.application.localDataBase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import weather.application.model.FavLocation

@Dao
interface LocationsDao {

    @Query("SELECT * FROM locations_table")
    fun getAll(): Flow<List<FavLocation>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favLocation: FavLocation): Long

    @Delete
    suspend fun delete(favLocation: FavLocation): Int
}