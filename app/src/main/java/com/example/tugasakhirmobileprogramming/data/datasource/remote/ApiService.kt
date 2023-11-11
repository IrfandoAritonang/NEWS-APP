package com.example.tugasakhirmobileprogramming.data.datasource.remote

import com.example.tugasakhirmobileprogramming.BuildConfig
import com.example.tugasakhirmobileprogramming.data.model.article.Article
import com.example.tugasakhirmobileprogramming.data.model.article.ArticleResponse
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface ApiService {

    @GET("https://newsapi.org/v2/")
    fun getNewsData(): Call<List<Article>>

    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String? = "us",
        @Query("pageSize") pageSize: Int = 20,
        @Query("page") page: Int = 1
    ): ArticleResponse

    @GET("everything")
    suspend fun getEverything(
        @Query("q") query: String,
        @Query("pageSize") pageSize: Int = 20,
        @Query("page") page: Int = 1
    ): ArticleResponse

    companion object Factory {

        private fun request(): Retrofit {
            val mLoggingInterceptor = HttpLoggingInterceptor()
            mLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            val contentType = "application/json".toMediaType()
            val json = Json {
                ignoreUnknownKeys = true
            }

            val mClient = if (BuildConfig.DEBUG) {
                OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        chain.proceed(chain.let {
                            it.request().newBuilder()
                                .header("X-Api-Key", BuildConfig.API_KEY)
                                .method(it.request().method, it.request().body)
                                .build()
                        })
                    }.readTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(mLoggingInterceptor)
                    .build()
            } else {
                OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        chain.proceed(chain.let {
                            it.request().newBuilder()
                                .header("X-Api-Key", BuildConfig.API_KEY)
                                .method(it.request().method, it.request().body)
                                .build()
                        })
                    }.readTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .build()
            }

            return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(json.asConverterFactory(contentType))
                .client(mClient)
                .build()

        }

        fun <T> createService(service: Class<T>): T {
            return request().create(service)
        }
    }
}