package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.database.ElectionRepository
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding

class VoterInfoFragment : Fragment() {

    lateinit var voterInfoVM: VoterInfoViewModel
    lateinit var bind: FragmentVoterInfoBinding

    companion object {
        private val TAG = VoterInfoFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentVoterInfoBinding.inflate(inflater, container, false)

        //TODO: Add ViewModel values and create ViewModel
        val dataSource =
            ElectionRepository(ElectionDatabase.getInstance(requireContext()).electionDao)
        voterInfoVM = ViewModelProvider(viewModelStore, VoterInfoViewModelFactory(dataSource)).get(
            VoterInfoViewModel::class.java
        )

        //TODO: Add binding values
        bind.lifecycleOwner = viewLifecycleOwner
        bind.voterInfoViewModel = voterInfoVM

        //TODO: Populate voter info -- hide views without provided data.
        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
         */
        val args = VoterInfoFragmentArgs.fromBundle(arguments!!)
        Log.d(TAG, "Ars: ElectionId=${args.argElectionId}, division=${args.argDivision}")
        voterInfoVM.setElectionId(args.argElectionId)
        voterInfoVM.setDivision(args.argDivision)

        voterInfoVM.checkElectionSaved()
        voterInfoVM.loadVoterInfo()

        //TODO: Handle loading of URLs
        voterInfoVM.loadUrlLD.observe(viewLifecycleOwner, Observer { url ->
            url?.let {
                launchFromUrl(it)
                voterInfoVM.resetLoadUrl()
            }
        })

        //TODO: Handle save button UI state
        //handled with dataBinding
        //TODO: cont'd Handle save button clicks
        //handled with dataBinding

        bind.executePendingBindings()
        bind.invalidateAll()
        return bind.root
    }

    //TODO: Create method to load URL intents
    private fun launchFromUrl(url: String) {
        Log.d(TAG, "launchFromUrl() url=$url")
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

}