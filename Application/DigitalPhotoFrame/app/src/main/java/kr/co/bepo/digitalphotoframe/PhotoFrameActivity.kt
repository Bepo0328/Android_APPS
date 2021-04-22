package kr.co.bepo.digitalphotoframe

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import java.util.*
import kotlin.concurrent.timer

class PhotoFrameActivity : AppCompatActivity() {
    private val photoList: MutableList<Uri> = mutableListOf()

    private var currentPosition: Int = 0

    private var timer: Timer? = null

    private val ivPhotoBackground: ImageView by lazy {
        findViewById<ImageView>(R.id.activity_photo_frame_iv_photo_background)
    }

    private val ivPhoto: ImageView by lazy {
        findViewById<ImageView>(R.id.activity_photo_frame_iv_photo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_frame)

        getPhotoUriFromIntent()
    }

    private fun getPhotoUriFromIntent() {
        val size = intent.getIntExtra("photoListSize", 0)
        for (i in 0..size) {
            intent.getStringExtra("photo$i")?.let {
                photoList.add(Uri.parse(it))
            }
        }
    }

    private fun startTimer() {
        var first: Boolean = true
        timer = timer(period = 5 * 1000) {
            runOnUiThread {
                val current = currentPosition
                val next = if (photoList.size <= currentPosition + 1) 0 else currentPosition + 1

                ivPhotoBackground.setImageURI(photoList[current])

                if (first) {
                    ivPhoto.setImageURI(photoList[current])
                    first = false
                } else {
                    ivPhoto.alpha = 0f
                    ivPhoto.setImageURI(photoList[next])
                    ivPhoto.animate()
                        .alpha(1.0f)
                        .setDuration(1000)
                        .start()
                }
                currentPosition = next
            }
        }
    }

    override fun onStop() {
        super.onStop()

        timer?.cancel()
    }

    override fun onStart() {
        super.onStart()

        startTimer()
    }

    override fun onDestroy() {
        super.onDestroy()

        timer?.cancel()
    }
}