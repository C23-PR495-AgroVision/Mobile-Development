package com.fari.agrovision.ui.signin

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import com.fari.agrovision.databinding.ActivitySignInBinding
import com.fari.agrovision.ui.MainMenuActivity
import com.fari.agrovision.ui.signup.SignUpActivity

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySignInBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnSignin.setOnClickListener {
            val moveToMainMenuActivity = Intent(this@SignInActivity, MainMenuActivity::class.java)
            startActivity(
                moveToMainMenuActivity,
                ActivityOptionsCompat.makeSceneTransitionAnimation(this@SignInActivity).toBundle()
            )
        }

        binding.btnTvSignup.setOnClickListener {
            val moveToSignUpActivity = Intent(this@SignInActivity, SignUpActivity::class.java)
            startActivity(
                moveToSignUpActivity,
                ActivityOptionsCompat.makeSceneTransitionAnimation(this@SignInActivity).toBundle()
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
        val welcome = ObjectAnimator.ofFloat(binding.tvWelcome, View.ALPHA, 1f).setDuration(500)
        val title = ObjectAnimator.ofFloat(binding.tvSignin, View.ALPHA, 1f).setDuration(500)
//        val name = ObjectAnimator.ofFloat(binding.tvName, View.ALPHA, 1f).setDuration(500)
//        val etName = ObjectAnimator.ofFloat(binding.etNameLayout, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(500)
        val etEmail = ObjectAnimator.ofFloat(binding.etEmailLayout, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(500)
        val etPassword =
            ObjectAnimator.ofFloat(binding.etPasswordLayout, View.ALPHA, 1f).setDuration(500)
        val signInNow = ObjectAnimator.ofFloat(binding.btnTvSignup, View.ALPHA, 1f).setDuration(500)
        val btnSignup = ObjectAnimator.ofFloat(binding.btnSignin, View.ALPHA, 1f).setDuration(500)

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
                welcome,
                title,
//                name,
//                etName,
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