package com.example.kangpismanapp.view.activity

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kangpismanapp.R
import com.example.kangpismanapp.adapter.ArtikelAdapter
import com.example.kangpismanapp.viewmodel.EdukasiViewModel
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EdukasiActivity : AppCompatActivity() {

    private val viewModel: EdukasiViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var artikelAdapter: ArtikelAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edukasi)

        val mainLayout: LinearLayout = findViewById(R.id.main_edukasi)
        ViewCompat.setOnApplyWindowInsetsListener(mainLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar: MaterialToolbar = findViewById(R.id.toolbar_edukasi)
        toolbar.setNavigationOnClickListener { finish() }


        recyclerView = findViewById(R.id.recycler_view_edukasi)
        artikelAdapter = ArtikelAdapter { artikel ->
            val intent = Intent(this, DetailArtikelActivity::class.java)
            intent.putExtra("ARTICLE_DATA", artikel)
            startActivity(intent)
        }
        recyclerView.adapter = artikelAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.articles.observe(this) { daftarArtikel ->
            artikelAdapter.submitList(daftarArtikel)
        }
    }
}