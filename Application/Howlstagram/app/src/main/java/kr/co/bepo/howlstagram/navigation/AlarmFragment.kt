package kr.co.bepo.howlstagram.navigation

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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.item_comment.view.*
import kr.co.bepo.howlstagram.R
import kr.co.bepo.howlstagram.model.AlarmDTO
import kotlinx.android.synthetic.main.fragment_alarm.view.*

class AlarmFragment: Fragment() {
    // Firebase
    private var auth: FirebaseAuth? = null
    private var firestore: FirebaseFirestore? = null

    // private String destinationUid;
    private var uid: String? = null

    private var mainView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainView = inflater.inflate(R.layout.fragment_alarm, container, false)

        mainView?.recyclerView?.adapter = AlarmRecyclerViewAdapter()
        mainView?.recyclerView?.layoutManager = LinearLayoutManager(activity)

        return mainView
    }

    init {
        auth = Firebase.auth
        firestore = Firebase.firestore
        uid = auth?.currentUser?.uid
    }

    inner class AlarmRecyclerViewAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private val alarmDTOList: ArrayList<AlarmDTO> = ArrayList()
        
        init {
            firestore?.collection("alarms")?.whereEqualTo("destinationUid", uid)
                ?.addSnapshotListener { value, _ ->
                    alarmDTOList.clear()
                    if (value == null) return@addSnapshotListener
                    for (snapshot in value.documents) {
                        alarmDTOList.add(snapshot.toObject(AlarmDTO::class.java)!!)
                    }
                    alarmDTOList.sortByDescending { it.timestamp }
                    notifyDataSetChanged()
                }
            
        }
        
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
            return CustomViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val profileImage = holder.itemView.profileImageView
            val commentTextView = holder.itemView.profileTextView

            firestore?.collection("profileImages")
                ?.document(alarmDTOList[position].uid!!)?.get()?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val url = task.result?.get("image")
                        Glide.with(activity!!)
                            .load(url)
                            .apply(RequestOptions().circleCrop())
                            .into(profileImage)
                    }
                }

            when (alarmDTOList[position].kind) {
                0 -> {
                    val str0 = alarmDTOList[position].userId + getString(R.string.alarm_favorite)
                    commentTextView.text = str0
                }

                1 -> {
                    val str1 = alarmDTOList[position].userId + getString(R.string.alarm_who) + alarmDTOList[position].message + getString(R.string.alarm_comment)
                    commentTextView.text = str1
                }

                2 -> {
                    val str2 = alarmDTOList[position].userId + getString(R.string.alarm_follow)
                    commentTextView.text = str2
                }
            }

        }

        override fun getItemCount(): Int {
            return alarmDTOList.size
        }

        inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)
    }
}