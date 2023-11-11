package com.example.tugasakhirmobileprogramming.presentation.splash.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tugasakhirmobileprogramming.databinding.ActivitySplashBinding
import com.example.tugasakhirmobileprogramming.di.AppModule
import com.example.tugasakhirmobileprogramming.di.DaggerAppComponent
import com.example.tugasakhirmobileprogramming.presentation.home.view.HomeActivity
import com.example.tugasakhirmobileprogramming.presentation.login.view.LoginActivity
import com.example.tugasakhirmobileprogramming.presentation.splash.viewmodel.SplashViewModel
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {

    private var _layout: ActivitySplashBinding? = null

    private val layout: ActivitySplashBinding
        get() = _layout ?: throw IllegalStateException("The activity has been destroyed")

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mSplashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySplashBinding.inflate(layoutInflater)
        _layout = binding
        setContentView(binding.root)

        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build().inject(this)

        mSplashViewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[SplashViewModel::class.java]

        Handler(Looper.getMainLooper()).postDelayed({
            mSplashViewModel.getUserEmail()

        }, 2000)

        mSplashViewModel.userEmail.observe(this, Observer {
            if (it.isNotBlank()) {
                val intent = Intent(this, HomeActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    addFlags(flags)
                }
                startActivity(intent)
            } else {
                val intent = Intent(this, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    addFlags(flags)
                }
                startActivity(intent)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _layout = null
    }

}