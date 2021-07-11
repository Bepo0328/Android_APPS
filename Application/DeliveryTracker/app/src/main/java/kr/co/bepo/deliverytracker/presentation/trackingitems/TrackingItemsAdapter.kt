package kr.co.bepo.deliverytracker.presentation.trackingitems

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.bepo.deliverytracker.R
import kr.co.bepo.deliverytracker.data.entity.Level
import kr.co.bepo.deliverytracker.data.entity.ShippingCompany
import kr.co.bepo.deliverytracker.data.entity.TrackingInformation
import kr.co.bepo.deliverytracker.data.entity.TrackingItem
import kr.co.bepo.deliverytracker.databinding.ItemTrackingBinding
import kr.co.bepo.deliverytracker.extension.setTextColorRes
import kr.co.bepo.deliverytracker.extension.toReadableDateString
import java.util.*

class TrackingItemsAdapter : RecyclerView.Adapter<TrackingItemsAdapter.ViewHolder>() {

    var data: List<Pair<TrackingItem, TrackingInformation>> = emptyList()
    var onClickItemListener: ((TrackingItem, TrackingInformation) -> Unit)? = null

    inner class ViewHolder(
        private val binding: ItemTrackingBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                data.getOrNull(adapterPosition)?.let { (item, information) ->
                    onClickItemListener?.invoke(item, information)
                }
            }
        }

        fun bind(company: ShippingCompany, information: TrackingInformation) = with(binding) {
            updateAtTextView.text = Date(
                information.lastDetail?.time ?: System.currentTimeMillis()
            ).toReadableDateString()

            levelLabelTextView.text = information.level?.label

            when (information.level) {
                Level.COMPLETE -> {
                    levelLabelTextView.setTextColor(R.attr.colorPrimary)
                    root.alpha = 0.5f
                }
                Level.PREPARE -> {
                    levelLabelTextView.setTextColorRes(R.color.orange)
                    root.alpha = 1f
                }

                else -> {
                    levelLabelTextView.setTextColorRes(R.color.green)
                    root.alpha = 1f
                }
            }

            invoiceTextView.text = information.invoiceNo

            if (information.itemName.isNullOrBlank()) {
                itemNameTextView.text = "이름 없음"
                itemNameTextView.setTextColorRes(R.color.gray)
            } else {
                itemNameTextView.text = information.itemName
                itemNameTextView.setTextColorRes(R.color.black)
            }

            lastStateTextView.text = information.lastDetail?.let { it.kind + " @${it.where}" }

            companyNameTextView.text = company.name
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemTrackingBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (item, trackingInformation) = data[position]

        holder.bind(item.company, trackingInformation)

    }

    override fun getItemCount(): Int = data.size
}