package application.fav.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import application.fav.viewModel.Communication
import application.home.view.HomeFragment
import application.model.FavLocation
import com.weather.application.databinding.FavItemBinding

class FavouriteAdaptor(
    private val context: Context, private val listener: (FavLocation) -> Unit,
    private val openIt: (FavLocation) -> Unit
) :
    ListAdapter<FavLocation, FavouriteAdaptor.LocationViewHolder>(LocationDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FavItemBinding.inflate(inflater, parent, false)
        return LocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val current = getItem(position)
        holder.binding.locationName.text = current.locationName
        holder.binding.locationName.setOnClickListener {openIt(current)}
        holder.binding.btnRemoveFavLocation.setOnClickListener { listener(current) }
    }

    inner class LocationViewHolder(val binding: FavItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}

class LocationDiffUtil : DiffUtil.ItemCallback<FavLocation>() {

    override fun areItemsTheSame(oldItem: FavLocation, newItem: FavLocation): Boolean {
        return oldItem.locationName == newItem.locationName
    }

    override fun areContentsTheSame(oldItem: FavLocation, newItem: FavLocation): Boolean {
        return oldItem == newItem
    }
}
