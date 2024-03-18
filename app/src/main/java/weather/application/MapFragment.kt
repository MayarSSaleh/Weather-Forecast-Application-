//package weather.application
//
//
//import android.graphics.Rect
//import android.location.GpsStatus
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.databinding.DataBindingUtil
//import androidx.fragment.app.Fragment
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.SupportMapFragment
//import com.google.android.gms.maps.model.LatLng
//import org.osmdroid.config.Configuration
//import org.osmdroid.api.IMapController
//import org.osmdroid.events.MapListener
//import org.osmdroid.events.ScrollEvent
//import org.osmdroid.events.ZoomEvent
//import org.osmdroid.tileprovider.tilesource.TileSourceFactory
//import org.osmdroid.views.MapView
//import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
//import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
//
//class MapFragment : Fragment(), MapListener, GpsStatus.Listener {
//
//    private lateinit var mMap: MapView
//    private lateinit var controller: IMapController
//    private lateinit var mMyLocationOverlay: MyLocationNewOverlay
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_map, container, false)
//        mMap = view.findViewById(R.id.osmmap)
//        return view
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        mMap.setTileSource(TileSourceFactory.MAPNIK)
//        mMap.mapCenter
//        mMap.setMultiTouchControls(true)
//        mMap.getLocalVisibleRect(Rect())
//
//        mMyLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(requireContext()), mMap)
//        controller = mMap.controller
//
//        mMyLocationOverlay.enableMyLocation()
//        mMyLocationOverlay.enableFollowLocation()
//        mMyLocationOverlay.isDrawAccuracyEnabled = true
//        mMyLocationOverlay.runOnFirstFix {
//            requireActivity().runOnUiThread {
//                controller.setCenter(mMyLocationOverlay.myLocation)
//                controller.animateTo(mMyLocationOverlay.myLocation)
//            }
//        }
//
//        controller.setZoom(6.0)
//
//        mMap.overlays.add(mMyLocationOverlay)
//        mMap.addMapListener(this)
//    }
//
//
//    override fun onScroll(event: ScrollEvent?): Boolean {
//        Log.e("TAG", "onCreate:la ${event?.source?.getMapCenter()?.latitude}")
//        Log.e("TAG", "onCreate:lo ${event?.source?.getMapCenter()?.longitude}")
//        return true
//    }
//
//    override fun onZoom(event: ZoomEvent?): Boolean {
//        Log.e("TAG", "onZoom zoom level: ${event?.zoomLevel}   source:  ${event?.source}")
//        return false
//    }
//
//    override fun onGpsStatusChanged(event: Int) {
//        // Handle GPS status changes
//    }
//}
