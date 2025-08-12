package com.example.kangpismanapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.kangpismanapp.data.model.Transaksi
import com.example.kangpismanapp.data.repository.ProfileRepository
import com.example.kangpismanapp.data.repository.TransaksiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransaksiViewModel @Inject constructor(
    private val transaksiRepository: TransaksiRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _transaksiList = MutableLiveData<List<Transaksi>>()
    val transaksiList: LiveData<List<Transaksi>> = _transaksiList

    val latestTransaksiList: LiveData<List<Transaksi>> = transaksiRepository.getLatestTransaksi().asLiveData()

    init {
        fetchTransaksiBasedOnRole()
    }

    private fun fetchTransaksiBasedOnRole() {
        viewModelScope.launch {
            val role = profileRepository.getUserRole()
            if (role == "petugas") {
                // Jika petugas, panggil fungsi untuk petugas
                transaksiRepository.getTransaksiHistoryForPetugas().collect {
                    _transaksiList.value = it
                }
            } else {
                // Jika warga, panggil fungsi untuk warga
                transaksiRepository.getTransaksiHistoryForWarga().collect {
                    _transaksiList.value = it
                }
            }
        }
    }
}