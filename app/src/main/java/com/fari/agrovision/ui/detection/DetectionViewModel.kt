package com.fari.agrovision.ui.detection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.fari.agrovision.data.remote.repository.DetectionRepository
import java.io.File

class DetectionViewModel(private val detectionRepository: DetectionRepository): ViewModel() {

    fun postDetection(
        uid: String, model: String, imageFile: File
    ) = detectionRepository.postDetection(uid,model,imageFile).asLiveData()

    fun getListDetectionDisease(
    ) = detectionRepository.getListDetectionDisease().asLiveData()

    fun getListDetectionRipeness(
    ) = detectionRepository.getListDetectionRipeness().asLiveData()
}