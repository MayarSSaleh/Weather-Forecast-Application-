package application.map.sharedMap

import application.model.FavLocation
import android.app.AlertDialog
import android.content.SharedPreferences
import android.location.Geocoder
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import application.MyConstant
import application.alerts.view.AlertDialogFragment
import application.data.localDataBase.AppDataBase
import application.data.localDataBase.LocalDataSource
import application.data.network.RemoteDataSource
import application.data.network.RetrofitHelper
import application.data.network.WeatherService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import application.favorite.viewModel.FavViewModel
import application.favorite.viewModel.FavViewModelFactory
import application.model.Repository
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.textfield.TextInputLayout
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

    private lateinit var searchTextInputLayout: TextInputLayout
    private lateinit var searchEditText: AutoCompleteTextView

    private val countries = listOf(
        Pair("Amsterdam", Pair(52.3676, 4.9041)),
        Pair("Athens", Pair(37.9838, 23.7275)),
        Pair("Austin", Pair(30.2672, -97.7431)),
        Pair("Sydney", Pair(-33.8688, 151.2093)),
        Pair("Cairo", Pair(30.0444, 31.2357)),
        Pair("Cape Town", Pair(-33.9249, 18.4241)),
        Pair("Santiago", Pair(-33.4489, -70.6693)),
        Pair("Beijing", Pair(39.9042, 116.4074)),
        Pair("Rabat", Pair(34.020882, -6.841650)),
        Pair("Doha", Pair(25.276987, 51.520069)),
        Pair("Dublin", Pair(53.3498, -6.2603)),
        Pair("Delhi", Pair(28.7041, 77.1025)),
        Pair("Detroit", Pair(42.3314, -83.0458)),
        Pair("Dallas", Pair(32.7767, -96.7970)),
        Pair("Bucharest", Pair(44.4268, 26.1025)),
        Pair("Moscow", Pair(55.7558, 37.6173)),
        Pair("London", Pair(51.5074, -0.1278)),
        Pair("Los Angeles", Pair(34.0522, -118.2437)),
        Pair("Lagos", Pair(6.5244, 3.3792)),
        Pair("Lima", Pair(-12.0464, -77.0428)),
        Pair("Lisbon", Pair(38.7223, -9.1393)),
        Pair("Tunis", Pair(36.8065, 10.1815)),
        Pair("Istanbul", Pair(41.0082, 28.9784)),
        Pair("Tokyo", Pair(35.6895, 139.6917))
    )


    private lateinit var searchRepository: SearchRepository

    private val searchResultsAdapter: ArrayAdapter<String> by lazy {
        ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_map2, container, false)
        searchRepository = SearchRepository(lifecycleScope)

        initViews(rootView)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        geocoder = Geocoder(requireContext())
        sharedPreferences = context?.getSharedPreferences(MyConstant.SHARED_PREFS, 0)!!
        editor = sharedPreferences.edit()
        theAddress = getString(R.string.Not_selected_place_yet)
        val remoteDataSource = RemoteDataSource(
            RetrofitHelper.retrofit.create(WeatherService::class.java)
        )
        val localDataSource = LocalDataSource(
            AppDataBase.getInstance(requireContext()).getWeatherDao(),
            AppDataBase.getInstance(requireContext()).getLocationDao(),
            AppDataBase.getInstance(requireContext()).getAlertsDao()
        )
        favFactory = FavViewModelFactory(Repository.getInstance(remoteDataSource,localDataSource))
        viewModel = ViewModelProvider(this, favFactory).get(FavViewModel::class.java)
        setupSearch()
        setupMap(savedInstanceState)
        setOnClickListener()
        return rootView
    }

    private fun setupMap(savedInstanceState: Bundle?) {
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
    }

    private fun initViews(rootView: View) {
        mylocation = rootView.findViewById(R.id.set_as_my_Location)
        addToFav = rootView.findViewById(R.id.btn_addToFav)
        alert = rootView.findViewById(R.id.img_alert_mapScreen)
        searchTextInputLayout = rootView.findViewById(R.id.searchTextInputLayout)// for design the AutoCompleteTextView
        searchEditText = rootView.findViewById(R.id.searchEditText)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        myMap = googleMap
        myMap.uiSettings.isZoomControlsEnabled = true
        myMap.setOnMapClickListener { click ->

            // Get the latitude and longitude of the clicked position
            latitude = click.latitude
            longitude = click.longitude
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            theAddress = addresses?.firstOrNull()?.getAddressLine(0) ?: "Sorry, Address not found"

            Toast.makeText(requireContext(), "The location is $theAddress", Toast.LENGTH_SHORT)
                .show()

            // Add a marker at the clicked position
            val clickedLatLng = LatLng(latitude, longitude)
            val markerOptions = MarkerOptions().position(clickedLatLng).title("Clicked Location")
            myMap.clear()
            myMap.addMarker(markerOptions)
        }
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
                val dialogFragment = AlertDialogFragment(theAddress, longitude, latitude)
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


    private fun setupSearch() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                if (query.isNotEmpty()) {
                    searchRepository.search(query, countries)
                } else {
                    searchResultsAdapter.clear()
                    searchResultsAdapter.notifyDataSetChanged()
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
        // Collect search results from the repository
        lifecycleScope.launch {
            searchRepository.searchResults.collectLatest { results ->
                // Update the adapter with search results
                searchResultsAdapter.clear()
                searchResultsAdapter.addAll(results)
                searchResultsAdapter.notifyDataSetChanged()
            }
        }
        searchEditText.setAdapter(searchResultsAdapter)
        setOnItemClickListener()
    }

    private fun setOnItemClickListener() {
        searchEditText.setOnItemClickListener { parent, _, position, _ ->
            val selectedCountry = parent.getItemAtPosition(position) as String
            val (latitude, longitude) = countries.first { (name, _) -> name == selectedCountry }.second
            val message =
                "$selectedCountry city is added to your favorites"
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            if (longitude != 0.0 && latitude != 0.0) {
                var favLocation = FavLocation(selectedCountry, longitude, latitude)
                viewModel.insertFavLocation(favLocation)
            }
        }
    }
}