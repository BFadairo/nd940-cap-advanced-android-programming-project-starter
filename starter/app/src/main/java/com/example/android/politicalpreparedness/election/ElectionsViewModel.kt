package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.models.Election

//TODO: Construct ViewModel and provide election datasource
class ElectionsViewModel(private val datasource: ElectionDao): ViewModel() {

    //TODO: Create live data val for upcoming elections
    private val _upcomingElections = MutableLiveData<List<Election>>()
    val upcomingElections: LiveData<List<Election>>
        get() = _upcomingElections

    //TODO: Create live data val for saved elections
    private val _savedElections = MutableLiveData<List<Election>>()
    val savedElections: LiveData<List<Election>>
        get() = _savedElections

    private val _eventNavigateToSavedElectionInfo = MutableLiveData<Boolean>()
    val eventNavigateToSavedElectionInfo: LiveData<Boolean>
        get() = _eventNavigateToSavedElectionInfo

    private val _eventNavigateToUpcomingElectionInfo = MutableLiveData<Boolean>()
    val eventNavigateToUpcomingElectionInfo: LiveData<Boolean>
        get() = _eventNavigateToUpcomingElectionInfo

    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database

    //TODO: Create functions to navigate to saved or upcoming election voter info

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