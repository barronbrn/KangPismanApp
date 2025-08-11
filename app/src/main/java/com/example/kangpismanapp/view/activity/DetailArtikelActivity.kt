package com.example.kangpismanapp.view.activity

import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.example.kangpismanapp.R
import com.example.kangpismanapp.data.model.Artikel
import com.google.android.material.appbar.CollapsingToolbarLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.coordinatorlayout.widget.CoordinatorLayout

class DetailArtikelActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail_artikel)

        val mainLayout: CoordinatorLayout = findViewById(R.id.main_layout_detail_artikel)
        ViewCompat.setOnApplyWindowInsetsListener(mainLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar_detail_artikel)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        val imageView: ImageView = findViewById(R.id.image_detail_artikel)
        val contentText: TextView = findViewById(R.id.text_content_artikel)
        val collapsingToolbar: CollapsingToolbarLayout = findViewById(R.id.collapsing_toolbar)

        val artikel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("ARTICLE_DATA", Artikel::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("ARTICLE_DATA")
        }

        if (artikel != null) {
            collapsingToolbar.title = artikel.title
            contentText.text = artikel.content

            Glide.with(this)
                .load(artikel.imageUrl)
                .into(imageView)
        }
    }
}