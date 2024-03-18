package weather.application

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

@BindingAdapter("myImageUrl", "myError")
fun loadImage(view: ImageView, url: String?, myError: Drawable) {
    url.let {
        Picasso.get().load(it).error(myError).into(view)
    }
}
object MyConstant{
    const val SHARED_PREFS = "sharedPreferences"
    const val  location="location"
    const val  wind_unit="wind_unit"
    const val  temp_unit="temp_unit"
    const val  lan="lan"
    const val curentLanguage ="current lan"
}