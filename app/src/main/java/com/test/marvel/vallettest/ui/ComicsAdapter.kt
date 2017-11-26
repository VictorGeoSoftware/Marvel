package com.test.marvel.vallettest.ui

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.test.marvel.vallettest.R
import com.test.marvel.vallettest.models.Comic
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.adapter_comics.view.*

/**
 * Created by victor on 20/11/17.
 *
 */

class ComicsAdapter(val comicList:ArrayList<Comic>, val listener: ComicAdapterListener) : RecyclerView.Adapter<ComicsAdapter.ComicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ComicViewHolder =
            ComicViewHolder(parent!!.inflate(R.layout.adapter_comics))

    override fun onBindViewHolder(holder: ComicViewHolder?, position: Int) {
        holder?.bind(comicList[position], listener)
    }

    override fun getItemCount(): Int = comicList.size



    class ComicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(comic: Comic, listener: ComicAdapterListener) = with(itemView) {

            val comicImageUrl = comic.thumbnail.path + "." + comic.thumbnail.extension
            Picasso.with(itemView.context).load(comicImageUrl).into(imgComic)
            txtTitle.text = comic.title
            txtDetail.text = comic.description

            setOnClickListener { listener.onComicSelected(comic) }
        }
    }

    interface ComicAdapterListener {
        fun onComicSelected(comic:Comic)
    }
}


fun ViewGroup.inflate(layoutRes: Int): View =
        LayoutInflater.from(context).inflate(layoutRes, this, false)