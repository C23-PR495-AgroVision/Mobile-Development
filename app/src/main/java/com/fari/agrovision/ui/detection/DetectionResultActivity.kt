package com.fari.agrovision.ui.detection

import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.fari.agrovision.R
import com.fari.agrovision.data.remote.model.detection.DetectionResponse
import com.fari.agrovision.databinding.ActivityDetectionResultBinding
import com.fari.agrovision.ui.camera.ImagePreviewActivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class DetectionResultActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDetectionResultBinding

    private var isHistory = false
    private lateinit var currentDate: String
    private var imageFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetectionResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setPreviewImage()
        getDataResult()
        setToolbar()
    }

    private fun getDataResult() {
        isHistory = intent.getBooleanExtra(IS_HISTORY, false)
        if (!isHistory) {
            val dataResultDetection =
                intent.getParcelableExtra<DetectionResponse>(DETECTION_RESULT) as DetectionResponse
            currentDate = SimpleDateFormat("dd MMM yyyy HH:mm:ss").format(Date())
            setDataResultView(
                currentDate = currentDate,
                classResult = dataResultDetection.model,
                title = intent.getStringExtra(TITLE).toString(),
                confidence = "${(dataResultDetection.confidence * 100).toInt()}%",
                description = dataResultDetection.description
            )
        } else {
//            val dataResultHistory =
//                intent.getParcelableExtra<HistoryDetectionItem>(DETECTION_RESULT) as HistoryDetectionItem
//            currentDate = dataResultHistory.datetime
//            setDataResultView(
//                currentDate = currentDate,
//                imgUrl = dataResultHistory.detectionImg,
//                classResult = dataResultHistory.predictedClass,
//                type = dataResultHistory.type,
//                description = dataResultHistory.description
//            )
        }
    }

    private fun setDataResultView(
        currentDate: String = "",
        classResult: String = "",
        title: String = "",
        confidence: String = "",
        description: String = ""
    ) {
        binding.apply {
            tvDate.text = currentDate
            tvResult.text = classResult
            tvResultTitle.text = title
            tvResultPercent.text = confidence
            tvDescDetection.text = description
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvDescDetection.text =
                    Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT)
            } else {
                tvDescDetection.text =
                    HtmlCompat.fromHtml(description, HtmlCompat.FROM_HTML_MODE_LEGACY)
            }
        }
    }

    private fun setPreviewImage() {
        val imagePath = intent.getStringExtra(IMAGE_PATH)
        val imageUri = Uri.parse(imagePath)
        imageFile = imageUri.path?.let { File(it) }
        binding.ivScanned.setImageURI(imageUri)

    }

    private fun setToolbar() {
        binding.toolbar.btnBackToolbar.setOnClickListener(this)
        binding.toolbar.tvToolbarTitle.text = if (!isHistory) "Hasil Deteksi" else "Riwayat"
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_back_toolbar -> {
                finish()
            }
        }
    }

    companion object {
        const val DETECTION_RESULT = "detection_result"
        const val IMAGE_PATH = "image_absolute_path"
        const val TITLE = "detection_title"
        const val IS_HISTORY = "is_history"
    }
}