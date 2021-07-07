package kr.co.bepo.shoppingapp.presentation.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.view.isGone
import androidx.core.view.isVisible
import kr.co.bepo.shoppingapp.databinding.ActivityProductDetailBinding
import kr.co.bepo.shoppingapp.extensions.load
import kr.co.bepo.shoppingapp.extensions.loadCenterCrop
import kr.co.bepo.shoppingapp.extensions.toast
import kr.co.bepo.shoppingapp.presentation.BaseActivity
import kr.co.bepo.shoppingapp.presentation.list.ProductListState
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

internal class ProductDetailActivity :
    BaseActivity<ProductDetailViewModel, ActivityProductDetailBinding>() {

    companion object {
        const val PRODUCT_ID_KEY = "PRODUCT_ID_KEY"
        const val PRODUCT_ORDERED_RESULT_CODE = 99

        fun newIntent(context: Context, productId: Long) =
            Intent(context, ProductDetailActivity::class.java).apply {
                putExtra(PRODUCT_ID_KEY, productId)
            }
    }

    override val viewModel: ProductDetailViewModel by inject() {
        parametersOf(
            intent.getLongExtra(PRODUCT_ID_KEY, -1)
        )
    }

    override fun getViewBinding(): ActivityProductDetailBinding =
        ActivityProductDetailBinding.inflate(layoutInflater)

    override fun observeData() = viewModel.productDetailStateLiveData.observe(this) {
        when (it) {
            is ProductDetailState.UnInitialized -> initViews()
            is ProductDetailState.Loading -> handleLoadingState()
            is ProductDetailState.Success -> handleSuccessState(it)
            is ProductDetailState.Order -> handleOrderState()
            is ProductDetailState.Error ->  handleErrorState()
        }
    }

    private fun initViews() = with(binding) {
        setSupportActionBar(toolbar)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
        title=""
        toolbar.setNavigationOnClickListener {
            finish()
        }

        orderButton.setOnClickListener {
            viewModel.orderProduct()
        }
    }

    private fun handleLoadingState() = with(binding) {
        progressBar.isVisible = true
    }

    private fun handleSuccessState(state: ProductDetailState.Success) = with(binding) {
        progressBar.isVisible = false
        val product = state.productEntity
        title = product.productName
        productCategoryTextView.text = product.productType
        productImageView.loadCenterCrop(product.productImage, 8f)
        productPriceTextView.text = "${product.productPrice}원"
        introductionImageView.load(product.productIntroductionImage)
    }

    private fun handleOrderState() {
        setResult(PRODUCT_ORDERED_RESULT_CODE)
        toast("주문이 완료 되었습니다.")
        finish()
    }

    private fun handleErrorState() {
        toast("제품 정보를 불러올 수 없습니다.")
        finish()
    }
}