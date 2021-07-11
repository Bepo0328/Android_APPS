package kr.co.bepo.deliverytracker.presentation.trackingitems

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.bepo.deliverytracker.R
import kr.co.bepo.deliverytracker.data.entity.TrackingInformation
import kr.co.bepo.deliverytracker.data.entity.TrackingItem
import kr.co.bepo.deliverytracker.databinding.FragmentTrackingItemsBinding
import kr.co.bepo.deliverytracker.extension.toGone
import kr.co.bepo.deliverytracker.extension.toInvisible
import kr.co.bepo.deliverytracker.extension.toVisible
import org.koin.android.scope.ScopeFragment

class TrackingItemsFragment : ScopeFragment(), TrackingItemsContract.View {

    override val presenter: TrackingItemsContract.Presenter by inject()

    private var binding: FragmentTrackingItemsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentTrackingItemsBinding.inflate(inflater, container, false)
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

    override fun showLoadingIndicator() {
        binding?.progressBar?.toVisible()
    }

    override fun hideLoadingIndicator() {
        binding?.progressBar?.toGone()
        binding?.refreshLayout?.isRefreshing = false
    }

    override fun showNoDataDescription() {
        binding?.refreshLayout?.toInvisible()
        binding?.noDataContainer?.toVisible()
    }

    override fun showTrackingItemInformation(trackingItemInformation: List<Pair<TrackingItem, TrackingInformation>>) {
        binding?.refreshLayout?.toVisible()
        binding?.noDataContainer?.toGone()
        (binding?.recyclerView?.adapter as? TrackingItemsAdapter)?.apply {
            this.data = trackingItemInformation
            notifyDataSetChanged()
        }
    }

    private fun initViews() {
        binding?.recyclerView?.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = TrackingItemsAdapter()
        }
    }

    private fun bindViews() {
        binding?.refreshLayout?.setOnRefreshListener {
            presenter.refresh()
        }

        binding?.addTrackingItemButton?.setOnClickListener {
            findNavController().navigate(R.id.to_add_tracking_item)
        }

        binding?.addTrackingItemFloatingActionButton?.setOnClickListener {
            findNavController().navigate(R.id.to_add_tracking_item)
        }
        (binding?.recyclerView?.adapter as? TrackingItemsAdapter)?.onClickItemListener =
            { item, information ->
                findNavController()
                    .navigate(TrackingItemsFragmentDirections.toTrackingHistory(item, information))
            }
    }

}