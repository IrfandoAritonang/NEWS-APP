package com.example.tugasakhirmobileprogramming.utils.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tugasakhirmobileprogramming.presentation.detail.viewmodel.DetailViewModel
import com.example.tugasakhirmobileprogramming.presentation.favorite.viewmodel.FavoriteViewModel
import com.example.tugasakhirmobileprogramming.presentation.home.viewmodel.HomeViewModel
import com.example.tugasakhirmobileprogramming.presentation.login.viewmodel.LoginViewModel
import com.example.tugasakhirmobileprogramming.presentation.splash.viewmodel.SplashViewModel
import com.example.tugasakhirmobileprogramming.utils.viewmodelfactory.ViewModelFactory
import com.example.tugasakhirmobileprogramming.utils.viewmodelfactory.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    internal abstract fun splashViewModel(viewModel: SplashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    internal abstract fun loginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    internal abstract fun homeViewModel(viewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavoriteViewModel::class)
    internal abstract fun favoriteViewModel(viewModel: FavoriteViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailViewModel::class)
    internal abstract fun detailViewModel(viewModel: DetailViewModel): ViewModel
}