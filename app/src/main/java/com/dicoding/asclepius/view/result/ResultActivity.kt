package com.dicoding.asclepius.view.result

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asclepius.data.ViewModelFactory
import com.dicoding.asclepius.data.local.History
import com.dicoding.asclepius.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private val viewModelFactory by lazy { ViewModelFactory(application) }
    private val resultViewModel: ResultViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupResultData()
    }

    private fun setupResultData() {
        val analysisData = getAnalysisDataFromIntent()
        displayResults(analysisData)
        setupSaveButton(analysisData)
    }

    private fun getAnalysisDataFromIntent(): AnalysisData {
        return AnalysisData(
            result = intent.getStringExtra(EXTRA_RESULT).orEmpty(),
            prediction = intent.getStringExtra(EXTRA_PREDICT).orEmpty(),
            score = intent.getStringExtra(EXTRA_SCORE).orEmpty(),
            imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
        )
    }

    private fun displayResults(data: AnalysisData) {
        with(binding) {
            data.imageUri?.let { uri ->
                Log.d(TAG, "Displaying image: $uri")
                resultImage.setImageURI(uri)
                resultText.text = data.result
            }
        }
    }

    private fun setupSaveButton(data: AnalysisData) {
        binding.btnSaveAnalyzeResult.setOnClickListener {
            val history = createHistory(data)
            resultViewModel.insert(history)
            showSaveSuccessMessage()
        }
    }

    private fun createHistory(data: AnalysisData) = History().apply {
        image = data.imageUri.toString()
        prediction = data.prediction
        score = data.score
    }

    private fun showSaveSuccessMessage() {
        Toast.makeText(this, "Result saved", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "ResultActivity"
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_RESULT = "extra_result"
        const val EXTRA_PREDICT = "extra_predict"
        const val EXTRA_SCORE = "extra_score"
    }
}

private data class AnalysisData(
    val result: String,
    val prediction: String,
    val score: String,
    val imageUri: Uri?
)