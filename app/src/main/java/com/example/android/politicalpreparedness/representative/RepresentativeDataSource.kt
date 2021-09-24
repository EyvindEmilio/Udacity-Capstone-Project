package com.example.android.politicalpreparedness.representative

import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative

interface RepresentativeDataSource {
    suspend fun getRepresentative(address: Address): List<Representative>
}