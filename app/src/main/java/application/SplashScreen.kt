package application.application

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.weather.application.R
import android.os.Handler

class SplashScreen : AppCompatActivity() {

    val SPLASH_DELAY: Long = 4000
    lateinit var lottieAnimationView: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)
        lottieAnimationView = findViewById(R.id.animation_view)
        lottieAnimationView.animate().translationX(2000F).setDuration(2000).setStartDelay(2900)
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_DELAY)
    }
}