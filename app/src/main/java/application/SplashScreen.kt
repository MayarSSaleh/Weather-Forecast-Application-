package application.application

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.weather.application.R

class SplashScreen : AppCompatActivity()   {

        val SPLASH_DELAY: Long = 3000 // 3 seconds delay

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.splash)


                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }, SPLASH_DELAY)
            }
        }
