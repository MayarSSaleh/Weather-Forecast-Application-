package application.model

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "alerts_table")
@Parcelize
data class Alert(
    @PrimaryKey
    @NonNull
    val alertlocationName: String,
    @NonNull
    val alertlongitude: Double,
    @NonNull
    val alertlatitude: Double,
    @NonNull
    val day: String,
    @NonNull
    val time: String,
    val typeOfAlarm: String// notification or  alarm .

) : Parcelable

