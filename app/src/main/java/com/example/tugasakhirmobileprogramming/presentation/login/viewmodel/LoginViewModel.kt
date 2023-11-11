package com.example.tugasakhirmobileprogramming.presentation.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tugasakhirmobileprogramming.data.repository.Repository
import javax.inject.Inject

class LoginViewModel@Inject
constructor(): ViewModel() {

    @Inject
    lateinit var mRepository: Repository

    private var _saveUserState = MutableLiveData<String>()
    val saveUserState: LiveData<String>
        get() = _saveUserState

    fun saveUserEmail(email: String) {
        _saveUserState.value = mRepository.saveUserEmail(email)
    }
}