package com.example.tugasakhirmobileprogramming.presentation.detail.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tugasakhirmobileprogramming.R
import com.example.tugasakhirmobileprogramming.data.model.article.Article
import com.example.tugasakhirmobileprogramming.databinding.ActivityDetailBinding
import com.example.tugasakhirmobileprogramming.di.AppModule
import com.example.tugasakhirmobileprogramming.di.DaggerAppComponent
import com.example.tugasakhirmobileprogramming.presentation.detail.viewmodel.DetailViewModel
import com.example.tugasakhirmobileprogramming.utils.AppEnvirontment.ConstKey.EXTRA_ARTICLE
import com.example.tugasakhirmobileprogramming.utils.extension.onClick
import com.example.tugasakhirmobileprogramming.utils.extension.showToast
import javax.inject.Inject

@Suppress("DEPRECATION")
class DetailActivity : AppCompatActivity() {

    private var _layout: ActivityDetailBinding? = null

    private val layout: ActivityDetailBinding
        get() = _layout ?: throw IllegalStateException("The activity has been destroyed")

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mDetailViewModel: DetailViewModel

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDetailBinding.inflate(layoutInflater)
        _layout = binding
        setContentView(binding.root)

        layout.btnShare.onClick {
            val news = intent.extras?.getParcelable(EXTRA_ARTICLE) as? Article
            news?.let {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, it.title)
                shareIntent.putExtra(Intent.EXTRA_TEXT, it.url)
                startActivity(Intent.createChooser(shareIntent, "Share News Link"))
            }
        }

        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build().inject(this)

        mDetailViewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[DetailViewModel::class.java]


        val news = intent.extras?.getParcelable(EXTRA_ARTICLE) ?: Article()

        setSupportActionBar(layout.toolbar.root)
        supportActionBar?.apply {
            title = getString(R.string.title_news_detail)
            setDisplayHomeAsUpEnabled(true)
        }

        layout.webView.apply {
            settings.apply {
                javaScriptEnabled = true
                loadWithOverviewMode = true
                loadsImagesAutomatically = true
            }

            webViewClient = WebViewClient() // for preventing opening browser
        }

        if (news.url.isNullOrBlank()) {
            layout.containerError.isVisible = true
            layout.webView.isVisible = false
            layout.tvErrorTitle.text = getString(R.string.detail_error_title)
        } else {
            layout.containerError.isVisible = false
            layout.webView.isVisible = true
            layout.webView.loadUrl(news.url!!)
        }

        setFavoriteIcon(news.isFavorite)

        layout.ivFavourite.onClick {
            if (news.isFavorite == true) {
                mDetailViewModel.setUnFavorite(news.id)
            } else {
                mDetailViewModel.saveArticle(news)
            }
        }

        mDetailViewModel.saveArticleState.observe(this, Observer {
            showToast("Added to Favourite")
            setFavoriteIcon(true)
        })

        mDetailViewModel.unFavoriteState.observe(this, Observer {
            showToast("Remove from Favourite")
            setFavoriteIcon(false)
        })
    }

    private fun setFavoriteIcon(isFavorite: Boolean?) {
        if (isFavorite == true) {
            layout.ivFavourite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_24))
        } else {
            layout.ivFavourite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_24))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _layout = null
    }
}