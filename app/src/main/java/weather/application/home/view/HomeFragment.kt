package weather.application.home.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import weather.application.R
import weather.application.home.viewModel.HomeViewModel
import weather.application.home.viewModel.HomeViewModelFactory
import weather.application.model.Repositry

class HomeFragment : Fragment() {
    private lateinit var homeViewModel: HomeViewModel
    lateinit var homeViewModelFactory: HomeViewModelFactory


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModelFactory = HomeViewModelFactory(Repositry.getInstance(requireContext()))
        //The ViewModelProvider checks if a HomeViewModel instance already exists for this Fragment.
        // If it doesn't, it uses the provided HomeViewModelFactory to create a new instance.
        // If it does exist, it returns the existing instance.
        homeViewModel = ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel::class.java)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
}