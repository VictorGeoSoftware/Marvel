package com.training.marvel.source

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.training.marvel.source.di.mainactivity.MainActivityModule
import com.training.marvel.source.models.Comic
import com.training.marvel.source.models.Constants
import com.training.marvel.source.presenters.MarvelPresenter
import com.training.marvel.source.presenters.MarvelView
import com.training.marvel.source.ui.ComicsAdapter
import com.training.marvel.source.ui.SpaceDecorator
import com.training.marvel.source.utils.MyUtils
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MarvelView {
    val Activity.app: ParentApplication
        get() = application as ParentApplication

    val component by lazy { app.component.plus(MainActivityModule(this)) }

    @Inject lateinit var marvelPresenter:MarvelPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        component.inject(this)

        marvelPresenter.setView(this)
        marvelPresenter.getSuperHeroComics()

        val myGridLayoutManager = GridLayoutManager(this, 2)
        lstComics?.layoutManager = myGridLayoutManager
        lstComics.addItemDecoration(SpaceDecorator(MyUtils.getDpFromValue(this, 10)))
    }

    override fun onDestroy() {
        super.onDestroy()
        marvelPresenter.onDestroy()
    }



    // -------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------ MARVEL VIEW ------------------------------------------------------------
    override fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    override fun onSuperHeroComicsReceived(comicList:ArrayList<Comic>) {
        progressBar.visibility = View.INVISIBLE
        comicList.sort()

        lstComics.adapter = ComicsAdapter(comicList, object : ComicsAdapter.ComicAdapterListener {
            override fun onComicSelected(comic: Comic) {
                launchComicDetailActivity(comic)
            }
        })
    }

    override fun onSuperHeroComicsError(error:String) {
        Snackbar.make(mainLayout, error, Snackbar.LENGTH_SHORT).show()
    }

    override fun onComicDetailReceived(comic: Comic) { }

    override fun onComicDetailError(error: String) {
        progressBar.visibility = View.INVISIBLE
        Snackbar.make(mainLayout, error, Snackbar.LENGTH_SHORT).show()
    }



    // -------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------ METHODS ----------------------------------------------------------------
    fun launchComicDetailActivity(comic: Comic) {
        val intent = Intent(this, DetailComicActivity::class.java)
        intent.putExtra(Constants.COMIC_ID, comic.id)
        intent.putExtra(Constants.COMIC_TITLE, comic.title)
        startActivity(intent)
    }
}


