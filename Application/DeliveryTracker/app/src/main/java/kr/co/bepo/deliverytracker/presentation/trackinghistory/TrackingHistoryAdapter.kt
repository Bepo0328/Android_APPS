package kr.co.bepo.deliverytracker.presentation.trackinghistory

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.bepo.deliverytracker.data.entity.TrackingDetail
import kr.co.bepo.deliverytracker.databinding.ItemTrackingHistoryBinding

class TrackingHistoryAdapter : RecyclerView.Adapter<TrackingHistoryAdapter.ViewHolder>() {

    var data: List<TrackingDetail> = emptyList()

    inner class ViewHolder(
        private val binding: ItemTrackingHistoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(info: TrackingDetail) = with(binding) {
            timeStampTextView.text = info.timeString
            stateTextView.text = info.kind
            locationTextView.text = "@${info.where}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemTrackingHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(data[position])

    override fun getItemCount(): Int =
        data.size
}