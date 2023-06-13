package com.fari.agrovision.ui.camera

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.fari.agrovision.R
import com.fari.agrovision.data.remote.utils.createFile
import com.fari.agrovision.data.remote.utils.uriToFile
import com.fari.agrovision.databinding.ActivityCameraBinding

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding

    private var isDetection = true
    private var isDiseaseRipe = true
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getCondition()

        setupAction()
    }

    private fun getCondition() {
        isDetection = intent.getBooleanExtra(IS_DETECTION, true)
        isDiseaseRipe = intent.getBooleanExtra(IS_DISEASE_RIPE, true)
    }

    private fun setupAction() {
        binding.apply {
            btnCamera.setOnClickListener { takePhoto() }
            btnSwitchCamera.setOnClickListener { startCamera() }
            btnGallery.setOnClickListener { startGallery() }
        }
    }

    public override fun onResume() {
        super.onResume()
        startCamera()
        hideSystemUI()
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = createFile(application)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(
                        this@CameraActivity,
                        "Gagal mengambil gambar",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val intent = Intent(this@CameraActivity, ImagePreviewActivity::class.java)
                    intent.putExtra(ImagePreviewActivity.IMAGE_PATH, photoFile.absolutePath)
                    intent.putExtra(ImagePreviewActivity.IS_DETECTION, isDetection)
                    intent.putExtra(ImagePreviewActivity.IS_DISEASE_RIPE, isDiseaseRipe)
                    intent.putExtra(
                        "isBackCamera",
                        cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA
                    )
                    startActivity(intent)
                    finish()
                }
            }
        )
    }

    private val launcherIntentGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val selectedImg = result.data?.data as Uri
                selectedImg.let { uri ->
                    val imageFile = uriToFile(uri, this@CameraActivity)
                    val intent = Intent(this@CameraActivity, ImagePreviewActivity::class.java)
                    intent.putExtra(ImagePreviewActivity.IMAGE_PATH, imageFile.absolutePath)
                    intent.putExtra(ImagePreviewActivity.IS_DETECTION, isDetection)
                    intent.putExtra(ImagePreviewActivity.IS_DISEASE_RIPE, isDiseaseRipe)
                    startActivity(intent)
                    finish()
                }
            }
        }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                val camera = cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )

                val cameraInfo = camera.cameraInfo
                val hasFlashUnit = cameraInfo.hasFlashUnit()

                if (hasFlashUnit) {
                    val cameraControl = camera.cameraControl

                    cameraControl.enableTorch(false)

                    binding.btnFlash.setOnClickListener {
                        val newFlashMode = camera.cameraInfo.torchState.value

                        if (newFlashMode == TorchState.OFF) {
                            cameraControl.enableTorch(true)
                            binding.btnFlash.setImageResource(R.drawable.ic_flash_on)
                        } else {
                            cameraControl.enableTorch(false)
                            binding.btnFlash.setImageResource(R.drawable.ic_flash_off)
                        }
                    }
                } else {
                    Toast.makeText(
                        this@CameraActivity,
                        "Perangkat tidak memiliki unit flash",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (exc: Exception) {
                Toast.makeText(
                    this@CameraActivity,
                    "Gagal memunculkan kamera",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }, ContextCompat.getMainExecutor(this))

        binding.btnSwitchCamera.setOnClickListener {
            cameraSelector =
                if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA
                else CameraSelector.DEFAULT_BACK_CAMERA
            startCamera()
        }
    }

    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    companion object {
        const val IS_DETECTION = "is_detection"
        const val IS_DISEASE_RIPE = "is_disease_ripe"
    }
}