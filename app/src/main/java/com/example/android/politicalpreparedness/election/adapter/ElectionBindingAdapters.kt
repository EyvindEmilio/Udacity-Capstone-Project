package com.example.android.politicalpreparedness.election.adapter

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.network.models.Election

@BindingAdapter("electionsList")
fun bindElectionsList(recyclerView: RecyclerView, data: List<Election>?) {
    val adapter = recyclerView.adapter as ElectionListAdapter
    adapter.submitList(data)
}

@BindingAdapter("showIfLoading")
fun showIfLoading(view: View, isLoading: Boolean) {
    view.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("hideIfLoading")
fun hideIfLoading(view: View, isLoading: Boolean) {
    view.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
}