package com.example.kangpismanapp.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kangpismanapp.R
import com.example.kangpismanapp.adapter.TimbangAdapter
import com.example.kangpismanapp.data.model.ItemTransaksi
import com.example.kangpismanapp.data.model.Sampah
import com.example.kangpismanapp.data.model.Transaksi
import com.example.kangpismanapp.viewmodel.TimbangViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import java.text.NumberFormat
import java.util.Locale

@AndroidEntryPoint
class TimbangActivity : AppCompatActivity() {
    private val viewModel: TimbangViewModel by viewModels()
    private lateinit var spinnerMaterial: Spinner
    private lateinit var editTextBerat: TextInputEditText
    private lateinit var buttonTambahItem: Button
    private lateinit var buttonSimpanTransaksi: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var timbangAdapter: TimbangAdapter

    private var daftarMaterialFromDb: List<Sampah> = emptyList()
    private val listTimbangSementara = mutableListOf<ItemTransaksi>()
    private lateinit var buttonScanQr: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timbang)

        setupUI()
        observeViewModel()
    }

    // Launcher untuk membuka ScannerActivity dan menerima hasilnya
    private val scannerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data?.getStringExtra("QR_CODE_DATA")
            if (data != null) {
                processQrCodeData(data)
            }
        }
    }

    private fun setupUI() {
        buttonTambahItem = findViewById(R.id.button_tambah_item)
        buttonSimpanTransaksi = findViewById(R.id.button_simpan_transaksi)
        recyclerView = findViewById(R.id.recycler_view_timbang)
        buttonScanQr = findViewById(R.id.button_scan_qr)


        spinnerMaterial = findViewById(R.id.spinner_material)
        editTextBerat = findViewById(R.id.editTextBerat)
        timbangAdapter = TimbangAdapter()

        // Setup RecyclerView dengan adapter
        recyclerView.adapter = timbangAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val toolbar: MaterialToolbar = findViewById(R.id.toolbar_timbang)
        toolbar.setNavigationOnClickListener { finish() }

        buttonTambahItem.setOnClickListener { addItemToList() }
        buttonSimpanTransaksi.setOnClickListener { saveTransaction() }
        buttonScanQr.setOnClickListener {
            scannerLauncher.launch(Intent(this, ScannerActivity::class.java))
        }
    }

    // Fungsi baru untuk memproses data dari QR code
    private fun processQrCodeData(data: String) {
        try {
            val jsonObject = JSONObject(data)
            val nama = jsonObject.getString("nama")
            val berat = jsonObject.getDouble("berat")

            // Cari material di daftar yang sudah kita fetch
            val selectedMaterial = daftarMaterialFromDb.find { it.nama.equals(nama, ignoreCase = true) }

            if (selectedMaterial != null) {
                val subtotal = (berat * selectedMaterial.harga).toInt()
                val newItem = ItemTransaksi(
                    namaMaterial = selectedMaterial.nama,
                    hargaPerKg = selectedMaterial.harga,
                    beratKg = berat,
                    subtotal = subtotal
                )
                listTimbangSementara.add(newItem)
                timbangAdapter.submitList(listTimbangSementara.toList())
                updateTotal()
                Toast.makeText(this, "Item dari QR berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Material '${nama}' tidak ditemukan", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Format QR Code tidak valid", Toast.LENGTH_SHORT).show()
            Log.e("TimbangActivity", "Error parsing QR data", e)
        }
    }




    private fun observeViewModel() {
        viewModel.daftarMaterial.observe(this) { materials ->
            Log.d("APP_DEBUG_TIMBANG", "Activity: Observer terpanggil dengan ${materials.size} item material.") // LOG 5
            daftarMaterialFromDb = materials
            val materialNames = materials.map { it.nama }
            val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, materialNames)
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerMaterial.adapter = spinnerAdapter
        }

        viewModel.isTransaksiSaved.observe(this) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(this, "Transaksi berhasil disimpan!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Gagal menyimpan transaksi.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addItemToList() {
        if (spinnerMaterial.selectedItem == null) {
            Toast.makeText(this, "Daftar material belum dimuat", Toast.LENGTH_SHORT).show()
            return
        }

        val beratString = editTextBerat.text.toString()
        if (beratString.isEmpty()) {
            editTextBerat.error = "Berat tidak boleh kosong"
            return
        }

        val selectedMaterial = daftarMaterialFromDb[spinnerMaterial.selectedItemPosition]
        val berat = beratString.toDouble()
        val subtotal = (berat * selectedMaterial.harga).toInt()

        val newItem = ItemTransaksi(
            namaMaterial = selectedMaterial.nama,
            hargaPerKg = selectedMaterial.harga,
            beratKg = berat,
            subtotal = subtotal
        )

        listTimbangSementara.add(newItem)
        timbangAdapter.submitList(listTimbangSementara.toList()) // Update RecyclerView
        updateTotal()

        // Reset input
        editTextBerat.text?.clear()
    }

    private fun updateTotal() {
        val total = listTimbangSementara.sumOf { it.subtotal }
        val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        buttonSimpanTransaksi.text = "Simpan Transaksi (Total: ${formatRupiah.format(total)})"
    }

    private fun saveTransaction() {
        if (listTimbangSementara.isEmpty()) {
            Toast.makeText(this, "Tambahkan minimal satu item", Toast.LENGTH_SHORT).show()
            return
        }

        val uid = Firebase.auth.currentUser?.uid
        if (uid == null) {
            Toast.makeText(this, "Gagal mendapatkan data pengguna", Toast.LENGTH_SHORT).show()
            return
        }

        val total = listTimbangSementara.sumOf { it.subtotal }
        val newTransaction = Transaksi(
            uid = uid,
            totalPoin = total,
            items = listTimbangSementara
        )

        viewModel.simpanTransaksi(newTransaction)
    }
}