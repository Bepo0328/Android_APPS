package kr.co.bepo.androidonline.Mytube

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import kr.co.bepo.androidonline.MasterApplication
import kr.co.bepo.androidonline.Outstagram.MyPostActivity
import kr.co.bepo.androidonline.R
import kr.co.bepo.androidonline.databinding.ActivityMyTubeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MytubeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyTubeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyTubeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (application as MasterApplication).service.getYoutubeList()
            .enqueue(object : Callback<ArrayList<Mytube>> {
                override fun onResponse(
                    call: Call<ArrayList<Mytube>>,
                    response: Response<ArrayList<Mytube>>
                ) {
                    if (response.isSuccessful) {
                        Log.d(TAG, "onResponse: Success!")

                        val youtubeList = response.body()
                        Log.d(TAG, "result: $youtubeList")

                        val adapter = MytubeAdapter(youtubeList, LayoutInflater.from(this@MytubeActivity), this@MytubeActivity)
                        binding.myTubeRecyclerView.adapter = adapter
                    }
                }

                override fun onFailure(call: Call<ArrayList<Mytube>>, t: Throwable) {
                    Log.d(TAG, "Error: ${t.message}")
                }
            })
    }

    companion object {
        private const val TAG = "MytubeActivity"
    }
}