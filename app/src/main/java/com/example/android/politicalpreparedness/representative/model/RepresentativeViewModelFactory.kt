package com.example.android.politicalpreparedness.representative.model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.representative.RepresentativeRepository
import com.example.android.politicalpreparedness.representative.RepresentativeViewModel

class RepresentativeViewModelFactory(
    val app: Application,
    private val repository: RepresentativeRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RepresentativeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RepresentativeViewModel(app,repository) as T
        }
        throw IllegalArgumentException("Unable to construct viewModel")
    }
}