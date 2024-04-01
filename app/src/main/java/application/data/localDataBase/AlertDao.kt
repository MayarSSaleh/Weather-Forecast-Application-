package application.data.localDataBase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import application.model.Alert
import application.model.FavLocation
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDao {

    @Query("SELECT * FROM alerts_table")
    fun getAllAlerts(): Flow<List<Alert>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(alert: Alert): Long

    @Delete
    suspend fun deleteAlert(alert: Alert): Int


    @Query("DELETE FROM alerts_table WHERE typeOfAlarm= 'Notification'")
    suspend fun deleteALLNotification()

    @Query("DELETE FROM alerts_table WHERE typeOfAlarm= 'Alarm'")
    suspend fun deleteALLAlarms()


}