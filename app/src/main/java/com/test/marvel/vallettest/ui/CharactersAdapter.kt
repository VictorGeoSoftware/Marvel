package com.test.marvel.vallettest.ui

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.test.marvel.vallettest.R
import com.test.marvel.vallettest.models.CharacterSummary
import kotlinx.android.synthetic.main.adapter_creators.view.*

/**
 * Created by victor on 25/11/17.
 *
 */
class CharactersAdapter(private val characterSummary: ArrayList<CharacterSummary>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CreatorViewHolder(parent.inflate(R.layout.adapter_creators))
    }

    override fun getItemCount(): Int {
        return characterSummary.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CreatorViewHolder) {
            holder.bind(characterSummary[position])
        }
    }

    class CreatorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(characterSummary: CharacterSummary) = with(itemView) {
            txtCreatorName.text = characterSummary.name
            txtCreatorRole.visibility = View.GONE
        }
    }
}