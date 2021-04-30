package kr.co.bepo.howlstagram.util
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kr.co.bepo.howlstagram.model.PushDTO
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException


class FcmPush() {
    private var firestore: FirebaseFirestore? = null

    private val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
    private val url = "https://fcm.googleapis.com/fcm/send"
    private val serverKey = "AAAAeK7I1AU:APA91bFlk_67ko4DK0ZrObebmRc9K30pBoTjS_Z84DHodp-UR64EHZW3KrO3yOcgPyFrdkWD_W6JX8Uty1eUKEkZSAr5UGn6N8ugntqv63YG0gNckI2WBcAZb6CmiR9_snvSxU2vDJsp"

    private var okHttpClient: OkHttpClient? = null
    private var gson: Gson? = null

    init {
        gson = Gson()
        okHttpClient = OkHttpClient()
        firestore = Firebase.firestore
    }

    fun sendMessage(destinationUid: String, title: String, message: String) {
        firestore?.collection("pushTokens")?.document(destinationUid)?.get()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result?.get("pushToken").toString()
                val pushDTO = PushDTO()

                println(token)
                pushDTO.to = token
                pushDTO.notification?.title = title
                pushDTO.notification?.body = message

                val body = gson?.toJson(pushDTO)!!.toRequestBody(JSON)
                val request = Request
                    .Builder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "key=$serverKey")
                    .url(url)
                    .post(body)
                    .build()

                okHttpClient?.newCall(request)?.enqueue(object: Callback {
                    override fun onFailure(call: Call, e: IOException) {
                    }

                    override fun onResponse(call: Call, response: Response) {
                        println(response.body?.string())
                    }
                })
            }
        }
    }

    companion object {
        private const val TAG = "FcmPush"
    }
}
