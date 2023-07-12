package com.eagleminds.jciindia

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class ThankYouActivity : AppCompatActivity() {

    private val DELAY_TIME = 1000L // Delay of 1 second

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.thankyou_screen)

        Handler(Looper.getMainLooper()).postDelayed({
            finish()
            // Close the app
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAndRemoveTask()
            } else {
                @Suppress("DEPRECATION")
                finishAffinity()
            }
        }, DELAY_TIME)
    }
}
