package com.example.kangpismanapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.kangpismanapp.data.model.UserProfile
import com.example.kangpismanapp.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {
    val userProfile: LiveData<UserProfile?> = repository.getUserProfileData().asLiveData()
}