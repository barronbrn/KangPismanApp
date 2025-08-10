package com.example.kangpismanapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kangpismanapp.data.model.BankSampah
import com.example.kangpismanapp.data.repository.LokasiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LokasiViewModel @Inject constructor(private val repository: LokasiRepository) : ViewModel() {
    private val _bankSampahList = MutableLiveData<List<BankSampah>>()
    val bankSampahList: LiveData<List<BankSampah>> = _bankSampahList

    init {
        fetchBankSampahList()
    }

    private fun fetchBankSampahList() {
        viewModelScope.launch {
            _bankSampahList.value = repository.getBankSampahList()
        }
    }
}