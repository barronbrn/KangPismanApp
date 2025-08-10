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
class HargaViewModel @Inject constructor(private val repository: HargaRepository) : ViewModel(){

    private val _daftarHarga = MutableLiveData<List<Sampah>>()
    val daftarHarga: LiveData<List<Sampah>> = _daftarHarga

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _semuaDaftarHarga = MutableLiveData<List<Sampah>>()
    val semuaDaftarHarga: LiveData<List<Sampah>> = _semuaDaftarHarga

    fun fetchSemuaDaftarHarga() {
        viewModelScope.launch {
            _semuaDaftarHarga.value = repository.getAllDaftarHarga()
        }
    }

    init {
        loadDaftarHarga()
    }

    private fun loadDaftarHarga() {
        viewModelScope.launch {
            _isLoading.value = true
            _daftarHarga.value = repository.getDaftarHarga()
            _isLoading.value = false
        }
    }
}