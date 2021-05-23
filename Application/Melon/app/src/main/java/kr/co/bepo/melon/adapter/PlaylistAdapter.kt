package kr.co.bepo.melon.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.co.bepo.melon.R
import kr.co.bepo.melon.model.MusicModel

class PlaylistAdapter(private val callback: (MusicModel) -> Unit): ListAdapter<MusicModel, PlaylistAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        fun bind(item: MusicModel) {
            val itemTrackTextView: TextView = view.findViewById(R.id.itemTrackTextView)
            val itemArtistTextView: TextView = view.findViewById(R.id.itemArtistTextView)
            val itemCoverImageView: ImageView = view.findViewById(R.id.itemCoverImageView)

            itemTrackTextView.text = item.track
            itemArtistTextView.text = item.artist
            Glide.with(itemCoverImageView.context)
                .load(item.coverUrl)
                .into(itemCoverImageView)

            if (item.isPlaying) {
                itemView.setBackgroundColor(Color.GRAY)
            } else {
                itemView.setBackgroundColor(Color.TRANSPARENT)
            }

            itemView.setOnClickListener {
                callback(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_music, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        currentList[position].also { musicModel ->
            holder.bind(musicModel)
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<MusicModel>() {
            override fun areItemsTheSame(oldItem: MusicModel, newItem: MusicModel): Boolean {
                return oldItem.idx == newItem.idx
            }

            override fun areContentsTheSame(oldItem: MusicModel, newItem: MusicModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}