package kr.co.bepo.bmicalculator

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.pow

class ResultActivity: AppCompatActivity(){

    private lateinit var tbBack: ImageView
    private lateinit var tvBmi: TextView
    private lateinit var tvResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        tbBack = findViewById(R.id.activity_result_tb_back)
        tvBmi = findViewById(R.id.activity_result_tv_bmi)
        tvResult = findViewById(R.id.activity_result_tv_result)

        val gender: Boolean = intent.getBooleanExtra("gender", true)
        val height: Int = intent.getIntExtra("height", 0)
        val weight: Int = intent.getIntExtra("weight", 0)
        val age: Int = intent.getIntExtra("age", 0)

        Log.d("ResultActivity", "Gender: $gender, Height: $height, Weight: $weight, Age: $age")

        val bmi = weight / (height / 100.0).pow(2.0)
        val resultText = when {
            bmi >= 35.0 -> "고도 비만"
            bmi >= 30.0 -> "중정도 비만"
            bmi >= 25.0 -> "경도 비만"
            bmi >= 23.0 -> "과체중"
            bmi >= 18.5 -> "정상체중"
            else -> "저체중"
        }

        tvBmi.text = String.format("%.2f", bmi)
        tvResult.text = resultText

        tbBack.setOnClickListener {
            onBackPressed();
        }
    }
}