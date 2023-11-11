package com.example.tugasakhirmobileprogramming.data.repository

import com.example.tugasakhirmobileprogramming.data.datasource.local.LocalDataSource
import com.example.tugasakhirmobileprogramming.data.datasource.remote.RemoteDataSource
import com.example.tugasakhirmobileprogramming.data.model.ResultSet
import com.example.tugasakhirmobileprogramming.data.model.article.Article
import com.example.tugasakhirmobileprogramming.data.model.article.ArticleResponse

class Repository(private val mLocalDataSource: LocalDataSource, private val mRemoteDataSource: RemoteDataSource) {

    // Local
    fun saveUserEmail(email: String): String = mLocalDataSource.saveUserEmail(email)

    fun getUserEmail(): String = mLocalDataSource.getUserEmail()

    fun saveArticle(article: Article) = mLocalDataSource.saveArticle(article)

    fun saveArticles(articles: List<Article>) = mLocalDataSource.saveArticles(articles)

    fun getArticles(isFavorite: Boolean) = mLocalDataSource.getArticles(isFavorite)

    fun setUnFavorite(id: Long, isFavorite: Boolean) = mLocalDataSource.setUnFavorite(id, isFavorite)

    fun logout() = mLocalDataSource.logout()

    // Remote
    suspend fun getTopHeadlines(page: Int, pageSize: Int): ResultSet<ArticleResponse> =  mRemoteDataSource.getTopHeadlines(page, pageSize)

    suspend fun getEverything(page: Int, pageSize: Int, query: String): ResultSet<ArticleResponse> =  mRemoteDataSource.getEverything(page, pageSize, query)

    companion object {
        @Volatile
        private var instance: Repository? = null
        // Metode untuk mendapatkan instance Repository
        fun getInstance(mLocalDataSource: LocalDataSource, mRemoteDataSource: RemoteDataSource): Repository {
            return instance ?: synchronized(this) {
                instance ?: Repository(mLocalDataSource, mRemoteDataSource).also { instance = it }
            }
        }
    }


}