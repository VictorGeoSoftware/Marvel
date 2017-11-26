package com.test.marvel.vallettest

import android.app.Activity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.squareup.picasso.Picasso
import com.test.marvel.vallettest.di.detailactivity.DetailComicActivityModule
import com.test.marvel.vallettest.models.Comic
import com.test.marvel.vallettest.models.Constants
import com.test.marvel.vallettest.presenters.MarvelPresenter
import com.test.marvel.vallettest.presenters.MarvelView
import com.test.marvel.vallettest.ui.CharactersAdapter
import com.test.marvel.vallettest.ui.CreatorsAdapter
import com.test.marvel.vallettest.ui.SpaceDecorator
import com.test.marvel.vallettest.utils.MyUtils
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
        Picasso.with(this).load(comicImageUrl).into(imgComicBig)
        txtDescription.text = comic.description
        lstCreators.adapter = CreatorsAdapter(comic.creators.items)
        lstCharacters.adapter = CharactersAdapter(comic.characters.items)
    }

    override fun onComicDetailError(error: String) {
        progressBar.visibility = View.INVISIBLE
        Snackbar.make(mainLayout, error, Snackbar.LENGTH_SHORT).show()
    }
}