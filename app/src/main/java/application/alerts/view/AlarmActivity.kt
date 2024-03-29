package application.alerts.view

import android.media.RingtoneManager
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.weather.application.R
import com.weather.application.databinding.AlarmBinding


class AlarmActivity() : AppCompatActivity() {

    private lateinit var binding: AlarmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alarm)
        binding = AlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val alarmSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val ringtone = RingtoneManager.getRingtone(applicationContext, alarmSoundUri)
        ringtone.play()

        window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.addFlags(
            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                    or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                    or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
        )
        setFinishOnTouchOutside(false)

        binding.tvCityAlarm.text = intent.getStringExtra("cityName")
        binding.tvDescAlarm.text = intent.getStringExtra("description")

        binding.btnStopTheAlerm.setOnClickListener {
            if (ringtone != null && ringtone.isPlaying()) {
                ringtone.stop();
            }
            this.finish()
        }
    }
}