package com.example.android.politicalpreparedness.representative.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.representative.RepresentativeRepository
import com.example.android.politicalpreparedness.representative.RepresentativeViewModel

class RepresentativeViewModelFactory(
    private val repository: RepresentativeRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RepresentativeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RepresentativeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unable to construct viewModel")
    }
}