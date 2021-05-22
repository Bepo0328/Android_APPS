package kr.co.bepo.androidonline.essential_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.co.bepo.androidonline.databinding.ActivityPhoneBookDetailBinding

class PhoneBookDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPhoneBookDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhoneBookDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getPersonInfoAndDraw()

        binding.backImageView.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getPersonInfoAndDraw() {
        val name = intent.getStringExtra("name")
        val number = intent.getStringExtra("number")

        binding.personDetailNameTextView.text = name
        binding.personDetailNumberTextView.text = number
    }
}