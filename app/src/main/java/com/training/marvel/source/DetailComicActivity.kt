package com.training.marvel.source

import android.app.Activity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.bumptech.glide.Glide
import com.training.marvel.source.di.detailactivity.DetailComicActivityModule
import com.training.marvel.source.models.Comic
import com.training.marvel.source.models.Constants
import com.training.marvel.source.presenters.MarvelPresenter
import com.training.marvel.source.presenters.MarvelView
import com.training.marvel.source.ui.CharactersAdapter
import com.training.marvel.source.ui.CreatorsAdapter
import com.training.marvel.source.ui.SpaceDecorator
import com.training.marvel.source.utils.MyUtils
import kotlinx.android.synthetic.main.activity_comic_detail.*
import javax.inject.Inject

/**
 * Created by victor on 21/11/17.
 *
 */
class DetailComicActivity:AppCompatActivity(), MarvelView {
    val Activity.app: ParentApplication
        get() = application as ParentApplication

    val component by lazy { app.component.plus(DetailComicActivityModule(this)) }

    @Inject lateinit var marvelPresenter: MarvelPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comic_detail)
        component.inject(this)

        marvelPresenter.setView(this)
        val comicId:Long = intent.getLongExtra(Constants.COMIC_ID, 0)
        val comicTitle:String = intent.getStringExtra(Constants.COMIC_TITLE)
        marvelPresenter.getComicDetail(comicId)

        val creatorsLayoutManager = LinearLayoutManager(this)
        creatorsLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        lstCreators.layoutManager = creatorsLayoutManager
        lstCreators.addItemDecoration(SpaceDecorator(MyUtils.getDpFromValue(this, 10)))

        val charactersLayoutManager = LinearLayoutManager(this)
        charactersLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        lstCharacters.layoutManager = charactersLayoutManager
        lstCharacters.addItemDecoration(SpaceDecorator(MyUtils.getDpFromValue(this, 10)))

        toolBar.title = comicTitle
        toolBar.setNavigationOnClickListener { finish() }
    }



    // -------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------ MARVEL VIEW ------------------------------------------------------------
    override fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    override fun onSuperHeroComicsReceived(comicList: ArrayList<Comic>) { }

    override fun onSuperHeroComicsError(error: String) { }

    override fun onComicDetailReceived(comic: Comic) {
        progressBar.visibility = View.INVISIBLE
        val comicImageUrl = comic.thumbnail.path + "." + comic.thumbnail.extension
        Glide.with(this).load(comicImageUrl).into(imgComicBig)
        txtDescription.text = comic.description
        lstCreators.adapter = CreatorsAdapter(comic.creators.items)
        lstCharacters.adapter = CharactersAdapter(comic.characters.items)
    }

    override fun onComicDetailError(error: String) {
        progressBar.visibility = View.INVISIBLE
        Snackbar.make(mainLayout, error, Snackbar.LENGTH_SHORT).show()
    }
}