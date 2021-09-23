package com.example.android.politicalpreparedness.database

import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class ElectionLocalRepository(
    private val electionsDao: ElectionDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ElectionDataSource {
    override suspend fun insertElections(elections: List<Election>) {
        electionsDao.insertElections(elections)
    }

    override suspend fun getElections(): List<Election> {
        return electionsDao.getElections()
    }

    override suspend fun getElectionById(id: Int): Election? {
        return electionsDao.getElectionById(id)
    }

    override suspend fun deleteElection(id: Int) {
        electionsDao.deleteElection(id)
    }

    override suspend fun deleteAllElections() {
        electionsDao.deleteAllElections()
    }

    override suspend fun getElectionsFromServer(): List<Election> {
        return withContext(Dispatchers.IO) {
            try {
                val electionResponse = CivicsApi.retrofitService.getElections().execute().body()
                electionResponse?.elections ?: listOf()
            } catch (e: HttpException) {
                listOf<Election>()
            } catch (e: IOException) {
                listOf<Election>()
            }
        }
    }

    override suspend fun refreshElectionsFromServer() {
        val elections = getElectionsFromServer()
        withContext(Dispatchers.Default) {
            deleteAllElections()
            insertElections(elections)
        }
    }

}