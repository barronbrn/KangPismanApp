package com.example.kangpismanapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kangpismanapp.data.model.Transaksi
import com.example.kangpismanapp.data.repository.TransaksiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransaksiViewModel @Inject constructor(
    private val repository: TransaksiRepository // Ganti tipe repository
) : ViewModel() {

    // LiveData untuk semua riwayat (sudah ada)
    private val _transaksiList = MutableLiveData<List<Transaksi>>()
    val transaksiList: LiveData<List<Transaksi>> = _transaksiList

    // LIVE DATA BARU: untuk transaksi terakhir di beranda
    private val _latestTransaksiList = MutableLiveData<List<Transaksi>>()
    val latestTransaksiList: LiveData<List<Transaksi>> = _latestTransaksiList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        fetchTransaksiHistory()
        fetchLatestTransaksi() // <-- Panggil fungsi baru di sini
    }

    fun fetchTransaksiHistory() {
        viewModelScope.launch {
            _isLoading.value = true
            _transaksiList.value = repository.getTransaksiHistory()
            _isLoading.value = false
        }
    }

    fun fetchLatestTransaksi() {
        viewModelScope.launch {
            // Kita tidak perlu isLoading di sini agar tidak mengganggu UI Beranda
            _latestTransaksiList.value = repository.getLatestTransaksi()
        }
    }
}