package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.data.ElectionRepository
import com.example.android.politicalpreparedness.data.local.database.ElectionDao
import com.example.android.politicalpreparedness.data.network.models.Address
import com.example.android.politicalpreparedness.data.network.models.Election
import com.example.android.politicalpreparedness.data.network.models.Result
import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse
import kotlinx.coroutines.launch
import timber.log.Timber

class VoterInfoViewModel(private val politicalRepository: ElectionRepository, private val electionId: Long) : ViewModel() {

    private val _voterInfo = MutableLiveData<VoterInfoResponse>()
    val voterInfo: LiveData<VoterInfoResponse>
        get() = _voterInfo

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage

    private val _isSaved = MutableLiveData<Boolean>()
    val isSaved: LiveData<Boolean>
        get() = _isSaved

    private val _election = MutableLiveData<Election>()
    val election: LiveData<Election>
        get() = _election

    private val _ballotInformationUrl = MutableLiveData<String?>()
    val ballotInformationUrl: LiveData<String?>
        get() = _ballotInformationUrl

    private val _voterInformationUrl = MutableLiveData<String?>()
    val voterInformationUrl: LiveData<String?>
        get() = _voterInformationUrl

    init {
        checkIfElectionInDb()
    }

    private fun parseVoterInfoResponse(response: VoterInfoResponse) {
        _election.value = response.election
    }

    private fun retrieveVoterInfo(address: Address) {
        viewModelScope.launch {
            val voterInfo = politicalRepository.getVoterInfo(address.toFormattedString(), electionId)
            when (voterInfo) {
                is Result.Success<VoterInfoResponse> -> {
                    _voterInfo.postValue(voterInfo.data)
                    parseVoterInfoResponse(voterInfo.data)
                }
                is Result.Error -> {
                    _toastMessage.postValue(voterInfo.message.toString())
                }
            }
        }
    }

    private fun saveElectionToDB(election: Election) {
        viewModelScope.launch {
            val result = politicalRepository.insertElection(election)
            when (result) {
                is Result.Success -> {
                    _toastMessage.postValue("Election Followed")
                    _isSaved.postValue(true)
                }
                is Result.Error -> {
                    _toastMessage.postValue(result.message?.toString())
                }
            }
        }
    }

    private fun removeElectionFromDB(electionId: Long) {
        viewModelScope.launch {
            val result = politicalRepository.deleteElection(electionId)
            when (result) {
                is Result.Success -> {
                    _toastMessage.postValue("Election Unfollowed")
                    _isSaved.postValue(false)
                }
                is Result.Error -> {
                    _toastMessage.postValue(result.message.toString())
                }
            }
        }
    }

    private fun checkIfElectionInDb() {
        viewModelScope.launch {
            val result = politicalRepository.getElectionById(electionId)
            when (result) {
                is Result.Success -> {
                    _isSaved.postValue(true)
                }
                is Result.Error -> {
                    _isSaved.postValue(false)
                }
            }
        }
    }

    fun followElectionButtonClicked() {
        val isSaved = _isSaved.value
        viewModelScope.launch {
            when (isSaved) {
                true -> {
                    removeElectionFromDB(electionId)
                }

                false -> {
                    if (_election.value != null) {
                        election.value?.let { saveElectionToDB(it) }
                    }
                }
            }
        }
    }

    fun validateAddress(passedAddress: Address?) {
        if (passedAddress != null) {
            retrieveVoterInfo(passedAddress)
        }
    }

    fun eventBallotInformation() {
        _ballotInformationUrl.value = _voterInfo.value?.state?.get(0)?.electionAdministrationBody?.ballotInfoUrl
    }

    fun eventBallotInformationFinished() {
        _ballotInformationUrl.value = null
    }

    fun eventVoterInformation() {
        _voterInformationUrl.value = _voterInfo.value?.state?.get(0)?.electionAdministrationBody?.votingLocationFinderUrl
    }

    fun eventVoterInformationFinished() {
        _voterInformationUrl.value = null
    }

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

}