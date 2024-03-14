package weather.application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import weather.application.alerts.view.AlertFragment
import weather.application.fav.view.FavouriteFragment
import weather.application.home.view.HomeFragment
import weather.application.setting.SettingFragment


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var toolbar: Toolbar

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

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, HomeFragment()).commit()

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
                    .replace(R.id.fragment_container, FavouriteFragment()).commit()
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

}