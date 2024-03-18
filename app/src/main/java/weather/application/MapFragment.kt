package weather.application

import android.app.AlertDialog
import android.content.SharedPreferences
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import weather.application.setting.SettingFragment

class MapFragment(private var mapButtonId: String, private var checkedId: Int) : Fragment(),
    OnMapReadyCallback {

    private lateinit var rootView: View
    private lateinit var myMap: GoogleMap
    private lateinit var mMapView: MapView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mylocation: Button
    private lateinit var addToFav: Button
    private lateinit var geocoder: Geocoder
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    private var latitude = 0.0
    private var longitude = 0.0

    private var theAddress: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_map2, container, false)
        mylocation = rootView.findViewById(R.id.set_as_my_Location)
        addToFav = rootView.findViewById(R.id.btn_addToFav)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        geocoder = Geocoder(requireContext())
        sharedPreferences = context?.getSharedPreferences(MyConstant.SHARED_PREFS, 0)!!
        editor = sharedPreferences.edit()
        theAddress = getString(R.string.Not_selected_place_yet)

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
            Log.d("map", "inside on clickkkkkkkkkk")
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Conformation")
                .setMessage("Make $theAddress is your location?")
                .setPositiveButton("OK") { dialog, id ->
                    // i changed to map we conformed to location
                    editor.putInt(mapButtonId, checkedId)
                    editor.putInt("mapButtonId",1)// to update the ui but it
                    editor.apply()
                    editor.putString(MyConstant.location, "Map")
                    editor.putString(MyConstant.address, "$theAddress")
                    editor.putString(MyConstant.latitude, "$latitude")
                    editor.putString(MyConstant.longitude, "$longitude")
                    editor.apply()
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.added_as_your_current_location), Toast.LENGTH_LONG
                    ).show()

                }
            val dialog = builder.create()
            dialog.show()
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        myMap = googleMap
        myMap.uiSettings.isZoomControlsEnabled = true
        myMap.setOnMapClickListener { click ->
            // Get the latitude and longitude of the clicked position
            latitude = click.latitude
            longitude = click.longitude
            Log.d("TAG", "onMapReady:lattttttt $latitude and longgggggg: $longitude")
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            theAddress =
                addresses?.firstOrNull()?.getAddressLine(0) ?: "Sorry, Address not found"
            Toast.makeText(requireContext(), "The location is $theAddress", Toast.LENGTH_LONG)
                .show()
            Log.d("TAG", "onMapReady:ADDRES $theAddress")
        }
    }
    /*
        // not need it as
        private fun getcurrentLo() {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    1
                )
                return
            }

            myMap.isMyLocationEnabled = true

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        val currentLatLng = LatLng(location.latitude, location.longitude)
                        myMap.addMarker(MarkerOptions().position(currentLatLng).title("My Location"))
                        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                    } else {
                        Toast.makeText(requireContext(), "Error getting location}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        requireContext(),
                        "Error getting location: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

        }

        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            if (requestCode == 1) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onMapReady(myMap)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Location permission denied, so sorry we can not open the map",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }*/
}
