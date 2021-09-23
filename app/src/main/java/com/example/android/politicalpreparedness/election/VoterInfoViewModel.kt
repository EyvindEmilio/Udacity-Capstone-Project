package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDataSource
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VoterInfoViewModel(private val dataSource: ElectionDataSource) : ViewModel() {

    companion object {
        private val TAG = VoterInfoViewModel::class.java.simpleName
    }


    //TODO: Add live data to hold voter info

    private val _loader = MutableLiveData<Boolean>()
    val loader: LiveData<Boolean>
        get() = _loader

    private val _noDataFound = MutableLiveData<Boolean>()
    val noDataFound: LiveData<Boolean>
        get() = _noDataFound

    private val _voterInfoLD = MutableLiveData<VoterInfoResponse?>()
    val voterInfoLD: LiveData<VoterInfoResponse?>
        get() = _voterInfoLD

    private val _electionLD = MutableLiveData<Election?>()
    val electionLD: LiveData<Election?>
        get() = _electionLD

    private val _electionIdLD = MutableLiveData<Int>()
    val electionIdLD: LiveData<Int>
        get() = _electionIdLD

    private val _divisionLD = MutableLiveData<Division>()
    val divisionLD: LiveData<Division>
        get() = _divisionLD

    private val _loadUrlLD = MutableLiveData<String?>()
    val loadUrlLD: LiveData<String?>
        get() = _loadUrlLD

    //TODO: Add var and methods to populate voter info

    fun setElectionId(electionId: Int) {
        _electionIdLD.value = electionId
    }

    fun setDivision(division: Division) {
        _divisionLD.value = division
    }

    fun loadVoterInfo() {
        _loader.value = true
        _noDataFound.value = false
        val electionId = _electionIdLD.value ?: -1
        var address = _divisionLD.value?.country ?: ""
        _divisionLD.value?.state?.let { address += "/$it" }

        viewModelScope.launch(Dispatchers.IO) {
            val voterInfo = dataSource.getVoterFromServer(address, electionId)
            withContext(Dispatchers.Main) {
                _voterInfoLD.value = voterInfo
                _electionLD.value = voterInfo?.election
                _loader.value = false
                _noDataFound.value = voterInfo == null
            }
        }
    }

    //TODO: Add var and methods to support loading URLs
    fun openLocationsUrl() {
        _loadUrlLD.value =
            _voterInfoLD.value?.state?.firstOrNull()?.electionAdministrationBody?.votingLocationFinderUrl
    }

    fun openBallotUrl() {
        _loadUrlLD.value =
            _voterInfoLD.value?.state?.firstOrNull()?.electionAdministrationBody?.ballotInfoUrl
    }

    fun resetLoadUrl() {
        _loadUrlLD.value = null
    }

    //TODO: Add var and methods to save and remove elections to local database
    private val _isElectionSavedLD = MutableLiveData<Boolean>()
    val isElectionSavedLD: LiveData<Boolean>
        get() = _isElectionSavedLD

    fun checkElectionSaved() {
        val electionId = _electionIdLD.value ?: -1
        viewModelScope.launch(Dispatchers.Default) {
            val election = dataSource.getElectionById(electionId)
            withContext(Dispatchers.Main) {
                _isElectionSavedLD.value = election != null
            }
        }
    }

    fun followElection() {
        Log.d(TAG, "followElection()")
        this.electionLD.value?.let { election ->
            viewModelScope.launch(Dispatchers.Default) {
                dataSource.insertElections(listOf(election))
                withContext(Dispatchers.Main) { checkElectionSaved() }
            }
        }
    }

    fun unfollowElection() {
        Log.d(TAG, "unfollowElection()")
        this.electionIdLD.value?.let { electionId ->
            viewModelScope.launch(Dispatchers.Default) {
                dataSource.deleteElection(electionId)
                withContext(Dispatchers.Main) { checkElectionSaved() }
            }
        }
    }

    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

}