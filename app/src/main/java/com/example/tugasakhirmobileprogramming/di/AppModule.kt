package com.example.tugasakhirmobileprogramming.di

import android.content.Context
import android.content.SharedPreferences
import com.example.tugasakhirmobileprogramming.data.datasource.local.AppDatabase
import com.example.tugasakhirmobileprogramming.data.datasource.local.LocalDataSource
import com.example.tugasakhirmobileprogramming.data.datasource.remote.ApiService
import com.example.tugasakhirmobileprogramming.data.datasource.remote.RemoteDataSource
import com.example.tugasakhirmobileprogramming.data.repository.Repository
import com.example.tugasakhirmobileprogramming.utils.dbhelper.SharedPreferencesFactory
import dagger.Module
import dagger.Provides

@Module
class AppModule(val mContext: Context) {

    @Provides
    fun provideContext(): Context {
        return mContext
    }

    @Provides
    fun provideService(): ApiService {
        return ApiService.createService(ApiService::class.java)
    }

    @Provides
    fun providePref(context: Context): SharedPreferences {
        return SharedPreferencesFactory.initPreferences(context)
    }

    @Provides
    fun provideDB(context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun provideRemote(apiService: ApiService): RemoteDataSource {
        return RemoteDataSource(apiService)
    }

    @Provides
    fun provideLocal(sharedPreferences: SharedPreferences, appDatabase: AppDatabase): LocalDataSource {
        return LocalDataSource(sharedPreferences, appDatabase)
    }

    @Provides
    fun provideRepository(
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource,
    ): Repository {
        return Repository(localDataSource, remoteDataSource)
    }


}