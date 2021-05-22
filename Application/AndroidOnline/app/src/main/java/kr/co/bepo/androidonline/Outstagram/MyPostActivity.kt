package kr.co.bepo.androidonline.Outstagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.bepo.androidonline.MasterApplication
import kr.co.bepo.androidonline.databinding.ActivityMyPostBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyPostBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getMyPosts()
        bottomNavigationClicked()
    }

    private fun getMyPosts() {
        (application as MasterApplication).service.getUserPostList()
            .enqueue(object : Callback<ArrayList<Post>> {
                override fun onResponse(
                    call: Call<ArrayList<Post>>,
                    response: Response<ArrayList<Post>>
                ) {
                    if (response.isSuccessful) {
                        Log.d(TAG, "onResponse: Success!")

                        val myPostList = response.body()
                        val adapter =
                            PostAdapter(myPostList, LayoutInflater.from(this@MyPostActivity))

                        binding.myPostRecyclerView.adapter = adapter
                        binding.myPostRecyclerView.layoutManager =
                            LinearLayoutManager(this@MyPostActivity)
                    }
                }

                override fun onFailure(call: Call<ArrayList<Post>>, t: Throwable) {
                    Log.d(TAG, "Error: ${t.message}")
                }
            })
    }

    private fun bottomNavigationClicked() {
        binding.homeTextView.setOnClickListener {
            startActivity(Intent(this, PostListActivity::class.java))
            finish()
        }

        binding.uploadTextView.setOnClickListener {
            startActivity(Intent(this, UploadActivity::class.java))
            finish()
        }

        binding.infoTextView.setOnClickListener {
            startActivity(Intent(this, UserInfoActivity::class.java))
            finish()
        }
    }

    companion object {
        private const val TAG = "MyPostActivity"
    }
}