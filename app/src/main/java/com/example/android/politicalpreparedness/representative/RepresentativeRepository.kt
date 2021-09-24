package com.example.android.politicalpreparedness.representative

import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class RepresentativeRepository : RepresentativeDataSource {
    override suspend fun getRepresentative(address: Address): List<Representative> {
        return withContext(Dispatchers.IO) {
            try {
                val representativeResponse =
                    CivicsApi.retrofitService.getRepresentatives(address.toFormattedString())
                        .execute().body()
                representativeResponse?.asRepresentatives() ?: listOf()
            } catch (e: HttpException) {
                listOf<Representative>()
            } catch (e: IOException) {
                listOf<Representative>()
            }
        }
    }
}

fun RepresentativeResponse.asRepresentatives(): List<Representative> {
    val mutableList = mutableListOf<Representative>()
    for (index in this.offices.indices) {
        mutableList.add(Representative(this.officials[index], this.offices[index]))
    }
    return mutableList
}