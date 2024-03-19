package application.home.view

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import application.model.WeatherItem
import com.squareup.picasso.Picasso
import com.weather.application.R
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class HoursAdapterList : ListAdapter<WeatherItem, HoursViewHolder>(HoursDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoursViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hour_item, parent, false)
        return HoursViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)//@RequiresApi(Build.VERSION_CODES.O) is an annotation used in Android development to specify that a particular method or class requires a certain API level to be available at runtime.
    override fun onBindViewHolder(holder: HoursViewHolder, position: Int) {
        val current = getItem(position)
        holder.next3hTemp.text = current.main.temp.toString()
        Picasso.get().load("https://openweathermap.org/img/wn/" + current.weather.get(0).icon + "@2x.png").into(holder.icon)
// get the time by 12 hrs am or pm
        val instant = Instant.ofEpochSecond(current.dt.toLong())
        val formatter = DateTimeFormatter.ofPattern("hh a", Locale.ENGLISH).withZone(ZoneId.of("UTC"))// for 12 hrs miro
        //val formatter = DateTimeFormatter.ofPattern("HH").withLocale(Locale.ENGLISH).withZone(ZoneId.of("UTC"))// for 24 hr
        val formattedTime = formatter.format(instant)
        holder.hour.text = formattedTime
    }
}

class HoursViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val hour: TextView = itemView.findViewById(R.id.tv_day)
    val icon: ImageView = itemView.findViewById(R.id.image_day_icon)
    val next3hTemp: TextView = itemView.findViewById(R.id.next3hTemp)
}

class HoursDiffUtil : DiffUtil.ItemCallback<WeatherItem>() {
    override fun areItemsTheSame(oldItem: WeatherItem, newItem: WeatherItem): Boolean {
        return oldItem.dt_txt == newItem.dt_txt
    }

    override fun areContentsTheSame(oldItem: WeatherItem, newItem: WeatherItem): Boolean {
        return oldItem == newItem
    }
}
