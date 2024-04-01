package application.fav.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import application.MapFragment
import application.fav.viewModel.Communication
import application.fav.viewModel.FavViewModel
import application.fav.viewModel.FavViewModelFactory
import application.model.FavLocation
import application.model.LocalStateFavouriteLocations
import application.model.Repository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.weather.application.R
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FavouriteFragment : Fragment() {

    private lateinit var favRecycler: RecyclerView
    private lateinit var fav_locationsAdaptor: FavouriteAdaptor
    private lateinit var fav_fab: FloatingActionButton

    private lateinit var favFactory: FavViewModelFactory
    private lateinit var viewModel: FavViewModel
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var comm: Communication
    lateinit var loading_view: ProgressBar



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_favourite, container, false)
        favFactory = FavViewModelFactory(Repository.getInstance(requireContext()))
        viewModel = ViewModelProvider(this, favFactory).get(FavViewModel::class.java)
        fav_fab = view.findViewById(R.id.fab)
        favRecycler = view.findViewById(R.id.fav_loc_recycler)
        loading_view=view.findViewById(R.id.favloading_view)
        loading_view.visibility = View.GONE

        val removeFromFav = { favLocation: FavLocation ->
            viewModel.deleteFavLocation(favLocation)
        }
        comm = activity as Communication
        val showFavLocation = { favLocation: FavLocation ->
            comm.setFavLocaionAtHome(favLocation)
        }

        fav_locationsAdaptor = FavouriteAdaptor(requireContext(), removeFromFav,showFavLocation)
        setUpRecycvlerView()

        lifecycleScope.launch {
            viewModel.favLocations.collectLatest {
                when (it) {
                    is LocalStateFavouriteLocations.LoadingLocal -> {
                        loading_view.visibility = View.VISIBLE
                    }
                    is LocalStateFavouriteLocations.SuccessLocal -> {
                        fav_locationsAdaptor.submitList(it.data)
                        loading_view.visibility = View.GONE
                    }
                    else -> {
                        loading_view.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            "Sorry Can not get the favourite countries}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        fav_fab.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragment_container, MapFragment("", 5))
            transaction?.addToBackStack(null)
            transaction?.commit()
        }
        return view
    }

    fun setUpRecycvlerView() {
        layoutManager = LinearLayoutManager(requireContext())
        favRecycler.adapter = fav_locationsAdaptor
        favRecycler.layoutManager = layoutManager
    }

}