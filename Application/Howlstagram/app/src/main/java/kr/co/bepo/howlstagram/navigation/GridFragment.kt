package kr.co.bepo.howlstagram.navigation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_grid.*
import kotlinx.android.synthetic.main.fragment_grid.view.*
import kr.co.bepo.howlstagram.R
import kr.co.bepo.howlstagram.model.ContentDTO

class GridFragment: Fragment() {
    // Firebase
    private var auth: FirebaseAuth? = null
    private var firestore: FirebaseFirestore? = null

    private var mainView: View? = null

    var imagesSnapshot  : ListenerRegistration? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainView = inflater.inflate(R.layout.fragment_grid, container, false)
        return mainView
    }

    init {
        auth = Firebase.auth
        firestore = Firebase.firestore
    }

    override fun onResume() {
        super.onResume()
        mainView?.recyclerView?.adapter = GridFragmentRecyclerViewAdapter()
        mainView?.recyclerView?.layoutManager = GridLayoutManager(activity, 3)
    }

    override fun onStop() {
        super.onStop()
        imagesSnapshot?.remove()
    }

    inner class GridFragmentRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private val contentDTOs: ArrayList<ContentDTO> = ArrayList()

        init {
            imagesSnapshot = firestore?.collection("images")?.orderBy("timestamp")
                ?.addSnapshotListener { value, _ ->
                    contentDTOs.clear()
                    if (value == null) return@addSnapshotListener
                    for (snapshot in value.documents) {
                        contentDTOs.add(snapshot.toObject(ContentDTO::class.java)!!)
                    }
                    notifyDataSetChanged()
                }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            // 현재 사이즈 뷰 화면 크기의 가로 크기의 1/3값을 가지고 오기
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

            imageView.setOnClickListener {
                val fragment = UserFragment()
                val bundle = Bundle()

                bundle.putString("destinationUid", contentDTOs[position].uid)
                bundle.putString("userId", contentDTOs[position].userId)

                fragment.arguments = bundle
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.mainContent, fragment)
                    ?.commit()
            }
        }

        override fun getItemCount(): Int {
            return contentDTOs.size
        }

        // RecyclerView Adapter - View Holder
        inner class CustomViewHolder(var imageView: ImageView) : RecyclerView.ViewHolder(imageView)
    }

    companion object {
        private const val TAG = "GridFragment"
    }
}