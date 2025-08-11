package com.example.kangpismanapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.kangpismanapp.data.model.UserProfile
import com.example.kangpismanapp.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {
    val userProfile: LiveData<UserProfile?> = repository.getUserProfileData().asLiveData()
    fun updateUserProfile(username: String, noTelepon: String, alamat: String) {
        viewModelScope.launch {
            // Panggil fungsi di repository untuk memperbarui data
            repository.updateUserProfile(username, noTelepon, alamat)
        }
    }
}