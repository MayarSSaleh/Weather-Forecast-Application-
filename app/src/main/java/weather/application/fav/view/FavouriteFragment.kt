package weather.application.fav.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import weather.application.MapFragment
import weather.application.R

class FavouriteFragment : Fragment() {
    private lateinit var fav_locations:RecyclerView
    private lateinit var fav_locationsAdaptor:FavouriteAdaptor
    private lateinit var fav_image: ImageView
// THERE ARE ADAPTOR HANDLINNNNNNNNNNNNNNNNNNNNNNG
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_favourite, container, false)
        fav_image = view.findViewById(R.id.image_fav)
        fav_locations=view.findViewById(R.id.fav_loc_recycler)
        fav_locationsAdaptor=FavouriteAdaptor(requireContext())

        fav_image.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragment_container, MapFragment("", 5))
            transaction?.addToBackStack(null)
            transaction?.commit()
        }
        return view
    }


}