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
import com.google.firebase.firestore.firestore
import java.security.MessageDigest
import java.util.Base64



class SplashActivity : AppCompatActivity() {
    private val splashScreenDuration = 2000L // 2 detik

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val logoImageView: ImageView = findViewById(R.id.logoImageView)
        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        logoImageView.startAnimation(fadeInAnimation)

        Handler(Looper.getMainLooper()).postDelayed({
            decideNextActivity()
        }, splashScreenDuration)
    }

    private fun decideNextActivity() {
        val currentUser = Firebase.auth.currentUser
        if (currentUser != null) {
            fetchUserRoleAndRedirect(currentUser.uid)
        } else {
            goToLoginActivity()
        }
    }

    private fun fetchUserRoleAndRedirect(uid: String) {
        val db = Firebase.firestore
        db.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val role = document.getString("role") ?: "warga"
                    goToMainActivity(role)
                } else {
                    goToLoginActivity()
                }
            }
            .addOnFailureListener {
                goToMainActivity("warga")
            }
    }


    private fun goToMainActivity(role: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("USER_ROLE", role) // Kirim data peran ke MainActivity
        startActivity(intent)
        finish()
    }

    private fun goToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}