package kr.co.bepo.bmicalculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    private lateinit var btnResult: Button
    private lateinit var etHeight: TextInputEditText
    private lateinit var etWeight: TextInputEditText
    private lateinit var etAge: TextInputEditText
    private lateinit var rgGender: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnResult = findViewById(R.id.activity_main_btn_result)
        etHeight = findViewById(R.id.activity_main_et_height)
        etWeight = findViewById(R.id.activity_main_et_Weight)
        etAge = findViewById(R.id.activity_main_et_age)
        rgGender = findViewById(R.id.activity_main_rg_gender)

        var gender: Boolean? = true
        rgGender.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.activity_main_rb_male -> gender = true
                R.id.activity_main_rb_female -> gender = false
            }
        }

        btnResult.setOnClickListener {
            Log.d("MainActivity", "ResultButton 이 클릭되었습니다.")

            if (etHeight.text.isNullOrBlank() || etWeight.text.isNullOrEmpty()) {
                Toast.makeText(this, "빈 값이 있습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val height: Int = etHeight.text.toString().toInt()
            val weight: Int = etWeight.text.toString().toInt()
            val age: Int = etAge.text.toString().toInt()

            val intent = Intent(this, ResultActivity::class.java)

            intent.putExtra("gender", gender)
            intent.putExtra("height", height)
            intent.putExtra("weight", weight)
            intent.putExtra("age", age)

            startActivity(intent)

            Log.d("MainActivity", "Gender: $gender, Height: $height, Weight: $weight, Age: $age")
        }
    }

}