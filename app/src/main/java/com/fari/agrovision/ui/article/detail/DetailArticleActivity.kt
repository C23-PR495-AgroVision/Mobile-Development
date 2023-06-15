package com.fari.agrovision.ui.article.detail

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.fari.agrovision.R
import com.fari.agrovision.databinding.ActivityDetailArticleBinding
import com.fari.agrovision.databinding.ActivityImagePreviewBinding
import com.fari.agrovision.ui.auth.AuthViewModel
import com.fari.agrovision.ui.auth.AuthViewModelFactory

class DetailArticleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailArticleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setToolBar()
    }

    private fun setToolBar() {
        binding.apply {
            setSupportActionBar(toolbar.toolbar)
            toolbar.tvToolbarTitle.text = getString(R.string.app_name)
            toolbar.btnBackToolbar.setOnClickListener {
                finish()
            }
        }
    }
}