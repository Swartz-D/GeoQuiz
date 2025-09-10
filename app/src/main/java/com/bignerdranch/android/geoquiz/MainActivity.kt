package com.bignerdranch.android.geoquiz

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.MutableIntList
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.bignerdranch.android.geoquiz.databinding.ActivityMainBinding
private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )
    private var currentIndex = 0
    private var points = 0
    private var answeredList = MutableIntList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.trueButton.setOnClickListener { view: View ->
            checkAnswer(true, view)
            displayGradeIfComplete()
        }
        binding.falseButton.setOnClickListener { view: View ->
            checkAnswer(false, view)
            displayGradeIfComplete()
        }
        binding.nextButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }
        binding.previousButton.setOnClickListener {
            currentIndex = (currentIndex - 1 + questionBank.size) % questionBank.size
            updateQuestion()
        }
        updateQuestion()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop(), called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }
    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex].textResId
        binding.questionTextView.setText(questionTextResId)

        if (currentIndex in answeredList) {
            toggleAnswerButtons(false)
        } else {
            toggleAnswerButtons(true)
        }

    }
    private fun checkAnswer(userAnswer: Boolean, v: View) {
        toggleAnswerButtons(false)
        answeredList.add(currentIndex)
        val correctAnswer = questionBank[currentIndex].answer
        var messageResId: Int

        if (userAnswer == correctAnswer) {
            messageResId = R.string.correct_toast
            points++
        } else {
            messageResId = R.string.incorrect_toast
        }

        Snackbar.make(
            v,
            messageResId,
            Snackbar.LENGTH_SHORT
        ).show()
    }
    private fun toggleAnswerButtons(enableButton: Boolean) {
        binding.trueButton.isEnabled = enableButton
        binding.falseButton.isEnabled = enableButton
    }

    private fun displayGradeIfComplete() {
        if (answeredList.size == questionBank.size) {
            val percentGrade = (points.toDouble() / questionBank.size.toDouble()) * 100
            Snackbar.make(
                findViewById(R.id.question_text_view),
                "$percentGrade%",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }
}