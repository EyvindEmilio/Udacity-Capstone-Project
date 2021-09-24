package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDataSource
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//TODO: Construct ViewModel and provide election datasource
class ElectionsViewModel(
    private val dataSource: ElectionDataSource
) : ViewModel() {
    private val _loadingUpcoming = MutableLiveData<Boolean>()
    val loadingUpcoming: LiveData<Boolean>
        get() = _loadingUpcoming

    private val _loadingSavedElections = MutableLiveData<Boolean>()
    val loadingSavedElections: LiveData<Boolean>
        get() = _loadingSavedElections

    private val _navigateElection = MutableLiveData<Election?>()
    val navigateElection: LiveData<Election?>
        get() = _navigateElection

    //TODO: Create live data val for upcoming elections
    private val _electionsList = MutableLiveData<List<Election>>()
    val electionsList: LiveData<List<Election>>
        get() = _electionsList

    //TODO: Create live data val for saved elections
    private val _savedElectionsList = MutableLiveData<List<Election>>()
    val savedElectionsList: LiveData<List<Election>>
        get() = _savedElectionsList

    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database
    fun loadUpcomingElections() {
        _loadingUpcoming.value = true
        viewModelScope.launch(Dispatchers.Default) {
            val elections = dataSource.getElectionsFromServer()
            withContext(Dispatchers.Main) {
                _electionsList.value = elections
                _loadingUpcoming.value = false
            }
        }
    }

    fun loadSavedElections() {
        _loadingSavedElections.value = true
        viewModelScope.launch(Dispatchers.Default) {
            val elections = dataSource.getElections()
            withContext(Dispatchers.Main) {
                _savedElectionsList.value = elections
                _loadingSavedElections.value = false
            }
        }
    }

    //TODO: Create functions to navigate to saved or upcoming election voter info
    fun onElectionSelect(election: Election) {
        _navigateElection.value = election
    }

    fun resetNavigation() {
        _navigateElection.value = null
    }
}