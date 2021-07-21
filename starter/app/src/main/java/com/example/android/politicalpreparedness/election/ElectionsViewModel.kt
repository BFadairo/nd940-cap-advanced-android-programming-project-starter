package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.data.ElectionRepository
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.data.network.models.Result
import kotlinx.coroutines.launch
import timber.log.Timber

class ElectionsViewModel(private val politicalRepository: ElectionRepository): ViewModel() {

    private val _upcomingElections = MutableLiveData<List<Election>>()
    val upcomingElections: LiveData<List<Election>>
        get() = _upcomingElections

    private val _savedElections = MutableLiveData<List<Election>>()
    val savedElections: LiveData<List<Election>>
        get() = _savedElections

    private val _upcomingElectionsLoading = MutableLiveData<Boolean>()
    val upcomingElectionsLoading: LiveData<Boolean>
        get() = _upcomingElectionsLoading

    private val _savedElectionsLoading = MutableLiveData<Boolean>()
    val savedElectionsLoading: LiveData<Boolean>
        get() = _savedElectionsLoading

    private val _eventNavigateToSavedElectionInfo = MutableLiveData<Boolean>()
    val eventNavigateToSavedElectionInfo: LiveData<Boolean>
        get() = _eventNavigateToSavedElectionInfo

    private val _eventNavigateToUpcomingElectionInfo = MutableLiveData<Boolean>()
    val eventNavigateToUpcomingElectionInfo: LiveData<Boolean>
        get() = _eventNavigateToUpcomingElectionInfo

    fun retrieveUpcomingElections() {
        _upcomingElectionsLoading.value = true
        viewModelScope.launch {
            val upcomingElections = politicalRepository.getElections()
            // Do I want to add some loading indiactor here?
            when (upcomingElections) {
                is Result.Success<List<Election>> -> {
                    Timber.i("${upcomingElections.data}")
                    _upcomingElections.postValue(upcomingElections.data)
                    _upcomingElectionsLoading.postValue(false)
                }
                is Result.Error -> {
                    Timber.e(upcomingElections.message)
                    _upcomingElectionsLoading.postValue(false)
                }
            }
        }
    }

    fun retrieveSavedElections() {
        _savedElectionsLoading.value = true
        viewModelScope.launch {
            val savedElections = politicalRepository.getAllElections()
            when (savedElections) {
                is Result.Success<List<Election>> -> {
                    _savedElections.postValue(savedElections.data)
                    _savedElectionsLoading.postValue(false)
                }
                is Result.Error -> {
                    Timber.e(savedElections.message)
                    _savedElectionsLoading.postValue(false)
                }
            }
        }
    }

    fun navigateToUpcomingElectionInfo() {
        _eventNavigateToUpcomingElectionInfo.value = true
    }

    fun navigateToUpcomingElectionInfoFinished() {
        _eventNavigateToUpcomingElectionInfo.value = false
    }

    fun navigateToSavedElectionInfo() {
        _eventNavigateToSavedElectionInfo.value = true
    }

    fun navigateToSavedElectionInfoFinished() {
        _eventNavigateToSavedElectionInfo.value = false
    }
}