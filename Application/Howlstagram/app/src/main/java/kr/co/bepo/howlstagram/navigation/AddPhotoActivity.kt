package kr.co.bepo.howlstagram.navigation

import android.R.attr
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_add_photo.*
import kr.co.bepo.howlstagram.R
import kr.co.bepo.howlstagram.model.ContentDTO
import java.text.SimpleDateFormat
import java.util.*


class AddPhotoActivity : AppCompatActivity() {
    // Firebase
    private var auth: FirebaseAuth? = null
    private var storage: FirebaseStorage? = null
    private var storageRef: StorageReference? = null
    private var firestore: FirebaseFirestore? = null

    private var photoUri: Uri? = null
    private var extension: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_photo)
        navigatePhoto()
    }

    init {
        auth = Firebase.auth
        firestore = Firebase.firestore
        storage = Firebase.storage
        storageRef = storage?.reference
    }

    private fun navigatePhoto() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_FROM_ALBUM)
    }

    fun contentUpload(view: View) {
        progressBar.visibility = View.VISIBLE

        val timestamp = SimpleDateFormat("yyMMdd_HHmmss", LOCAL).format(Date())
        val imageFileName = "IMAGE_${timestamp}_.$extension"
        val imageRef = storageRef?.child("images")?.child(imageFileName)

        imageRef?.putFile(photoUri!!)?.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            imageRef.downloadUrl
        }?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                progressBar.visibility = View.GONE

                val url = task.result
                // 디비에 바인딩 할 위치 생성 및 컬렉션(테이블)에 데이터 집합 생성

                // 시간 생성
                val contentDTO = ContentDTO()

                // 이미지 주소
                contentDTO.imageUrl = url.toString()

                // 유저의 UID
                contentDTO.uid = auth?.currentUser?.uid

                // 유저의 아이디
                contentDTO.userId = auth?.currentUser?.email

                // 게시물의 설명
                contentDTO.explain = addPhotoEditExplain.text.toString()

                // 게시물 업로드 시간
                contentDTO.timestamp = System.currentTimeMillis()

                // 게시물을 데이터를 생성 및 엑티비티 종료
                firestore?.collection("images")?.document()?.set(contentDTO)
                setResult(Activity.RESULT_OK)
                finish()
            }
        }?.addOnFailureListener {
            progressBar.visibility = View.GONE

            Toast.makeText(this, getString(R.string.upload_fail), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_FROM_ALBUM) {
            if (resultCode == Activity.RESULT_OK) {
                photoUri = data?.data
                val cursor = contentResolver
                val mime = cursor.getType(photoUri!!)
                extension = mime?.substring(mime.indexOf("/") + 1)

                Log.d(TAG, "cursor: $cursor")
                Log.d(TAG, "extension: $extension")

                addPhotoImageView.setImageURI(photoUri)
            } else {
                finish()
            }
        }
    }

    companion object {
        private const val TAG = "AddPhotoActivity"
        private var PICK_IMAGE_FROM_ALBUM = 0
        private var LOCAL = Locale.KOREA
    }
}