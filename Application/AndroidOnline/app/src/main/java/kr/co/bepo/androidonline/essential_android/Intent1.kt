package kr.co.bepo.androidonline.essential_android

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kr.co.bepo.androidonline.databinding.ActivityIntent1Binding

class Intent1 : AppCompatActivity() {
    private lateinit var binding: ActivityIntent1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIntent1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.changeActivityButton.setOnClickListener {
//            val intent = Intent(this@Intent1, Intent2::class.java)
//            intent.putExtra("number1", 1)
//            intent.putExtra("number2", 2)
//            startActivity(intent)

//            val intent2 = Intent(this@Intent1, Intent2::class.java)
//            intent2.apply {
//                this.putExtra("number1", 1)
//                this.putExtra("number2", 2)
//            }
//            startActivityForResult(intent2, intent2RequestCode)

            val intent3 = Intent(Intent.ACTION_VIEW, Uri.parse("http://m.naver.com"))
            startActivityForResult(intent3, intent3RequestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == intent3RequestCode) {
            Log.d(TAG, "requestCode: $requestCode")
            Log.d(TAG, "resultCode: $resultCode")

            val result = data?.getIntExtra("result", 0)
            Log.d(TAG, "result: $result")
        }
    }

    companion object {
        private const val TAG = "Intent1Activity"
        private const val intent2RequestCode = 1000
        private const val intent3RequestCode = 2000

    }
}