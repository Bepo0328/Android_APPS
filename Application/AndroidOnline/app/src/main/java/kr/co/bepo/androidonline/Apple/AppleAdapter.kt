package kr.co.bepo.androidonline.Apple

import android.app.Activity
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.co.bepo.androidonline.R
import java.lang.Exception

class AppleAdapter(
    private var songList: ArrayList<Song>?,
    private val inflater: LayoutInflater,
    private val activity: Activity
) : RecyclerView.Adapter<AppleAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.titleTextView)
        val thumbnail: ImageView = itemView.findViewById(R.id.thumbnailImageView)
        private val play: ImageButton = itemView.findViewById(R.id.playButton)
        private var mediaPlayer: MediaPlayer? = null

        init {
            play.setOnClickListener {
                val position: Int = absoluteAdapterPosition
                val path = songList?.get(position)?.song

                try {
                    if (mediaPlayer == null) {
                        mediaPlayer = MediaPlayer.create(activity, Uri.parse(path))
                    }
                    if (mediaPlayer?.isPlaying == true) {
                        mediaPlayer?.stop()
                        mediaPlayer?.release()
                        mediaPlayer = null
                        play.setImageResource(R.drawable.ic_play_arrow)
                    } else {
                        mediaPlayer?.start()
                        play.setImageResource(R.drawable.ic_stop)
                    }
                } catch (e: Exception) {
                    Log.d(TAG, "Error: ${e.message}")
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.item_song_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = songList?.get(position)?.title
        Glide.with(holder.itemView.context)
            .load(songList?.get(position)?.thumbnail)
            .into(holder.thumbnail)
    }

    override fun getItemCount(): Int = songList?.size ?: 0

    companion object {
        private const val TAG = "AppleAdapter"
    }
}