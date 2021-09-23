package com.example.android.politicalpreparedness.election.adapter

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse

@BindingAdapter("showIfVoterInfo")
fun showIfVoterInfo(view: View, voterInfoResponse: VoterInfoResponse?) {
    view.visibility = if (voterInfoResponse != null) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("showIfNoData")
fun showIfNoData(view: View, noData: Boolean) {
    view.visibility = if (noData) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("hideIfNoData")
fun hideIfNoData(view: View, noData: Boolean) {
    view.visibility = if (!noData) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("showIfLocations")
fun showIfLocations(view: View, voterInfoResponse: VoterInfoResponse?) {
    view.visibility =
        if (voterInfoResponse?.state?.firstOrNull()?.electionAdministrationBody?.votingLocationFinderUrl != null) {
            View.VISIBLE
        } else {
            View.GONE
        }
}

@BindingAdapter("showIfBallot")
fun showIfBallot(view: View, voterInfoResponse: VoterInfoResponse?) {
    view.visibility =
        if (voterInfoResponse?.state?.firstOrNull()?.electionAdministrationBody?.ballotInfoUrl != null) {
            View.VISIBLE
        } else {
            View.GONE
        }
}

@BindingAdapter("showIfAddress")
fun showIfAddress(view: View, voterInfoResponse: VoterInfoResponse?) {
    view.visibility =
        if (voterInfoResponse?.state?.firstOrNull()?.electionAdministrationBody?.correspondenceAddress != null) {
            View.VISIBLE
        } else {
            View.GONE
        }
}

@BindingAdapter("textAddressFromVoterInfo")
fun textAddressFromVoterInfo(view: TextView, voterInfoResponse: VoterInfoResponse?) {
    view.text =
        voterInfoResponse?.state?.firstOrNull()?.electionAdministrationBody?.correspondenceAddress?.toFormattedString()
            ?: ""
}

@BindingAdapter("textToggleFollowVoterInfo")
fun textToggleFollowVoterInfo(view: TextView, isSaved: Boolean) {
    view.setText(if (isSaved) R.string.unfollow_election else R.string.follow_election)
}