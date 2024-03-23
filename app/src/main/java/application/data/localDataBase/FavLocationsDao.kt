package application.data.localDataBase

import application.model.FavLocation
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavLocationsDao {

    @Query("SELECT * FROM locations_table")
    fun getAll(): Flow<List<FavLocation>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favLocation: FavLocation): Long

    @Delete
    suspend fun delete(favLocation: FavLocation): Int
}