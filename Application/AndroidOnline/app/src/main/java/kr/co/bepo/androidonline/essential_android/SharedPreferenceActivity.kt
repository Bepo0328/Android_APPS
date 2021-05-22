package kr.co.bepo.androidonline.essential_android

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kr.co.bepo.androidonline.databinding.ActivitySharedPreferenceBinding

class SharedPreferenceActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySharedPreferenceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySharedPreferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreference = getSharedPreferences("sp1", Context.MODE_PRIVATE)

        binding.saveButton.setOnClickListener {
            sharedPreference.edit().apply {
                putString("hello", "안녕하세요")
                putString("goodbye", "안녕히가세요")
            }.apply()
        }

        binding.loadButton.setOnClickListener {
            val value1 = sharedPreference.getString("hello", "데이터 없음1")
            val value2 = sharedPreference.getString("goodbye", "데이터 없음2")
            Log.d(TAG, "value1: $value1")
            Log.d(TAG, "value2: $value2")
        }

        binding.deleteOneButton.setOnClickListener {
            sharedPreference.edit().remove("hello").apply()
        }

        binding.deleteAllButton.setOnClickListener {
            sharedPreference.edit().clear().apply()
        }
    }

    companion object {
        private const val TAG = "SharedPreference"
    }
}