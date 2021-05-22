package kr.co.bepo.androidonline.Outstagram

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kr.co.bepo.androidonline.MasterApplication
import kr.co.bepo.androidonline.databinding.ActivityUploadBinding
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    private lateinit var filePath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListener()
        bottomNavigationClicked()
    }

    private fun setupListener() {
        binding.selectPictureButton.setOnClickListener {
            getPicture()
        }

        binding.uploadButton.setOnClickListener {
            uploadPost()
        }
    }

    private fun bottomNavigationClicked() {
        binding.homeTextView.setOnClickListener {
            startActivity(Intent(this, PostListActivity::class.java))
            finish()
        }

        binding.userTextView.setOnClickListener {
            startActivity(Intent(this, MyPostActivity::class.java))
            finish()
        }

        binding.infoTextView.setOnClickListener {
            startActivity(Intent(this, UserInfoActivity::class.java))
            finish()
        }
    }

    private fun getPicture() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            type = "image/*"
        }
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            val uri: Uri = data?.data ?: Uri.EMPTY
            Log.d(TAG, "uri: $uri")

            filePath = getImageFilePath(uri)
            Log.d(TAG, "filePath: $filePath")
        }
    }

    private fun getImageFilePath(contentUri: Uri): String {
        var columnIndex = 0
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(contentUri, projection, null, null, null)
        if (cursor?.moveToFirst() == true) {
            columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        }
        val result = cursor?.getString(columnIndex).orEmpty()

        cursor?.close()
        return result
    }

    private fun uploadPost() {
        val file = File(filePath)
        val fileRequestBody = RequestBody.create(MediaType.parse("image/*"), file)
        val part = MultipartBody.Part.createFormData("image", file.name, fileRequestBody)
        val content = RequestBody.create(MediaType.parse("text/plain"), getContent())

        (application as MasterApplication).service.uploadPost(part, content)
            .enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (response.isSuccessful) {
                        Log.d(TAG, "onResponse: Success!")

                        val post = response.body()
                        Log.d(TAG, "post: ${post?.content}")

                        startActivity(Intent(this@UploadActivity, MyPostActivity::class.java))
                        finish()
                    }
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    Log.d(TAG, "Error: ${t.message}")
                }
            })
    }

    private fun getContent(): String {
        return binding.inputTextEditText.text.toString()
    }

    companion object {
        private const val TAG = "UploadActivity"
        private const val REQUEST_CODE_PICK_IMAGE = 1000
    }
}