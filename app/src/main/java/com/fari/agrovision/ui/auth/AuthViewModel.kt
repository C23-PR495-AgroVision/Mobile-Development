package com.fari.agrovision.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.fari.agrovision.data.remote.repository.UserRepository
import java.io.File

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun isLogin() = userRepository.isLogin().asLiveData()

    fun login(email: String, password: String) = userRepository.login(email, password).asLiveData()

    fun register(email: String, password: String, name: String) =
        userRepository.register(email, password, name).asLiveData()

    fun reset(email: String) = userRepository.reset(email).asLiveData()

    fun logout() = userRepository.logout().asLiveData()

    fun getDataUser(token:String) = userRepository.getDataUser(token).asLiveData()

    fun editName(
        token: String,
        newName: String,
    ) = userRepository.editName(token,newName).asLiveData()

    fun editProfilePicture(
        uid: String, imageFile: File
    ) = userRepository.editProfilePicture(uid,imageFile).asLiveData()
}