package com.training.marvel.source.ui

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.training.marvel.source.R
import com.training.marvel.source.models.CreatorSummary
import kotlinx.android.synthetic.main.adapter_creators.view.*

/**
 * Created by victor on 25/11/17.
 */
class CreatorsAdapter(private val creatorSummary: ArrayList<CreatorSummary>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CreatorViewHolder(parent.inflate(R.layout.adapter_creators))
    }

    override fun getItemCount(): Int {
        return creatorSummary.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CreatorViewHolder) {
            holder.bind(creatorSummary[position])
        }
    }

    class CreatorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(creatorSummary: CreatorSummary) = with(itemView) {
            txtCreatorName.text = creatorSummary.name
            txtCreatorRole.text = creatorSummary.role
        }
    }
}