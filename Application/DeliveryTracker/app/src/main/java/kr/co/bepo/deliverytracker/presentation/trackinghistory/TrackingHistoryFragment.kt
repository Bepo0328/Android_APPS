package kr.co.bepo.deliverytracker.presentation.trackinghistory

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.bepo.deliverytracker.data.entity.TrackingInformation
import kr.co.bepo.deliverytracker.data.entity.TrackingItem
import kr.co.bepo.deliverytracker.databinding.FragmentTrackingHistoryBinding
import org.koin.android.scope.ScopeFragment
import org.koin.core.parameter.parametersOf

class TrackingHistoryFragment : ScopeFragment(), TrackingHistoryContract.View {

    override val presenter: TrackingHistoryContract.Presenter by inject {
        parametersOf(arguments.item, arguments.information)
    }

    private var binding: FragmentTrackingHistoryBinding? = null
    private val arguments: TrackingHistoryFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentTrackingHistoryBinding.inflate(layoutInflater)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        bindViews()
        presenter.onViewCreated()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun hideLoadingIndicator() {
        binding?.refreshLayout?.isRefreshing = false
    }

    @SuppressLint("SetTextI18n")
    override fun showTrackingItemInformation(
        trackingItem: TrackingItem,
        trackingInformation: TrackingInformation
    ) {
        binding?.resultTextView?.text = trackingInformation.level?.label
        binding?.invoiceTextView?.text =
            "${trackingInformation.invoiceNo} (${trackingItem.company.name})"

        binding?.itemNameTextView?.text =
            if (trackingInformation.itemName.isNullOrBlank()) "이름 없음"
            else trackingInformation.itemName

        (binding?.recyclerVIew?.adapter as? TrackingHistoryAdapter)?.run {
            data = trackingInformation.trackingDetails ?: emptyList()
            notifyDataSetChanged()
        }
    }

    override fun finish() {
        findNavController().popBackStack()
    }

    private fun initViews() {
        binding?.recyclerVIew?.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = TrackingHistoryAdapter()
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        }
    }

    private fun bindViews() {
        binding?.refreshLayout?.setOnRefreshListener {
            presenter.refresh()
        }
        binding?.deleteTrackingItemButton?.setOnClickListener {
            presenter.deleteTrackingItem()
        }
    }

}