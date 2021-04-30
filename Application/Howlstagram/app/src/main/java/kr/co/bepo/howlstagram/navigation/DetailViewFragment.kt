package kr.co.bepo.howlstagram.navigation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_detail.view.*
import kotlinx.android.synthetic.main.fragment_detail.view.recyclerView
import kotlinx.android.synthetic.main.fragment_user.view.*
import kotlinx.android.synthetic.main.item_detail.view.*
import kotlinx.android.synthetic.main.item_detail.view.profileImageView
import kr.co.bepo.howlstagram.MainActivity
import kr.co.bepo.howlstagram.R
import kr.co.bepo.howlstagram.model.AlarmDTO
import kr.co.bepo.howlstagram.model.ContentDTO
import kr.co.bepo.howlstagram.util.FcmPush

class DetailViewFragment : Fragment() {
    // Firebase
    private var user: FirebaseUser? = null
    private var firestore: FirebaseFirestore? = null

    private var uid: String? = null
    private var userId: String? = null

    private var mainView: View? = null
    private var fcmPush: FcmPush? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 리사이클러 뷰와 어뎁터랑 연결
        mainView = inflater.inflate(R.layout.fragment_detail, container, false)

        return mainView
    }

    init {
        user = Firebase.auth.currentUser
        firestore = Firebase.firestore
        uid = user?.uid
        userId = user?.email
        fcmPush = FcmPush()
    }

    override fun onResume() {
        super.onResume()
        mainView?.recyclerView?.adapter = DetailViewRecyclerViewAdapter()
        mainView?.recyclerView?.layoutManager = LinearLayoutManager(activity)
        val mainActivity = activity as MainActivity
        mainActivity.progressBar.visibility = View.INVISIBLE
    }

    inner class DetailViewRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private val contentDTOs: ArrayList<ContentDTO> = ArrayList()
        private val contentUidList: ArrayList<String> = ArrayList()

        init {
            firestore?.collection("images")?.orderBy("timestamp")?.addSnapshotListener { value, _ ->
                contentDTOs.clear()
                contentUidList.clear()
                if (value == null) return@addSnapshotListener
                for (snapshot in value!!.documents) {
                    val item = snapshot.toObject(ContentDTO::class.java)
                    contentDTOs.add(item!!)
                    contentUidList.add(snapshot.id)
                }
                notifyDataSetChanged()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_detail, parent, false)
            return CustomViewHolder(view)

        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val viewHolder = (holder as CustomViewHolder).itemView

            // Profile Image 가져오기
            firestore?.collection("profileImages")?.document(contentDTOs[position].uid!!)
                ?.get()?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val url = task.result?.get("image")
                        Glide.with(holder.itemView.context)
                            .load(url)
                            .apply(RequestOptions().circleCrop())
                            .into(viewHolder.profileImageView)
                    }
                }


            // UserFragment 로 이동
            viewHolder.profileImageView.setOnClickListener {
                val fragment = UserFragment()
                val bundle = Bundle()

                bundle.putString("destinationUid", contentDTOs[position].uid)
                bundle.putString("userId", contentDTOs[position].userId)

                fragment.arguments = bundle
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.mainContent, fragment)
                    ?.commit()
            }

            // 유저 아이디
            viewHolder.profileTextView.text = contentDTOs[position].userId

            // 가운데 이미지
            Glide.with(holder.itemView.context).load(contentDTOs[position].imageUrl)
                .into(viewHolder.contentImageView)

            // 설명 텍스트
            viewHolder.explainTextView.text = contentDTOs[position].explain

            // 좋아요 이벤트
            viewHolder.favoriteImageView.setOnClickListener {
                favoriteEvent(position)
            }

            // 좋아요 버튼 설정
            if (contentDTOs[position].favorites.containsKey(uid)) {
                viewHolder.favoriteImageView.setImageResource(R.drawable.ic_favorite)
            } else {
                viewHolder.favoriteImageView.setImageResource(R.drawable.ic_favorite_border)
            }

            // 좋아요 카운터 설정
            viewHolder.favoriteCounterTextView.text = "좋아요 ${contentDTOs[position].favoritesCount}개"

            viewHolder.commentImageView.setOnClickListener {
                val intent = Intent(it.context, CommentActivity::class.java)
                intent.putExtra("contentUid", contentUidList[position])
                intent.putExtra("destinationUid", contentDTOs[position].uid)
                startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return contentDTOs.size
        }

        // 좋아요 이벤트 기능
        private fun favoriteEvent(position: Int) {
            val tsDoc = firestore?.collection("images")?.document(contentUidList[position])
            firestore?.runTransaction { transaction ->
                val contentDTO = transaction.get(tsDoc!!).toObject(ContentDTO::class.java)

                if (contentDTO!!.favorites.containsKey(uid)) {
                    contentDTO.favoritesCount -= 1
                    contentDTO.favorites.remove(uid)
                } else {
                    contentDTO.favoritesCount += 1
                    contentDTO.favorites[uid!!] = true
                    favoriteAlarm(contentDTOs[position].uid!!)
                }

                transaction.set(tsDoc, contentDTO)
            }
        }

        private fun favoriteAlarm(destinationUid: String) {
            val alarmDTO = AlarmDTO()
            alarmDTO.destinationUid = destinationUid
            alarmDTO.userId = userId
            alarmDTO.uid = uid
            alarmDTO.kind = 0
            alarmDTO.timestamp = System.currentTimeMillis()
            firestore?.collection("alarms")?.document()?.set(alarmDTO)

            val message = userId + getString(R.string.alarm_favorite)
            fcmPush?.sendMessage(destinationUid, getString(R.string.alarm), message)
        }

        inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)
    }
}