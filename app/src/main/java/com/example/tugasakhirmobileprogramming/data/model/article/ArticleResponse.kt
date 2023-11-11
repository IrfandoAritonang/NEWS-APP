package com.example.tugasakhirmobileprogramming.data.model.article

import com.example.tugasakhirmobileprogramming.data.model.article.Article
import kotlinx.serialization.Serializable

@Serializable
data class ArticleResponse(
    val articles: List<Article>?,
    val status: String?,
    val totalResults: Int?
)