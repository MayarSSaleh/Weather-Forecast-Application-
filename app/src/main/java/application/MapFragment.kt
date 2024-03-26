package application

import application.model.FavLocation
import android.app.AlertDialog
import android.content.SharedPreferences
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import application.alerts.view.AlertDialogFragment
import application.alerts.viewModel.AlertViewModel
import application.alerts.viewModel.AlertViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import application.fav.viewModel.FavViewModel
import application.fav.viewModel.FavViewModelFactory
import application.model.Alert
import application.model.LocalStateAlerts
import application.model.Repository
import application.model.SetAlert
import com.weather.application.R
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MapFragment(
    private var mapButtonIdReferTOKEY_LOCATION_RADIO_BUTTON_ID: String,
    private var checkedId: Int
) : Fragment(), OnMapReadyCallback {


    private lateinit var rootView: View
    private lateinit var myMap: GoogleMap
    private lateinit var mMapView: MapView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mylocation: Button
    private lateinit var addToFav: Button
    private lateinit var geocoder: Geocoder
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    private lateinit var favFactory: FavViewModelFactory
    private lateinit var viewModel: FavViewModel
    private lateinit var alert: ImageView
    private var latitude = 0.0
    private var longitude = 0.0
    private var theAddress = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_map2, container, false)
        mylocation = rootView.findViewById(R.id.set_as_my_Location)
        addToFav = rootView.findViewById(R.id.btn_addToFav)
        alert = rootView.findViewById(R.id.img_alert_mapScreen)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        geocoder = Geocoder(requireContext())
        sharedPreferences = context?.getSharedPreferences(MyConstant.SHARED_PREFS, 0)!!
        editor = sharedPreferences.edit()
        theAddress = getString(R.string.Not_selected_place_yet)
        favFactory = FavViewModelFactory(Repository.getInstance(requireContext()))
        viewModel = ViewModelProvider(this, favFactory).get(FavViewModel::class.java)


        try {
            MapsInitializer.initialize(requireActivity())
            mMapView = rootView.findViewById(R.id.map)
            mMapView.onCreate(savedInstanceState)
            mMapView.getMapAsync(this)
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                "Error getting location: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
        setOnClickListener()
        return rootView
    }

    private fun setOnClickListener() {
        mylocation.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Conformation")
                .setMessage("Make $theAddress is your location?")
                .setPositiveButton("OK") { dialog, id ->
                    // i changed to map we conformed to location
                    editor.putInt(mapButtonIdReferTOKEY_LOCATION_RADIO_BUTTON_ID, checkedId)
                    editor.putString(MyConstant.User_CURRENT_LOCATION, "isMap")
                    editor.putString(MyConstant.address, "$theAddress")
                    editor.putString(MyConstant.location_lat, "$latitude")
                    editor.putString(MyConstant.location_lon, "$longitude")
                    editor.apply()
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.added_as_your_current_location),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            val dialog = builder.create()
            dialog.show()
        }
        addToFav.setOnClickListener {
            if (longitude != 0.0 && latitude != 0.0) {
                var favLocation = FavLocation(theAddress, longitude, latitude)
                viewModel.insertFavLocation(favLocation)
                Toast.makeText(
                    requireContext(),
                    "The country is added to your favorites",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        alert.setOnClickListener {
            if (theAddress != getString(R.string.Not_selected_place_yet)) {
                val dialogFragment = AlertDialogFragment(theAddress,longitude,latitude)
                dialogFragment.setTargetFragment(this, 0)
                dialogFragment.show(parentFragmentManager, "CustomDialog")
            } else {
                Toast.makeText(
                    requireContext(),
                    "Choose country first",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        myMap = googleMap
        myMap.uiSettings.isZoomControlsEnabled = true
        myMap.setOnMapClickListener { click ->
            // Get the latitude and longitude of the clicked position
            latitude = click.latitude
            longitude = click.longitude
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            theAddress =
                addresses?.firstOrNull()?.getAddressLine(0) ?: "Sorry, Address not found"
            Toast.makeText(requireContext(), "The location is $theAddress", Toast.LENGTH_SHORT)
                .show()

        }
    }


//        viewModelAlert.insertALert(Alert(theAddress,longitude,latitude,selectedDay,selectedTime,alarmType))

}
