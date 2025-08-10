package com.example.kangpismanapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.kangpismanapp.data.model.Transaksi
import com.example.kangpismanapp.data.repository.TransaksiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransaksiViewModel @Inject constructor(
    private val repository: TransaksiRepository
) : ViewModel() {

    val transaksiList: LiveData<List<Transaksi>> = repository.getTransaksiHistory().asLiveData()
    val latestTransaksiList: LiveData<List<Transaksi>> = repository.getLatestTransaksi().asLiveData()

}