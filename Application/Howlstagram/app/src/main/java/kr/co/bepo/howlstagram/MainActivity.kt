package kr.co.bepo.howlstagram

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kr.co.bepo.howlstagram.navigation.*
import kotlinx.android.synthetic.main.activity_main.*
import kr.co.bepo.howlstagram.util.FcmPush

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    // Firebase
    private var auth: FirebaseAuth? = null
    private var storage: FirebaseStorage? = null
    private var firestore: FirebaseFirestore? = null
    private var storageRef: StorageReference? = null
    private var uid: String? = null

    private var pushToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar.visibility = View.VISIBLE

        // Bottom Navigation View
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
        bottomNavigationView.selectedItemId = R.id.actionHome

        // 앨범 접근 권한 요청
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        registerPushToken()
    }

    init {
        auth = Firebase.auth
        storage = Firebase.storage
        firestore = Firebase.firestore
        storageRef = storage?.reference
        uid = auth?.currentUser?.uid
    }

    private fun setToolbarDefault() {
        titleImageView.visibility = View.VISIBLE
        usernameTextView.visibility = View.GONE
        backImageButton.visibility = View.GONE
    }

    private fun registerPushToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) return@OnCompleteListener
            pushToken = task.result
            val map = mutableMapOf<String, Any>()
            map["pushToken"] = pushToken!!
            firestore?.collection("pushTokens")?.document(uid!!)?.set(map)
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        setToolbarDefault()
        return when (item.itemId) {
            R.id.actionHome -> {
                supportFragmentManager.beginTransaction().replace(R.id.mainContent, DetailViewFragment()).commit()
                true
            }

            R.id.actionSearch -> {
                supportFragmentManager.beginTransaction().replace(R.id.mainContent, GridFragment()).commit()
                true
            }

            R.id.actionAddPhoto -> {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(Intent(this, AddPhotoActivity::class.java))
                } else {
                    Toast.makeText(this, "스토리지 읽기 권한이 없습니다.", Toast.LENGTH_LONG).show()
                }
                true
            }

            R.id.actionFavoriteAlarm -> {
                supportFragmentManager.beginTransaction().replace(R.id.mainContent, AlarmFragment()).commit()
                true
            }

            R.id.actionAccount -> {
                val userFragment = UserFragment()
                val bundle = Bundle()
                val uid = Firebase.auth.currentUser?.uid

                bundle.putString("destinationUid", uid)
                userFragment.arguments = bundle

                supportFragmentManager.beginTransaction()
                    .replace(R.id.mainContent, userFragment)
                    .commit()
                true
            }

            else -> {
                false
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // 앨범에서 Profile Image 사진 선택시 호출 되는 부분분
        if (requestCode == PICK_PROFILE_FROM_ALBUM && resultCode == Activity.RESULT_OK) {
            val photoUri = data?.data
            val imageRef = storageRef?.child("userProfileImages")?.child(uid!!)

            // 사진을 업로드 하는 부분  userProfileImages 폴더에 uid 에 파일을 업로드함
            imageRef?.putFile(photoUri!!)?.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                imageRef.downloadUrl
            }?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val url = task.result.toString()
                    val map = HashMap<String, Any>()
                    map["image"] = url
                    firestore?.collection("profileImages")?.document(uid!!)?.set(map)
                }
            }
        }
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val PICK_PROFILE_FROM_ALBUM = 10
    }
}