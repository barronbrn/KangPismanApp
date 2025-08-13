package com.example.kangpismanapp.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kangpismanapp.R
import com.example.kangpismanapp.adapter.DraftAdapter
import com.example.kangpismanapp.data.model.DraftTransaksi
import com.example.kangpismanapp.data.model.ItemDraft
import com.example.kangpismanapp.data.model.Sampah
import com.example.kangpismanapp.util.DialogUtils
import com.example.kangpismanapp.viewmodel.TimbangViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BuatSetoranActivity : AppCompatActivity() {

    private val viewModel: TimbangViewModel by viewModels()
    private lateinit var autoCompleteMaterial: AutoCompleteTextView
    private lateinit var editTextEstimasiBerat: TextInputEditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var draftAdapter: DraftAdapter

    private var daftarMaterialFromDb: List<Sampah> = emptyList()
    private val listDraftSementara = mutableListOf<ItemDraft>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_buat_setoran)

        val mainLayout: LinearLayout = findViewById(R.id.main_buat_setoran)
        ViewCompat.setOnApplyWindowInsetsListener(mainLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        autoCompleteMaterial = findViewById(R.id.auto_complete_material)
        editTextEstimasiBerat = findViewById(R.id.editTextEstimasiBerat)
        recyclerView = findViewById(R.id.recycler_view_draft)
        val buttonTambah: Button = findViewById(R.id.button_tambah_item_draft)
        val buttonBuatQr: Button = findViewById(R.id.button_buat_qrcode)
        val toolbar: MaterialToolbar = findViewById(R.id.toolbar_buat_setoran)

        toolbar.setNavigationOnClickListener { finish() }

        setupRecyclerView()
        observeViewModel()

        buttonTambah.setOnClickListener { addItemToDraft() }
        buttonBuatQr.setOnClickListener { saveDraftAndGenerateQr() }
    }

    private fun setupRecyclerView() {
        draftAdapter = DraftAdapter { itemToDelete ->
            listDraftSementara.remove(itemToDelete)
            draftAdapter.submitList(listDraftSementara.toList())
        }
        recyclerView.adapter = draftAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun observeViewModel() {
        viewModel.daftarMaterial.observe(this) { materials ->
            if (materials.isNotEmpty()) {
                daftarMaterialFromDb = materials
                val materialNames = materials.map { it.nama }
                val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, materialNames)
                autoCompleteMaterial.setAdapter(spinnerAdapter)
            }
        }
    }

    private fun addItemToDraft() {
        val selectedMaterialName = autoCompleteMaterial.text.toString()
        val beratString = editTextEstimasiBerat.text.toString()

        if (selectedMaterialName.isEmpty() || !daftarMaterialFromDb.any { it.nama == selectedMaterialName }) {
            Toast.makeText(this, "Pilih jenis sampah yang valid", Toast.LENGTH_SHORT).show()
            return
        }
        if (beratString.isEmpty()) {
            Toast.makeText(this, "Estimasi berat tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        val berat = beratString.toDoubleOrNull() ?: 0.0
        listDraftSementara.add(ItemDraft(namaMaterial = selectedMaterialName, estimasiBeratKg = berat))
        draftAdapter.submitList(listDraftSementara.toList())

        autoCompleteMaterial.text.clear()
        editTextEstimasiBerat.text?.clear()
    }

    private fun saveDraftAndGenerateQr() {
        if (listDraftSementara.isEmpty()) {
            Toast.makeText(this, "Tambahkan minimal satu item sampah", Toast.LENGTH_SHORT).show()
            return
        }

        val currentUser = Firebase.auth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "Silakan login terlebih dahulu", Toast.LENGTH_SHORT).show()
            return
        }

        val userDocRef = Firebase.firestore.collection("users").document(currentUser.uid)
        userDocRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val username = document.getString("username") ?: "Pengguna"
                    val alamat = document.getString("alamat") ?: "Alamat tidak tersedia"
                    val noTelepon = document.getString("noTelepon") ?: "No. HP tidak tersedia"

                    val draft = DraftTransaksi(
                        wargaUid = currentUser.uid,
                        wargaUsername = username,
                        wargaAlamat = alamat,
                        wargaNoTelepon = noTelepon,
                        items = listDraftSementara
                    )

                    Firebase.firestore.collection("draft_transaksi")
                        .add(draft)
                        .addOnSuccessListener { documentReference ->
                            val intent = Intent(this, MyQrCodeActivity::class.java)
                            intent.putExtra("QR_CODE_DATA", documentReference.id)
                            startActivity(intent)
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Gagal membuat draft. Coba lagi.", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "Data profil Anda tidak ditemukan.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal mengambil data profil.", Toast.LENGTH_SHORT).show()
            }
    }
}