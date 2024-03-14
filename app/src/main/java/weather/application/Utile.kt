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