package kr.co.bepo.androidonline.Apple

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import kr.co.bepo.androidonline.MasterApplication
import kr.co.bepo.androidonline.R
import kr.co.bepo.androidonline.databinding.ActivityAppleBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAppleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAppleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (application as MasterApplication).service.getSongList().enqueue(object : Callback<ArrayList<Song>> {
            override fun onResponse(call: Call<ArrayList<Song>>, response: Response<ArrayList<Song>>) {
                if (response.isSuccessful) {
                    Log.d(TAG, "onResponse: Success!")

                    val songList = response.body()
                    val adapter = AppleAdapter(songList, LayoutInflater.from(this@AppleActivity), this@AppleActivity)
                    binding.songListRecyclerView.adapter = adapter
                }
            }

            override fun onFailure(call: Call<ArrayList<Song>>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")

            }
        })
    }

    companion object {
        private const val TAG = "AppleActivity"
    }
}