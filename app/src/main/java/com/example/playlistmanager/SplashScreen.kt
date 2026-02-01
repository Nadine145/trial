package com.example.playlistmanager

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SplashScreen : AppCompatActivity() {

    private val TAG = "SplashActivity"
    private val SPLASH_DURATION = 10000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ FIX 1: Correct layout name
        setContentView(R.layout.activity_splash_screen)  // Changed from activity_splash_screen

        // Hide status bar for fullscreen splash
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        // ✅ FIX 2: Correct view IDs and types
        val tvAppName: TextView = findViewById(R.id.tvSplashAppName)
        val tvSubtitle: TextView = findViewById(R.id.tvSplashSubtitle)
        val tvTagline: TextView = findViewById(R.id.tvSplashTagline)
        val ivIcon: ImageView = findViewById(R.id.imageView)  // Changed to ImageView and correct ID
        val tvLoading: TextView = findViewById(R.id.tvLoading)
        val ivLoading: ImageView = findViewById(R.id.imageView2)

        // Start entrance animations
        startAnimations(tvAppName, tvSubtitle, ivIcon, tvTagline, tvLoading, ivLoading)

        // Navigate to MainActivity after SPLASH_DURATION
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            // Smooth fade transition
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

            // Close splash screen
            finish()
        }, SPLASH_DURATION)
    }

    /**
     * Start all splash screen animations
     */
    private fun startAnimations(
        appName: TextView,
        subtitle: TextView,
        icon: ImageView,  // Changed to ImageView
        tagline: TextView,
        loading: TextView,
        loadingImage: ImageView  // Added loading image
    ) {
        // Animation 1: App name - Fade in with scale
        appName.alpha = 0f
        appName.scaleX = 0.5f
        appName.scaleY = 0.5f
        appName.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(1000)
            .setStartDelay(300)
            .start()

        // Animation 2: Icon - Scale from small to full size
        icon.alpha = 0f
        icon.scaleX = 0.3f
        icon.scaleY = 0.3f
        icon.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(1000)
            .setStartDelay(500)
            .start()

        // Animation 3: Subtitle - Fade in
        subtitle.alpha = 0f
        subtitle.animate()
            .alpha(1f)
            .setDuration(1000)
            .setStartDelay(700)
            .start()

        // Animation 4: Tagline - Slide up from below
        tagline.alpha = 0f
        tagline.translationY = 50f
        tagline.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(1000)
            .setStartDelay(800)
            .start()

        // Animation 5: Loading image - Fade in
        loadingImage.alpha = 0f
        loadingImage.animate()
            .alpha(1f)
            .setDuration(800)
            .setStartDelay(1000)
            .start()

        // Animation 6: Loading text - Fade in
        loading.alpha = 0f
        loading.animate()
            .alpha(1f)
            .setDuration(800)
            .setStartDelay(1200)
            .start()

        // Start pulsing animation for loading text
        loading.postDelayed({
            startPulseAnimation(loading)
        }, 1500)
    }

    /**
     * Creates continuous pulsing effect for loading text
     */
    private fun startPulseAnimation(view: TextView) {
        view.animate()
            .scaleX(1.1f)
            .scaleY(1.1f)
            .alpha(0.5f)
            .setDuration(500)
            .withEndAction {
                view.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .alpha(1f)
                    .setDuration(500)
                    .withEndAction {
                        if (!isFinishing) {
                            startPulseAnimation(view)
                        }
                    }
                    .start()
            }
            .start()
    }
}