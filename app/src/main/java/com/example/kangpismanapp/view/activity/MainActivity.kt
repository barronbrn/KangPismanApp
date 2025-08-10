package com.example.kangpismanapp.view.activity


import android.os.Bundle
import android.widget.RelativeLayout
import androidx.activity.enableEdgeToEdge

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.kangpismanapp.R
import com.example.kangpismanapp.view.fragment.AccountFragment
import com.example.kangpismanapp.view.fragment.HistoryFragment
import com.example.kangpismanapp.view.fragment.HomeFragment
import com.example.kangpismanapp.view.fragment.LokasiFragment
import com.example.kangpismanapp.view.fragment.PetugasHomeFragment
import com.example.kangpismanapp.view.fragment.RewardFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavView: BottomNavigationView
    private var userRole: String="warga"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val mainLayout: RelativeLayout = findViewById(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(mainLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        userRole = intent.getStringExtra("USER_ROLE") ?: "warga"
        bottomNavView = findViewById(R.id.bottom_navigation)

        if (userRole == "petugas") {
            setupPetugasUI()
        } else {
            setupWargaUI()
        }
    }

    private fun setupWargaUI() {
        bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> loadFragment(HomeFragment())
                R.id.nav_riwayat -> loadFragment(HistoryFragment())
                R.id.nav_lokasi -> loadFragment(LokasiFragment())
                R.id.nav_reward -> loadFragment(RewardFragment())
                R.id.nav_account -> loadFragment(AccountFragment())
            }
            true
        }
        // Muat halaman awal Warga
        if (supportFragmentManager.fragments.isEmpty()) {
            loadFragment(HomeFragment())
        }
    }

    private fun setupPetugasUI() {
        // Ganti menu di BottomNav
        bottomNavView.menu.clear()
        bottomNavView.inflateMenu(R.menu.petugas_nav_menu)

        bottomNavView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_scan_qr -> loadFragment(PetugasHomeFragment())
                R.id.nav_riwayat -> loadFragment(HistoryFragment())
                R.id.nav_account -> loadFragment(AccountFragment())
            }
            true
        }
        // Muat halaman awal Petugas
        if (supportFragmentManager.fragments.isEmpty()) {
            loadFragment(PetugasHomeFragment())
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}