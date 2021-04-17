package kr.co.bepo.thesecretdiary

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {


    private val npHundreds: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.activity_main_np_hundreds)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }

    private val npTens: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.activity_main_np_tens)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }

    private val npUnits: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.activity_main_np_units)
            .apply {
                minValue = 0
                maxValue = 9
            }
    }

    private val btnOpen: AppCompatButton by lazy {
        findViewById<AppCompatButton>(R.id.activity_main_btn_open)
    }

    private val btnChangePassword: AppCompatButton by lazy {
        findViewById<AppCompatButton>(R.id.activity_main_btn_change_password)
    }

    private var changePasswordMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        npHundreds
        npTens
        npUnits

        btnOpen.setOnClickListener {
            if (changePasswordMode) {
                Toast.makeText(this, "비밀번호 변경 중입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val passwordPreference = getSharedPreferences("password", Context.MODE_PRIVATE)
            val passwordFromUser: String = "${npHundreds.value}${npTens.value}${npUnits.value}"

            if (passwordPreference.getString("password", "000").equals(passwordFromUser)) {
                startActivity(Intent(this, DiaryActivity::class.java))
            } else {
                showErrorAlertDialog()
            }
        }

        btnChangePassword.setOnClickListener {
            val passwordPreference = getSharedPreferences("password", Context.MODE_PRIVATE)
            val passwordFromUser: String = "${npHundreds.value}${npTens.value}${npUnits.value}"

            if (changePasswordMode) {
                // 번호를 저장하는 기능
                passwordPreference.edit(true) {
                    putString("password", passwordFromUser)
                }

                changePasswordMode = false
                btnChangePassword.setBackgroundColor(Color.BLACK)

            } else {
                // changePasswordMode 가 활성화 :: 비밀번호가 맞는지 체크
                if (passwordPreference.getString("password", "000").equals(passwordFromUser)) {
                    changePasswordMode = true
                    Toast.makeText(this, "변경할 패스워드를 입력해주세요.", Toast.LENGTH_SHORT).show()
                    btnChangePassword.setBackgroundColor(Color.RED)
                } else {
                    showErrorAlertDialog()
                }
            }
        }
    }

    // 패스워드 인증 실패
    private fun showErrorAlertDialog() {
        AlertDialog.Builder(this)
            .setTitle("실패!!")
            .setMessage("비밀번호가 잘못되었습니다.")
            .setPositiveButton("확인") { _, _ -> }
            .create()
            .show()
    }
}