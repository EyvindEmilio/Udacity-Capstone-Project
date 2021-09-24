package com.example.android.politicalpreparedness.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.database.ElectionDataSource

//TODO: Create Factory to generate ElectionViewModel with provided election datasource
class ElectionsViewModelFactory(
    private val electionDataSource: ElectionDataSource
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ElectionsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ElectionsViewModel(electionDataSource) as T
        }
        throw IllegalArgumentException("Unable to construct viewModel")
    }
}