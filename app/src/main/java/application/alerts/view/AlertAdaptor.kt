package application.alerts.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import application.model.Alert
import com.weather.application.databinding.AlertBinding

class AlertAdaptor() :
    ListAdapter<Alert, AlertAdaptor.AlertViewHolder>(AlertsDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AlertBinding.inflate(inflater, parent, false)
        return AlertViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        val current = getItem(position)
        holder.binding.tvAlertCityName.text = current.alertlocationName
        holder.binding.tvAlertAlertType.text = current.typeOfAlarm
        holder.binding.tvAlertData.text = current.day
        holder.binding.tvAlertTime.text = current.time
    }

    inner class AlertViewHolder(val binding: AlertBinding) :
        RecyclerView.ViewHolder(binding.root)
}


class AlertsDiffUtil : DiffUtil.ItemCallback<Alert>() {
    override fun areItemsTheSame(oldItem: Alert, newItem: Alert): Boolean {
        return oldItem.alertlocationName == newItem.alertlocationName
    }

    override fun areContentsTheSame(oldItem: Alert, newItem: Alert): Boolean {
        return oldItem == newItem
    }
}
