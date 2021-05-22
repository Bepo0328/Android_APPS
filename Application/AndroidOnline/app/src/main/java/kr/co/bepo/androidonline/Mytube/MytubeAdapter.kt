package kr.co.bepo.androidonline.Mytube

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.co.bepo.androidonline.R

class MytubeAdapter(
    private var mytubeList: ArrayList<Mytube>?,
    private val inflater: LayoutInflater,
    private val activity: Activity
) : RecyclerView.Adapter<MytubeAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.titleTextView)
        val content: TextView = itemView.findViewById(R.id.contentTextView)
        val thumbnail: ImageView = itemView.findViewById(R.id.thumbnailImageView)

        init {
            itemView.setOnClickListener {
                val position: Int = absoluteAdapterPosition
                val intent = Intent(activity, MytubeDetailActivity::class.java).apply {
                    putExtra("video_url", mytubeList?.get(position)?.video)
                }
                activity.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.item_mytube_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = mytubeList?.get(position)?.title
        holder.content.text = mytubeList?.get(position)?.content
        holder.content.isSelected = true
        Glide.with(holder.itemView.context)
            .load(mytubeList?.get(position)?.thumbnail)
            .into(holder.thumbnail)
    }

    override fun getItemCount(): Int = mytubeList?.size ?: 0
}