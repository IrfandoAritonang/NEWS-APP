package com.example.tugasakhirmobileprogramming.presentation.login.view

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tugasakhirmobileprogramming.R
import com.example.tugasakhirmobileprogramming.databinding.ActivityLoginBinding
import com.example.tugasakhirmobileprogramming.di.AppModule
import com.example.tugasakhirmobileprogramming.di.DaggerAppComponent
import com.example.tugasakhirmobileprogramming.presentation.home.view.HomeActivity
import com.example.tugasakhirmobileprogramming.presentation.login.viewmodel.LoginViewModel
import com.example.tugasakhirmobileprogramming.utils.extension.onClick
import java.util.regex.Pattern
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {

    private var _layout: ActivityLoginBinding? = null

    private val layout: ActivityLoginBinding
        get() = _layout ?: throw IllegalStateException("The activity has been destroyed")

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mLoginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        _layout = binding
        setContentView(binding.root)

        val imageView = findViewById<ImageView>(R.id.ivTc)
        val slideLeftAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_left)
        val slideRightAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_right)

        val animationListener = object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                if (animation == slideLeftAnimation) {
                    imageView.startAnimation(slideRightAnimation)
                } else {
                    imageView.startAnimation(slideLeftAnimation)
                }
            }
        }

        slideLeftAnimation.setAnimationListener(animationListener)
        slideRightAnimation.setAnimationListener(animationListener)

        imageView.startAnimation(slideLeftAnimation)



        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build().inject(this)

        mLoginViewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[LoginViewModel::class.java]

        layout.tieEmail.addTextChangedListener(
            afterTextChanged = {
                validateEmailPassword(it.toString(), binding.tiePassword.text.toString())
            })

        layout.tiePassword.addTextChangedListener(
            afterTextChanged = {
                validateEmailPassword(binding.tieEmail.text.toString(), it.toString())
            })

//        layout.tieEmail.setText("irfando@mail.com")
//        layout.tiePassword.setText("12345678")

        layout.btnLogin.onClick {
            mLoginViewModel.saveUserEmail(layout.tieEmail.text.toString())
        }

        mLoginViewModel.saveUserState.observe(this, Observer {
            if (it == "success") {
                val intent = Intent(this, HomeActivity::class.java).apply {
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

    private fun validateEmailPassword(email: String, password: String) {
        if (email.isNotBlank()) {
            layout.tilEmail.error =
                if (isValidEmail(email)) "" else getString(R.string.invalid_email)
        }
        if (password.isNotBlank()) {
            layout.tilPassword.error =
                if (isValidPassword(password)) "" else getString(R.string.invalid_password)
        }
        layout.btnLogin.isEnabled = isValidEmail(email) && isValidPassword(password)
    }


    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        val pattern = Pattern.compile(emailPattern)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 8
    }

}