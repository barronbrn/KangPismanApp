package com.example.kangpismanapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kangpismanapp.data.model.Artikel
import com.example.kangpismanapp.data.repository.EdukasiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EdukasiViewModel  @Inject constructor(private val repo: EdukasiRepository) : ViewModel(){
    private val _articles = MutableLiveData<List<Artikel>>()
    val articles: LiveData<List<Artikel>> = _articles
    init {
        fetchArticles()
    }
    private fun fetchArticles() {
        viewModelScope.launch {
            val data = repo.getArticles()
            Log.d("EDUKASI_DEBUG", "ViewModel: Menerima ${data.size} artikel dari Repository.")
            _articles.value = data
        }
    }
}