package kr.co.bepo.howlstagram.navigation

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
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
import kotlinx.android.synthetic.main.activity_main.*
import kr.co.bepo.howlstagram.R
import kr.co.bepo.howlstagram.model.ContentDTO
import kotlinx.android.synthetic.main.fragment_user.*
import kotlinx.android.synthetic.main.fragment_user.view.*
import kr.co.bepo.howlstagram.LoginActivity
import kr.co.bepo.howlstagram.MainActivity
import kr.co.bepo.howlstagram.model.AlarmDTO
import kr.co.bepo.howlstagram.model.FollowDTO
import kr.co.bepo.howlstagram.util.FcmPush

class UserFragment : Fragment() {
    // Firebase
    private var auth: FirebaseAuth? = null
    private var user: FirebaseUser? = null
    private var firestore: FirebaseFirestore? = null

    // private String destinationUid;
    private var uid: String? = null
    private var currentUserUid: String? = null
    private var userId: String? = null

    private var fragmentView: View? = null

    private var fcmPush: FcmPush? = null

    private var followListenerRegistration: ListenerRegistration? = null
    private var followingListenerRegistration: ListenerRegistration? = null
    private var imageProfileListenerRegistration: ListenerRegistration? = null
    private var recyclerListenerRegistration: ListenerRegistration? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentView = inflater.inflate(R.layout.fragment_user, container, false)

        if (arguments != null) {
            uid = arguments?.getString("destinationUid")
            Log.d(TAG, "uid: $uid")

            // 본인 계정을 확인 후 -> 로그아웃, Toolbar 기본으로 설정
            if (uid != null && uid == currentUserUid) { // MyPage
                fragmentView?.followSignOutButton?.text = getString(R.string.signout)
                fragmentView?.followSignOutButton?.setOnClickListener {
                    startActivity(Intent(activity, LoginActivity::class.java))
                    activity?.finish()
                    auth?.signOut()
                }

            } else { // OtherUserPage
                fragmentView?.followSignOutButton?.text = getString(R.string.follow)
                val mainActivity = (activity as MainActivity)
                mainActivity.usernameTextView.text = arguments?.getString("userId")
                mainActivity.backImageButton.setOnClickListener {
                    mainActivity.bottomNavigationView.selectedItemId = R.id.actionHome
                }

                mainActivity.titleImageView.visibility = View.GONE
                mainActivity.usernameTextView.visibility = View.VISIBLE
                mainActivity.backImageButton.visibility = View.VISIBLE

                fragmentView?.followSignOutButton?.setOnClickListener {
                    requestFollow()
                }
            }
        }

