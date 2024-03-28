package application.alerts.view

import android.app.AlertDialog
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import application.MapFragment
import application.alerts.viewModel.AlertViewModel
import application.alerts.viewModel.AlertViewModelFactory
import application.model.LocalStateAlerts
import application.model.Repository
import com.weather.application.R
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AlertFragment : Fragment() {
    private lateinit var add: ImageView
    private lateinit var img_stop: ImageView

    private lateinit var alertRecycler: RecyclerView
    private lateinit var alertAdaptor: AlertAdaptor
    private lateinit var alertFactory: AlertViewModelFactory
    private lateinit var viewModel: AlertViewModel
    private lateinit var layoutManager: LinearLayoutManager
    lateinit var loading_view: ProgressBar
    lateinit var stopNotifcation: ImageView
    private val CHANNEL_ID: String = "CHANNEL_ID"


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_alert, container, false)
        Ui(view)
        setOnClickLisenter()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val notificationManager =
                requireContext().getSystemService(NotificationManager::class.java) as NotificationManager
            if (notificationManager.areNotificationsEnabled()) {
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERM
                )
            }
        }
        lifecycleScope.launch {
            viewModel.alertsList.collectLatest {
                when (it) {
                    is LocalStateAlerts.LoadingLocaAlertl -> {
                        loading_view.visibility = View.VISIBLE
                    }

                    is LocalStateAlerts.SuccessLocalAlert -> {
                        alertAdaptor.submitList(it.data)
                        loading_view.visibility = View.GONE
                    }

                    else -> {
                        loading_view.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            "Sorry Can not get them}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        return view
    }

    private fun Ui(view: View) {
        add = view.findViewById(R.id.addAlert)
        alertFactory = AlertViewModelFactory(Repository.getInstance(requireContext()))
        viewModel = ViewModelProvider(this, alertFactory).get(AlertViewModel::class.java)
        alertRecycler = view.findViewById(R.id.alert_recycler)
        loading_view = view.findViewById(R.id.alert_loading_view)
        loading_view.visibility = View.GONE
        img_stop = view.findViewById(R.id.img_stop)
        stopNotifcation = view.findViewById(R.id.btn_stopNotification)
        alertAdaptor = AlertAdaptor()

        layoutManager = LinearLayoutManager(requireContext())
        alertRecycler.adapter = alertAdaptor
        alertRecycler.layoutManager = layoutManager
    }


    fun setOnClickLisenter() {

        add.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragment_container, MapFragment("", 5))
            transaction?.addToBackStack(null)
            transaction?.commit()
        }

        img_stop.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Conformation")
                .setMessage("Are your stop the alarms?")
                .setPositiveButton("OK") { dialog, id ->
                    viewModel.deleteALL()// no implmenation yet
                    Toast.makeText(
                        requireContext(),
                        "The alarms is gone", Toast.LENGTH_SHORT
                    ).show()
                }
            val dialog = builder.create()
            dialog.show()
        }
        
        stopNotifcation.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Conformation")
                .setMessage("Are your stop the Notifications?")
                .setPositiveButton("OK") { dialog, id ->
                    NotificationManagerCompat.from(requireContext()).cancel(NOTIFICATION_PERM)
                    Toast.makeText(
                        requireContext(),
                        "The Notification is gone", Toast.LENGTH_SHORT
                    ).show()
                }
            val dialog = builder.create()
            dialog.show()
        }
    }

}