package com.dicoding.asclepius.view.main

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.BottomNavigationHelper
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.view.result.ResultActivity
import com.yalantis.ucrop.UCrop
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper

    private var currentImageUri: Uri? = null
    private var results: String? = null
    private var prediction: String? = null
    private var score: String? = null

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navBottom.selectedItemId = R.id.analyze
        BottomNavigationHelper.setupBottomNavigation(this, binding.navBottom)

        mainViewModel.currentImageUri.observe(this) { uri ->
            uri?.let {
                binding.previewImageView.setImageURI(it)
            }
        }

        binding.galleryButton.setOnClickListener{
            startGallery()
        }

        val intent = Intent(this, ResultActivity::class.java)
        binding.analyzeButton.setOnClickListener {
            if (mainViewModel.currentImageUri.value == null) {
                showToast("Choose image first")
            } else {
                analyzeImage(intent)
                moveToResult(intent)
            }
        }
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->

        if (uri != null) {
            currentImageUri = uri
            startCrop(uri)
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private val cropLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val resultUri = UCrop.getOutput(result.data!!)
            resultUri?.let { uri ->
                mainViewModel.setImageUri(uri)
                showImage()
            }
        } else if (result.resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(result.data!!)
            cropError?.let {
                showToast("Crop Error: ${it.message}")
            }
        }
    }

    private fun startGallery() {
        galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startCrop(uri: Uri) {
        val uniqueFileName = "cropped_image_${System.currentTimeMillis()}.jpg"
        val destinationUri = Uri.fromFile(File(cacheDir, uniqueFileName))
        val uCrop = UCrop.of(uri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(1000, 1000)

        cropLauncher.launch(uCrop.getIntent(this))
    }

    private fun showImage() {
        Log.d("MainActivity", "Current Image URI: $currentImageUri")
        mainViewModel.currentImageUri.value?.let { uri ->
            binding.previewImageView.setImageURI(uri)
        } ?: showToast("No image to display")
    }

    private fun analyzeImage(argument: Intent) {
        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onResults(result: List<Classifications>?, inferenceTime: Long) {
                    result?.let { it ->
                        if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                            // Mengurutkan kategori berdasarkan skor terbesar
                            val sortedCategories = it[0].categories.sortedByDescending { category -> category.score }

                            val highestCategory = sortedCategories[0]
                            prediction = highestCategory.label
                            score = NumberFormat.getPercentInstance().format(highestCategory.score)

                            // Mengirimkan hanya hasil terbesar ke ResultActivity
                            results = "$prediction $score"
                        } else {
                            showToast("Analyze failed, try again")
                        }
                    }
                }

                override fun onError(error: String) {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )

        mainViewModel.currentImageUri.value?.let { uri ->
            imageClassifierHelper.classifyStaticImage(uri)
            argument.putExtra(ResultActivity.EXTRA_RESULT, results)
            argument.putExtra(ResultActivity.EXTRA_PREDICT, prediction)
            argument.putExtra(ResultActivity.EXTRA_SCORE, score)
        }
    }


    private fun moveToResult(intent: Intent) {
        intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, mainViewModel.currentImageUri.value.toString())
        startActivity(intent)

        binding.previewImageView.setImageResource(R.drawable.ic_place_holder)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}