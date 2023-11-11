package com.example.tugasakhirmobileprogramming.presentation.home.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tugasakhirmobileprogramming.data.model.ResultSet
import com.example.tugasakhirmobileprogramming.data.model.article.Article
import com.example.tugasakhirmobileprogramming.data.repository.Repository
import com.example.tugasakhirmobileprogramming.utils.extension.toArrayList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class HomeViewModel @Inject
constructor(): ViewModel(), CoroutineScope {

    @Inject
    lateinit var mRepository: Repository

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    private var _isLastPage = MutableLiveData<Boolean>()
    val isLastPage: LiveData<Boolean>
        get() = _isLastPage

    private var _topHeadlines = MutableLiveData<ResultSet<List<Article>>>()
    val topHeadlines: LiveData<ResultSet<List<Article>>>
        get() = _topHeadlines
    private var allTopHeadlines = ArrayList<Article>()

    private var _logoutState = MutableLiveData<String>()
    val logoutState: LiveData<String>
        get() = _logoutState

    private var _modeOffline = MutableLiveData<Boolean>()
    val modeOffline: LiveData<Boolean>
        get() = _modeOffline

    fun getTopHeadlines(context: Context, page: Int, pageSize: Int) {
        if (isInternetAvailable(context)) {
            getTopHeadlinesRemote(page, pageSize)
        } else {
            getTopHeadlinesLocal()
        }
    }

    private fun getTopHeadlinesLocal() {
        launch {
            val result = withContext(Dispatchers.IO) {
                mRepository.getArticles(false)
            }
            allTopHeadlines.addAll(result.toArrayList())
            _topHeadlines.value = ResultSet.Success(allTopHeadlines)
            _modeOffline.value = true
        }
    }

    fun getTopHeadlinesRemote(page: Int, pageSize: Int) {
        if (page == 1) {
            _topHeadlines.value = ResultSet.Loading
            allTopHeadlines.clear()
        }

        launch {
            val result = withContext(Dispatchers.IO) {
                mRepository.getTopHeadlines(page, pageSize)
            }
            when (result) {
                is ResultSet.Loading -> {}
                is ResultSet.Success -> {
                    allTopHeadlines.addAll(result.data.articles.toArrayList())
                    _topHeadlines.value = ResultSet.Success(allTopHeadlines)
                    _isLastPage.value = result.data.articles?.size != pageSize

                    saveArticles(allTopHeadlines)
                }

                is ResultSet.Error -> {
                    _topHeadlines.value = result
                }
            }

        }
    }

    fun saveArticles(articles: List<Article>) {
        launch {
            val result = withContext(Dispatchers.IO) {
                mRepository.saveArticles(articles)
            }
        }
    }

    fun getEverything(page: Int, pageSize: Int, query: String) {
        if (page == 1) {
            _topHeadlines.value = ResultSet.Loading
            allTopHeadlines.clear()
        }

        launch {
            val result = withContext(Dispatchers.IO) {
                mRepository.getEverything(page, pageSize, query)
            }
            when (result) {
                is ResultSet.Loading -> {}
                is ResultSet.Success -> {
                    allTopHeadlines.addAll(result.data.articles.toArrayList())
                    _topHeadlines.value = ResultSet.Success(allTopHeadlines)
                    _isLastPage.value = result.data.articles?.size != pageSize
                }

                is ResultSet.Error -> {}
            }

        }
    }

    fun logout() {
        _logoutState.value = mRepository.logout()
    }



    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }


}