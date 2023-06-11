package com.fari.agrovision.ui.auth.signin

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.fari.agrovision.databinding.ActivitySignInBinding
import com.fari.agrovision.ui.MainMenuActivity
import com.fari.agrovision.ui.auth.AuthViewModel
import com.fari.agrovision.ui.auth.AuthViewModelFactory
import com.fari.agrovision.ui.auth.signup.SignUpActivity
import com.fari.agrovision.data.local.Result

class SignInActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory.getInstance(dataStore)
    }

    private lateinit var binding: ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySignInBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupAction()
        playAnimation()
    }

    private fun setupAction() {
//        binding.btnTvReset.setOnClickListener {
//            val moveToResetPasswordActivity =
//                Intent(this@LoginActivity, ResetPasswordActivity::class.java)
//            startActivity(
//                moveToResetPasswordActivity,
//                ActivityOptionsCompat.makeSceneTransitionAnimation(this@LoginActivity).toBundle()
//            )
//        }
        binding.btnTvSignup.setOnClickListener {
            val moveToSignUpActivity = Intent(this@SignInActivity, SignUpActivity::class.java)
            startActivity(
                moveToSignUpActivity,
                ActivityOptionsCompat.makeSceneTransitionAnimation(this@SignInActivity).toBundle()
            )
        }
        binding.btnSignin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            when {
                email.isEmpty() -> {
                    binding.etEmailLayout.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    binding.etPasswordLayout.error = "Masukkan password"
                }
                Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    binding.apply {
                        etEmail.onEditorAction(EditorInfo.IME_ACTION_DONE)
                        etPassword.onEditorAction(EditorInfo.IME_ACTION_DONE)
                    }
                    authViewModel.login(email, password).observe(this) { result ->
                        when (result) {
                            is Result.Loading -> {
                                binding.progressBar.visibility = View.VISIBLE
                            }
                            is Result.Success -> {
                                binding.progressBar.visibility = View.GONE
                                val moveToMainMenuActivity =
                                    Intent(this@SignInActivity, MainMenuActivity::class.java)
                                startActivity(
                                    moveToMainMenuActivity,
                                    ActivityOptionsCompat.makeSceneTransitionAnimation(this@SignInActivity)
                                        .toBundle()
                                )
                            }
                            is Result.Error -> {
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(
                                    this@SignInActivity,
                                    "Email atau password salah",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }

                else -> {
                    Toast.makeText(
                        this@SignInActivity,
                        "Email atau password invalid",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun playAnimation() {
        val logo = ObjectAnimator.ofFloat(binding.imgLogoNameGreen, View.ALPHA, 1f).setDuration(500)
        val welcome = ObjectAnimator.ofFloat(binding.tvWelcome, View.ALPHA, 1f).setDuration(500)
        val title = ObjectAnimator.ofFloat(binding.tvSignin, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(500)
        val etEmail = ObjectAnimator.ofFloat(binding.etEmailLayout, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(500)
        val etPassword = ObjectAnimator.ofFloat(binding.etPasswordLayout, View.ALPHA, 1f).setDuration(500)
        val signInNow = ObjectAnimator.ofFloat(binding.btnTvSignup, View.ALPHA, 1f).setDuration(500)
        val btnSignup = ObjectAnimator.ofFloat(binding.btnSignin, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playTogether(
                logo,
                welcome,
                title,
                email,
                etEmail,
                password,
                etPassword,
                signInNow,
                btnSignup
            )
            startDelay = 500
        }.start()
    }
}