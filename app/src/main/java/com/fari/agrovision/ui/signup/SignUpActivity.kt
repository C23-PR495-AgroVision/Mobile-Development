package com.fari.agrovision.ui.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import com.fari.agrovision.databinding.ActivitySignUpBinding
import com.fari.agrovision.ui.signin.SignInActivity

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnTvSignin.setOnClickListener {
            val moveToSignInActivity = Intent(this@SignUpActivity, SignInActivity::class.java)
            startActivity(
                moveToSignInActivity,
                ActivityOptionsCompat.makeSceneTransitionAnimation(this@SignUpActivity).toBundle()
            )
        }

//        confInput()
        playAnimation()
    }

    private fun playAnimation() {
//        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
//            duration = 6000
//            repeatCount = ObjectAnimator.INFINITE
//            repeatMode = ObjectAnimator.REVERSE
//        }.start()

        val logo =
            ObjectAnimator.ofFloat(binding.imgLogoNameGreen, View.ALPHA, 1f).setDuration(500)
        val started = ObjectAnimator.ofFloat(binding.tvStarted, View.ALPHA, 1f).setDuration(500)
        val title = ObjectAnimator.ofFloat(binding.tvSignup, View.ALPHA, 1f).setDuration(500)
        val name = ObjectAnimator.ofFloat(binding.tvName, View.ALPHA, 1f).setDuration(500)
        val etName = ObjectAnimator.ofFloat(binding.etNameLayout, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(500)
        val etEmail = ObjectAnimator.ofFloat(binding.etEmailLayout, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(500)
        val etPassword =
            ObjectAnimator.ofFloat(binding.etPasswordLayout, View.ALPHA, 1f).setDuration(500)
        val signInNow = ObjectAnimator.ofFloat(binding.btnTvSignin, View.ALPHA, 1f).setDuration(500)
        val btnSignup = ObjectAnimator.ofFloat(binding.btnSignup, View.ALPHA, 1f).setDuration(500)

//        val tgtname = AnimatorSet().apply {
//            playTogether(nameTextView, nameEditTextLayout)
//        }
//        val tgtemail = AnimatorSet().apply {
//            playTogether(emailTextView, emailEditTextLayout)
//        }
//        val tgtpass = AnimatorSet().apply {
//            playTogether(passwordTextView, passwordEditTextLayout)
//        }

        AnimatorSet().apply {
            playTogether(
                logo,
                started,
                title,
                name,
                etName,
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