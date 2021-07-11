package kr.co.bepo.deliverytracker.presentation.trackinghistory

import kr.co.bepo.deliverytracker.data.entity.TrackingInformation
import kr.co.bepo.deliverytracker.data.entity.TrackingItem
import kr.co.bepo.deliverytracker.presentation.BasePresenter
import kr.co.bepo.deliverytracker.presentation.BaseView

class TrackingHistoryContract {

    interface View: BaseView<Presenter> {

        fun hideLoadingIndicator()

        fun showTrackingItemInformation(trackingItem: TrackingItem, trackingInformation: TrackingInformation)

        fun finish()
    }

    interface Presenter: BasePresenter {

        fun refresh()

        fun deleteTrackingItem()
    }
}