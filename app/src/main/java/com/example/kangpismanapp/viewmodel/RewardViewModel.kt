package com.example.kangpismanapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kangpismanapp.data.model.Reward
import com.example.kangpismanapp.data.repository.RewardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RewardViewModel @Inject constructor(
    private val repository: RewardRepository
) : ViewModel() {

    private val _rewardsList = MutableLiveData<List<Reward>>()
    val rewardsList: LiveData<List<Reward>> = _rewardsList

    init {
        fetchRewards()
    }

    private fun fetchRewards() {
        viewModelScope.launch {
            val data = repository.getRewards()
            Log.d("REWARD_DEBUG", "ViewModel: Menerima ${data.size} item reward dari Repository.")
            _rewardsList.value = data
        }
    }
}