package kr.co.bepo.androidonline.Outstagram

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.co.bepo.androidonline.R

class PostAdapter(
    private var postList: ArrayList<Post>?,
    private val inflater: LayoutInflater
) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postImageView: ImageView = itemView.findViewById(R.id.postImageView)
        val postOwnerTextView: TextView = itemView.findViewById(R.id.postOwnerTextView)
        val postContentTextView: TextView = itemView.findViewById(R.id.postContentTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.item_outstagram_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
            .load(postList?.get(position)?.image)
            .into(holder.postImageView)
        holder.postOwnerTextView.text = postList?.get(position)?.owner
        holder.postContentTextView.text = postList?.get(position)?.content
    }

    override fun getItemCount(): Int = postList?.size ?: 0
}