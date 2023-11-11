package com.example.tugasakhirmobileprogramming.data.model.article

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
@Entity(tableName = "article")
data class Article(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "author")
    var author: String? = "",
    @ColumnInfo(name = "content")
    var content: String? = "",
    @ColumnInfo(name = "description")
    var description: String? = "",
    @ColumnInfo(name = "publishedAt")
    var publishedAt: String? = "",
    @ColumnInfo(name = "title")
    var title: String? = "",
    @ColumnInfo(name = "url")
    var url: String? = "",
    @ColumnInfo(name = "url_to_image")
    var urlToImage: String? = "",
    @ColumnInfo(name = "is_favorite")
    var isFavorite: Boolean? = false
) : Parcelable