package com.example.tugasakhirmobileprogramming.presentation.favorite.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tugasakhirmobileprogramming.data.model.article.Article
import com.example.tugasakhirmobileprogramming.data.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class FavoriteViewModel @Inject
constructor(): ViewModel(), CoroutineScope {

    @Inject
    lateinit var mRepository: Repository

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    private var _articles = MutableLiveData<List<Article>?>()
    val articles: LiveData<List<Article>?>
        get() = _articles

    fun getArticles() {
        launch {
            val result = withContext(Dispatchers.IO) {
                mRepository.getArticles(true)
            }
            _articles.value = result
        }

    }


}