package weather.application.home.view

import android.content.Context
import android.content.SharedPreferences
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
import com.squareup.picasso.Picasso
import weather.application.MyConstant
import weather.application.R
import weather.application.model.WeatherItem
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class DaysAdaptor (private val context: Context): ListAdapter<WeatherItem, DaysViewHolder>(DaysDiffUtil()) {
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaysViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.day_item, parent, false)

        return DaysViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: DaysViewHolder, position: Int) {
        val current = getItem(position)
        holder.temp.text = current.main.temp.toString()
        Picasso.get()
            .load("https://openweathermap.org/img/wn/" + current.weather.get(0).icon + "@2x.png")
            .into(holder.icon)
        holder.dayWeatherDesc.text = current.weather.get(0).description
        val dateTime =
            LocalDateTime.ofInstant(Instant.ofEpochSecond(current.dt.toLong()), ZoneOffset.UTC)
        sharedPreferences = context?.getSharedPreferences(MyConstant.SHARED_PREFS, 0)!!
        var selectedLanguage = sharedPreferences.getString(MyConstant.lan, "en")
        if (selectedLanguage == "ar") {
            holder.tv_day.text = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("ar"))
        }
        else {
            holder.tv_day.text = dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
        }

    }
}

class DaysViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tv_day: TextView = itemView.findViewById(R.id.tv_day)
    val icon: ImageView = itemView.findViewById(R.id.image_day_icon)
    val dayWeatherDesc: TextView = itemView.findViewById(R.id.tv_day_weather_des)
    val temp: TextView = itemView.findViewById(R.id.tv_day_tem)
}

class DaysDiffUtil : DiffUtil.ItemCallback<WeatherItem>() {
    override fun areItemsTheSame(oldItem: WeatherItem, newItem: WeatherItem): Boolean {
        return oldItem.dt_txt == newItem.dt_txt
    }

    override fun areContentsTheSame(oldItem: WeatherItem, newItem: WeatherItem): Boolean {
        return oldItem == newItem
    }
}
