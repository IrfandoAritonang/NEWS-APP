package com.example.tugasakhirmobileprogramming.presentation.favorite.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasakhirmobileprogramming.R
import com.example.tugasakhirmobileprogramming.databinding.ActivityFavoriteBinding
import com.example.tugasakhirmobileprogramming.di.AppModule
import com.example.tugasakhirmobileprogramming.di.DaggerAppComponent
import com.example.tugasakhirmobileprogramming.presentation.favorite.view.FavoriteAdapter
import com.example.tugasakhirmobileprogramming.presentation.detail.view.DetailActivity
import com.example.tugasakhirmobileprogramming.presentation.favorite.viewmodel.FavoriteViewModel
import com.example.tugasakhirmobileprogramming.utils.AppEnvirontment
import javax.inject.Inject

class FavoriteActivity : AppCompatActivity() {

    private var _layout: ActivityFavoriteBinding? = null

    private val layout: ActivityFavoriteBinding
        get() = _layout ?: throw IllegalStateException("The activity has been destroyed")

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mFavoriteViewModel: FavoriteViewModel
    private var mFavoriteAdapter: FavoriteAdapter? = null

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityFavoriteBinding.inflate(layoutInflater)
        _layout = binding
        setContentView(binding.root)

        setSupportActionBar(layout.toolbar.root)
        supportActionBar?.apply {
            title = getString(R.string.title_favorite)
            setDisplayHomeAsUpEnabled(true)
        }

        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build().inject(this)

        mFavoriteViewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[FavoriteViewModel::class.java]

        mFavoriteAdapter = FavoriteAdapter { article ->
            val intent = Intent(this, DetailActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra(AppEnvirontment.ConstKey.EXTRA_ARTICLE, article)
            }
            startActivity(intent)
        }

        layout.rvArticle.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = mFavoriteAdapter
        }

        mFavoriteViewModel.getArticles()

        mFavoriteViewModel.articles.observe(this, Observer { articles ->
            articles?.let {
                mFavoriteAdapter?.setData(it)
            }

        })



    }

    override fun onDestroy() {
        super.onDestroy()
        _layout = null
    }
}