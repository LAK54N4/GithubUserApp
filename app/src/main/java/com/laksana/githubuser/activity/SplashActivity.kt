package com.laksana.githubuser.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.laksana.githubuser.MainActivity
import com.laksana.githubuser.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            val goToMain = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(goToMain)
            finish()
        }, 3000)
    }
}