package weather.application

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class GetGeocoder : AppCompatActivity() {

    lateinit var tvLongitudinal: TextView
    lateinit var tvLatitudeValue: TextView
    lateinit var tvAddressValue: TextView
    lateinit var sentSMS: Button
    lateinit var showInMap: Button
    lateinit var longitud: String
    lateinit var latitud: String

    private lateinit var geocoder: Geocoder
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        geocoder = Geocoder(this)

        /*
//        setContentView(R.layout.activity_main)
//        initUI();

//        sentSMS.setOnClickListener {
//            val smsIntent = Intent(Intent.ACTION_VIEW)
//            smsIntent.type = "vnd.android-dir/mms-sms"
//            smsIntent.putExtra("address", "01148267233")
//            smsIntent.putExtra(
//                "sms_body",
//                tvAddressValue.text
//            ) // Add the address as the message body
//            startActivity(smsIntent)
//        }
*/
        /* showInMap.setOnClickListener {
            if (::longitud.isInitialized || ::latitud.isInitialized) {
                if (longitud != null && latitud != null) {
                    //url <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                    val uri = Uri.parse("geo:$longitud,$latitud")
                    val mapIntent = Intent(Intent.ACTION_VIEW, uri)
                    mapIntent.setPackage("com.google.android.apps.maps")
                    if (mapIntent.resolveActivity(packageManager) != null) {
                        startActivity(mapIntent)
                    } else {
                        Toast.makeText(this, "Google Maps not installed", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "null", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, " not isInitialized", Toast.LENGTH_SHORT).show()
            }
        }*/
// if use location... need permission so -> check permision -> then check permission result
// not
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {      // get permission for what i need
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), 5
            )
            return
        }
        getFreshLocation()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // sure at some point we stop rerequest the permission.....line 99
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
        } else {
            getFreshLocation()
        }
    }

    //The MissingPermission warning is typically raised by the lint tool when it detects code that requires certain permissions, but those permissions are not explicitly checked for or requested. In the context of your code, the warning is suppressed because you're using location-related functions (getFreshLocation() method) without explicitly checking for location permissions again within that method.
//
//However, it's important to note that simply suppressing the warning doesn't handle the actual permission request logic; it just silences the warning from being displayed during lint checks. In your case, you're handling the permission request logic separately in the onRequestPermissionsResult method.
    @SuppressLint("MissingPermission")
    fun getFreshLocation() {
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        // build the request  details of my request: get the location every... time? priority? low .high. balance....
        val locationRequest: LocationRequest = LocationRequest.Builder(1000).apply {
            setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
        }.build()
        // call back will get the response which location
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                // what will make by it?
                //show
//So, locationResult.lastLocation ?: return translates to: If locationResult.lastLocation is not null, assign it to the variable location;
                // otherwise, immediately return from the current function.
                val location = locationResult.lastLocation ?: return
                tvLongitudinal.text = location.longitude.toString()
                tvLatitudeValue.text = location.latitude.toString()

/*                longitud = location.longitude.toString()
               latitud = location.latitude.toString()

                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                val addressText =
                    addresses?.firstOrNull()?.getAddressLine(0) ?: "Sorry, Address not found"
                tvAddressValue.text = addressText*/
            }
        }
        // to get the update
        locationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()//build in the programmer
        )
    }
}
/*
//    fun initUI() {
//        tvLongitudinal = findViewById(R.id.tv_longitude_value)
//        tvLatitudeValue = findViewById(R.id.tv_latitude_value)
//        tvAddressValue = findViewById(R.id.tv_laddress_value)
//        sentSMS = findViewById(R.id.btn_SMS)
//        showInMap = findViewById(R.id.btn_map)
//    }
}*/
