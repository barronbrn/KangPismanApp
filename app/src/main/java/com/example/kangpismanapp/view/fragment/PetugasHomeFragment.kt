package com.example.kangpismanapp.view.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.kangpismanapp.R
import com.example.kangpismanapp.view.activity.ScannerActivity
import com.example.kangpismanapp.view.activity.TimbangActivity
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException

@AndroidEntryPoint
class PetugasHomeFragment : Fragment(R.layout.fragment_petugas_home) {

    // Launcher untuk membuka kamera (ScannerActivity)
    private val scannerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val draftId = result.data?.getStringExtra("QR_CODE_DATA")
            if (draftId != null) {
                goToTimbangActivity(draftId)
            }
        }
    }

    // Launcher BARU untuk memilih gambar dari galeri
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            scanQrCodeFromUri(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonScanLive: Button = view.findViewById(R.id.button_scan_live)
        val buttonScanGallery: Button = view.findViewById(R.id.button_scan_gallery)

        buttonScanLive.setOnClickListener {
            // Jalankan pemindai kamera langsung
            scannerLauncher.launch(Intent(activity, ScannerActivity::class.java))
        }

        buttonScanGallery.setOnClickListener {
            // Buka galeri untuk memilih gambar
            galleryLauncher.launch("image/*")
        }
    }

    private fun scanQrCodeFromUri(uri: Uri) {
        try {
            // Buat InputImage dari URI gambar yang dipilih
            val image = InputImage.fromFilePath(requireContext(), uri)

            val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build()
            val scanner = BarcodeScanning.getClient(options)

            // Proses gambar untuk mencari QR code
            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    if (barcodes.isNotEmpty()) {
                        val draftId = barcodes[0].rawValue
                        if (draftId != null) {
                            Toast.makeText(requireContext(), "QR Code berhasil dipindai!", Toast.LENGTH_SHORT).show()
                            goToTimbangActivity(draftId)
                        }
                    } else {
                        Toast.makeText(requireContext(), "Tidak ada QR Code yang ditemukan di gambar.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Gagal memindai gambar.", Toast.LENGTH_SHORT).show()
                }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    // Fungsi bantuan untuk pindah ke TimbangActivity
    private fun goToTimbangActivity(draftId: String) {
        val intent = Intent(activity, TimbangActivity::class.java)
        intent.putExtra("DRAFT_ID", draftId) // Kirim ID draft
        startActivity(intent)
    }
}