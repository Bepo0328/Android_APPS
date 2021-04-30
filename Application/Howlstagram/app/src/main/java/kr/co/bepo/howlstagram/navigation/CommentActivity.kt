package kr.co.bepo.howlstagram.navigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_comment.*
import kotlinx.android.synthetic.main.activity_comment.recyclerView
import kotlinx.android.synthetic.main.fragment_user.*
import kotlinx.android.synthetic.main.item_comment.view.*
import kr.co.bepo.howlstagram.R
import kr.co.bepo.howlstagram.model.AlarmDTO
import kr.co.bepo.howlstagram.model.ContentDTO
import kr.co.bepo.howlstagram.util.FcmPush

class CommentActivity : AppCompatActivity() {
    // Firebase
    private var user: FirebaseUser? = null
    private var firestore: FirebaseFirestore? = null

    private var uid: String? = null
    private var userId: String? = null
    private var contentUid: String? = null
    private var destinationUid: String? = null

    private var fcmPush: FcmPush? = null

    private var commentSnapshot: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        contentUid = intent.getStringExtra("contentUid")
        destinationUid = intent.getStringExtra("destinationUid")

        sendButton.setOnClickListener {
            val comment = ContentDTO.Comment()

            comment.userId = userId
            comment.uid = uid
            comment.comment = messageEditText.text.toString()
            comment.timestamp = System.currentTimeMillis()

            firestore?.collection("images")?.document(contentUid!!)
                ?.collection("comments")?.document()?.set(comment)
            commentAlarm(destinationUid!!, messageEditText.text.toString())
            messageEditText.setText("")
        }

        recyclerView.adapter = CommentRecyclerViewAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    init {
        user = Firebase.auth.currentUser
        firestore = Firebase.firestore
        uid = user?.uid
        userId = user?.email
        fcmPush = FcmPush()
    }

    override fun onStop() {
        super.onStop()
        commentSnapshot?.remove()
    }

    private fun commentAlarm(destinationUid: String, message: String) {
        val alarmDTO = AlarmDTO()
        alarmDTO.destinationUid = destinationUid
        alarmDTO.userId = userId
        alarmDTO.kind = 1
        alarmDTO.uid = uid
        alarmDTO.timestamp = System.currentTimeMillis()
        alarmDTO.message = message
        firestore?.collection("alarms")?.document()?.set(alarmDTO)

        val message = userId + getString(R.string.alarm_who) + message + getString(R.string.alarm_comment)
        fcmPush?.sendMessage(destinationUid, getString(R.string.alarm), message)
    }

    inner class CommentRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private val comments: ArrayList<ContentDTO.Comment> = ArrayList()

        init {
            commentSnapshot = firestore?.collection("images")?.document(contentUid!!)
                ?.collection("comments")?.addSnapshotListener { value, _ ->
                    comments.clear()
                    if (value == null) return@addSnapshotListener
                    for (snapshot in value.documents) {
                        comments.add(snapshot.toObject(ContentDTO.Comment::class.java)!!)
                    }
                    notifyDataSetChanged()
                }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_comment, parent,false)
            return CustomViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val view = holder.itemView

            // Profile Image
            firestore?.collection("profileImages")
                ?.document(comments[position].uid!!)?.addSnapshotListener { value, _ ->
                    if (value?.data != null) {
                        val url = value.data!!["image"]
                        Glide.with(holder.itemView.context)
                            .load(url)
                            .apply(RequestOptions().circleCrop()).into(view.profileImageView)
                    }
                }

            view.profileTextView.text = comments[position].userId
            view.commentTextView.text = comments[position].comment
        }

        override fun getItemCount(): Int {
            return comments.size
        }

        // RecyclerView Adapter - View Holder
        inner class CustomViewHolder(imageView: View) : RecyclerView.ViewHolder(imageView)

    }

    companion object {
        private const val TAG = "CommentActivity"
    }
}