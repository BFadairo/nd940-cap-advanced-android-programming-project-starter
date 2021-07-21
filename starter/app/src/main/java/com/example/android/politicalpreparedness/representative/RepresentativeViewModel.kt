package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.data.ElectionRepository
import com.example.android.politicalpreparedness.data.network.models.Address
import com.example.android.politicalpreparedness.data.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.data.network.models.Result
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch

class RepresentativeViewModel(private val repository: ElectionRepository): ViewModel() {

    private val _representativesList = MutableLiveData<List<Representative>>()
    val representativeList: LiveData<List<Representative>>
        get() = _representativesList

    // LiveData used for two-way binding the Address
    val addressLineOne = MutableLiveData<String>()
    val addressLineTwo = MutableLiveData<String>()
    val city = MutableLiveData<String>()
    val state = MutableLiveData<String>()
    val zip = MutableLiveData<String>()

    private val _eventSearch = MutableLiveData<Boolean>()
    val eventSearch: LiveData<Boolean>
        get() = _eventSearch

    private val _eventGetMyLocation = MutableLiveData<Boolean>()
    val eventGetMyLocation: LiveData<Boolean>
        get() = _eventGetMyLocation

    private val _representativesLoading = MutableLiveData<Boolean>()
    val representativesLoading: LiveData<Boolean>
        get() = _representativesLoading

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage

    private lateinit var address: Address

    fun getRepresentatives(address: Address) {
        _representativesLoading.value = true
        viewModelScope.launch {
            val representativeResponse = repository.getRepresentativesByAddress(address.toFormattedString())

            when (representativeResponse) {
                is Result.Success<RepresentativeResponse> -> {
                    val (offices, officials) = representativeResponse.data
                    _representativesList.postValue(offices.flatMap { office -> office.getRepresentatives(officials) })
                }

                is Result.Error -> {
                    _toastMessage.value = representativeResponse.message.toString()
                }
            }
        }
    }

    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */

    fun validateAddress() {
        val line1 = addressLineOne.value
        val line2 = addressLineTwo.value
        val city = city.value
        val state = state.value
        val zip = zip.value

        if (line1 != null && city != null && state != null && zip != null) {
            address = Address(line1, line2, city, state, zip)
            getRepresentatives(address)
        } else {
            // Show a toast to show the address is not valid
            _toastMessage.value = "Please double check you have entered all required information"
        }
    }

    fun validateGeocodeLocationAddress(passedAddress: Address) {
        this.address = passedAddress
        addressLineOne.value = passedAddress.line1
        addressLineTwo.value = passedAddress.line2 ?: ""
        city.value = passedAddress.city
        state.value = passedAddress.state
        zip.value = passedAddress.zip
        getRepresentatives(passedAddress)
    }

    fun eventGetMyLocation() {
        _eventGetMyLocation.value = true
    }

    fun eventGetMyLocationFinished() {
        _eventGetMyLocation.value = false
    }

    fun eventSearch() {
        _eventSearch.value = true
    }

    fun eventSearchFinished() {
        _eventSearch.value = false
    }

    /*
    Using this method to set the state manual as two-way binding doesn't appear to work
     */
    fun setState(state: String) {
        this.state.value = state
    }

}
