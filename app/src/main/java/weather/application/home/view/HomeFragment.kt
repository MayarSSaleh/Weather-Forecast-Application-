package weather.application.home.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import weather.application.R
import weather.application.home.viewModel.HomeViewModel
import weather.application.home.viewModel.HomeViewModelFactory
import weather.application.model.Repositry

class HomeFragment : Fragment() {
    private lateinit var homeViewModel: HomeViewModel
    lateinit var homeViewModelFactory: HomeViewModelFactory
    lateinit var tv_Weather_description: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModelFactory = HomeViewModelFactory(Repositry.getInstance(requireContext()))
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        tv_Weather_description = view.findViewById(R.id.tv_Weather_description)
        tv_Weather_description.text =
            homeViewModel.showWeatherResponse.list.get(0).weather.get(0).description

        //The ViewModelProvider checks if a HomeViewModel instance already exists for this Fragment.
        // If it doesn't, it uses the provided HomeViewModelFactory to create a new instance.else return it
        homeViewModel = ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel::class.java)
        // Inflate the layout for this fragment
        return view
    }
}