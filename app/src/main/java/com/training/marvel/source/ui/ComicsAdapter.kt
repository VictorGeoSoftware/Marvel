package com.training.marvel.source.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.training.marvel.source.R
import com.training.marvel.source.models.Comic
import kotlinx.android.synthetic.main.adapter_comics.view.*
import java.util.*


/**
 * Created by victor on 20/11/17.
 *
 */

class ComicsAdapter(private val comicList:ArrayList<Comic>, private val listener: ComicAdapterListener):
        RecyclerView.Adapter<ComicsAdapter.ComicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicViewHolder =
            ComicViewHolder(parent.inflate(R.layout.adapter_comics))

    override fun onBindViewHolder(holder: ComicViewHolder, position: Int) {
        holder.bind(comicList[position], listener)
    }

    override fun getItemCount(): Int = comicList.size



    class ComicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(comic: Comic, listener: ComicAdapterListener) = with(itemView) {

            txtTitle.text = comic.title
            txtDetail.text = comic.description

            val comicImageUrl = comic.thumbnail.path + "." + comic.thumbnail.extension
            Glide.with(itemView.context).load(comicImageUrl).into(imgComic)

            setOnClickListener { listener.onComicSelected(comic) }
        }
    }

    interface ComicAdapterListener {
        fun onComicSelected(comic:Comic)
    }
}


fun ViewGroup.inflate(layoutRes: Int): View =
        LayoutInflater.from(context).inflate(layoutRes, this, false)