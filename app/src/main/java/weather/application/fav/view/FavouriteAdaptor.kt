package weather.application.fav.view

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import weather.application.R
import weather.application.model.FavLocation

class FavouriteAdaptor (private val context: Context): ListAdapter<FavLocation, LocationViewHolder>(LocationDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fav_item, parent, false)
        return LocationViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val current = getItem(position)
        holder.location_name.text = current.locationName
    }
}

class LocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val location_name: TextView = itemView.findViewById(R.id.location_name)
}

class LocationDiffUtil : DiffUtil.ItemCallback<FavLocation>() {

    override fun areItemsTheSame(oldItem: FavLocation, newItem: FavLocation): Boolean {
        return oldItem.locationName == newItem.locationName
    }

    override fun areContentsTheSame(oldItem: FavLocation, newItem: FavLocation): Boolean {
        return oldItem == newItem
    }
}
