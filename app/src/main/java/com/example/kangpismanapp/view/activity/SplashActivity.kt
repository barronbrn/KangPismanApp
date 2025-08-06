package com.example.kangpismanapp.view.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import com.example.kangpismanapp.R
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import java.security.MessageDigest
import java.util.Base64



class SplashActivity : AppCompatActivity() {
    private val splashScreenDuration = 2000L // 2 detik

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash) // Tampilkan layout splash screen

        // Ambil komponen ImageView
        val logoImageView: ImageView = findViewById(R.id.logoImageView)

        // Muat dan jalankan animasi fade_in
        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        logoImageView.startAnimation(fadeInAnimation)

        // Gunakan Handler untuk menunda eksekusi
        Handler(Looper.getMainLooper()).postDelayed({
            // Cek status login pengguna setelah durasi splash screen selesai
            checkUserStatus()
        }, splashScreenDuration)
    }

    private fun checkUserStatus() {
        val currentUser = Firebase.auth.currentUser
        if (currentUser != null) {
            // Jika ada pengguna yang login, langsung ke MainActivity
            goToMainActivity()
        } else {
            // Jika tidak ada, ke LoginActivity
            goToLoginActivity()
        }
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Tutup SplashActivity
    }

    private fun goToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Tutup SplashActivity
    }
}