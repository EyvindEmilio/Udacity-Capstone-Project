package com.example.android.politicalpreparedness.representative

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RepresentativeViewModel(
    val application: Application,
    val representativeDataSource: RepresentativeDataSource
) : ViewModel() {

    companion object {
        private val TAG = RepresentativeViewModel::class.java.simpleName
    }

    //TODO: Establish live data for representatives and address
    private val _representativesLD = MutableLiveData<List<Representative>>()
    val representativesLD: LiveData<List<Representative>>
        get() = _representativesLD

    val addressLD = MutableLiveData<Address>(Address("", "", "", "", ""))

    private val _loader = MutableLiveData<Boolean>()
    val loader: LiveData<Boolean>
        get() = _loader

    private val _statesLD = MutableLiveData<List<String>>()
    val statesLD: LiveData<List<String>>
        get() = _statesLD

    private val _currentStateLD = MutableLiveData<String?>()
    val currentStateLD: LiveData<String?>
        get() = _currentStateLD

    //TODO: Create function to fetch representatives from API from a provided address
    fun setAddress(address: Address) {
        addressLD.value = address
    }

    fun setState(states: List<String>) {
        _statesLD.value = states
    }

    fun setCurrentStatePosition(position: Int) {
        _currentStateLD.value = _statesLD.value?.get(position)
        addressLD.value?.state = _statesLD.value?.get(position) ?: ""
    }

    fun setCurrentState(state: String?) {
        _currentStateLD.value = state
        addressLD.value?.state = state ?: ""
    }

    fun getStatePosition(): Int {
        return _statesLD.value?.indexOf(_currentStateLD.value) ?: -1
    }

    fun loadRepresentatives() {
        Log.d(TAG, "loadRepresentatives()")
        Log.d(TAG, "address = ${addressLD.value?.toFormattedString()}")
        addressLD.value?.let {
            _loader.value = true
            viewModelScope.launch(Dispatchers.IO) {
                val representatives = representativeDataSource.getRepresentative(it)
                withContext(Dispatchers.Main) {
                    _representativesLD.value = representatives
                    _loader.value = true
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

    //TODO: Create function get address from geo location

    //TODO: Create function to get address from individual fields

}
