package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.database.ElectionRepository
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import com.example.android.politicalpreparedness.network.models.Election

class ElectionsFragment : Fragment() {

    //TODO: Declare ViewModel
    private lateinit var electionsVM: ElectionsViewModel

    lateinit var bind: FragmentElectionBinding

    companion object {
        private val TAG = ElectionsFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = FragmentElectionBinding.inflate(inflater, container, false)
        //TODO: Add ViewModel values and create ViewModel
        val repository =
            ElectionRepository(ElectionDatabase.getInstance(requireContext()).electionDao)

        electionsVM = ViewModelProvider(
            viewModelStore,
            ElectionsViewModelFactory(repository)
        ).get(ElectionsViewModel::class.java)

        //TODO: Add binding values
        bind.lifecycleOwner = viewLifecycleOwner
        bind.electionViewModel = electionsVM
        bind.invalidateAll()


        //TODO: Link elections to voter info
        electionsVM.navigateElection.observe(viewLifecycleOwner, Observer { election ->
            election?.let {
                findNavController().navigate(
                    ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(
                        election.id, election.division
                    )
                )
                electionsVM.resetNavigation()
            }
        })
        val electionListener = object : ElectionListener {
            override fun onClick(election: Election) {
                electionsVM.onElectionSelect(election)
            }
        }

        //TODO: Initiate recycler adapters
        val electionListAdapter = ElectionListAdapter(electionListener)
        val savedElectionListAdapter = ElectionListAdapter(electionListener)

        //TODO: Populate recycler adapters
        bind.rvUpcomingElections.adapter = electionListAdapter
        bind.rvSavedElections.adapter = savedElectionListAdapter

        bind.executePendingBindings()

        return bind.root
    }

    //TODO: Refresh adapters when fragment loads
    override fun onResume() {
        super.onResume()
        electionsVM.loadUpcomingElections()
        electionsVM.loadSavedElections()

    }
}