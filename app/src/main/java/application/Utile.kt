package application

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import android.app.AlarmManager
import android.content.Context

@BindingAdapter("myImageUrl", "myError")
fun loadImage(view: ImageView, url: String?, myError: Drawable) {
    url.let {
        Picasso.get().load(it).error(myError).into(view)
    }
}

object MyConstant {

    const val alarmNumbers = "alarmNumbers"
    const val location_lon = "loc_lon"
    const val location_lat = "loc_lat"
    const val address = "address"
    const val SHARED_PREFS = "sharedPreferences"
    const val User_CURRENT_LOCATION = "user_location"

    const val wind_unit = "wind_unit"
    const val temp_unit = "temp_unit"
    const val lan = "lan"
    const val curentLanguage = "current lan"

    const val CHANNEL_ID: String = "CHANNEL_ID"

}
