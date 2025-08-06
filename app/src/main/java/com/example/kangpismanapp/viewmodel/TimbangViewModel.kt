package com.example.kangpismanapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kangpismanapp.data.model.Sampah
import com.example.kangpismanapp.data.model.Transaksi
import com.example.kangpismanapp.data.repository.TimbangRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimbangViewModel @Inject constructor() : ViewModel() {
    private val repository = TimbangRepository()

    private val _daftarMaterial = MutableLiveData<List<Sampah>>()
    val daftarMaterial: LiveData<List<Sampah>> = _daftarMaterial

    private val _isTransaksiSaved = MutableLiveData<Boolean>()
    val isTransaksiSaved: LiveData<Boolean> = _isTransaksiSaved

    init {
        fetchDaftarMaterial()
    }

    private fun fetchDaftarMaterial() {
        viewModelScope.launch {
            val data = repository.getDaftarMaterial()
            Log.d("APP_DEBUG_TIMBANG", "ViewModel: Menerima ${data.size} item material dari Repository.") // LOG 4
            _daftarMaterial.value = data
        }
    }

    fun simpanTransaksi(transaksi: Transaksi) {
        viewModelScope.launch {
            val success = repository.simpanTransaksi(transaksi)
            _isTransaksiSaved.value = success
        }
    }
}