        // Profile Image Click Listener
        fragmentView?.profileImageView?.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    activity!!,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {

                //앨범 오픈
                val photoPickerIntent = Intent(Intent.ACTION_PICK)
                photoPickerIntent.type = "image/*"
                activity?.startActivityForResult(photoPickerIntent, PICK_PROFILE_FROM_ALBUM)
            }
        }
        getFollowing()
        getFollower()
        fragmentView?.recyclerView?.adapter = UserFragmentRecyclerViewAdapter()
        fragmentView?.recyclerView?.layoutManager = GridLayoutManager(activity, 3)

        return fragmentView
    }

    init {
        auth = Firebase.auth
        firestore = Firebase.firestore
        user = auth?.currentUser
        currentUserUid = user?.uid
        userId = user?.email
        fcmPush = FcmPush()
    }

    override fun onResume() {
        super.onResume()
        getProfileImage()
    }

    private fun getProfileImage() {
        imageProfileListenerRegistration = firestore?.collection("profileImages")?.document(uid!!)
            ?.addSnapshotListener { value, _ ->
                if (value?.data != null) {
                    val url = value.data!!["image"]
                    Glide.with(activity!!)
                        .load(url)
                        .apply(RequestOptions().circleCrop())
                        .into(fragmentView?.profileImageView!!)
                }
            }
    }

    private fun getFollowing() {
        followingListenerRegistration =
            firestore?.collection("users")?.document(uid!!)?.addSnapshotListener { value, _ ->
                val followDTO = value?.toObject(FollowDTO::class.java) ?: return@addSnapshotListener

                fragmentView?.followingCountTextView?.text = followDTO.followingCount.toString()
            }

    }

    private fun getFollower() {
        followListenerRegistration =
            firestore?.collection("users")?.document(uid!!)?.addSnapshotListener { value, _ ->
                val followDTO = value?.toObject(FollowDTO::class.java) ?: return@addSnapshotListener
                fragmentView?.followerCountTextView?.text = followDTO.followerCount.toString()
                if (followDTO.followers.containsKey(currentUserUid)) {
                    fragmentView?.followSignOutButton?.text = activity?.getString(R.string.follow_cancel)
                    //fragmentView?.followSignOutButton?.text = getString(R.string.follow_cancel)
                    fragmentView?.followSignOutButton?.background?.colorFilter =
                        PorterDuffColorFilter(
                            (ContextCompat.getColor(fragmentView?.context!!, R.color.colorLightGray)),
                            PorterDuff.Mode.MULTIPLY)

                } else {
                    if (uid != currentUserUid) {
                        fragmentView?.followSignOutButton?.text = activity?.getString(R.string.follow)
                        //fragmentView?.followSignOutButton?.text = getString(R.string.follow)
                        fragmentView?.followSignOutButton?.background?.colorFilter = null
                    }
                }
            }

    }

    private fun requestFollow() {
        val tsDocFollowing = firestore?.collection("users")?.document(currentUserUid!!)
        firestore?.runTransaction { transaction ->

            var followDTO = transaction.get(tsDocFollowing!!).toObject(FollowDTO::class.java)
            if (followDTO == null) {
                followDTO = FollowDTO()
                followDTO.followingCount = 1
                followDTO.followings[uid!!] = true

                transaction.set(tsDocFollowing, followDTO)
                return@runTransaction
            }

            // UnStar the post and remove self from stars
            if (followDTO.followings.containsKey(uid)) {
                followDTO.followingCount -= 1
                followDTO.followings.remove(uid)
            } else {
                followDTO.followingCount += 1
                followDTO.followings[uid!!] = true
                //followerAlarm(uid!!)
            }
            transaction.set(tsDocFollowing, followDTO)
            return@runTransaction
        }

        val tsDocFollower = firestore?.collection("users")?.document(uid!!)
        firestore?.runTransaction { transaction ->

            var followDTO = transaction.get(tsDocFollower!!).toObject(FollowDTO::class.java)
            if (followDTO == null) {
                followDTO = FollowDTO()
                followDTO?.followerCount = 1
                followDTO?.followers!![currentUserUid!!] = true
                followerAlarm(uid!!)

                transaction.set(tsDocFollower, followDTO!!)
                return@runTransaction
            }

            if (followDTO?.followers?.containsKey(currentUserUid!!)!!) {
                followDTO!!.followerCount -= 1
                followDTO?.followers?.remove(currentUserUid!!)
            } else {
                followDTO!!.followerCount += 1
                followDTO?.followers!![currentUserUid!!] = true
                followerAlarm(uid!!)
            }
            transaction.set(tsDocFollower, followDTO!!)
            return@runTransaction
        }
    }

    private fun followerAlarm(destinationUid: String) {
        val alarmDTO = AlarmDTO()
        alarmDTO.destinationUid = destinationUid
        alarmDTO.userId = userId
        alarmDTO.uid = currentUserUid
        alarmDTO.kind = 2
        alarmDTO.timestamp = System.currentTimeMillis()
        firestore?.collection("alarms")?.document()?.set(alarmDTO)

        val message = userId + getString(R.string.alarm_follow)
        fcmPush?.sendMessage(destinationUid, getString(R.string.alarm), message)
    }

    inner class UserFragmentRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private val contentDTOs: ArrayList<ContentDTO> = ArrayList()

        init {
            Log.d(TAG, "Start Init!")

            // 나의 사진만 찾기
            recyclerListenerRegistration = firestore?.collection("images")?.whereEqualTo("uid", uid)
                ?.addSnapshotListener { value, _ ->
                    contentDTOs.clear()
                    if (value == null) return@addSnapshotListener
                    for (snapshot in value.documents) {
                        contentDTOs.add(snapshot.toObject(ContentDTO::class.java)!!)
                    }
                    postCountTextView?.text = contentDTOs.size.toString()
                    notifyDataSetChanged()
                }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val width = resources.displayMetrics.widthPixels / 3
            val imageView = ImageView(parent.context)

            imageView.layoutParams = LinearLayoutCompat.LayoutParams(width, width)
            return CustomViewHolder(imageView)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val imageView = (holder as CustomViewHolder).imageView
            Glide.with(holder.itemView.context)
                .load(contentDTOs[position].imageUrl)
                .apply(RequestOptions().centerCrop())
                .into(imageView)
        }

        override fun getItemCount(): Int {
            return contentDTOs.size
        }

        // RecyclerView Adapter - View Holder
        inner class CustomViewHolder(var imageView: ImageView) : RecyclerView.ViewHolder(imageView)

    }

    companion object {
        private const val TAG = "UserFragment"
        private const val PICK_PROFILE_FROM_ALBUM = 10
    }
}