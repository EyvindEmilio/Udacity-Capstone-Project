package com.example.android.politicalpreparedness.database

import com.example.android.politicalpreparedness.network.models.Election

interface ElectionDataSource {
    suspend fun insertElections(elections: List<Election>)
    suspend fun getElections(): List<Election>
    suspend fun getElectionById(id: Int): Election?
    suspend fun deleteElection(id: Int)
    suspend fun deleteAllElections()
    suspend fun getElectionsFromServer(): List<Election>
    suspend fun refreshElectionsFromServer()
}