package kr.co.bepo.shoppingapp.presentation.detail

import kr.co.bepo.shoppingapp.data.entity.product.ProductEntity

sealed class ProductDetailState {

    object UnInitialized : ProductDetailState()

    object Loading : ProductDetailState()

    data class Success(
        val productEntity: ProductEntity
    ) : ProductDetailState()

    object Order : ProductDetailState()

    object Error : ProductDetailState()
}