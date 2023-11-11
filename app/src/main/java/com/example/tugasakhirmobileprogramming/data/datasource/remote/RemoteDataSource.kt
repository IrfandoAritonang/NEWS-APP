package com.example.tugasakhirmobileprogramming.data.datasource.remote

import com.example.tugasakhirmobileprogramming.data.model.ResultSet
import com.example.tugasakhirmobileprogramming.data.model.article.ArticleResponse
import org.json.JSONObject
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class RemoteDataSource(private val apiService: ApiService) {

    suspend fun getTopHeadlines(page: Int, pageSize: Int): ResultSet<ArticleResponse> {
        return try {
            val response = apiService.getTopHeadlines(page = page, pageSize = pageSize)
            ResultSet.Success(response)
        } catch (e: Exception) {
            getExceptionResponse(e)
        }
    }

    suspend fun getEverything(page: Int, pageSize: Int, query: String): ResultSet<ArticleResponse> {
        return try {
            val response = apiService.getEverything(page = page, pageSize = pageSize, query = query)
            ResultSet.Success(response)
        } catch (e: Exception) {
            getExceptionResponse(e)
        }
    }

    private fun <T> getExceptionResponse(e: Exception): ResultSet<T> {
        e.printStackTrace()
        when (e) {
            is HttpException -> {
                val code = e.code()
                var msg = e.message()
                val errorMessage: String?
                try {
                    val jsonObj = e.response()?.errorBody()?.charStream()?.readText()
                        ?.let { JSONObject(it) }
                    errorMessage = jsonObj?.getString("message")
                } catch (exception: java.lang.Exception) {
                    return when (exception) {
                        is UnknownHostException -> ResultSet.Error(
                            code,
                            "Telah terjadi kesalahan ketika koneksi ke server: ${e.message}",
                        )
                        is SocketTimeoutException -> ResultSet.Error(
                            code,
                            "Telah terjadi kesalahan ketika koneksi ke server: ${e.message}",
                        )
                        else -> ResultSet.Error(
                            code,
                            "Terjadi kesalahan pada server. errorcode : <b>$code</b>",
                        )
                    }
                }

                when (code) {
                    504 -> {
                        msg = errorMessage ?: "Error Response"
                    }
                    502, 404 -> {
                        msg = errorMessage ?: "Error Connect or Resource Not Found"
                    }
                    400 -> {
                        msg = errorMessage ?: "Bad Request"
                    }
                    401 -> {
                        msg = errorMessage ?: "Not Authorized"
                    }
                    422 -> {
                        msg = errorMessage ?: "Unprocessable Entity"
                    }
                }
                return ResultSet.Error(code, msg)
            }

            else -> return ResultSet.Error(-1,  e.message ?: "Unknown error occured")
        }
    }



}