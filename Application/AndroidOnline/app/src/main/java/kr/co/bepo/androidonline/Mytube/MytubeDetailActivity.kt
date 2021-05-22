package kr.co.bepo.androidonline.Mytube

import android.os.Bundle
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import kr.co.bepo.androidonline.databinding.ActivityMytubeDetailBinding

class MytubeDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMytubeDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMytubeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val url = intent.getStringExtra("video_url")
        val mediaController = MediaController(this)
        binding.videoView.apply {
            setVideoPath(url)
            requestFocus()
            start()
        }
        mediaController.show()
    }
}