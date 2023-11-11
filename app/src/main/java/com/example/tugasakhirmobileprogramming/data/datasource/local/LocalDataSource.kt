package com.example.tugasakhirmobileprogramming.data.datasource.local

import android.content.SharedPreferences
import com.example.tugasakhirmobileprogramming.data.model.article.Article
import com.example.tugasakhirmobileprogramming.utils.AppEnvirontment.ConstKey.PREF_EMAIL
import com.example.tugasakhirmobileprogramming.utils.dbhelper.SharedPreferencesFactory.clearPref
import com.example.tugasakhirmobileprogramming.data.datasource.local.AppDatabase

class LocalDataSource(private val mPref: SharedPreferences, private val mAppDatabase: AppDatabase) {

    // Pref
    fun saveUserEmail(email: String): String {
        return try {
            mPref.edit().putString(PREF_EMAIL, email).apply()
            "success"
        } catch (e: Exception) {
            e.localizedMessage ?: ""
        }
    }

    fun getUserEmail(): String = mPref.getString(PREF_EMAIL, "") ?: ""

    fun logout(): String {
        return try {
            mPref.clearPref()
            "success"
        } catch (e: Exception) {
            e.localizedMessage ?: ""
        }

    }

    // Room
    fun saveArticle(article: Article): String {
        return try {
            mAppDatabase.articleDao()
                .insertArticle(article)
            return "success"
        } catch (e: Exception) {
            e.localizedMessage ?: "failed"
        }
    }

    fun saveArticles(articles: List<Article>): String {
        return try {
            mAppDatabase.articleDao()
                .insertArticles(articles)
            return "success"
        } catch (e: Exception) {
            e.localizedMessage ?: "failed"
        }
    }

    fun getArticles(isFavorite: Boolean): List<Article>? {
        return try {
            mAppDatabase.articleDao()
                .getArticles(isFavorite)
        } catch (e: Exception) {
            null
        }
    }

    fun setUnFavorite(id: Long, isFavorite: Boolean): String {
        return try {
            mAppDatabase.articleDao()
                .setUnFavorite(id, isFavorite)
            return "success"
        } catch (e: Exception) {
            e.localizedMessage ?: "failed"
        }
    }




}