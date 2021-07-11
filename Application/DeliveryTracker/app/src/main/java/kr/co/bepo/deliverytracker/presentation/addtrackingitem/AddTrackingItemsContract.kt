package kr.co.bepo.deliverytracker.presentation.addtrackingitem

import kr.co.bepo.deliverytracker.data.entity.ShippingCompany
import kr.co.bepo.deliverytracker.presentation.BasePresenter
import kr.co.bepo.deliverytracker.presentation.BaseView

class AddTrackingItemsContract {

    interface View : BaseView<Presenter> {

        fun showShippingCompaniesLoadingIndicator()

        fun hideShippingCompaniesLoadingIndicator()

        fun showSaveTrackingItemIndicator()

        fun hideSaveTrackingItemIndicator()

        fun showRecommendCompanyLoadingIndicator()

        fun hideRecommendCompanyLoadingIndicator()

        fun showCompanies(companies: List<ShippingCompany>)

        fun showRecommendCompany(company: ShippingCompany)

        fun enableSaveButton()

        fun disableSaveButton()

        fun showErrorToast(message: String)

        fun finish()
    }

    interface Presenter : BasePresenter {
        var invoice: String?
        var shippingCompanies: List<ShippingCompany>?
        var selectedShippingCompany: ShippingCompany?

        fun fetchShippingCompanies()

        fun fetchRecommendShippingCompany()

        fun changeSelectedShippingCompany(companyName: String)

        fun changeShippingInvoice(invoice: String)

        fun saveTrackingItem()
    }
}