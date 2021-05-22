package kr.co.bepo.androidonline.essential_android

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kr.co.bepo.androidonline.databinding.ActivityOpenInternetBinding

class OpenInternet : AppCompatActivity() {
    private lateinit var binding: ActivityOpenInternetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOpenInternetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.resultButton.setOnClickListener {
            val address = binding.urlEditText.text.toString()
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(address))
            startActivity(intent)
        }


        binding.urlEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                Log.d(TAG, "beforeTextChanged: $s")
//                Log.d(TAG, "start: $start")
//                Log.d(TAG, "count: $count")
//                Log.d(TAG, "after: $after")

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d(TAG, "onTextChanged: $s")
                Log.d(TAG, "start: $start")
                Log.d(TAG, "before: $before")
                Log.d(TAG, "count: $count")
            }

            override fun afterTextChanged(s: Editable?) {
                //Log.d(TAG, "afterTextChanged: $s")
            }
        })
    }

    companion object {
        private const val TAG = "OpenInternetActivity"
    }
}