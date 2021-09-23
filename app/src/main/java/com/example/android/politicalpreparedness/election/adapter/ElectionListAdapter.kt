package com.example.android.politicalpreparedness.election.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.databinding.ViewholderElectionBinding
import com.example.android.politicalpreparedness.network.models.Election

class ElectionListAdapter(private val clickListener: ElectionListener) :
    ListAdapter<Election, ElectionViewHolder>(ElectionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
        return ElectionViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        val election = getItem(position)
        holder.bind(election, clickListener)
    }

}

//TODO: Create ElectionViewHolder
class ElectionViewHolder(val binding: ViewholderElectionBinding) :
    RecyclerView.ViewHolder(binding.root) {
    companion object {
        //TODO: Add companion object to inflate ViewHolder (from)
        fun from(view: ViewGroup): ElectionViewHolder {
            val inflater = LayoutInflater.from(view.context)
            val bind = ViewholderElectionBinding.inflate(inflater, view, false)
            return ElectionViewHolder(bind)
        }
    }

    //TODO: Bind ViewHolder
    fun bind(election: Election, listener: ElectionListener) {
        binding.election = election
        binding.listener = listener
        binding.executePendingBindings()
    }
}

class ElectionDiffCallback : DiffUtil.ItemCallback<Election>() {
    override fun areItemsTheSame(oldItem: Election, newItem: Election): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Election, newItem: Election): Boolean {
        return oldItem.id == newItem.id &&
                oldItem.division == newItem.division &&
                oldItem.electionDay == newItem.electionDay &&
                oldItem.name == newItem.name
    }

}

//TODO: Create ElectionListener
interface ElectionListener {
    fun onClick(election: Election)
}