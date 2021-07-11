package kr.co.bepo.deliverytracker.presentation.trackingitems

import kr.co.bepo.deliverytracker.data.entity.TrackingInformation
import kr.co.bepo.deliverytracker.data.entity.TrackingItem
import kr.co.bepo.deliverytracker.presentation.BasePresenter
import kr.co.bepo.deliverytracker.presentation.BaseView

class TrackingItemsContract {

    interface View : BaseView<Presenter> {

        fun showLoadingIndicator()

        fun hideLoadingIndicator()

        fun showNoDataDescription()

        fun showTrackingItemInformation(trackingItemInformation: List<Pair<TrackingItem, TrackingInformation>>)

    }

    interface Presenter : BasePresenter {
        var trackingItemInformation: List<Pair<TrackingItem, TrackingInformation>>

        fun refresh()
    }
}