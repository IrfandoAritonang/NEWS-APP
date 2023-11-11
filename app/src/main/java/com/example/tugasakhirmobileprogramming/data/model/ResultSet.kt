package com.example.tugasakhirmobileprogramming.data.model

sealed class ResultSet<out T> {
    data object Loading : ResultSet<Nothing>()
    data class Success<out T>(val data: T) : ResultSet<T>()
    data class Error(val code: Int, val message: String) : ResultSet<Nothing>()
}
