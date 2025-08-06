package com.example.kangpismanapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kangpismanapp.data.model.Sampah
import com.example.kangpismanapp.data.repository.HargaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HargaViewModel @Inject constructor() : ViewModel(){
    private val repository = HargaRepository()

    private val _daftarHarga = MutableLiveData<List<Sampah>>()
    val daftarHarga: LiveData<List<Sampah>> = _daftarHarga

    // Tambahan: LiveData untuk status loading
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        loadDaftarHarga()
    }

    private fun loadDaftarHarga() {
        // Jalankan coroutine yang terikat dengan siklus hidup ViewModel
        viewModelScope.launch {
            // Tampilkan loading indicator
            _isLoading.value = true
            // Panggil suspend function dari repository
            val data = repository.getDaftarHarga()
            // Update LiveData dengan data yang didapat
            _daftarHarga.value = data
            // Sembunyikan loading indicator
            _isLoading.value = false
        }
    }
}