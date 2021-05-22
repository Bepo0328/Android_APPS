package kr.co.bepo.androidonline.Outstagram

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kr.co.bepo.androidonline.MasterApplication
import kr.co.bepo.androidonline.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if ((application as MasterApplication).checkIsLogin()) {
            nextPage()
        }

        setupListener()
    }

    private fun setupListener() {
        binding.signUpTextView.setOnClickListener {
            startActivity(Intent(this, EmailSignUpActivity::class.java))
        }

        binding.signInButton.setOnClickListener {
            login()
        }
    }
    private fun nextPage(){
        startActivity(Intent(this@LoginActivity, PostListActivity::class.java))
        finish()
    }

    private fun login() {
        val username = getUserName()
        val password = getUserPassword()

        (application as MasterApplication).service.login(username, password)
            .enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        Log.d(TAG, "onResponse: Success!")

                        val user = response.body()
                        val token = user?.token
                        saveUserToken(token.orEmpty())
                        (application as MasterApplication).createRetrofit()
                        Toast.makeText(this@LoginActivity, "로그인 되었습니다.", Toast.LENGTH_SHORT).show()

                        nextPage()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.d(TAG, "Error: ${t.message}")
                }
            })
    }

    private fun saveUserToken(token: String) {
        val sharedPreference =
            this@LoginActivity.getSharedPreferences("login_sp", Context.MODE_PRIVATE)
        sharedPreference.edit().apply {
            putString("login_sp", token)
        }.apply()
    }


    private fun getUserName(): String {
        return binding.emailEditText.text.toString()
    }

    private fun getUserPassword(): String {
        return binding.passwordEditText.text.toString()
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}