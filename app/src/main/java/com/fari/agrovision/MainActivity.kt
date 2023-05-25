package com.fari.agrovision

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.fari.agrovision.databinding.ActivityMainBinding
import com.fari.agrovision.ui.home.HomeFragment
import com.fari.agrovision.ui.onboard.OnboardingActivity
import java.util.*
import kotlin.concurrent.timerTask

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


        Timer().schedule(timerTask {
            val moveToOnboardingActivity = Intent(this@MainActivity, OnboardingActivity::class.java)
            startActivity(moveToOnboardingActivity)
            finish()
        }, timerSplashScreen)
    }

    companion object {
        const val timerSplashScreen = 3000L
    }
}