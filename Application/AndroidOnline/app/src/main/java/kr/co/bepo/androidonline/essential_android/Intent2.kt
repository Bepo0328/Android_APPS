package kr.co.bepo.androidonline.essential_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kr.co.bepo.androidonline.databinding.ActivityIntent2Binding

class Intent2 : AppCompatActivity() {
    private lateinit var binding: ActivityIntent2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIntent2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.resultButton.setOnClickListener {
            val number1 = intent.getIntExtra("number1", 0)
            val number2 = intent.getIntExtra("number2", 0)

            Log.d(TAG, "number1 =  $number1")
            Log.d(TAG, "number2 =  $number2")

            val result = number1 + number2
            val resultIntent = Intent()
            resultIntent.apply {
                this.putExtra("result", result)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }

    }

    companion object {
        private const val TAG = "Intent2Activity"
    }
}