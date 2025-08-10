package com.example.kangpismanapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kangpismanapp.data.model.Sampah
import com.example.kangpismanapp.data.model.Transaksi
import com.example.kangpismanapp.data.repository.TimbangRepository
import com.example.kangpismanapp.data.model.DraftTransaksi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimbangViewModel @Inject constructor(
    private val repository: TimbangRepository
) : ViewModel() {

    private val _daftarMaterial = MutableLiveData<List<Sampah>>()
    val daftarMaterial: LiveData<List<Sampah>> = _daftarMaterial

    private val _isTransaksiSaved = MutableLiveData<Boolean>()
    val isTransaksiSaved: LiveData<Boolean> = _isTransaksiSaved

    private val _draftTransaksi = MutableLiveData<DraftTransaksi?>()
    val draftTransaksi: LiveData<DraftTransaksi?> = _draftTransaksi

    init {
        fetchDaftarMaterial()
    }

    private fun fetchDaftarMaterial() {
        viewModelScope.launch {
            _daftarMaterial.value = repository.getDaftarMaterial()
        }
    }

    fun loadDraftTransaksi(draftId: String) {
        viewModelScope.launch {
            _draftTransaksi.value = repository.getDraftById(draftId)
        }
    }

    fun simpanTransaksi(transaksi: Transaksi) {
        viewModelScope.launch {
            val success = repository.simpanTransaksi(transaksi)
            _isTransaksiSaved.value = success
        }
    }

    fun updateDraftStatus(draftId: String, status: String) {
        viewModelScope.launch {
            repository.updateDraftStatus(draftId, status)
        }
    }

    fun clearDraft() {
        _draftTransaksi.value = null
    }
}
