package kr.co.bepo.androidonline.Outstagram

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.bepo.androidonline.MasterApplication
import kr.co.bepo.androidonline.databinding.ActivityPostListBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPostListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getAllPosts()
        bottomNavigationClicked()
    }

    private fun bottomNavigationClicked(){
        binding.userTextView.setOnClickListener {
            startActivity(Intent(this, MyPostActivity::class.java))
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

    private fun getAllPosts() {
        (application as MasterApplication).service.getAllPosts()
            .enqueue(object : Callback<ArrayList<Post>> {
                override fun onResponse(
                    call: Call<ArrayList<Post>>,
                    response: Response<ArrayList<Post>>
                ) {
                    if (response.isSuccessful) {
                        Log.d(TAG, "onResponse: Success!")

                        val postList = response.body()
                        val adapter =
                            PostAdapter(postList, LayoutInflater.from(this@PostListActivity))

                        binding.postRecyclerView.adapter = adapter
                        binding.postRecyclerView.layoutManager =
                            LinearLayoutManager(this@PostListActivity)
                    }
                }

                override fun onFailure(call: Call<ArrayList<Post>>, t: Throwable) {
                    Log.d(TAG, "Error: ${t.message}")
                }
            })
    }

    companion object {
        private const val TAG = "PostListActivity"
    }
}