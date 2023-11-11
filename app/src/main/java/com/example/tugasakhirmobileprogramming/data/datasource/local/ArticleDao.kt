package com.example.tugasakhirmobileprogramming.data.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.tugasakhirmobileprogramming.data.model.article.Article

@Dao
interface ArticleDao {
    @Insert
    fun insertArticle(data: Article)
    @Insert
    fun insertArticles(data: List<Article>)

    @Query("UPDATE article SET is_favorite = :isFavorite WHERE id = :id")
    fun setUnFavorite(id: Long, isFavorite: Boolean)

    @Query("DELETE FROM article WHERE id == :id")
    fun deleteArticle(id: Long)

    @Query("SELECT * FROM article WHERE is_favorite == :isFavorite")
    fun getArticles(isFavorite: Boolean): List<Article>

}