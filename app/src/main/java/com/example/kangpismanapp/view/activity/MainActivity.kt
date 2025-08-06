package com.example.kangpismanapp.view.activity


import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.kangpismanapp.R
import com.example.kangpismanapp.view.fragment.AccountFragment
import com.example.kangpismanapp.view.fragment.HistoryFragment
import com.example.kangpismanapp.view.fragment.HomeFragment
import com.example.kangpismanapp.view.fragment.LokasiFragment
import com.example.kangpismanapp.view.fragment.RewardFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavView = findViewById(R.id.bottom_navigation)


        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }

        bottomNavView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.nav_riwayat -> { // Ganti ID jika Anda menamainya beda
                    loadFragment(HistoryFragment())
                    true
                }
                R.id.nav_lokasi -> {
                    loadFragment(LokasiFragment())
                    true
                }
                R.id.nav_reward -> {
                    loadFragment(RewardFragment())
                    true
                }
                R.id.nav_account -> {
                    loadFragment(AccountFragment())
                    true
                }
                else -> false
            }
        }
    }

    // Fungsi bantuan untuk memuat Fragment ke dalam container
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}