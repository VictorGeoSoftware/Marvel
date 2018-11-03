package com.training.marvel.source

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.training.marvel.source.context.ComicsContext
import com.training.marvel.source.di.mainactivity.MainActivityModule
import com.training.marvel.source.models.CharacterError
import com.training.marvel.source.models.Comic
import com.training.marvel.source.models.Constants
import com.training.marvel.source.network.MarvelRequest
import com.training.marvel.source.presenters.MarvelPresenter
import com.training.marvel.source.presenters.MarvelView
import com.training.marvel.source.ui.ComicsAdapter
import com.training.marvel.source.ui.SpaceDecorator
import com.training.marvel.source.utils.MyUtils
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MarvelView, ComicsAdapter.ComicAdapterListener {
    val Activity.app: ParentApplication
        get() = application as ParentApplication

    val component by lazy { app.component.plus(MainActivityModule(this)) }

    private lateinit var comicsAdapter: ComicsAdapter
    @Inject lateinit var marvelPresenter: MarvelPresenter
    @Inject lateinit var marvelRequest: MarvelRequest

    private var mComicList = ArrayList<Comic>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        component.inject(this)

        val myGridLayoutManager = GridLayoutManager(this, 2)
        lstComics.layoutManager = myGridLayoutManager
        lstComics.addItemDecoration(SpaceDecorator(MyUtils.getDpFromValue(this, 10)))
        comicsAdapter = ComicsAdapter(mComicList, this)
        lstComics.adapter = comicsAdapter


        marvelPresenter.setView(this)


    }

    override fun onResume() {
        super.onResume()
        marvelPresenter.getSuperHeroComics().run(ComicsContext.GetComicContext(this, this, marvelRequest))
    }

    override fun onDestroy() {
        super.onDestroy()
        marvelPresenter.onDestroy()
    }



    // -------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------ USER INTERACTION -------------------------------------------------------
    override fun onComicSelected(comic: Comic) {
        launchComicDetailActivity(comic)
    }



    // -------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------ MARVEL VIEW ------------------------------------------------------------
    override fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    override fun onSuperHeroComicsReceived(comicList: List<Comic>) {
        runOnUiThread {
            progressBar.visibility = View.GONE
            mComicList.clear()
            mComicList.addAll(comicList)
            comicsAdapter.notifyDataSetChanged()
        }
    }

    override fun onSuperHeroComicsError(error: CharacterError) {
        runOnUiThread {
            progressBar.visibility = View.GONE
            drawError(error)
        }
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

    private fun drawError(error: CharacterError) {
        when (error) {
            is CharacterError.NoResultError -> Snackbar.make(mainLayout, "Character not found", Snackbar.LENGTH_SHORT).show()
            is CharacterError.UnknownServerError -> Snackbar.make(mainLayout, "Unknown server error!", Snackbar.LENGTH_SHORT).show()
        }
    }
}


