package com.example.kangpismanapp.view.activity

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kangpismanapp.R
import com.example.kangpismanapp.adapter.TimbangAdapter
import com.example.kangpismanapp.data.model.ItemTransaksi
import com.example.kangpismanapp.data.model.Sampah
import com.example.kangpismanapp.data.model.Transaksi
import com.example.kangpismanapp.viewmodel.ProfileViewModel
import com.example.kangpismanapp.viewmodel.TimbangViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.util.Locale


@AndroidEntryPoint
class TimbangActivity : AppCompatActivity() {
    private val viewModel: TimbangViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()
    private val timbangViewModel: TimbangViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var timbangAdapter: TimbangAdapter
    private lateinit var textInfoWarga: TextView
    private lateinit var buttonSimpan: Button
    private var currentDraftId: String? = null
    private var daftarMaterialFromDb: List<Sampah> = emptyList()
    private val listTimbangSementara = mutableListOf<ItemTransaksi>()
    private lateinit var textInfoPetugas: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timbang)

        setupUI()
        observeViewModel()

        currentDraftId = intent.getStringExtra("DRAFT_ID")
        if (currentDraftId == null) {
            Toast.makeText(this, "ID Draft tidak valid.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        viewModel.loadDraftTransaksi(currentDraftId!!)
    }

    private fun setupUI() {
        textInfoWarga = findViewById(R.id.text_info_warga)
        textInfoPetugas = findViewById(R.id.text_info_petugas)
        buttonSimpan = findViewById(R.id.button_simpan_transaksi)
        recyclerView = findViewById(R.id.recycler_view_timbang)

        val toolbar: MaterialToolbar = findViewById(R.id.toolbar_timbang)
        toolbar.setNavigationOnClickListener { finish() }

        timbangAdapter = TimbangAdapter()
        recyclerView.adapter = timbangAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        buttonSimpan.setOnClickListener { saveTransaction() }
    }

    private fun observeViewModel() {

        profileViewModel.userProfile.observe(this) { profile ->
            profile?.let {
                // Tampilkan email petugas yang sedang login
                textInfoPetugas.text = "Dikonfirmasi oleh: ${it.username}"
            }
        }

        viewModel.daftarMaterial.observe(this) { materials ->
            if (materials.isNotEmpty()) {
                daftarMaterialFromDb = materials
                processLoadedDraft()
            }
        }

        viewModel.draftTransaksi.observe(this) { draft ->
            if (draft != null) {
                processLoadedDraft()
            }
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

    private fun processLoadedDraft() {
        val draft = viewModel.draftTransaksi.value
        if (draft != null && daftarMaterialFromDb.isNotEmpty()) {

            val infoWarga = """
                Nama: ${draft.wargaUsername}
                Alamat: ${draft.wargaAlamat}
                No. HP: ${draft.wargaNoTelepon}
            """.trimIndent()
            textInfoWarga.text = infoWarga

            listTimbangSementara.clear()
            draft.items.forEach { draftItem ->
                val material = daftarMaterialFromDb.find { it.nama == draftItem.namaMaterial }
                if (material != null) {
                    listTimbangSementara.add(ItemTransaksi(
                        namaMaterial = material.nama,
                        hargaPerKg = material.harga,
                        beratKg = draftItem.estimasiBeratKg
                    ))
                }
            }
            timbangAdapter.submitList(listTimbangSementara.toList())
            updateTotal()
        }
    }


    private fun updateTotal() {
        val totalBerat = listTimbangSementara.sumOf { it.beratKg }
        val totalPoin = (totalBerat / 0.5 * 10).toInt()

        buttonSimpan.text = "Konfirmasi Transaksi (Total: $totalPoin Poin)"
    }

    private fun saveTransaction() {
        if (listTimbangSementara.isEmpty()) {
            Toast.makeText(this, "Daftar item kosong.", Toast.LENGTH_SHORT).show()
            return
        }

        val draft = viewModel.draftTransaksi.value
        val wargaUid = draft?.wargaUid
        if (wargaUid == null) {
            Toast.makeText(this, "ID Warga tidak ditemukan. Coba pindai ulang.", Toast.LENGTH_SHORT).show()
            return
        }

        // --- LOGIKA PERHITUNGAN BARU ---
        // Hitung subtotal untuk setiap item (berat x harga)
        listTimbangSementara.forEach { it.subtotal = (it.beratKg * it.hargaPerKg).toInt() }

        // Hitung total nilai dalam Rupiah
        val totalRupiah = listTimbangSementara.sumOf { it.subtotal }

        // Hitung total poin berdasarkan berat
        val totalBerat = listTimbangSementara.sumOf { it.beratKg }
        val totalPoin = (totalBerat / 0.5 * 10).toInt()
        // --------------------------------

        val petugas = Firebase.auth.currentUser ?: return
        Firebase.firestore.collection("users").document(petugas.uid).get()
            .addOnSuccessListener { userDoc ->
                val petugasNama = userDoc.getString("username") ?: "Petugas"

                val newTransaction = Transaksi(
                    uid = wargaUid,
                    totalPoin = totalPoin,       // Simpan total poin
                    totalRupiah = totalRupiah,   // Simpan total Rupiah
                    items = listTimbangSementara,
                    petugasUid = petugas.uid,
                    petugasNama = petugasNama
                )

                viewModel.simpanTransaksi(newTransaction)
                currentDraftId?.let { viewModel.updateDraftStatus(it, "completed") }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal mengambil data petugas.", Toast.LENGTH_SHORT).show()
            }
    }
}