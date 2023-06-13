package com.fari.agrovision.ui.profile

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.fari.agrovision.R
import com.fari.agrovision.data.remote.model.auth.DataUser
import com.fari.agrovision.databinding.FragmentProfileBinding
import com.fari.agrovision.ui.auth.AuthViewModel
import com.fari.agrovision.ui.auth.AuthViewModelFactory
import com.google.android.material.textfield.TextInputLayout
import com.fari.agrovision.data.local.Result
import com.fari.agrovision.ui.auth.signup.SignUpActivity
import com.fari.agrovision.ui.camera.CameraActivity
import java.io.ByteArrayInputStream
import java.io.IOException

class ProfileFragment : Fragment() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory.getInstance(requireContext().dataStore)
    }

    private lateinit var uid: String
    private lateinit var dataUser: DataUser

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var isEditing = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etPasswordLayout.endIconMode = TextInputLayout.END_ICON_NONE

        authViewModel.isLogin().observe(requireActivity()) { uid ->
            if (uid.isNullOrEmpty()) {
                Log.i("PROFILE", "ABCDEF GA GET DATA")
                navigateToSignup()
            } else {
                this.uid = uid.toString()
                Log.i("PROFILE", "ABCDEF GETDATA $uid")
                Log.i("PROFILE", "ABCDEF $isAdded")
                if (isAdded)
                    getDataUser(uid)
            }
        }

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

    private fun setDataUserView(dataUser: DataUser) {
        Log.i("SETDATAUSER", "ABCDEF ${dataUser.imgUrl}")
//        val defaultImg = "https://picsum.photos/200/300.jpg"
//        val shownImgUrl = dataUser.imgUrl ?: defaultImg
        binding.apply {
            etName.setText(dataUser.name)
            etEmail.setText(dataUser.email)

            val base64String = dataUser.imgUrl // Your Base64-encoded image string
            val bitmap = convertBase64ToBitmap(base64String)

            ivProfile.setImageBitmap(bitmap)
//            Glide.with(requireActivity())
//                .load(shownImgUrl)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)
//                .into(binding.ivProfile)
        }
    }

    private fun navigateToSignup() {
        Toast.makeText(
            requireContext(),
            activity?.getString(R.string.logout),
            Toast.LENGTH_SHORT
        ).show()
        startActivity(Intent(requireActivity(), SignUpActivity::class.java))
        activity?.finish()
    }


    private fun setupAction() {
        binding.apply {
            btnSignout.setOnClickListener {
                logout()
            }
            btnIvEditProfile.setOnClickListener {
                isEditing = true
                changeEditable()
            }
            btnCancel.setOnClickListener {
                isEditing = false
                changeEditable()
            }
            ivProfile.setOnClickListener {
                if (isEditing) {
                    if (!allPermissionsGranted()) ActivityCompat.requestPermissions(
                        requireActivity(),
                        REQUIRED_PERMISSIONS,
                        REQUEST_CODE_PERMISSIONS
                    )
                    else {
                        startCameraX()
                    }
                }
            }
            btnSaveChanges.setOnClickListener {
                saveChanges()
            }
        }
    }

    private fun logout() {
        authViewModel.logout().observe(requireActivity()) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        requireActivity(),
                        "Gagal logout",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
    }

    private fun saveChanges() {
//        val currentEmail = dataUser.email
//        val currentName = dataUser.name
        val newName = binding.etName.text.toString()
//        val newEmail = binding.etEmail.text.toString()
//        val currentPassword = binding.etPassword.text.toString()
//        val newPassword = binding.etNewPassword.text.toString()
        when {
//            !Patterns.EMAIL_ADDRESS.matcher(newEmail).matches() -> {
//                binding.etEmailLayout.error = "Email does not match the format"
//            }
            newName.isEmpty() -> {
                binding.etName.error = "Enter your name"
            }
//            currentPassword.isEmpty() && newPassword.isNotEmpty() -> {
//                binding.etPasswordLayout.error = "Enter your current password"
//            }
//            newPassword.isNotEmpty() && newPassword.length <= 8 -> {
//                binding.etNewPasswordLayout.error =
//                    "New Password must be more than 8 characters"
//            }
            else -> {
//                if (currentName != newName)
                    authViewModel.editName(
                        uid,
                        newName,
//                        currentName
                    ).observe(requireActivity()) { result ->
                        when (result) {
                            is Result.Loading -> {
                                binding.progressBar.visibility = View.VISIBLE
                            }
                            is Result.Success -> {
                                isEditing = false
                                changeEditable()
                                binding.progressBar.visibility = View.GONE
                                AlertDialog.Builder(requireActivity()).apply {
                                    setTitle("Success")
                                    setMessage(result.data)
                                    setPositiveButton("Next") { dialog, _ ->
                                        dialog.cancel()
                                        getDataUser(uid)
                                    }
                                    create()
                                    show()
                                }
                                Log.i("Output Name", result.data)
                            }
                            is Result.Error -> {
                                isEditing = false
                                changeEditable()
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(
                                    requireActivity(),
                                    getString(R.string.msg_failed_changes_name),
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.i("Name", result.error)
                            }
                        }
                    }
//                if ((currentPassword.isNotEmpty() && newPassword.isNotEmpty()) || (currentEmail != newEmail))
//                    authViewModel.editEmailPassword(
//                        uid,
//                        currentEmail,
//                        newEmail,
//                        currentPassword,
//                        newPassword
//                    ).observe(requireActivity()) { result ->
//                        when (result) {
//                            is Result.Loading -> {
//                                binding.progressBar.visibility = View.VISIBLE
//                            }
//                            is Result.Success -> {
//                                isEditing = false
//                                changeEditable()
//                                binding.progressBar.visibility = View.GONE
//                                AlertDialog.Builder(requireActivity()).apply {
//                                    setTitle("Success")
//                                    setMessage(result.data)
//                                    setPositiveButton("Next") { dialog, _ ->
//                                        dialog.cancel()
//                                        getDataUser(uid)
//                                    }
//                                    create()
//                                    show()
//                                }
//                                Log.i("Output Email", result.data)
//                            }
//                            is Result.Error -> {
//                                isEditing = false
//                                changeEditable()
//                                binding.progressBar.visibility = View.GONE
//                                Toast.makeText(
//                                    requireActivity(),
//                                    getString(R.string.msg_failed_changes),
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                                setDataUserView(dataUser)
//                                Log.i("Email", result.error)
//                            }
//                        }
//                    }
            }
        }
    }

    private fun changeEditable() {
        binding.apply {
            etName.isEnabled = isEditing
//            etEmail.isEnabled = isEditing
//            etPassword.isEnabled = isEditing
//            etNewPassword.isEnabled = isEditing
//            etEmailLayout.error = null
            etNameLayout.error = null
//            etPasswordLayout.error = null
//            etNewPasswordLayout.error = null
//            etPassword.setText("")
//            etNewPassword.setText("")
            if (isEditing) {
                animateViewVisibility(btnIvEditProfile, View.GONE)
                ivProfile.alpha = 0.7f
                ivProfile.borderColor =
                    ContextCompat.getColor(requireActivity(), R.color.green_soft)
                animateViewVisibility(btnSignout, View.GONE)
//                animateViewVisibility(tvNewPassword, View.VISIBLE)
//                animateViewVisibility(etNewPasswordLayout, View.VISIBLE)
                animateViewVisibility(btnSaveChanges, View.VISIBLE)
                animateViewVisibility(btnCancel, View.VISIBLE)
//                binding.etPasswordLayout.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
//                binding.etNewPasswordLayout.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
            } else {
                animateViewVisibility(btnIvEditProfile, View.VISIBLE)
                ivProfile.alpha = 1f
                ivProfile.borderColor = ContextCompat.getColor(requireActivity(), R.color.white)
                animateViewVisibility(btnSignout, View.VISIBLE)
//                animateViewVisibility(tvNewPassword, View.INVISIBLE)
//                animateViewVisibility(etNewPasswordLayout, View.INVISIBLE)
                animateViewVisibility(btnSaveChanges, View.INVISIBLE)
                animateViewVisibility(btnCancel, View.INVISIBLE)
//                binding.etPasswordLayout.endIconMode = TextInputLayout.END_ICON_NONE
//                binding.etNewPasswordLayout.endIconMode = TextInputLayout.END_ICON_NONE
            }
        }
    }

    private fun animateViewVisibility(view: View, visibility: Int) {
        if (view.visibility == visibility) return // No need to animate if already in desired visibility

        val animation: Animation = if (visibility == View.VISIBLE) {
            AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
        } else {
            AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out)
        }

        view.apply {
            startAnimation(animation)
            this.visibility = visibility
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.allow_permission),
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                startCameraX()
            }
        }
    }

    private fun startCameraX() {
        val intent = Intent(requireActivity(), CameraActivity::class.java)
        intent.putExtra(CameraActivity.IS_DETECTION, false)

        startActivity(intent)
    }

    private fun convertBase64ToBitmap(base64Image: String): Bitmap? {
        val decodedBytes = Base64.decode(base64Image, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

//    private fun convertBase64ToBitmap(base64Image: String): Bitmap? {
//        val decodedBytes = Base64.decode(base64Image, Base64.DEFAULT)
//        val inputStream = ByteArrayInputStream(decodedBytes)
//
//        try {
//            val exif = ExifInterface(inputStream)
//            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
//            val bitmapOptions = BitmapFactory.Options()
//            bitmapOptions.inSampleSize = 1
//            val bitmap = BitmapFactory.decodeStream(inputStream, null, bitmapOptions)
//
//            return rotateBitmap(bitmap, orientation)
//        } catch (e: IOException) {
//            e.printStackTrace()
//        } finally {
//            try {
//                inputStream.close()
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }
//
//        return null
//    }

//    private fun rotateBitmap(bitmap: Bitmap?, orientation: Int): Bitmap? {
//        if (bitmap != null) {
//            val matrix = android.graphics.Matrix()
//            when (orientation) {
//                ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90F)
//                ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180F)
//                ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270F)
//            }
//            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
//        }
//        return null
//    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}