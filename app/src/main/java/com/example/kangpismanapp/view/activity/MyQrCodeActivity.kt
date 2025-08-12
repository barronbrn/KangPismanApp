package com.example.kangpismanapp.view.activity

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kangpismanapp.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.BarcodeFormat


class MyQrCodeActivity : AppCompatActivity() {
    private lateinit var imageViewQr: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_my_qr_code)

        val mainLayout: LinearLayout = findViewById(R.id.main_qr_code)
        ViewCompat.setOnApplyWindowInsetsListener(mainLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imageViewQr = findViewById(R.id.image_view_qr)

        val toolbar: MaterialToolbar = findViewById(R.id.toolbar_qr)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        val imageViewQr: ImageView = findViewById(R.id.image_view_qr)

        // Ambil data (ID Draft) yang dikirim dari BuatSetoranActivity
        val qrContent = intent.getStringExtra("QR_CODE_DATA")

        if (qrContent != null) {
            // Hasilkan QR Code dari ID Draft, bukan dari UID
            generateAndShowQrCode(qrContent, imageViewQr)
        } else {
            // Penanganan jika tidak ada data yang dikirim
            Toast.makeText(this, "Gagal mendapatkan data untuk QR Code", Toast.LENGTH_SHORT).show()
        }
    }

    private fun generateAndShowQrCode(text: String, imageView: ImageView) {
        val writer = QRCodeWriter()
        try {
            val bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 512, 512)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            imageView.setImageBitmap(bmp)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}