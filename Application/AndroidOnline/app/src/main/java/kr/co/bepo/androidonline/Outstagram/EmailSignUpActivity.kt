package kr.co.bepo.androidonline.Outstagram

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kr.co.bepo.androidonline.MasterApplication
import kr.co.bepo.androidonline.databinding.ActivityEmailSignupBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmailSignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEmailSignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEmailSignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListener()
    }

    private fun setupListener() {
        binding.signUpButton.setOnClickListener {
            register()
        }
    }

    private fun register() {
        val username = getUserName()
        val userPassword = getUserPassword()
        val userPasswordCheck = getUserPasswordCheck()

        (application as MasterApplication).service.register(
            username,
            userPassword,
            userPasswordCheck
        ).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    Log.d(TAG, "onResponse: Success!")

                    val user = response.body()
                    val token = user?.token
                    saveUserToken(token.orEmpty())
                    (application as MasterApplication).createRetrofit()
                    Toast.makeText(this@EmailSignUpActivity, "회원가입 되었습니다.", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun saveUserToken(token: String) {
        val sharedPreference =
            this@EmailSignUpActivity.getSharedPreferences("login_sp", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit().also {
            it.putString("login_sp", token)
        }.commit()
    }

    private fun getUserName(): String {
        return binding.emailEditText.text.toString()
    }

    private fun getUserPassword(): String {
        return binding.passwordEditText.text.toString()
    }

    private fun getUserPasswordCheck(): String {
        return binding.passwordCheckEditText.text.toString()
    }

    companion object {
        private const val TAG = "EmailSignUpActivity"
    }
}