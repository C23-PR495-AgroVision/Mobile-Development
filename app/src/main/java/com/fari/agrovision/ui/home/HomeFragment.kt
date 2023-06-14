package com.fari.agrovision.ui.home

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.fari.agrovision.R
import com.fari.agrovision.data.remote.model.auth.DataUser
import com.fari.agrovision.databinding.FragmentHomeBinding
import com.fari.agrovision.ui.auth.AuthViewModel
import com.fari.agrovision.ui.auth.AuthViewModelFactory
import com.fari.agrovision.ui.auth.signup.SignUpActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.fari.agrovision.data.local.Result

class HomeFragment : Fragment() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
//    private val articleViewModel: ArticleViewModel by viewModels{
//        ArticleViewModelFactory.getInstance(requireContext(),requireContext().dataStore)
//    }

    private lateinit var dataUser: DataUser

    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory.getInstance(requireContext().dataStore)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel.isLogin().observe(requireActivity()) { uid ->
            if (uid.isNullOrEmpty()) {
                navigateToSignup()
            } else {
                getDataUser(uid)
            }
        }

//        articleViewModel.getListArticle().observe(requireActivity()){result->
//            when(result){
//                is Result.Loading ->{
//                    binding.progressBar.visibility = View.VISIBLE
//                }
//                is Result.Success -> {
//                    binding.progressBar.visibility = View.GONE
//                    val listArticle = result.data
//                    setArticleData(listArticle)
//                }
//                is Result.Error -> {
//                    binding.progressBar.visibility = View.GONE
//                    Toast.makeText(
//                        requireActivity(),
//                        "Failed to load article",
//                        Toast.LENGTH_SHORT
//                    )
//                        .show()
//                }
//            }
//        }

        setupAction()
    }

    private fun getDataUser(uid: String) {
        authViewModel.getDataUser(uid).observe(requireActivity()) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    dataUser = result.data
                    setDataUserView(dataUser)
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        requireActivity(),
                        "Failed to load user data",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }

    }

    private fun setupAction() {
        binding.btnDetection.setOnClickListener {

            val bottomNavView =
                requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav_view)
            bottomNavView.selectedItemId = R.id.navigation_detection

            val navController =
                Navigation.findNavController(requireActivity(), R.id.bottom_nav_host_fragment)
            navController.navigate(R.id.navigation_detection)

        }
    }

    private fun setDataUserView(dataUser: DataUser) {
//        val defaultImg = "https://picsum.photos/200/300.jpg"
//        val shownImgUrl = dataUser.imgUrl ?: defaultImg
        val defaultBitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.img_profile)
        binding.apply {
            val greet = getString(R.string.greet, dataUser.name)
            val base64String = dataUser.imgBase64 // Your Base64-encoded image string
            val bitmap = base64String?.let { convertBase64ToBitmap(it) } ?: defaultBitmap
            tvGreet.text = greet
            ivHello.setImageBitmap(bitmap)
//            Glide.with(requireActivity())
//                .load(shownImgUrl)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)
//                .into(binding.ivHello)
        }
    }

    private fun convertBase64ToBitmap(base64Image: String): Bitmap? {
        val decodedBytes = Base64.decode(base64Image, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

//    private fun setArticleData(listArticle: List<ArticleItem>) {
//        val adapter = ListArticleAdapter(listArticle)
//
//        adapter.setOnItemClickCallback(object : ListArticleAdapter.OnItemClickCallback {
//            override fun onItemArticleClicked(item: ArticleItem) {
//                navigateToDetailArticle(item)
//            }
//        })
//
//        binding.apply {
//            rvArticle.adapter = adapter
//
//            val layoutManager = LinearLayoutManager(requireContext())
//            binding.rvArticle.layoutManager = layoutManager
//            val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
//            binding.rvArticle.addItemDecoration(itemDecoration)
//        }
//
//    }

//    private fun navigateToDetailArticle(article: ArticleItem) {
//        val moveToDetail = Intent(requireActivity(), DetailArticleActivity::class.java)
//        moveToDetail.putExtra(DetailArticleActivity.ID_ARTICLE, article.id)
//        startActivity(moveToDetail)
//    }

    private fun navigateToSignup() {
        Toast.makeText(
            requireContext(),
            getString(R.string.logout),
            Toast.LENGTH_SHORT
        ).show()
        val intent = Intent(context, SignUpActivity::class.java)
        startActivity(intent)
    }


}