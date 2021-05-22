package kr.co.bepo.androidonline.Outstagram

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.co.bepo.androidonline.MasterApplication
import kr.co.bepo.androidonline.databinding.ActivityUserInfoBinding

class UserInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListener()
        bottomNavigationClicked()
    }

    private fun setupListener() {
        binding.logoutButton.setOnClickListener {
            val sharedPreference = getSharedPreferences("login_sp", Context.MODE_PRIVATE)
            sharedPreference.edit().apply {
                putString("login_sp", "null")
            }.apply()
            (application as MasterApplication).createRetrofit()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun bottomNavigationClicked(){
        binding.homeTextView.setOnClickListener {
            startActivity(Intent(this, PostListActivity::class.java))
            finish()
        }

        binding.userTextView.setOnClickListener {
            startActivity(Intent(this, MyPostActivity::class.java))
            finish()
        }

        binding.uploadTextView.setOnClickListener {
            startActivity(Intent(this, UploadActivity::class.java))
            finish()
        }
    }

}