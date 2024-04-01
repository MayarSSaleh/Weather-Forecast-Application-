package application.application

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationView
import application.MyConstant
import application.alerts.view.AlertFragment
import application.fav.view.FavouriteFragment
import application.fav.viewModel.Communication
import application.home.view.HomeFragment
import application.showWeatherDetailsViewModel.WeatherShowViewModel
import application.showWeatherDetailsViewModel.WeatherShowModelFactory
import application.data.localDataBase.AppDataBase
import application.data.localDataBase.LocalDataSource
import application.data.network.RemoteDataSource
import application.data.network.RetrofitHelper
import application.data.network.WeatherService
import application.model.FavLocation
import application.model.Repository
import application.setting.view.SettingFragment
import com.weather.application.R


class MainActivity : AppCompatActivity(), Communication,
    NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var toolbar: Toolbar
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var homeViewModel: WeatherShowViewModel
    private lateinit var homeViewModelFactory: WeatherShowModelFactory
    private lateinit var editor: Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_item)
        navigationView.setNavigationItemSelectedListener(this)
        drawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setLanguage()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, HomeFragment()).commit()
    }




    fun setLanguage() {
        sharedPreferences = this.getSharedPreferences(MyConstant.SHARED_PREFS, 0)!!
        val remoteDataSource = RemoteDataSource(
            RetrofitHelper.retrofit.create(WeatherService::class.java)
        )
        val localDataSource = LocalDataSource(
            AppDataBase.getInstance(this).getWeatherDao(),
            AppDataBase.getInstance(this).getLocationDao(),
            AppDataBase.getInstance(this).getAlertsDao()
        )
        homeViewModelFactory = WeatherShowModelFactory(Repository.getInstance(remoteDataSource,localDataSource))
        homeViewModel = ViewModelProvider(this, homeViewModelFactory).get(WeatherShowViewModel::class.java)
        var selectedLanguage = sharedPreferences.getString(MyConstant.lan, "en")
        var currentLanguage = sharedPreferences.getString(MyConstant.curentLanguage, "en")
        if (selectedLanguage == "ar" && currentLanguage != "ar") {
            homeViewModel.mySetLocale(selectedLanguage!!, this, this)
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, HomeFragment()).commit()
                drawerLayout.closeDrawer(GravityCompat.START)
                return true
            }

            R.id.fav -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, FavouriteFragment())
                    .commit()
                drawerLayout.closeDrawer(GravityCompat.START)
                return true
            }

            R.id.alert -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, AlertFragment()).commit()
                drawerLayout.closeDrawer(GravityCompat.START)
                return true
            }

            R.id.setting -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, SettingFragment()).commit()
                drawerLayout.closeDrawer(GravityCompat.START)
                return true
            }

            else -> return false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Toggle the drawer when the home/up button is clicked
        return if (item.itemId == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        editor = sharedPreferences.edit()
        editor.putString(MyConstant.curentLanguage, "en")
        editor.apply()
    }

    override fun setFavLocaionAtHome(favLocation: FavLocation) {
        val homeFragment = HomeFragment().apply {
            arguments = Bundle().apply {
                putParcelable("favLocation", favLocation)
            }
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, homeFragment).commit()
    }
}