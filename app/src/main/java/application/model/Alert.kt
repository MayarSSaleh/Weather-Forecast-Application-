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
    var alertlocationName: String,
    @NonNull
    var alertlongitude: Double,
    @NonNull
    var alertlatitude: Double,
    @NonNull
    var day: String,
    @NonNull
    var time: String,
    var typeOfAlarm: String// notification or  alarm .

) : Parcelable

