package com.example.tugasakhirmobileprogramming.di

import com.example.tugasakhirmobileprogramming.di.AppModule
import com.example.tugasakhirmobileprogramming.presentation.detail.view.DetailActivity
import com.example.tugasakhirmobileprogramming.presentation.favorite.view.FavoriteActivity
import com.example.tugasakhirmobileprogramming.presentation.home.view.HomeFragment
import com.example.tugasakhirmobileprogramming.presentation.login.view.LoginActivity
import com.example.tugasakhirmobileprogramming.presentation.splash.view.SplashActivity
import com.example.tugasakhirmobileprogramming.utils.viewmodelfactory.ViewModelModule
import dagger.Component

@Component(modules = [AppModule::class, ViewModelModule::class])
interface AppComponent {

    fun inject(splashActivity: SplashActivity)

    fun inject(loginActivity: LoginActivity)

    fun inject(homeFragment: HomeFragment)

    fun inject(favoriteActivity: FavoriteActivity)

    fun inject(detailActivity: DetailActivity)

}