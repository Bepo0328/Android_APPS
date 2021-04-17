package kr.co.bepo.thesecretdiary

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener

class DiaryActivity : AppCompatActivity() {
    private val tbBack: ImageView by lazy {
        findViewById<ImageView>(R.id.activity_diary_tb_back)
    }

    private val etDiary: EditText by lazy {
        findViewById<EditText>(R.id.activity_diary_et_diary)
    }

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        tbBack.setOnClickListener {
            onBackPressed()
        }

        val detailPreferences = getSharedPreferences("diary", Context.MODE_PRIVATE)

        etDiary.setText(detailPreferences.getString("detail", ""))

        val runnable = Runnable {
            getSharedPreferences("diary", Context.MODE_PRIVATE).edit {
                putString("detail", etDiary.text.toString())
            }
            Log.d("DiaryActivity", "Save!!! ${etDiary.text}")
        }

        etDiary.addTextChangedListener {
            Log.d("DiaryActivity", "TextChanged :: $it")
            handler.removeCallbacks(runnable)
            handler.postDelayed(runnable, 500)
        }
    }


}