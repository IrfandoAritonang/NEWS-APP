package com.example.tugasakhirmobileprogramming.presentation.splash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tugasakhirmobileprogramming.data.repository.Repository
import javax.inject.Inject

class SplashViewModel @Inject
constructor(): ViewModel() {

    @Inject
    lateinit var mRepository: Repository

    private var _userEmail = MutableLiveData<String>()
    val userEmail: LiveData<String>
        get() = _userEmail

    fun getUserEmail() {
        _userEmail.value = mRepository.getUserEmail()
    }

}