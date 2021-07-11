package kr.co.bepo.deliverytracker.presentation

interface BaseView<PresenterT : BasePresenter> {

    val presenter: PresenterT
}